package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder

class ProgramAdapter(val arr :ArrayList<String>) :RecyclerView.Adapter<CardViewHolder>() {

    private lateinit var context :Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card,parent,false)

        context = parent.context

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        when(arr[position]){
            CardVHConfig.TITLE -> {
                CardVHConfig.load(context, holder)
                        .titleItem("หัวข้อ")
            }
            CardVHConfig.INFORMATION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("หัวข้อ","รหัส xxxxxxxxxx")
                        .infoTheme("1 มกราคม 25561","10 วัน","พ่อ-แม่พันธุ์")
            }
            CardVHConfig.INJECTION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ฉีดยา","รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
            }
            CardVHConfig.PARASITE -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ถ่ายพยาธิ","รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
            }
            CardVHConfig.CHECKED_INFORMATION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("หัวข้อ","รหัส xxxxxxxxxx")
                        .infoTheme("1 มกราคม 25561","10 วัน","พ่อ-แม่พันธุ์")
                        .checked()
            }
            CardVHConfig.CHECKED_INJECTION -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ฉีดยา","รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
                        .checked()
            }
            CardVHConfig.CHECKED_PARASITE -> {
                CardVHConfig.load(context, holder)
                        .cardItem("ถ่ายพยาธิ","รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
                        .checked()
            }
        }

    }
}