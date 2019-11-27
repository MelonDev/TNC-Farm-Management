package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.mylhyl.circledialog.CircleDialog

import com.mylhyl.circledialog.params.*
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.AddNotiCardActivity
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.DetailNotificationActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.*
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class DetailNotificationAdapter(val activity: DetailNotificationActivity, val ID: Int, val data: ArrayList<Event>, val unData: ArrayList<Event>, val cardKey: String, val userID: String) : RecyclerView.Adapter<CardViewHolder>() {

    var arr = ArrayList<Event>()


    lateinit var waitDialog: DialogFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (ID == 0 || ID == 2 || ID == 3) {
            data.size
        } else {
            unData.size
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {


        var slot = Event()
        if (ID == 0 && data.size > 0) {
            slot = data[position]
        } else if (ID == 1 && unData.size > 0) {
            slot = unData[position]
        } else if (ID == 2 && data.size > 0) {
            slot = data[position]
        } else if(ID == 3 && data.size > 0){
            slot = data[position]
            holder.card_more.visibility = View.GONE

        }
        if (data.size > 1) {
            if (slot.title.contentEquals("null")) {
                setTitle(position, holder)
            } else {
                getValue(position, holder)
            }
        } else if (unData.size > 1) {
            if (slot.title.contentEquals("null")) {
                setTitle(position, holder)
            } else {
                getValue(position, holder)
            }
        }

        holder.card_more.setOnClickListener {
            if (ID == 0) {
                showDialog(arrayOf("แก้ไข", "ทำแล้ว", "ไม่ทำ", "ลบ"), userID, position)
            } else if (ID == 1) {
                if (unData[position].status.contentEquals("PASSED")) {
                    showDialog(arrayOf("แก้ไข", "กู้คืน", "ลบ"), userID, position)
                } else if (unData[position].status.contentEquals("CANCEL")) {
                    showDialog(arrayOf("แก้ไข", "ทำแล้ว", "กู้คืน", "ลบ"), userID, position)
                }

            } else if (ID == 2) {
                showDialog(arrayOf("แก้ไข", "ลบ"), userID, position)
            }

        }


    }

    fun showDialog(arr: Array<String>, userID: String, pos: Int) {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }

                .setItems(arr) { parent, view, position, id ->
                    var ref = Firebase.reference
                    var refDelete = Firebase.reference
                    var status = ""
                    if (ID == 0) {
                        ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายการที่ต้องทำ").child(data[pos].cardID).child("status")
                        refDelete = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายการที่ต้องทำ").child(data[pos].cardID)

                        status = data[pos].status
                    } else if (ID == 1) {
                        ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายการที่ต้องทำ").child(unData[pos].cardID).child("status")
                        refDelete = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายการที่ต้องทำ").child(unData[pos].cardID)

                        status = unData[pos].status
                    } else if (ID == 2) {
                        ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รูปแบบ").child(cardKey).child("รายการที่ต้องทำ").child(data[pos].cardID)
                        status = data[pos].status
                    }

                    if (status.contentEquals("ACTIVE")) {
                        if (ID == 2) {
                            when (position) {
                                0 -> {
                                    goToEdit(data[pos].cardID,3)
                                }
                                1 -> {
                                    showDeleteDialog(ref)
                                }
                            }
                        } else {
                            when (position) {
                                0 -> {
                                    goToEdit(data[pos].cardID,1)
                                }
                                1 -> {
                                    setWaitDialog("กำลังบันทึก...")
                                    ref.setValue("PASSED") { p0, _ ->
                                        if (p0 != null) {
                                            showErrorDialog()
                                        } else {
                                            showMoveDialog(0)
                                        }
                                    }
                                }
                                2 -> {
                                    setWaitDialog("กำลังบันทึก...")
                                    ref.setValue("CANCEL") { p0, _ ->
                                        if (p0 != null) {
                                            showErrorDialog()
                                        } else {
                                            showMoveDialog(0)
                                        }
                                    }
                                }
                                3 -> {
                                    showDeleteDialog(refDelete)
                                }
                            }
                        }
                    } else if (status.contentEquals("PASSED")) {
                        when (position) {
                            0 -> {
                                goToEdit(unData[pos].cardID,1)
                            }
                            1 -> {
                                setWaitDialog("กำลังบันทึก...")
                                ref.setValue("ACTIVE") { p0, _ ->
                                    if (p0 != null) {
                                        showErrorDialog()
                                    } else {
                                        showMoveDialog(1)
                                    }
                                }
                            }
                            2 -> {
                                showDeleteDialog(refDelete)
                            }
                        }
                    } else if (status.contentEquals("CANCEL")) {
                        when (position) {
                            0 -> {
                                goToEdit(unData[pos].cardID,1)
                            }
                            1 -> {
                                setWaitDialog("กำลังบันทึก...")
                                ref.setValue("PASSED") { p0, _ ->
                                    if (p0 != null) {
                                        showErrorDialog()
                                    } else {
                                        showMoveDialog(0)
                                    }
                                }
                            }
                            2 -> {
                                setWaitDialog("กำลังบันทึก...")
                                ref.setValue("ACTIVE") { p0, _ ->
                                    if (p0 != null) {
                                        showErrorDialog()
                                    } else {
                                        showMoveDialog(1)
                                    }
                                }
                            }
                            3 -> {
                                showDeleteDialog(refDelete)
                            }
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

    fun showDeleteDialog(ref: DatabaseReference) {
        val arr = arrayOf("ยืนยันการลบ")

        CircleDialog.Builder(activity
        )
                .configDialog { params ->
                    params.animStyle = R.style.dialogWindowAnim
                    params.canceledOnTouchOutside = false
                }
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(activity, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    setWaitDialog("กำลังลบ...")
                    ref.removeValue { p3, _ ->
                        if (p3 != null) {
                            showErrorDialog()
                        } else {
                            showConDialog()
                        }
                    }

                }
                .configItems { params ->
                    params!!.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                    params.backgroundColorPress = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorText)
                }
                .show()
    }

    fun goToEdit(notiKey: String,id: Int) {
        val intent = Intent(activity, AddNotiCardActivity::class.java)
        intent.putExtra("ID", id.toString())
        intent.putExtra("USER_ID", userID)
        intent.putExtra("CARD_KEY", cardKey)
        intent.putExtra("NOTI_KEY", notiKey)
        activity.startActivity(intent)
    }

    fun showConDialog() {
        waitDialog.dismiss()
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
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }

                .show()


    }

    fun showMoveDialog(action: Int) {
        waitDialog.dismiss()
        var title = ""
        if (action == 0) {
            title = "เก็บไปที่ประวัติเรียบร้อย"
        } else if (action == 1) {
            title = "กู้คืนเรียบร้อย"
        }
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(title)
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

    fun setWaitDialog(str: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(str)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)
    }

    fun showErrorDialog() {
        waitDialog.dismiss()
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

    fun setTitle(position: Int, holder: CardViewHolder) {
        holder.title_item.visibility = View.VISIBLE
        holder.card_item.visibility = View.GONE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.GONE
        holder.histort_area.visibility = View.GONE

        var slot = Event()
        if (ID == 0) {
            slot = data[position + 1]
        } else if (ID == 1) {
            slot = unData[position + 1]
        } else if (ID == 2 || ID == 3) {
            slot = data[position + 1]
        }

        val userRef = Firebase.reference.child("ผู้ใช้").child(userID)
        val container = userRef.child("รายการ")
        if (ID == 0 || ID == 1) {

            val ref = container.child("ใช้งาน").child(cardKey).child("รายละเอียด")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val card = p0.getValue(CardData::class.java)!!

                        val calendar = Calendar.getInstance()

                        calendar.set(card.dateYear.toInt(), card.dateMonth.toInt() - 1, card.dateDay.toInt())

                        calendar.add(Calendar.WEEK_OF_YEAR, slot.week - card.ageWeek.toInt())
                        calendar.add(Calendar.DAY_OF_YEAR, slot.day - card.ageDay.toInt())


                        val today = Calendar.getInstance()

                        val difference = calendar.timeInMillis - today.timeInMillis
                        val day = (difference / (1000 * 60 * 60 * 24)).toInt()

                        if (day == 0) {
                            holder.title_item.text = "วันนี้"
                        } else if (day == 1) {
                            holder.title_item.text = "พรุ่งนี้"
                        } else {
                            holder.title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"

                        }



                    }
                }
            })
        } else {
            holder.title_item.text = "${slot.week} สัปดาห์ ${slot.day} วัน"
        }


    }

    fun getValue(position: Int, holder: CardViewHolder) {
        val color = MelonTheme.from(activity).getColor()
        var slot = Event()

        if (ID == 0) {
            slot = data[position]
        } else if (ID == 1) {
            slot = unData[position]
        } else if (ID == 2 || ID == 3) {
            slot = data[position]
        }

        val calendar = Calendar.getInstance()
        val current = Calendar.getInstance()

        val userRef = Firebase.reference.child("ผู้ใช้").child(userID)
        val container = userRef.child("รายการ")

        if (ID == 0 || ID == 1) {


            val ref = container.child("ใช้งาน").child(cardKey).child("รายละเอียด")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val card = p0.getValue(CardData::class.java)!!

                        calendar.set(card.dateYear.toInt(), card.dateMonth.toInt() - 1, card.dateDay.toInt())

                        calendar.add(Calendar.WEEK_OF_YEAR, slot.week - card.ageWeek.toInt())
                        calendar.add(Calendar.DAY_OF_YEAR, slot.day - card.ageDay.toInt())

                        if (ID == 0) {
                            unpass(position, slot.title, holder)
                        } else if (ID == 1) {
                            passed(holder, slot.status)
                        }

                    }
                }
            })
        } else {
            unpass(position, slot.title, holder)
        }
        holder.apply {
            title_item.visibility = View.GONE
            card_item.visibility = View.VISIBLE
            message_area.visibility = View.VISIBLE
            info_area.visibility = View.GONE
            histort_area.visibility = View.GONE

            if (ID == 0) {
                card_des.text = "อายุ ${data[position].week} สัปดาห์ ${data[position].day} วัน"
            } else if (ID == 1) {
                card_des.text = "อายุ ${unData[position].week} สัปดาห์ ${unData[position].day} วัน"
            } else if(ID == 2 || ID == 3){
                card_des.text = "อายุ ${data[position].week} สัปดาห์ ${data[position].day} วัน"
            }


            message_text.text = slot.message
            card_title.text = slot.title


        }

    }

    private fun setIcon(objective: String, imageView: ImageView, area: CardView, textView: TextView) {

        when (objective) {
            "ฉีดวัคซีน" -> {
                Picasso.get().load(R.drawable.ic_inject_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightBlue))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorLightBlue))
            }
            "ถ่ายพยาธิ" -> {
                Picasso.get().load(R.drawable.ic_parasite_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightGreen))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorLightGreen))
            }
            "ตัดปาก" -> {
                Picasso.get().load(R.drawable.ic_cut_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorRed))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorRed))
            }
            "ให้แสงสว่าง" -> {
                Picasso.get().load(R.drawable.ic_sun_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorAmber))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorAmber))
            }
            else -> {
                Picasso.get().load(R.drawable.ic_chicken_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorDeepPurple))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorDeepPurple))
            }
        }
    }


    private fun unpass(position: Int, objective: String, holder: CardViewHolder) {

        setIcon(objective, holder.icon_image, holder.icon_area, holder.card_title)

    }

    private fun passed(holder: CardViewHolder, status: String) {
        if (status.contentEquals("ACTIVE") || status.contentEquals("PASSED")) {
            Picasso.get().load(R.drawable.ic_checked_icon).into(holder.icon_image)
        } else if (status.contentEquals("CANCEL")) {
            Picasso.get().load(R.drawable.ic_new_close_icon).into(holder.icon_image)
        }
        holder.card_title.setTextColor(ContextCompat.getColor(activity, R.color.colorText))
        holder.icon_area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorText))
    }
}