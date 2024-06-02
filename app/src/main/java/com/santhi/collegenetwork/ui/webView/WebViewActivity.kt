package com.santhi.collegenetwork.ui.webView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.santhi.collegenetwork.R
import com.santhi.collegenetwork.businessLogic.loading.Loading
import com.santhi.collegenetwork.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //   Loading.showAlertDialogForLoading(this)
        val webView: WebView = binding.web
        val link = intent.getStringExtra("link")
        val  headText = intent.getStringExtra("headText")
        binding.back.setOnClickListener {
            finish()
        }

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true

        // Enable caching

        //  webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        // Optimize WebView load
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // Enable hardware acceleration
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

        // Load the URL
        link?.let {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    // Hide ProgressBar when page finishes loading
                    Loading.dismissDialogForLoading()
                }
            }

            webView.loadUrl(it)
            binding.headText.text= headText
        }
    }
}