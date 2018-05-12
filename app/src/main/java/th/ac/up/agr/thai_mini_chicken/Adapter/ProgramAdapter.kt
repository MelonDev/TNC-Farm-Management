package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Animation
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder

class ProgramAdapter(val arr: ArrayList<String>) : RecyclerView.Adapter<CardViewHolder>() {

    private lateinit var context: Context

    private lateinit var lastHolder:CardViewHolder
    private var lastPosition :Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        context = parent.context

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    fun resetMenu(){
        if (lastPosition != -1){
        dialogHide(lastHolder)
        }
    }

    private fun dialogShow(cardViewHolder: CardViewHolder){
        cardViewHolder.menu_dialog.visibility = View.VISIBLE
        Animation.use.cardShow(cardViewHolder)
    }

    private fun dialogHide(cardViewHolder: CardViewHolder){
        cardViewHolder.menu_dialog.visibility = View.GONE
    }
    private fun dialogAnimationHide(cardViewHolder: CardViewHolder){
        Animation.use.cardHide(cardViewHolder)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.card_more.setOnClickListener {
            if (lastPosition == -1){
                lastHolder = holder
                lastPosition = position

                dialogShow(lastHolder)
            } else {
                dialogHide(lastHolder)

                lastHolder = holder
                lastPosition = position

                dialogShow(lastHolder)
            }
        }

        holder.menu_dialog.setOnClickListener {
            //dialogHide(lastHolder)
            //dialogHide(holder)
            dialogAnimationHide(lastHolder)
        }

        when (arr[position]) {
            CardVHConfig.TITLE -> {
                CardVHConfig.load(context, holder)
                        .titleItem("หัวข้อ")
            }
            CardVHConfig.INFORMATION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("หัวข้อ", "รหัส xxxxxxxxxx")
                        .infoTheme("1 มกราคม 25561", "10 วัน", "พ่อ-แม่พันธุ์")
            }
            CardVHConfig.INJECTION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ฉีดยา", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
            }
            CardVHConfig.PARASITE -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ถ่ายพยาธิ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
            }
            CardVHConfig.CHECKED_INFORMATION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("หัวข้อ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .infoCheckTheme()
                        .checked()
            }
            CardVHConfig.CHECKED_INJECTION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ฉีดยา", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
                        .checked()
            }
            CardVHConfig.CHECKED_PARASITE -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ถ่ายพยาธิ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
                        .checked()
            }
        }

    }
}