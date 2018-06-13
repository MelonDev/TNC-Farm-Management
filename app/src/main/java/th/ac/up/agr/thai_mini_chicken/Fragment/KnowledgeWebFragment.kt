package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_knowledge_web.view.*

import th.ac.up.agr.thai_mini_chicken.R

class KnowledgeWebFragment : Fragment() {

    companion object {

        fun newInstance(url: String): KnowledgeWebFragment {
            val args = Bundle()
            //args.putString("TITLE", title)
            args.putString("URL", url)
            val fragment = KnowledgeWebFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_knowledge_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val title = arguments!!.getString("TITLE")
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
