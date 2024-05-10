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
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialFadeThrough
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.databinding.FragmentSearchBinding
import com.taingdev.weatherapp.extensions.focusAndShowKeyboard
import com.taingdev.weatherapp.util.Utils
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.util.Locale

class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

//    private lateinit var searchAdapter: SearchAdapter
    private var query: String? = null

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

        binding.voiceSearch.setOnClickListener { startMicSearch() }
        binding.clearText.setOnClickListener {
            binding.searchView.clearText()
//            searchAdapter.swapDataSet(listOf())
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
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) {
            if (it) {
                binding.keyboardPopup.isGone = true
            } else {
                binding.keyboardPopup.show()
            }
        }
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())
    }
    private fun showData(data: List<Any>) {
        if (data.isNotEmpty()) {
//            searchAdapter.swapDataSet(data)
        } else {
//            searchAdapter.swapDataSet(ArrayList())
        }
    }

    private fun setupRecyclerView() {
//        searchAdapter = SearchAdapter(requireActivity(), emptyList())
//        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onChanged() {
//                super.onChanged()
//                binding.empty.isVisible = searchAdapter.itemCount < 1
//            }
//        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
//            adapter = searchAdapter
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
