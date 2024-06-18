package com.behnamkhani.unboxd.searchproduct.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamkhani.unboxd.common.domain.NetworkException
import com.behnamkhani.unboxd.common.domain.NetworkUnavailableException
import com.behnamkhani.unboxd.common.domain.NoMoreProductsException
import com.behnamkhani.unboxd.common.domain.usecases.InsertProduct
import com.behnamkhani.unboxd.common.domain.usecases.RequestProductById
import com.behnamkhani.unboxd.common.domain.usecases.ToggleWishlist
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.mappers.UiProductMapper
import com.behnamkhani.unboxd.common.utils.createExceptionHandler
import com.behnamkhani.unboxd.searchproduct.domain.usecases.SearchProduct
import com.behnamkhani.unboxd.searchproduct.presentation.ViewState.SearchProductViewState
import com.behnamkhani.unboxd.searchproduct.presentation.event.SearchProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SearchProductViewModel @Inject constructor(
    private val searchProduct: SearchProduct,
    private val requestProductById: RequestProductById,
    private val insertProduct: InsertProduct,
    private val toggleWishlist: ToggleWishlist,
    private val uiProductMapper: UiProductMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchProductViewState())
    val state: StateFlow<SearchProductViewState> = _state.asStateFlow()
    private var searchJob: Job = Job()

    fun onEvent(event: SearchProductEvent) {
        when (event) {
            is SearchProductEvent.ToggleWishlistForProduct -> toggleWishlistForProduct(
                event.productId,
                event.isChecked
            )

            is SearchProductEvent.SearchByQuery -> {
                searchJob = viewModelScope.launch {
                    searchByQuery(event.query)
                }
            }
        }
    }

    private fun cancelPreviousSearch() {
        searchJob.cancel()
        _state.update { oldState ->
            oldState.copy(
                products = emptyList()
            )
        }
    }

    private fun toggleWishlistForProduct(productId: Long, isChecked: Boolean) {
        val errorMessage = "Failed to fetch products!"

        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onException(it) }
        viewModelScope.launch(exceptionHandler) {

            if (isChecked) {
                insertProduct(requestProductById(productId))
            }

            toggleWishlist(productId)
        }
    }

    private fun searchByQuery(query: String) {
        loadSearchResult(query)
    }

    private fun loadSearchResult(query: String) {
        val errorMessage = "Failed to fetch wishlist!"
        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onException(it) }
        viewModelScope.launch(exceptionHandler) {
            searchProduct(query)
                .map { products -> products.map { uiProductMapper.mapToView(it) } }
                .flowOn(Dispatchers.IO)
                .catch { onException(it) }
                .collect { products ->
                    if (query.isEmpty()) {
                        cancelPreviousSearch()
                    } else {
                        onProductsReceived(products)
                    }
                }
        }
    }

    private fun onProductsReceived(products: List<UIProduct>) {
        _state.update { oldState ->
            oldState.copy(
                products = products.toList()
            )
        }
    }


    private fun onException(exception: Throwable) {
        when (exception) {
            is NetworkException,
            is NetworkUnavailableException,
            -> {
                _state.update { oldState ->
                    oldState.copy(
                        exception = Event(exception)
                    )
                }
            }

            is NoMoreProductsException -> {
                _state.update { oldState ->
                    oldState.copy(exception = Event(exception))
                }
            }
        }
    }
}