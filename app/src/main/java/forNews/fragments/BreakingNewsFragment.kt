package forNews.fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mudrax.fitu.databinding.FragmentBreakingNewsBinding
import com.mudrax.fitu.NewsActivity
import forNews.adapters.NewsAdapter
import com.mudrax.fitu.R
import forNews.NewsViewModel
import forNews.util.Constants
import forNews.util.Constants.Companion.QUERY_PAGE_SIZE
import forNews.util.Resource


class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {


    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BreakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articleFragment22,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner , Observer {
            when(it){
                is Resource.Success<*> ->{
                    hideProgressBar()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults/ Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            val rvSearchNews = view.findViewById<RecyclerView>(R.id.rvBreakingNews)
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resource.Error<*> ->{
                    hideProgressBar()
                    it.message?.let {
                        Toast.makeText(activity , "an error occured " , Toast.LENGTH_SHORT).show()

                        Log.e(TAG , "an error occured : $it")
                    }
                }
                is Resource.Loading<*> ->{
                    showProgressBar()
                }
            }
        })
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
            val isTotalMoreThanVisible = totalItemCount>=QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeggining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("fitness")
                isScrolling=false

            }
            else{
                //val rvBreakingNews = view?.findViewById<RecyclerView>(R.id.rvBreakingNews)

            }
        }
    }

    private fun setupRecyclerView()
    {
        newsAdapter= NewsAdapter()
        val rvBreakingNews = view?.findViewById<RecyclerView>(R.id.rvBreakingNews)
        rvBreakingNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener )
        }

    }
}