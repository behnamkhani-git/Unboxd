package com.behnamkhani.unboxd.productdetails.presentation.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.behnamkhani.unboxd.common.utils.setImage
import com.behnamkhani.unboxd.productdetails.databinding.FragmentProductDetailsBinding
import com.behnamkhani.unboxd.productdetails.presentation.event.ProductDetailsEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val binding get() = _binding!!

    private val viewModel: com.behnamkhani.unboxd.productdetails.presentation.viewmodel.ProductDetailsViewModel by viewModels()
    private var _binding: FragmentProductDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
        observeViewStateChanges()
        requestInitialProductDetails()
    }

    private fun prepareUI() {
        prepareToolbar()
    }

    private fun observeViewStateChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetailsState
                    .collect {
                        updateUiState(it)
                    }
            }
        }
    }

    private fun requestInitialProductDetails() {
        viewModel.onEvent(
            ProductDetailsEvent.LoadProductById(
                arguments?.getLong("productId")!!.toLong()
            )
        )
    }

    private fun updateUiState(state: com.behnamkhani.unboxd.productdetails.presentation.viewstate.ProductDetailsViewState) {
        binding.textViewTitle.text = state.productDetails.title
        binding.textViewDescription.text = state.productDetails.description
        binding.ratingBar.rating = state.productDetails.rating
        binding.imageView.setImage(state.productDetails.thumbnail)
        binding.textViewNumberOfReviews.text = state.numberOfReviews.toString()
        (activity as? AppCompatActivity)?.supportActionBar?.title = state.productDetails.category
        generateTags(state.productDetails.tags)
        binding.constraintLayoutMain.isVisible = !state.loading
    }

    private fun generateTags(tags: List<String>) {
        for(tag in tags){
            val textView = TextView(requireContext())
            textView.text = tag
            textView.setTextColor(resources.getColor(android.R.color.white))
            textView.gravity = Gravity.CENTER
            textView.setPadding(16, 8, 16, 8)

            applyRoundedBackground(textView)

            binding.linearLayoutTags.addView(textView)

            val params = textView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 16, 0)
            textView.layoutParams = params
        }
    }

    private fun applyRoundedBackground(textView: TextView) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = 16f
        drawable.setColor(ContextCompat.getColor(requireContext(), android.R.color.background_dark))
        textView.background = drawable
    }

    private fun prepareToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            show()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        _binding = null
    }
}