package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import kotlinx.android.synthetic.main.activity_add_program.*
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.Fragment.HistoryFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Animation
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder

class ProgramAdapter(var fragment: FragmentActivity, val ID: Int, val arr: ArrayList<String>) : RecyclerView.Adapter<CardViewHolder>() {

    private lateinit var context: Context

    private lateinit var lastHolder: CardViewHolder
    private var lastPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        //val a = LayoutInflater.from(parent.context)
        //val themeWrapper = ContextThemeWrapper(parent.context,R.style.MelonTheme_LightGreen_Material)
        //val b = a.cloneInContext(themeWrapper)
        //val view = b.inflate(R.layout.data_card,parent,false)
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
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                //.setTitle(title)
                //.setTitleColor(ContextCompat.getColor(fragment, R.color.colorPrimary))
                //.setSubTitle(sub)
                .setItems(arr) { parent, view, position, id ->

                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(fragment, R.color.colorText)
                    }
                })
                .show()
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
/*
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

*/

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
            /* var popupMenu = PopupMenu(context,it)
             popupMenu.inflate(R.menu.menu_main)
             popupMenu.show()
             */
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

/*
            var powerMenu = PowerMenu.Builder(context)
                    .addItem(PowerMenuItem("ทดสอบ",false))
                    .addItem(PowerMenuItem("HI",false))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)

                    .setMenuRadius(10f)
                    .setMenuShadow(10f)
                    .setSelectedEffect(true)
                    .build()

            powerMenu.showAsDropDown(holder.card_more)
*/
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