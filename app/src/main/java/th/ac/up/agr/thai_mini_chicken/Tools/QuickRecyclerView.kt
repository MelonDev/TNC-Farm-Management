package th.ac.up.agr.thai_nativechickenexpertsystem.Tools

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import th.ac.up.agr.thai_nativechickenexpertsystem.Tools.PreCaching.PreCachingGridLayoutManager
import th.ac.up.agr.thai_nativechickenexpertsystem.Tools.PreCaching.PreCachingLinearLayoutManager

class QuickRecyclerView(private val context: Context, private val recyclerView: RecyclerView, private val layoutManagerType: String, private val columns: Int, private val orientation: String, private val reverse: Boolean, private val overScroll: String, private val cacheQuality: String) {

    fun recyclerView(): RecyclerView {
        when (layoutManagerType) {
            "linear" -> {
                val layoutManager = PreCachingLinearLayoutManager(context)
                layoutManager.orientation = linearOrientation()
                //layoutManager.extraLayoutSpace = extraLayoutSpace()
                layoutManager.reverseLayout = reverse
                recyclerView.layoutManager = layoutManager
            }
            "grid" -> {
                val layoutManager = PreCachingGridLayoutManager(context,columns)
                layoutManager.orientation = gridOrientation()
                //layoutManager.extraLayoutSpace = extraLayoutSpace()
                layoutManager.reverseLayout = reverse
                recyclerView.layoutManager = layoutManager
            }
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(10000)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.overScrollMode = overScroll()
        recyclerView.drawingCacheQuality = cacheQuality()

        return recyclerView
    }


    private fun extraLayoutSpace(): Int {
        return when (orientation) {
            "vertical" -> DeviceUtills(context).getScreenHeight()
            "horizontal" -> DeviceUtills(context).getScreenWidth()
            else -> DeviceUtills(context).getScreenHeight()
        }
    }

    private fun linearOrientation(): Int {
        return when (orientation) {
            "vertical" -> LinearLayoutManager.VERTICAL
            "horizontal" -> LinearLayoutManager.HORIZONTAL
            else -> LinearLayoutManager.VERTICAL
        }
    }


    private fun gridOrientation(): Int {
        return when (orientation) {
            "vertical" -> GridLayoutManager.VERTICAL
            "horizontal" -> GridLayoutManager.HORIZONTAL
            else -> GridLayoutManager.VERTICAL
        }
    }


    private fun overScroll(): Int {
        return when (overScroll) {
            "alway" -> View.OVER_SCROLL_ALWAYS
            "never" -> View.OVER_SCROLL_NEVER
            "content_scroll" -> View.OVER_SCROLL_IF_CONTENT_SCROLLS
            else -> View.OVER_SCROLL_ALWAYS
        }
    }

    private fun cacheQuality(): Int {
        return when (cacheQuality) {
            "auto" -> View.DRAWING_CACHE_QUALITY_AUTO
            "high" -> View.DRAWING_CACHE_QUALITY_HIGH
            "low" -> View.DRAWING_CACHE_QUALITY_LOW
            else -> View.DRAWING_CACHE_QUALITY_AUTO
        }
    }


}