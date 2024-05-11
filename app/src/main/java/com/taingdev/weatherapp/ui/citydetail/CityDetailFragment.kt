package com.taingdev.weatherapp.ui.citydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.databinding.FragmentCityDetailBinding
import com.taingdev.weatherapp.databinding.FragmentSearchBinding
import com.taingdev.weatherapp.model.ForecastDatumItem
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.ui.widget.ForecastDatumView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityDetailFragment : Fragment() {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CityDetailFragmentArgs>()

    private val cityDetailViewModel by viewModels<CityDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBarLayout.toolbar)

        args.extraCity.also {
            cityDetailViewModel.cityItem = it
            binding.appBarLayout.title = it.name
        }

        observerData()
        bindEvents()
    }

    private fun bindEvents() {
        binding.layoutProgressBar.btRetry.setOnClickListener {
            cityDetailViewModel.handleFetchData(args.extraCity)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            cityDetailViewModel.handleFetchData(args.extraCity)
        }

        binding.ivFavoriteIcon.setOnClickListener {
            if (cityDetailViewModel.isFavoriteCity.value == true) {
                cityDetailViewModel.handleRemoveFavoriteCity()
                Toast.makeText(requireContext(),
                    getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT).show()
            } else {
                cityDetailViewModel.handleSaveFavoriteCity()
                Toast.makeText(requireContext(),
                    getString(R.string.text_saved_to_favorite), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerData() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            showLoading = cityDetailViewModel.showLoading
            showError = cityDetailViewModel.showError
            errorMessage = cityDetailViewModel.errorMessage
            isFavoriteCity = cityDetailViewModel.isFavoriteCity
        }

        cityDetailViewModel.forecastItemLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResource.Success -> {
                    binding.forecastDatumItem = it.data?.data?.get(0)
                    bindForecastDatumView(it.data?.data)
                    binding.executePendingBindings()
                }

                else -> {

                }
            }
        }
    }

    private fun bindForecastDatumView(forecastDatumList: List<ForecastDatumItem>?) {
        binding.llForecastList.removeAllViews()
        forecastDatumList?.mapIndexed { index, forecastDatumItem ->
            if (index > 0) {
                binding.llForecastList.addView(
                    ForecastDatumView(requireContext()).apply {
                        forecastDatum = forecastDatumItem

                        layoutParams = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                            setMargins(
                                0,
                                requireContext().resources.getDimensionPixelSize(R.dimen.margin_small),
                                0, 0
                            )
                        }
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}