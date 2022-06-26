package forNews.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mudrax.fitu.NewsActivity
import com.mudrax.fitu.R
import forNews.NewsViewModel
import forNews.adapters.NewsAdapter
import forNews.util.Constants
import forNews.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import forNews.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {
    val TAG = "SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment2_to_articleFragment2,
                bundle
            )
        }

        //delay in request
        var job: Job?=null
        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener { editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner , Observer {
            when(it){
                is Resource.Success<*> ->{
                    hideProgressBar()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults/ Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            val rvSearchNews = view.findViewById<RecyclerView>(R.id.rvSearchNews)
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resource.Error<*> ->{
                    hideProgressBar()
                    it.message?.let {
                        Toast.makeText(activity , "an error occured " , Toast.LENGTH_SHORT).show()

                        //Log.e(TAG , "an error occured : $it")
                    }
                }
                is Resource.Loading<*> ->{
                    showProgressBar()
                }
            }
        })
//        viewModel.searchNews.observe(viewLifecycleOwner , Observer { response->
//            when(response){
//                is Resource.Success<*> ->{
//                    hideProgressBar()
//                    response.data?.let {
//                        newsAdapter.differ.submitList(it.articles.toList())
//                        val totalPages = it.totalResults/ Constants.QUERY_PAGE_SIZE +2
//                        isLastPage = viewModel.searchNewsPage == totalPages
//                        if(isLastPage){
//                            val rvSearchNews = view.findViewById<RecyclerView>(R.id.rvSearchNews)
//                            rvSearchNews.setPadding(0,0,0,0)
//                        }
//                    }
//                }
//                is Resource.Error->{
//                    hideProgressBar()
//                    response.message?.let {
//                        Toast.makeText(activity , "an error occured " , Toast.LENGTH_SHORT).show()
//
//                        //Log.e(TAG , "an error occured : $it")
//                    }
//                }
//                is Resource.Loading->{
//                    showProgressBar()
//                }
//            }
//        })


    }


    private fun hideProgressBar() {
        val paginationProgressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBar)
        paginationProgressBar?.visibility = View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar() {
        val paginationProgressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBar)
        paginationProgressBar?.visibility = View.VISIBLE
        isLoading=true
    }



    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener()
    {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling=true
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = (firstVisibleItemPosition+visibleItemCount) >= totalItemCount
            val isNotAtBeggining = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount>= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeggining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                val etSearch = view?.findViewById<EditText>(R.id.etSearch)
                viewModel.searchNews(etSearch?.text.toString())
                isScrolling=false

            }
            else{
                //val rvSearchNews = view?.findViewById<RecyclerView>(R.id.rvSearchNews)

            }
        }
    }

    private fun setupRecyclerView()
    {
        newsAdapter= NewsAdapter()
        val rvSearchNews = view?.findViewById<RecyclerView>(R.id.rvSearchNews)
        rvSearchNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener )
        }

    }
}