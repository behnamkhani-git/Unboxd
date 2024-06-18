package com.behnamkhani.unboxd.wishlist.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.behnamkhani.unboxd.R
import com.behnamkhani.unboxd.common.presentation.adapter.ProductsAdapter
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.databinding.FragmentWishlistBinding
import com.behnamkhani.unboxd.wishlist.presentation.event.WishlistEvent
import com.behnamkhani.unboxd.wishlist.presentation.viewmodel.WishlistViewModel
import com.behnamkhani.unboxd.wishlist.presentation.viewstate.WishlistViewState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    companion object {
        private const val NUM_OF_COLUMNS = 2
    }

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishlistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
    }

    private fun prepareUI() {
        val adapter = createAdapter(
            onProductTouch = { productId ->
                onProductTouch(productId)
            },
            onHeartTouch = { productId, isChecked ->
                onHeartTouch(productId, isChecked)
            })

        prepareRecyclerView(adapter)
        observeViewStateChanges(adapter)
    }

    private fun observeViewStateChanges(adapter: ProductsAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.stateWishlist.collect { state ->
                    updateUiState(state, adapter)
                }
            }
        }
    }

    private fun updateUiState(
        state: WishlistViewState,
        adapter: ProductsAdapter,
    ) {
        adapter.submitList(state.products)
        handleException(state.exception)
        binding.imageViewEmptyWishlist.isVisible = state.products.isEmpty()
    }

    private fun handleException(exception: Event<Throwable>?) {
        val unhandledException = exception?.getContentIfNotHandled() ?: return
        val snackbarMessage = unhandledException.message?.takeIf { it.isNotEmpty() } ?: run {
            getString(R.string.failed_to_fetch_wishlist)
        }
        Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
    }

    private fun prepareRecyclerView(productsNearYouAdapter: ProductsAdapter) {
        binding.recyclerView.apply {
            adapter = productsNearYouAdapter
            layoutManager = GridLayoutManager(requireContext(), NUM_OF_COLUMNS)
            setHasFixedSize(true)
        }
    }

    private fun createAdapter(
        onProductTouch: (Long) -> Unit,
        onHeartTouch: (Long, Boolean) -> Unit,
    ): ProductsAdapter {
        return ProductsAdapter(onProductTouch, onHeartTouch)
    }

    private fun onProductTouch(productId: Long) {
        navigateToProductDetails(productId)
    }

    private fun navigateToProductDetails(productId: Long) {
        val navController = findNavController()
        val bundle = bundleOf("productId" to productId)
        navController.navigate(R.id.action_wishlistFragment_to_productDetailsFragment2, bundle)
    }

    private fun onHeartTouch(productId: Long, isChecked: Boolean) {
        viewModel.onEvent(WishlistEvent.ToggleWishlistForProduct(productId))
    }
}