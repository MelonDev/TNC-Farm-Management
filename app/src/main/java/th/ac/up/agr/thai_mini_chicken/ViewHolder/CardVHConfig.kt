package th.ac.up.agr.thai_mini_chicken.ViewHolder

import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class CardVHConfig(val fragment :FragmentActivity, val holder: CardViewHolder) {

    /* วิธีใช้งาน
        CardVHConfig.load(this, CardViewHolder(View(this)))
                .cardItem("","")
                .messages("")
                .paraTheme()

        CardVHConfig.load(this, CardViewHolder(View(this)))
                .cardItem("","")
                .messages("")
                .injectTheme()

        CardVHConfig.load(this, CardViewHolder(View(this)))
                .cardItem("","")
                .information("","","")

        CardVHConfig.load(this, CardViewHolder(View(this)))
                .titleItem("")
       */

    private val resource: Resources = fragment.resources
    private val infoTheme: Int = resource.getColor(R.color.colorPrimary)
    private val injectTheme: Int = resource.getColor(R.color.colorSkyBlue)
    private val parasiteTheme: Int = resource.getColor(R.color.colorLightGreen)

    init {
        resetTheme()
    }

    companion object {
        fun load(fragment: FragmentActivity, holder: CardViewHolder) = CardVHConfig(fragment, holder)
        val TITLE = "CARD_TITLE"
        val INJECTION = "CARD_INJECTION"
        val PARASITE = "CARD_PARASITE"
        val INFORMATION = "CARD_INFORMATION"

        val CHECKED_INJECTION = "CHECK_CARD_INJECTION"
        val CHECKED_PARASITE = "CHECK_CARD_PARASITE"
        val CHECKED_INFORMATION = "CHECK_CARD_INFORMATION"
    }


    fun titleItem(title :String) :CardVHConfig{
        holder.title_item.visibility = View.VISIBLE
        holder.title_item.text = title
        return this
    }

    fun cardItem(title: String,description :String) :CardVHConfig{
        holder.card_item.visibility = View.VISIBLE
        setCardTitle(title,description)
        return this
    }

    fun messages(message: String) :CardVHConfig{
        setCardMessageText(message)
        return this
    }

    fun infoTheme(date: String, age: String, objective: String):CardVHConfig {
        //cardThemeColor(infoTheme)
        cardThemeColor(resource.getColor(MelonTheme.from(fragment).getColor()))
        holder.info_area.visibility = View.VISIBLE
        setCardInfoText(date,age,objective)
        holder.icon_image.setImageDrawable(resource.getDrawable(R.drawable.ic_chicken_icon))
        return this
    }

    fun infoCheckTheme() :CardVHConfig{
        holder.message_area.visibility = View.VISIBLE
        cardThemeColor(infoTheme)
        return this
    }

    fun injectTheme() :CardVHConfig{
        holder.message_area.visibility = View.VISIBLE
        cardThemeColor(injectTheme)
        holder.icon_image.setImageDrawable(resource.getDrawable(R.drawable.ic_inject_icon))
        //setCardMessageText(message)
        return this
    }

    fun paraTheme() :CardVHConfig{
        holder.message_area.visibility = View.VISIBLE
        cardThemeColor(parasiteTheme)
        holder.icon_image.setImageDrawable(resource.getDrawable(R.drawable.ic_parasite_icon))
        //setCardMessageText(message)
        return this
    }

    fun checked(){
        holder.icon_image.setImageDrawable(resource.getDrawable(R.drawable.ic_checked_icon))
    }

    private fun resetTheme() {
        holder.title_item.visibility = View.GONE
        holder.card_item.visibility = View.GONE
        holder.info_area.visibility = View.GONE
        holder.message_area.visibility = View.GONE
    }

    private fun setCardTitle(title: String, des: String) {
        holder.card_title.text = title
        holder.card_des.text = des
    }

    private fun setCardInfoText(date: String, age: String, objective: String) {
        holder.info_age.text = age
        holder.info_date.text = date
        holder.info_objective.text = objective
    }

    private fun cardThemeColor(color: Int) {
        holder.icon_area.setCardBackgroundColor(color)
        holder.card_title.setTextColor(color)
    }

    private fun setCardMessageText(message: String) {
        holder.message_text.text = message
    }

}