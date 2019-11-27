package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mylhyl.circledialog.CircleDialog

import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Animation

import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder

class ProgramAdapter(var fragment: FragmentActivity, val ID: Int, val arr: ArrayList<String>) : RecyclerView.Adapter<CardViewHolder>() {

    private lateinit var context: Context

    private lateinit var lastHolder: CardViewHolder
    private var lastPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        context = parent.context

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    fun resetMenu() {
        if (lastPosition != -1) {
            dialogHide(lastHolder)
        }
    }

    private fun dialogShow(cardViewHolder: CardViewHolder) {
        cardViewHolder.menu_dialog.visibility = View.VISIBLE
        Animation.use.cardShow(cardViewHolder)
    }

    private fun dialogHide(cardViewHolder: CardViewHolder) {
        cardViewHolder.menu_dialog.visibility = View.GONE
    }

    private fun dialogAnimationHide(cardViewHolder: CardViewHolder) {
        Animation.use.cardHide(cardViewHolder)
    }

    fun showDialog(ID: Int, title: String, sub: String, arr: Array<String>) {
        CircleDialog.Builder(fragment
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                //.setTitle(title)
                //.setTitleColor(ContextCompat.getColor(fragment, R.color.colorPrimary))
                //.setSubTitle(sub)
                .setItems(arr) { parent, view, position, id ->

                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(fragment, R.color.colorText)
                }
                .show()
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {


        holder.card_item.setOnClickListener {
            when (ID) {
                0 -> {
                    val intent = Intent(context, DetailActivity::class.java)
                    context.startActivity(intent)
                }
                1 -> {
                }
                2 -> {
                }
            }
        }

        holder.card_more.setOnClickListener {

            when (ID) {
                0 -> {
                    showDialog(0, "หัวข้อ", "รหัส xxxxxxxxxx", arrayOf("แก้ไข", "เก็บประวัติ", "ลบ"))
                }
                1 -> {
                    showDialog(1, "หัวข้อ", "รหัส xxxxxxxxxx", arrayOf("เก็บประวัติ", "ลบ"))
                }
                2 -> {
                    showDialog(2, "หัวข้อ", "รหัส xxxxxxxxxx", arrayOf("กู้คืน", "ลบ"))
                }
            }


        }

        when (arr[position]) {
            CardVHConfig.TITLE -> {
                CardVHConfig.load(fragment, holder)
                        .titleItem("หัวข้อ")
            }
            CardVHConfig.INFORMATION -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("หัวข้อ", "รหัส xxxxxxxxxx")
                        .infoTheme("1 มกราคม 25561", "10 วัน", "พ่อ-แม่พันธุ์")
            }
            CardVHConfig.INJECTION -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("ฉีดยา", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
            }
            CardVHConfig.PARASITE -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("ถ่ายพยาธิ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
            }
            CardVHConfig.CHECKED_INFORMATION -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("หัวข้อ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .infoCheckTheme()
                        .checked()
            }
            CardVHConfig.CHECKED_INJECTION -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("ฉีดยา", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .injectTheme()
                        .checked()
            }
            CardVHConfig.CHECKED_PARASITE -> {
                CardVHConfig.load(fragment, holder)
                        .cardItem("ถ่ายพยาธิ", "รหัส xxxxxxxxxx")
                        .messages("ข้อความ")
                        .paraTheme()
                        .checked()
            }
        }

    }
}