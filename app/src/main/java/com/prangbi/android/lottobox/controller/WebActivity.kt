package com.prangbi.android.lottobox.controller

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.helper.Util
import kotlinx.android.synthetic.main.nlotto_web_activity.*

/**
 * Created by Prangbi on 2017. 7. 25..
 */
class WebActivity : AppCompatActivity() {
    // Variable
    private lateinit var mContext: Activity
    private lateinit var loadingDialog: Dialog

    // Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.nlotto_web_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("title")
        loadingDialog = Util.makeLoadingDialog(mContext)
        initWebView()

        println(intent.getStringExtra("url"))
        webView.loadUrl(intent.getStringExtra("url"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Function
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(object: WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingDialog.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingDialog.dismiss()
            }
        })
    }
}
