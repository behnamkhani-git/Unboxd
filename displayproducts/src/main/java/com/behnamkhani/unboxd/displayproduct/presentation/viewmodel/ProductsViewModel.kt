package com.behnamkhani.unboxd.displayproduct.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamkhani.unboxd.common.domain.NetworkUnavailableException
import com.behnamkhani.unboxd.common.domain.NoMoreProductsException
import com.behnamkhani.unboxd.common.domain.model.pagination.Pagination
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.mappers.UiProductMapper
import com.behnamkhani.unboxd.common.utils.createExceptionHandler
import com.behnamkhani.unboxd.displayproduct.domain.usecases.GetProducts
import com.behnamkhani.unboxd.displayproduct.domain.usecases.RequestNextPageOfProducts
import com.behnamkhani.unboxd.common.domain.usecases.ToggleWishlist
import com.behnamkhani.unboxd.displayproduct.presentation.event.ProductsEvent
import com.behnamkhani.unboxd.displayproduct.presentation.viewstate.ProductsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chromium.net.NetworkException
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProducts: GetProducts,
    private val toggleWishlist: ToggleWishlist,
    private val uiProductMapper: UiProductMapper,
    private val requestNextPageOfProducts: RequestNextPageOfProducts,
) : ViewModel() {

    private val _stateProducts = MutableStateFlow(ProductsViewState())
    private var currentSkip = 0
    val stateProducts: StateFlow<ProductsViewState> = _stateProducts.asStateFlow()


    val isLastPage: Boolean
        get() = stateProducts.value.isProductListEmpty

    var isLoadingMoreProducts: Boolean = false
        private set

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_LIMIT
    }

    init {
        observeViewStateChanges()
    }

    private fun observeViewStateChanges() {
        viewModelScope.launch {
            getProducts()
                .map { products -> products.map { uiProductMapper.mapToView(it) } }
                .flowOn(Dispatchers.IO)
                .catch { onException(it) }
                .collect { onNewProductList(it) }
        }
    }

    fun onEvent(event: ProductsEvent) {
        when (event) {
            is ProductsEvent.RequestInitialProductsList -> loadInitialProducts()
            is ProductsEvent.RequestMoreProducts -> loadNextProductPage()
            is ProductsEvent.ToggleWishlistForProduct -> toggleWishlistForProduct(event.productId)
        }
    }

    private fun toggleWishlistForProduct(productId: Long) {
        val errorMessage = "Failed to fetch products!"

        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onException(it) }

        viewModelScope.launch(exceptionHandler) {
            toggleWishlist(productId)
        }
    }


    private fun loadInitialProducts() {
        if (stateProducts.value.products.isEmpty()) {
            loadNextProductPage()
        }
    }

    private fun loadNextProductPage() {
        isLoadingMoreProducts = true
        val errorMessage = "Failed to fetch products!"
        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onException(it) }
        viewModelScope.launch(exceptionHandler) {

            val pagination = requestNextPageOfProducts(currentSkip)
            onPaginationInfoObtained(pagination)

            isLoadingMoreProducts = false

        }
    }


    private fun onNewProductList(products: List<UIProduct>) {
        _stateProducts.update { oldState ->
            oldState.copy(
                loading = false,
                products = products.toList()
            )
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentSkip = (pagination.skip + pagination.limit)
    }

    private fun onException(exception: Throwable) {
        when (exception) {
            is NetworkException,
            is NetworkUnavailableException,
            -> {
                _stateProducts.update { oldState ->
                    oldState.copy(
                        loading = false,
                        exception = Event(exception)
                    )
                }
            }

            is NoMoreProductsException -> {
                _stateProducts.update { oldState ->
                    oldState.copy(isProductListEmpty = true, exception = Event(exception))
                }
            }
        }
    }
}