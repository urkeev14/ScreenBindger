package com.example.screenbindger.view.fragment.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.databinding.FragmentTrendingBinding
import com.example.screenbindger.util.adapter.recyclerview.ItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.util.extensions.snack
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TrendingFragment : DaggerFragment(),
    OnCardItemClickListener {

    @Inject
    lateinit var viewModel: TrendingViewModel

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initRecyclerView()
        observeViewModel()
        return view
    }

    private fun initRecyclerView() {
        binding.rvTrending.also {
            it.layoutManager = GridLayoutManager(requireContext(), 2)
            it.addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
            it.adapter = ItemMovieRecyclerViewAdapter(this)
        }
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.response.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val list = response.body()?.list?.toMutableList() ?: mutableListOf()
                binding.rvTrending.adapter =
                    ItemMovieRecyclerViewAdapter(this, list)
                binding.rvTrending.startLayoutAnimation()
            }
        })
    }

    override fun onCardItemClick(position: Int) {
        val movieId = viewModel.list?.get(position)?.id

        if (movieId != null) {
            val action =
                TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(movieId)
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()

        showDetails()
    }

    fun showDetails() {
        val message = viewModel.getAccountDetails()
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}