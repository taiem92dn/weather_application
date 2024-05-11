package com.taingdev.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.databinding.FragmentHomeBinding
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.ui.adapter.SearchCityAdapter
import com.taingdev.weatherapp.util.EXTRA_CITY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var searchAdapter: SearchCityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBarLayout.title = getString(R.string.app_name)

        setupRecyclerView()
        bindEvents()
        observerData()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.handleGetFavoriteCities()
    }

    private fun observerData() {
        homeViewModel.favoriteCities.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchCityAdapter()
        binding.rvFavoriteCities.apply {
//            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun bindEvents() {
        binding.tvSearchBar.setOnClickListener {
            findNavController().navigate(R.id.action_search, null, null)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}