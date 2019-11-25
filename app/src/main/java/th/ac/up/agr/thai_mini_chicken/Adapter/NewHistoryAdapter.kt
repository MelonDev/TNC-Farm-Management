package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigItems
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ItemsParams
import com.mylhyl.circledialog.params.TextParams
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Fragment.NewHistoryFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class NewHistoryAdapter(val fragment: NewHistoryFragment, val ID: String, val data: ArrayList<CardData>) : RecyclerView.Adapter<CardViewHolder>() {

    private val activity = fragment.activity!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card_key = data[position].cardID

        val userRef = Firebase.reference.child("ผู้ใช้").child(ID)
        val container = userRef.child("รายการ")


        if (data[position].cardID.contentEquals("null")) {
            setTitle(data[position], holder)
        } else {
            getValue(data[position], holder)
        }

        holder.card_more.setOnClickListener {
            showDialog(arrayOf("กู้คืน", "ลบ"), ID, data[position].cardID)
        }

        holder.card_item.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("CARD_KEY", data[position].cardID)
            intent.putExtra("USER_ID", ID)
            activity.startActivity(intent)
            //Log.e("ACTION", "CLICKED")
        }


        Picasso.get().load(R.drawable.ic_checked_icon).into(holder.icon_image)

        holder.card_title.setTextColor(ContextCompat.getColor(activity, R.color.colorText))
        holder.icon_area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorText))

    }


    fun setTitle(card: CardData, holder: CardViewHolder) {
        holder.title_item.visibility = View.VISIBLE
        holder.card_item.visibility = View.GONE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.GONE
        holder.histort_area.visibility = View.GONE

        val today = Calendar.getInstance()
        val part = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        val x = Date().reDate(card.createDate)

        part.time = x

        calendar.apply {
            set(Calendar.YEAR,part.get(Calendar.YEAR))
            set(Calendar.MONTH,part.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH,part.get(Calendar.DAY_OF_MONTH))
        }

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        if (days == 0) {
            holder.title_item.text = "วันนี้"
            //Log.e((difference/ (1000 * 60 * 60)).toString(),days.toString())
        } else if (days == 1) {
            holder.title_item.text = "เมื่อวานนี้"
        } else {
            holder.title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"
        }

    }

    fun getValue(card: CardData, holder: CardViewHolder) {
        holder.title_item.visibility = View.GONE
        holder.card_item.visibility = View.VISIBLE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.VISIBLE
        holder.histort_area.visibility = View.GONE

        setData(holder,card)

    }

    fun setData(holder: CardViewHolder, slot: CardData) {
        if (slot.cardName.isNotEmpty()) {
            holder.card_title.text = slot.cardName
        } else {
            holder.card_title.text = "ชื่อรายการ"
        }

        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        calendar.set(slot.dateYear.toInt(), slot.dateMonth.toInt() - 1, slot.dateDay.toInt())

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()

        if(days >= 0){
            val w: Int = days / 7
            val d: Int = days % 7

            var week = slot.ageWeek.toInt() + w
            var day = slot.ageDay.toInt() + d

            if (day >= 7) {
                week += (day / 7)
                day = (day % 7)
            }

            holder.info_age.text = "$week สัปดาห์ $day วัน"
        }else {
            holder.info_age.text = "ยังไม่ถึงวันรับเข้า"

        }

        holder.apply {
            info_date.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
            info_objective.text = ConvertCard().getObjective(slot.userObjective)
        }


    }


    fun showDialog(arr: Array<String>, userID: String, cardKey: String) {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setItems(arr) { parent, view, position, id ->
                    when (position) {
                        0 -> {
                            val ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายละเอียด").child("status")

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e("","")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.value != null) {
                                        ref.setValue("ACTIVE"){ p0, p1 ->
                                            if (p0 != null) {
                                                showErrorDialog()
                                            } else {
                                                showMoveDialog()
                                            }
                                        }
                                    }
                                }
                            })

                        }
                        1 -> {
                            showDeleteDialog(position, userID, cardKey)
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }
                .show()
    }


    fun showDeleteDialog(positions: Int, userID: String, cardKey: String) {
        val arr = arrayOf("ยืนยันการลบ")

        val database = FirebaseDatabase.getInstance().reference
        val path = database.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey)

        CircleDialog.Builder(activity
        )
                .configDialog { params ->
                    params.animStyle = R.style.dialogWindowAnim
                    params.canceledOnTouchOutside = false
                }
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(activity, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    path.removeValue{p3,_->
                        if (p3 != null){
                            showErrorDialog()
                        }else {
                            showConDialog()
                        }
                    }
                }
                .configItems { params ->
                    params!!.textColor = ContextCompat.getColor(activity, R.color.colorRed)
                    params.backgroundColorPress = ContextCompat.getColor(activity, R.color.colorRed)
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }
                .show()
    }

    fun showConDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("ลบเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                    //activity.onLoad()
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }

                .show()


    }

    fun showMoveDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("กู้คืนเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                    //activity.onLoad()
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }

                .show()


    }

    fun showErrorDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("เกิดข้อผิดพลาด")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }

                .show()


    }

}