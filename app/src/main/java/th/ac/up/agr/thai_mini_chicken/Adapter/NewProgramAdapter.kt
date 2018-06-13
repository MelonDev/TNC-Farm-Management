package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigItems
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ItemsParams
import com.mylhyl.circledialog.params.TextParams
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Fragment.NewProgramFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class NewProgramAdapter(val fragment: NewProgramFragment, val ID: String, val data: ArrayList<CardData>) : RecyclerView.Adapter<CardViewHolder>() {

    private val activity = fragment.activity!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        //Log.e("SIZE",data.size.toString())
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        val card_key = data[position].cardID

        val userRef = Firebase.reference.child("ผู้ใช้").child(ID)
        val container = userRef.child("รายการ")

        val ref = container.child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ")

        if (data[position].cardID.contentEquals("null")) {
            setTitle(data[position], holder)
        } else {
            getValue(data[position], ref, holder)
        }

        holder.card_more.setOnClickListener {
            showDialog(arrayOf("แก้ไข", "เก็บประวัติ", "ลบ"), ID, data[position].cardID, position)
        }

        holder.card_item.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("CARD_KEY", data[position].cardID)
            intent.putExtra("USER_ID", ID)
            activity.startActivity(intent)
        }
    }

    fun showDialog(arr: Array<String>, userID: String, cardKey: String, pos: Int) {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                .setItems(arr) { parent, view, position, id ->
                    when (position) {
                        0 -> {
                            val intent = Intent(activity, AddProgramActivity::class.java)
                            intent.putExtra("ID", "1")
                            intent.putExtra("USER_ID", userID)
                            intent.putExtra("CARD_KEY", cardKey)
                            activity.startActivity(intent)
                        }
                        1 -> {
                            val ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey).child("รายละเอียด").child("status")

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e("", "")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.value != null) {
                                        ref.setValue("INACTIVE") { p0, p1 ->
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
                        2 -> {
                            showDeleteDialog(position, userID, cardKey, pos)
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })
                .show()
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
            set(Calendar.YEAR, part.get(Calendar.YEAR))
            set(Calendar.MONTH, part.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, part.get(Calendar.DAY_OF_MONTH))
        }

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        if (days == 0) {
            holder.title_item.text = "วันนี้"
        } else if (days == 1) {
            holder.title_item.text = "เมื่อวานนี้"
        } else {
            holder.title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"
        }
    }

    fun getValue(card: CardData, ref: DatabaseReference, holder: CardViewHolder) {

        holder.title_item.visibility = View.GONE
        holder.card_item.visibility = View.VISIBLE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.VISIBLE
        holder.histort_area.visibility = View.GONE

        setData(holder, ref, card)

    }

    fun setData(holder: CardViewHolder, ref: DatabaseReference, slot: CardData) {
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

        if (days >= 0) {

            val w: Int = days / 7
            val d: Int = days % 7

            var week = slot.ageWeek.toInt() + w
            var day = slot.ageDay.toInt() + d

            if (day >= 7) {
                week += (day / 7)
                day = (day % 7)
            }
            holder.info_age.text = "$week สัปดาห์ $day วัน"
        } else {
            holder.info_age.text = "ยังไม่ถึงวันรับเข้า"
        }


        holder.apply {
            card_des.text = plusDes("0")
            info_date.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
            info_objective.text = ConvertCard().getObjective(slot.userObjective)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    getEvents(p0, holder, slot)
                }

            }
        })

    }

    private fun plusDes(str: String): String {
        return "รายการที่ต้องทำ $str รายการ"
    }

    fun getEvents(dataSnapshot: DataSnapshot?, holder: CardViewHolder, cardData: CardData) {

        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())

        calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
        calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

        val arrEvent = ArrayList<Event>()
        arrEvent.clear()

        holder.card_des.text = plusDes(arrEvent.size.toString())

        dataSnapshot!!.children.forEach {

            val slot = it.getValue(Event::class.java)!!

            val x = calendar.clone() as Calendar

            x.add(Calendar.WEEK_OF_YEAR, slot.week)
            x.add(Calendar.DAY_OF_YEAR, slot.day)

            val difference = x.timeInMillis - today.timeInMillis
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()



            if (days >= 0 && slot.status.contentEquals("ACTIVE")) {
                arrEvent.add(slot)
            }


            holder.card_des.text = plusDes(arrEvent.size.toString())


        }

    }


    fun showDeleteDialog(positions: Int, userID: String, cardKey: String, pos: Int) {
        val arr = arrayOf("ยืนยันการลบ")

        val database = FirebaseDatabase.getInstance().reference
        val path = database.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey)
        val ref = database.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน")

        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                        params.canceledOnTouchOutside = false
                    }
                })
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(activity, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    path.removeValue { p3, _ ->
                        if (p3 != null) {
                            showErrorDialog()
                        } else {
                            showConDialog(pos)
                        }
                    }
                }
                .configItems(object : ConfigItems() {
                    override fun onConfig(params: ItemsParams?) {
                        params!!.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                        params.backgroundColorPress = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                    }
                })
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorText)
                    }
                })
                .show()
    }

    fun showConDialog(pos: Int) {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("ลบเรียบร้อย")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad(false)
                    //fragment.recyclerView.scrollToPosition(pos)
                    //fragment.recyclerView.adapter.notifyItemRemoved(pos)

                    Log.e("POS", pos.toString())
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()


    }

    fun showMoveDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("เก็บไปที่ประวัติเรียบร้อย")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad(false)
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()


    }

    fun showErrorDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("เกิดข้อผิดพลาด")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad(false)
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()


    }
}
