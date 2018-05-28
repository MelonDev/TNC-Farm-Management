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
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardDate
import th.ac.up.agr.thai_mini_chicken.DetailActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class PageProgramAdapter(val activity: FragmentActivity, val ID: String, val data: ArrayList<String>) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        if (data[position].indexOf("-99-99-99") > -1) {
            setTitle(data[position], holder)
        } else {
            getValue(data[position], holder)
        }

        holder.card_more.setOnClickListener {
            showDialog(arrayOf("แก้ไข", "เก็บประวัติ", "ลบ"), ID, data[position])
        }

        holder.card_item.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("CARD_KEY", data[position])
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
                            intent.putExtra("ID","1")
                            intent.putExtra("USER_ID",userID)
                            intent.putExtra("CARD_KEY",cardKey)
                            activity.startActivity(intent)
                        }
                        2 -> {
                            showDeleteDialog(position,userID, cardKey)
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

    fun setTitle(key: String, holder: CardViewHolder) {
        val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key)
        holder.title_item.visibility = View.VISIBLE
        holder.card_item.visibility = View.GONE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.GONE
        databaseReferences.addValueEventListener(object : ValueEventListener {
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
        })
        //val date = Date().reDate()
    }

    fun getValue(key: String, holder: CardViewHolder) {
        val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        val y = databaseReferences.child(key).child("รายละเอียด")

        holder.title_item.visibility = View.GONE
        holder.card_item.visibility = View.VISIBLE
        holder.message_area.visibility = View.GONE
        holder.info_area.visibility = View.VISIBLE

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


    }

    fun setData(holder: CardViewHolder, slot: CardData) {
        if (slot.cardName.isNotEmpty()) {
            holder.card_title.text = slot.cardName
        } else {
            holder.card_title.text = "ชื่อรายการ"
        }
        holder.apply {
            card_des.text = slot.cardID
            info_date.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
            info_age.text = "${slot.ageWeek} สัปดาห์ ${slot.ageDay} วัน"
            info_objective.text = ConvertCard().getObjective(slot.userObjective)
        }
    }


    fun showDeleteDialog(positions: Int,userID: String, cardKey: String) {
        val arr = arrayOf("ยืนยันการลบ")

        val database = FirebaseDatabase.getInstance().reference
        val path = database.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(cardKey)

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
                    path.removeValue()
                    //this.notifyItemRemoved(positions)
                    showConDialog()
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

}