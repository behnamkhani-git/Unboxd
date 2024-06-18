package com.behnamkhani.unboxd.wishlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.behnamkhani.unboxd.common.domain.NetworkUnavailableException
import com.behnamkhani.unboxd.common.domain.NoMoreProductsException
import com.behnamkhani.unboxd.common.domain.usecases.ToggleWishlist
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.mappers.UiProductMapper
import com.behnamkhani.unboxd.common.utils.createExceptionHandler
import com.behnamkhani.unboxd.wishlist.domain.usecases.GetNumberOfWishlistItems
import com.behnamkhani.unboxd.wishlist.domain.usecases.GetWishlist
import com.behnamkhani.unboxd.wishlist.presentation.event.WishlistEvent
import com.behnamkhani.unboxd.wishlist.presentation.viewstate.WishlistViewState
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
class WishlistViewModel @Inject constructor(
    private val getWishlist: GetWishlist,
    private val getNumberOfWishlistItems: GetNumberOfWishlistItems,
    private val toggleWishlist: ToggleWishlist,
    private val uiProductMapper: UiProductMapper,
) : ViewModel() {

    private val _stateWishlist = MutableStateFlow(WishlistViewState())
    val stateWishlist: StateFlow<WishlistViewState> = _stateWishlist.asStateFlow()

    init {
        observeViewStateChanges()
    }

    private fun observeViewStateChanges() {
        viewModelScope.launch {
            getWishlist()
                .map { products -> products.map { uiProductMapper.mapToView(it) } }
                .flowOn(Dispatchers.IO)
                .catch { onException(it) }
                .collect { products ->
                    onWishlistReceived(products)
                }
        }
    }

    fun onEvent(event: WishlistEvent) {
        when (event) {
            is WishlistEvent.ToggleWishlistForProduct -> {
                toggleWishlistForProduct(event.productId)

            }
        }
    }

    private fun toggleWishlistForProduct(productId: Long) {
        val errorMessage = "Failed to fetch products!"

        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onException(it) }
        viewModelScope.launch(exceptionHandler) {
            toggleWishlist(productId)
            if (getNumberOfWishlistItems() == 0) {
                _stateWishlist.update { oldState ->
                    oldState.copy(
                        products = emptyList()
                    )
                }
            }

        }
    }

    private fun onWishlistReceived(products: List<UIProduct>) {

        _stateWishlist.update { oldState ->
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
                _stateWishlist.update { oldState ->
                    oldState.copy(
                        exception = Event(exception),
                    )
                }
            }

            is NoMoreProductsException -> {
                _stateWishlist.update { oldState ->
                    oldState.copy(exception = Event(exception))
                }
            }
        }
    }
}
