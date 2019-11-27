package th.ac.up.agr.thai_mini_chicken.Tools

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import th.ac.up.agr.thai_mini_chicken.R

class MelonTheme(private val activity: FragmentActivity) {

    companion object {
        fun from(activity: FragmentActivity) = MelonTheme(activity)
    }

    fun getStyle(): Int {

        val sharedPref: SharedPreferences = activity.getSharedPreferences("MELON_THEME", 0)
        val style = sharedPref.getInt("STYLE", -1)

        return when (style) {
            -1 -> R.style.MelonTheme_Amber_Material
            else -> style
        }
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

    fun getOverlay(): Int {

        return when (getStyle()) {
            R.style.MelonTheme_LightGreen_Material -> R.drawable.wallpaper_overlay_light_green
            R.style.MelonTheme_LightGreen_Flat -> R.drawable.wallpaper_overlay_light_green
            R.style.MelonTheme_LightBlue_Material -> R.drawable.wallpaper_overlay_light_blue
            R.style.MelonTheme_LightBlue_Flat -> R.drawable.wallpaper_overlay_light_blue
            R.style.MelonTheme_Red_Material -> R.drawable.wallpaper_overlay_red
            R.style.MelonTheme_Red_Flat -> R.drawable.wallpaper_overlay_red
            R.style.MelonTheme_DeepPurple_Material -> R.drawable.wallpaper_overlay_purple
            R.style.MelonTheme_DeepPurple_Flat -> R.drawable.wallpaper_overlay_purple
            R.style.MelonTheme_Amber_Material -> R.drawable.wallpaper_overlay_amber
            R.style.MelonTheme_Amber_Flat -> R.drawable.wallpaper_overlay_amber
            else -> {
                R.drawable.wallpaper_overlay_amber
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