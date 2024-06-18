package com.behnamkhani.unboxd.searchproduct.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.behnamkhani.unboxd.common.presentation.adapter.ProductsAdapter
import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.searchproduct.R
import com.behnamkhani.unboxd.searchproduct.databinding.FragmentSearchProductBinding
import com.behnamkhani.unboxd.searchproduct.presentation.ViewState.SearchProductViewState
import com.behnamkhani.unboxd.searchproduct.presentation.event.SearchProductEvent
import com.behnamkhani.unboxd.searchproduct.presentation.viewmodel.SearchProductViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchProductFragment : Fragment() {
    companion object {
        private const val NUM_OF_COLUMNS = 2
    }

    private var _binding: FragmentSearchProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
        attachListeners()
    }

    private fun attachListeners() {

        val searchView = binding.searchViewSearchProduct

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    handleTextChange(query.orEmpty())
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    handleTextChange(newText.orEmpty())
                    return true
                }
            }
        )
    }

    private fun handleTextChange(query: String) {
        requestSearch(query)
    }



    private fun requestSearch(query: String) {
        viewModel.onEvent(SearchProductEvent.SearchByQuery(query))
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
                viewModel.state.collect {
                    updateUiState(it, adapter)
                }
            }
        }
    }

    private fun updateUiState(
        state: SearchProductViewState,
        adapter: ProductsAdapter,
    ) {
        binding.textViewMessage.isVisible = state.products.isEmpty()
        adapter.submitList(state.products)
        handleException(state.exception)
    }

    private fun handleException(exception: Event<Throwable>?) {
        val unhandledException = exception?.getContentIfNotHandled() ?: return
        val snackbarMessage = unhandledException.message?.takeIf { it.isNotEmpty() } ?: run {
            getString(R.string.failed_to_search_products)
        }
        Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
    }

    private fun prepareRecyclerView(productsNearYouAdapter: ProductsAdapter) {
        binding.recyclerViewSearchedProducts.apply {
            adapter = productsNearYouAdapter
            layoutManager = GridLayoutManager(requireContext(), NUM_OF_COLUMNS)
            setHasFixedSize(true)
        }
    }

    private fun onHeartTouch(productId: Long, isChecked: Boolean) {
        viewModel.onEvent(SearchProductEvent.ToggleWishlistForProduct(productId, isChecked))
    }

    private fun navigateToProductDetails(productId: Long) {
        val navController = findNavController()
        val bundle = bundleOf("productId" to productId)
        navController.navigate(R.id.action_searchProductFragment_to_productDetailsFragment2, bundle)
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

}