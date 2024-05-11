package com.taingdev.weatherapp.ui.search

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialFadeThrough
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.databinding.FragmentSearchBinding
import com.taingdev.weatherapp.extensions.focusAndShowKeyboard
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.ui.adapter.SearchCityAdapter
import com.taingdev.weatherapp.util.EXTRA_CITY
import com.taingdev.weatherapp.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.util.Locale

@AndroidEntryPoint
class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchCityAdapter
    private var query: String? = null

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialFadeThrough().addTarget(view)
        reenterTransition = MaterialFadeThrough().addTarget(view)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setupRecyclerView()

        bindEvents()
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())

        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            searchViewModel.cities.collect {
                when (it) {
                    is ApiResource.Success -> {
                        showData(it.data)
                    }
                    else -> {
                    }
                }
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showLoading = searchViewModel.showLoading
            showError = searchViewModel.showError
            errorMessage = searchViewModel.errorMessage
        }
    }

    private fun bindEvents() {
        binding.voiceSearch.setOnClickListener { startMicSearch() }
        binding.clearText.setOnClickListener {
            binding.searchView.clearText()
            showData(emptyList())
            searchViewModel.showError.value = false
        }
        binding.layoutProgressBar.btRetry.setOnClickListener {
            if (!query.isNullOrEmpty()) search(query!!)
        }
        binding.searchView.apply {
            doAfterTextChanged {
                if (!it.isNullOrEmpty())
                    search(it.toString())
                else {
                    TransitionManager.beginDelayedTransition(binding.appBarLayout)
                    binding.voiceSearch.isVisible = true
                    binding.clearText.isGone = true
                }
            }
            focusAndShowKeyboard()
        }
        binding.keyboardPopup.apply {
            setOnClickListener {
                binding.searchView.focusAndShowKeyboard()
            }
        }

        postponeEnterTransition()
        requireView().doOnPreDraw {
            startPostponedEnterTransition()
        }

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) {
            if (it) {
                binding.keyboardPopup.isGone = true
            } else {
                binding.keyboardPopup.show()
            }
        }

        searchAdapter.onItemClickListener = {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(cityItem: CityItem) {
        findNavController().navigate(
            R.id.action_city_detail,
            bundleOf(EXTRA_CITY to  cityItem),
            null
        )
    }

    private fun showData(data: List<CityItem>?) {
        if (!data.isNullOrEmpty()) {
            searchAdapter.submitList(data)
        } else {
            searchAdapter.submitList(ArrayList())
        }
        binding.empty.isVisible = data.isNullOrEmpty()
                && searchViewModel.showError.value == false
                && searchViewModel.showLoading.value == false
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchCityAdapter()
        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.empty.isVisible = searchAdapter.itemCount < 1
            }
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        binding.keyboardPopup.shrink()
                    } else if (dy < 0) {
                        binding.keyboardPopup.extend()
                    }
                }
            })
        }
    }

    private fun search(query: String) {
        this.query = query
        TransitionManager.beginDelayedTransition(binding.appBarLayout)
        binding.voiceSearch.isGone = query.isNotEmpty()
        binding.clearText.isVisible = query.isNotEmpty()
        searchViewModel.handleSearch(query)
    }

    private fun startMicSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
        try {
            speechInputLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }
    }

    private val speechInputLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val spokenText: String? =
                    result?.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                binding.searchView.setText(spokenText)
            }
        }

    override fun onDestroyView() {
        Utils.hideKeyboard(view)
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        Utils.hideKeyboard(view)
    }
}

fun TextInputEditText.clearText() {
    text = null
}
