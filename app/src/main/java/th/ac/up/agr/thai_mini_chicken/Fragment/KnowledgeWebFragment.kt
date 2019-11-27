package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_knowledge_web.view.*

import th.ac.up.agr.thai_mini_chicken.R

class KnowledgeWebFragment : Fragment() {

    companion object {

        fun newInstance(url: String): KnowledgeWebFragment {
            val args = Bundle()
            args.putString("URL", url)
            val fragment = KnowledgeWebFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_knowledge_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments!!.getString("URL")

        val webview = view.knowledge_web_webView
        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = WebViewClient()
        webview.setInitialScale(1)
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.javaScriptEnabled = true
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = false
        webview.loadUrl(url)

    }

}
