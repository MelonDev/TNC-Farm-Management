package th.ac.up.agr.thai_mini_chicken.Tools.PreCaching

import android.content.Context

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PreCachingLinearLayoutManager(context: Context) : LinearLayoutManager(context){

    private val DEFAULT_EXTRA_LAYOUT_SPACE = 600
    var extraLayoutSpace = -1

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE
    }

}