package th.ac.up.agr.thai_mini_chicken.Tools.PreCaching

import android.content.Context

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PreCachingGridLayoutManager (context: Context, columns :Int) : GridLayoutManager(context,columns) {

    private val DEFAULT_EXTRA_LAYOUT_SPACE = 600
    var extraLayoutSpace = -1

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE
    }
}
