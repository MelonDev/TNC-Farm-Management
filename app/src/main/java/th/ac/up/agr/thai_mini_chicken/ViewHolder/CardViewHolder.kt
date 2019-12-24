package th.ac.up.agr.thai_mini_chicken.ViewHolder

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.data_card.view.*
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Fragment.NewHistoryFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import java.util.*

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    val title_item :TextView = itemView.data_card_title_item
    val card_item: CardView = itemView.data_card_card_item
    val icon_area :CardView = itemView.data_card_card_icon_area
    val icon_image :ImageView = itemView.data_card_card_icon_image
    val card_title :TextView = itemView.data_card_title_text_view
    val card_des :TextView = itemView.data_card_title_description_text_view
    val card_more :CardView = itemView.data_card_card_more_area
    val info_date :TextView = itemView.data_card_card_info_date_text_view
    val info_age :TextView = itemView.data_card_card_info_age_text_view
    val info_objective :TextView = itemView.data_card_card_info_objective_text_view
    val info_area :LinearLayout = itemView.data_card_card_info_area
    val message_area :RelativeLayout = itemView.data_card_card_message_area
    val message_text :TextView = itemView.data_card_card_message_text_view

    val menu_dialog :RelativeLayout = itemView.data_card_card_menu_dialog
    val card_in_dialog :CardView = itemView.data_card_card_menu_dialog_card
    val layout :RelativeLayout = itemView.data_card_layout
    val histort_area :CardView = itemView.data_card_card_history_area

    fun bind(data: CardData, ID: String, fragment: NewHistoryFragment) {

        if (data.cardID.contentEquals("null")) {
            setTitle(data)
        } else {
            getValue(data)
        }

        card_more.setOnClickListener {
            showDialog(arrayOf("กู้คืน", "ลบ"), ID, data.cardID, fragment)
        }

        card_item.setOnClickListener {
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra("CARD_KEY", data.cardID)
            intent.putExtra("USER_ID", ID)
            itemView.context.startActivity(intent)
        }


        Picasso.get().load(R.drawable.ic_checked_icon).into(icon_image)

        card_title.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorText))
        icon_area.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorText))

    }

    fun setTitle(data: CardData) {
        title_item.visibility = View.VISIBLE
        card_item.visibility = View.GONE
        message_area.visibility = View.GONE
        info_area.visibility = View.GONE
        histort_area.visibility = View.GONE

        val today = Calendar.getInstance()
        val part = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        val x = Date().reDate(data.createDate)

        part.time = x

        calendar.apply {
            set(Calendar.YEAR, part.get(Calendar.YEAR))
            set(Calendar.MONTH, part.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, part.get(Calendar.DAY_OF_MONTH))
        }

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        if (days == 0) {
            title_item.text = "วันนี้"
        } else if (days == 1) {
            title_item.text = "เมื่อวานนี้"
        } else {
            title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"
        }

    }

    fun getValue(data: CardData) {
        title_item.visibility = View.GONE
        card_item.visibility = View.VISIBLE
        message_area.visibility = View.GONE
        info_area.visibility = View.VISIBLE
        histort_area.visibility = View.GONE

        setData(data)
    }

    fun setData(data: CardData) {
        if (data.cardName.isNotEmpty()) {
            card_title.text = data.cardName
        } else {
            card_title.text = "ชื่อรายการ"
        }

        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        calendar.set(data.dateYear.toInt(), data.dateMonth.toInt() - 1, data.dateDay.toInt())

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()

        if (days >= 0) {
            val w: Int = days / 7
            val d: Int = days % 7

            var week = data.ageWeek.toInt() + w
            var day = data.ageDay.toInt() + d

            if (day >= 7) {
                week += (day / 7)
                day = (day % 7)
            }

            info_age.text = "$week สัปดาห์ $day วัน"
        } else {
            info_age.text = "ยังไม่ถึงวันรับเข้า"

        }

        data.apply {
            info_date.text = "${data.dateDay} ${ConvertCard().getMonth(data.dateMonth)} ${ConvertCard().getYear(data.dateYear)}"
            info_objective.text = ConvertCard().getObjective(data.userObjective)
        }


    }


    private fun showDialog(arr: Array<String>, userID: String, cardKey: String, fragment: NewHistoryFragment) {
        CircleDialog.Builder()
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setItems(arr) { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            val ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายละเอียด").child("status")

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e("", "")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.value != null) {
                                        ref.setValue("ACTIVE") { p0, p1 ->
                                            if (p0 != null) {
                                                showErrorDialog(fragment)
                                            } else {
                                                showMoveDialog(fragment)
                                            }
                                        }
                                    }
                                }
                            })

                        }
                        1 -> {
                            fragment.fragmentManager?.let {
                                showDeleteDialog(userID, cardKey, fragment)

                            }
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(itemView.context, R.color.colorText)
                }
                .show(fragment.fragmentManager)
    }


    private fun showDeleteDialog(userID: String, cardKey: String, fragment: NewHistoryFragment) {
        val arr = arrayOf("ยืนยันการลบ")

        val database = FirebaseDatabase.getInstance().reference
        val path = database.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey)

        CircleDialog.Builder()
                .configDialog { params ->
                    params.animStyle = R.style.dialogWindowAnim
                    params.canceledOnTouchOutside = false
                }
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(itemView.context, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    path.removeValue { p3, _ ->
                        if (p3 != null) {
                            showErrorDialog(fragment)
                        } else {
                            showConDialog(fragment)
                        }
                    }
                }
                .configItems { params ->
                    params!!.textColor = ContextCompat.getColor(itemView.context, R.color.colorRed)
                    params.backgroundColorPress = ContextCompat.getColor(itemView.context, R.color.colorRed)
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(itemView.context, R.color.colorText)
                }
                .show(fragment.fragmentManager)
    }

    fun showConDialog(fragment: NewHistoryFragment) {

        fragment.activity?.let {
            CircleDialog.Builder(
            )
                    .configDialog { params -> params.canceledOnTouchOutside = false }
                    .setText("ลบเรียบร้อย")
                    .configText { params ->
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(itemView.context, MelonTheme.from(it).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                    .setPositive("รับทราบ") {
                    }
                    .configPositive { params ->
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(itemView.context, R.color.colorText)
                    }

                    .show(fragment.fragmentManager)
        }

    }

    fun showMoveDialog(fragment: NewHistoryFragment) {

        fragment.activity?.let {
            CircleDialog.Builder(
            )
                    .configDialog { params -> params.canceledOnTouchOutside = false }
                    .setText("กู้คืนเรียบร้อย")
                    .configText { params ->
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(itemView.context, MelonTheme.from(it).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                    .setPositive("รับทราบ") {
                    }
                    .configPositive { params ->
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(itemView.context, R.color.colorText)
                    }

                    .show(fragment.fragmentManager)
        }
    }

    fun showErrorDialog(fragment: NewHistoryFragment) {

        fragment.activity?.let {
            CircleDialog.Builder(
            )
                    .configDialog { params -> params.canceledOnTouchOutside = false }
                    .setText("เกิดข้อผิดพลาด")
                    .configText { params ->
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(itemView.context, MelonTheme.from(it).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                    .setPositive("รับทราบ") {}
                    .configPositive { params ->
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(itemView.context, R.color.colorText)
                    }

                    .show(fragment.fragmentManager)
        }

    }

}