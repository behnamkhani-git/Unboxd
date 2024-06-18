package com.behnamkhani.unboxd.displayproduct.presentation.ui


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
import androidx.recyclerview.widget.RecyclerView
import com.behnamkhani.unboxd.R
import com.behnamkhani.unboxd.common.presentation.adapter.ProductsAdapter
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.databinding.FragmentAllProductsBinding
import com.behnamkhani.unboxd.displayproduct.presentation.event.ProductsEvent
import com.behnamkhani.unboxd.common.utils.InfiniteScrollListener
import com.behnamkhani.unboxd.displayproduct.presentation.viewmodel.ProductsViewModel
import com.behnamkhani.unboxd.displayproduct.presentation.viewstate.ProductsViewState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllProductsFragment : Fragment() {

    companion object {
        private const val NUM_OF_COLUMNS = 2
    }

    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
        requestInitialProductsList()
    }

    private fun requestInitialProductsList() {
        viewModel.onEvent(ProductsEvent.RequestInitialProductsList)
    }

    private fun prepareUI() {
        val adapter = createAdapter(
            onProductTouch = { productId ->
                onProductTouch(productId)
            },
            onHeartTouch = { productId, isChecked ->
                onHeartTouch(productId)
            })

        prepareRecyclerView(adapter)
        observeViewStateChanges(adapter)
    }

    private fun onHeartTouch(productId: Long) {
        viewModel.onEvent(ProductsEvent.ToggleWishlistForProduct(productId))
    }

    private fun onProductTouch(productId: Long) {
        navigateToProductDetails(productId)
    }

    private fun navigateToProductDetails(productId: Long) {
        val navController = findNavController()
        val bundle = bundleOf("productId" to productId)
        navController.navigate(R.id.action_allProductsFragment_to_productDetailsFragment, bundle)
    }

    private fun createAdapter(
        onProductTouch: (Long) -> Unit,
        onHeartTouch: (Long, Boolean) -> Unit,
    ): ProductsAdapter {
        return ProductsAdapter(onProductTouch, onHeartTouch)
    }

    private fun prepareRecyclerView(productsNearYouAdapter: ProductsAdapter) {
        binding.productsRecyclerView.apply {
            adapter = productsNearYouAdapter
            layoutManager = GridLayoutManager(requireContext(), NUM_OF_COLUMNS)
            setHasFixedSize(true)

            addOnScrollListener(createInfiniteScrollListener(layoutManager as GridLayoutManager))
        }
    }


    private fun createInfiniteScrollListener(
        layoutManager: GridLayoutManager,
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(
            layoutManager,
            ProductsViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() {
                requestMoreProducts()
            }

            override fun isLoading(): Boolean = viewModel.isLoadingMoreProducts
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMoreProducts() {
        viewModel.onEvent(ProductsEvent.RequestMoreProducts)
    }

    private fun observeViewStateChanges(adapter: ProductsAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.stateProducts.collect {
                    updateUiState(it, adapter)
                }
            }
        }
    }

    private fun updateUiState(
        state: ProductsViewState,
        adapter: ProductsAdapter,
    ) {
        binding.progressBar.isVisible = state.loading
        adapter.submitList(state.products)
        handleNoMoreProducts(state.isProductListEmpty)
        handleException(state.exception)
    }

    private fun handleNoMoreProducts(noMoreProducts: Boolean) {
        if (noMoreProducts)
            Snackbar.make(
                requireView(),
                getString(R.string.there_is_no_more_products_please_search_another_keyword),
                Snackbar.LENGTH_SHORT
            ).show()
    }

    private fun handleException(exception: Event<Throwable>?) {
        val unhandledException = exception?.getContentIfNotHandled() ?: return
        val snackbarMessage = unhandledException.message?.takeIf { it.isNotEmpty() } ?: run {
            getString(R.string.failed_to_search_products)
        }
        Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}