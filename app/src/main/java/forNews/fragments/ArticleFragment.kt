package forNews.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import forNews.fragments.ArticleFragmentArgs
import com.mudrax.fitu.R
import com.mudrax.fitu.NewsActivity
import forNews.NewsViewModel

class ArticleFragment:Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            viewModel.saveArticles(article)
            Snackbar.make(view , "Article saved successfully" , Snackbar.LENGTH_SHORT).show()
        }
    }
}