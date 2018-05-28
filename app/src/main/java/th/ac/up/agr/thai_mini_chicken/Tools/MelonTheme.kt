package th.ac.up.agr.thai_mini_chicken.Tools

import android.os.Build
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.Toast
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme

class MelonTheme(private val activity: FragmentActivity) {

    companion object {
        fun from(activity: FragmentActivity) = MelonTheme(activity)
    }

    fun getStyle(): Int {
        //return R.style.MelonTheme_DeepPurple_Material
        return AppTheme(activity).read()
    }

    fun getStyleForFragment(activity: FragmentActivity?, inflater: LayoutInflater): LayoutInflater {
        val themeWrapper = ContextThemeWrapper(activity, getStyle())
        val layoutInflater = inflater.cloneInContext(themeWrapper)
        return layoutInflater
    }

    fun getColor(): Int {

        return when (getStyle()) {
            R.style.MelonTheme_LightGreen_Material -> R.color.colorLightGreen
            R.style.MelonTheme_LightGreen_Flat -> R.color.colorLightGreen
            R.style.MelonTheme_LightBlue_Material -> R.color.colorLightBlue
            R.style.MelonTheme_LightBlue_Flat -> R.color.colorLightBlue
            R.style.MelonTheme_Red_Material -> R.color.colorRed
            R.style.MelonTheme_Red_Flat -> R.color.colorRed
            R.style.MelonTheme_DeepPurple_Material -> R.color.colorDeepPurple
            R.style.MelonTheme_DeepPurple_Flat -> R.color.colorDeepPurple
            R.style.MelonTheme_Amber_Material -> R.color.colorAmber
            R.style.MelonTheme_Amber_Flat -> R.color.colorAmber
            else -> {
                R.color.colorAmber
            }
        }
    }

    fun getStatusBarOverlay(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            R.color.colorLightStatusBarOverlay
        } else {
            R.color.colorStatusBarOverlay
        }
    }

}