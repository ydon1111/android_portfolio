package com.example.navermovieapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.example.navermovieapp.R
import com.example.navermovieapp.databinding.FragmentMovieWebViewBinding
import com.example.navermovieapp.model.MovieItem


@ExperimentalPagingApi
class MovieWebViewFragment : Fragment(R.layout.fragment_movie_web_view) {
    private var _binding: FragmentMovieWebViewBinding? = null
    val binding get() = _binding!!

    private val args: MovieWebViewFragmentArgs by navArgs()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieWebViewBinding.inflate(inflater, container, false)
        val movie = args.movieItem
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(movie.link)
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}