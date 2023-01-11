package com.example.navermovieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieapp.R
import com.example.navermovieapp.adapter.LoaderStateAdapter
import com.example.navermovieapp.adapter.MovieListAdapter
import com.example.navermovieapp.adapter.SearchKeywordAdapter
import com.example.navermovieapp.databinding.FragmentMovieListBinding
import com.example.navermovieapp.model.Keyword
import com.example.navermovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.logging.Level.INFO

@AndroidEntryPoint
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {
    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!

    private lateinit var searchedWord: String

    var list = ArrayList<Keyword>()

    val movieAdapter: MovieListAdapter by lazy {
        MovieListAdapter()
    }

    val viewModel: MovieViewModel by activityViewModels()


    val searchKeywordAdapter: SearchKeywordAdapter by lazy {
        SearchKeywordAdapter()
    }

    val loadStateAdapter: LoaderStateAdapter by lazy {
        LoaderStateAdapter { movieAdapter.retry() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchMovieList()
        observeIsDataChanged()
        searchTitle()
        historyTitle()
        returnMain()

        return binding.root
    }

    fun searchTitle() {
        binding.btnSearch.setOnClickListener {
            if (binding.etMovieSearch.text.isEmpty()) {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                searchedWord = binding.etMovieSearch.text.toString()
                if (list.size == 10) viewModel.deleteKeyword(list[list.size - 1])
                viewModel.addSearchData(Keyword(keyword = searchedWord))
                viewModel.postKeyword(searchedWord)

                binding.etMovieSearch.setText(null)
            }
        }
    }

    fun returnMain() {
        binding.btnReturn.setOnClickListener {
            binding.searchHistoryView.isVisible = false
            binding.searchView.isVisible = true
        }
    }

    fun historyTitle() {
        binding.btnRecent.setOnClickListener {
            binding.searchHistoryView.isVisible = true
            binding.searchView.isVisible = false
        }

    }

    fun fetchMovieList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest { movie ->
                binding.recycler.scrollToPosition(0)
                movieAdapter.submitData(viewLifecycleOwner.lifecycle, movie)
            }
        }
    }

    fun setupRecyclerView() {
        binding.recycler.apply {
            adapter = movieAdapter.withLoadStateFooter(loadStateAdapter)
            layoutManager = context?.let { LinearLayoutManager(it) }
            addItemDecoration(DividerItemDecoration(context, 1))
        }
        binding.searchHistoryRecyclerView.apply {
            adapter = searchKeywordAdapter
            scrollToPosition(0)
            itemAnimator = null
            layoutManager = context?.let { GridLayoutManager(it, 2) }
        }

        movieAdapter.setOnItemClickListener { item ->
            val action =
                MovieListFragmentDirections.actionMovieListFragmentToMovieWebViewFragment(item)
            findNavController().navigate(action)
        }

        searchKeywordAdapter.apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.searchHistoryRecyclerView.smoothScrollToPosition(0)
                }
            })
        }

        searchKeywordAdapter.setOnSearchKeywordClickListener { item ->
            viewModel.postKeyword(item.keyword)
            binding.searchHistoryView.isVisible = false
            binding.searchView.isVisible = true
        }
    }


    fun observeIsDataChanged() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getSavedKeywords().collectLatest {
                list = it.toMutableList() as ArrayList<Keyword>
                Log.d("ListTest", "$it")
                searchKeywordAdapter.submitList(it.toMutableList())
            }
        }
    }

    fun sendRequest(query: String) {
        viewModel.postKeyword(query)
    }
}

