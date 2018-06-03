package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import th.ac.up.agr.thai_mini_chicken.Data.CardDate
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.ToolReference
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import com.google.firebase.FirebaseError
import com.google.firebase.database.DatabaseReference.CompletionListener
import th.ac.up.agr.thai_mini_chicken.Data.Event


class PageProgramAdapter(val activity: FragmentActivity, val ID: String, val data: ArrayList<CardData>) : RecyclerView.Adapter<CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
/*
        if (data[position].indexOf("-99-99-99") > -1) {
            setTitle(data[position], holder)
        } else {
            getValue(data[position], holder)
        }


*/

        val card_key = data[position].cardID

        val userRef = Firebase.reference.child("ผู้ใช้").child(ID)
        val container = userRef.child("รายการ")

        val ref = container.child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ")

        if (data[position].cardID.contentEquals("null")) {
            setTitle(data[position], holder)
        } else {
            //Log.e("asda",data[position].createDate)
            getValue(data[position], ref, holder)
        }

        holder.card_more.setOnClickListener {
            showDialog(arrayOf("แก้ไข", "เก็บประวัติ", "ลบ"), ID, data[position].cardID)
        }

        holder.card_item.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("CARD_KEY", data[position].cardID)
            intent.putExtra("USER_ID", ID)
            activity.startActivity(intent)
        }
    }

    fun showDialog(arr: Array<String>, userID: String, cardKey: String) {
        CircleDialog.Builder(activity
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
                            //val pathTo = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ประวัติ").child(cardKey)

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.value != null) {
                                        ref.setValue("INACTIVE"){ p0, p1 ->
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
                            showDeleteDialog(position, userID, cardKey)
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
        //val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key)
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

        //calendar.set(card.dateYear.toInt(), card.dateMonth.toInt() - 1, card.dateDay.toInt())

        //Log.e(today.get(Calendar.DAY_OF_MONTH).toString(),calendar.get(Calendar.DAY_OF_MONTH).toString())
        //Log.e(card.cardID,card.createDate)

        val difference = today.timeInMillis - calendar.timeInMillis
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        if (days == 0) {
            holder.title_item.text = "วันนี้"
            //Log.e((difference/ (1000 * 60 * 60)).toString(),days.toString())
        } else if (days == 1) {
            holder.title_item.text = "เมื่อวานนี้"
            //Log.e(card.cardID,days.toString())
        } else {
            holder.title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"
            //Log.e(card.cardID,days.toString())
        }

        /*databaseReferences.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val slot = p0.getValue(CardDate::class.java)
                    val date = Date().reDateNull(slot!!.dateTime)
                    if (Date().getMonth().contentEquals(DateFormat.format("MM", date).toString()) && Date().getYear().contentEquals(DateFormat.format("yyyy", date).toString())) {
                        val x = Date().getDay().toInt()
                        val y = DateFormat.format("dd", date).toString().toInt()
                        if (x == y) {
                            holder.title_item.text = "วันนี้"
                        } else if ((x - 1) == y) {
                            holder.title_item.text = "เมื่อวานนี้"
                        } else if ((x + 1) == y) {
                            holder.title_item.text = "วันพรุ่งนี้"
                        } else {
                            holder.title_item.text = "${DateFormat.format("dd", date)} ${ConvertCard().getMonth(DateFormat.format("MM", date).toString())} ${ConvertCard().getYear(DateFormat.format("yyyy", date).toString())}"
                        }
                    } else {
                        holder.title_item.text = "${DateFormat.format("dd", date)} ${ConvertCard().getMonth(DateFormat.format("MM", date).toString())} ${ConvertCard().getYear(DateFormat.format("yyyy", date).toString())}"
                    }
                }
            }
        })*/
        //val date = Date().reDate()
    }

    fun getValue(card: CardData, ref: DatabaseReference, holder: CardViewHolder) {
        //val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        //val y = databaseReferences.child(key).child("รายละเอียด")

        holder.title_item.visibility = View.GONE
        holder.card_item.visibility = View.VISIBLE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.VISIBLE
        holder.histort_area.visibility = View.GONE

        setData(holder, ref, card)

/*
        y.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val slot = p0.getValue(CardData::class.java)!!
                    if (slot.cardID.isEmpty()) {
                        slot.cardID = key
                        y.setValue(slot)
                    }
                    setData(holder, slot)
                }
            }
        })
*/

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

        val w: Int = days / 7
        val d: Int = days % 7

        var week = slot.ageWeek.toInt() + w
        var day = slot.ageDay.toInt() + d

        if (day >= 7) {
            week += (day / 7)
            day = (day % 7)
        }

        holder.apply {
            card_des.text = plusDes("0")
            info_date.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
            info_age.text = "$week สัปดาห์ $day วัน"
            info_objective.text = ConvertCard().getObjective(slot.userObjective)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

        //activity_detail_indicator_text.text = arrEvent.size.toString()
        holder.card_des.text = plusDes(arrEvent.size.toString())

        dataSnapshot!!.children.forEach {
            //Log.e("KEY",it.key.toString())

            val slot = it.getValue(Event::class.java)!!

            val x = calendar.clone() as Calendar

            x.add(Calendar.WEEK_OF_YEAR, slot.week)
            x.add(Calendar.DAY_OF_YEAR, slot.day)

            val difference = x.timeInMillis - today.timeInMillis
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()

            //Log.e("DAY", days.toString())


            if (days >= 0 && slot.status.contentEquals("ACTIVE")) {
                arrEvent.add(slot)
            }


            holder.card_des.text = plusDes(arrEvent.size.toString())
            //Log.e("arr",arrEvent.size.toString())


        }

    }


    fun showDeleteDialog(positions: Int, userID: String, cardKey: String) {
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
                            showConDialog()
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

    fun showConDialog() {
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