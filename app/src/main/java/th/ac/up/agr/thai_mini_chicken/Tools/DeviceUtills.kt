package th.ac.up.agr.thai_mini_chicken.Tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.WindowManager

class DeviceUtills(){

    private lateinit var context : Context

    constructor(context: Context) : this() {
        this.context = context
    }

    fun getScreenHeight(): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }


    fun getScreenWidth(): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

}