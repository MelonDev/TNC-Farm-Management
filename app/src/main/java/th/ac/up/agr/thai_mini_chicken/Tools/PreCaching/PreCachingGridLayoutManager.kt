package th.ac.up.agr.thai_nativechickenexpertsystem.Tools.PreCaching

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

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
