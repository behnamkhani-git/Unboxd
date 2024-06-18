package com.behnamkhani.unboxd.productdetails.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamkhani.unboxd.common.domain.NetworkException
import com.behnamkhani.unboxd.common.domain.NetworkUnavailableException
import com.behnamkhani.unboxd.common.domain.usecases.CheckProductExistenceInLocalDb
import com.behnamkhani.unboxd.common.domain.usecases.GetNumberOfListItems
import com.behnamkhani.unboxd.common.domain.usecases.InsertProduct
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.mappers.UiProductMapper
import com.behnamkhani.unboxd.common.utils.createExceptionHandler
import com.behnamkhani.unboxd.productdetails.domain.usecases.GetProductById
import com.behnamkhani.unboxd.productdetails.domain.usecases.RequestProductById
import com.behnamkhani.unboxd.productdetails.presentation.event.ProductDetailsEvent
import com.behnamkhani.unboxd.productdetails.presentation.viewstate.ProductDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val uiProductMapper: UiProductMapper,
    private val requestProductById: RequestProductById,
    private val getProductById: GetProductById,
    private val insertProduct: InsertProduct,
    private val checkProductExistenceInLocalDb: CheckProductExistenceInLocalDb,
    private val getNumberOfReviews: GetNumberOfListItems,
) : ViewModel() {

    private val _productDetailsState = MutableStateFlow(ProductDetailsViewState())
    val productDetailsState: StateFlow<ProductDetailsViewState> = _productDetailsState.asStateFlow()

    fun onEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.LoadProductById -> loadProductById(event.productId)
        }
    }

    private fun loadProductById(productId: Long) {
        val errorMessage = "Failed to fetch product!"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onException(it) }

        viewModelScope.launch(exceptionHandler) {
            val isProductInLocalDb = checkProductExistenceInLocalDb(productId)
            if (!isProductInLocalDb) {
                insertProduct(requestProductById(productId))
            }
            val productDetails = getProductById(productId)
            val numberOfReviews = getNumberOfReviews(productDetails.reviews)
            updateStateWithProductDetails(
                uiProductMapper.mapToView(productDetails),
                numberOfReviews
            )
        }
    }

    private fun updateStateWithProductDetails(productDetails: UIProduct, numberOfReviews: Int) {
        _productDetailsState.update { oldState ->
            oldState.copy(
                loading = false,
                productDetails = productDetails,
                numberOfReviews = "$numberOfReviews reviews"
            )
        }
    }

    private fun onException(exception: Throwable) {
        when (exception) {
            is NetworkException,
            is NetworkUnavailableException,
            -> {
                _productDetailsState.update { oldState ->
                    oldState.copy(
                        loading = false,
                        exception = Event(exception)
                    )
                }
            }
        }
    }
}