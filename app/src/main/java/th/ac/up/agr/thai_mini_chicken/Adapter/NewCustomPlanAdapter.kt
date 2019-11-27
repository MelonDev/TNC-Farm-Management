package th.ac.up.agr.thai_mini_chicken.Adapter

import android.app.Activity
import android.content.Intent

import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog

import com.mylhyl.circledialog.params.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.CustomPlanActivity
import th.ac.up.agr.thai_mini_chicken.Data.CustomData
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.DetailNotificationActivity
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.TimeManager
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*
import kotlin.collections.ArrayList

class NewCustomPlanAdapter(val activity: CustomPlanActivity, val data: ArrayList<CustomData>) : RecyclerView.Adapter<CardViewHolder>() {

    lateinit var waitDialog: DialogFragment


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val slot = data[position]

        if (slot.key.contentEquals("title")) {
            holder.apply {
                title_item.visibility = View.VISIBLE
                card_item.visibility = View.GONE
                message_area.visibility = View.GONE
                info_area.visibility = View.GONE
                histort_area.visibility = View.GONE

                title_item.text = slot.name

            }
        } else if (slot.key.contentEquals("example")) {
            holder.apply {
                title_item.visibility = View.GONE
                card_item.visibility = View.VISIBLE
                message_area.visibility = View.GONE
                info_area.visibility = View.GONE
                histort_area.visibility = View.GONE

                card_title.text = slot.name.toString()
                card_title.setTextColor(ContextCompat.getColor(activity, R.color.colorText))
                icon_area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorText))

                Picasso.get().load(R.drawable.ic_chicken_icon).into(icon_image)

                card_des.text = "รูปแบบการจัดการแบบมาตรฐาน"


                if (activity.type.contentEquals("0")) {
                    card_more.visibility = View.VISIBLE
                    card_item.setOnClickListener {
                        var intent = Intent(activity, DetailNotificationActivity::class.java)
                        intent.putExtra("USER_ID", FirebaseAuth.getInstance().currentUser!!.uid)
                        intent.putExtra("CARD_KEY", slot.name)
                        intent.putExtra("TYPE", "3")
                        activity.startActivity(intent)
                    }
                } else if (activity.type.contentEquals("1")) {
                    card_more.visibility = View.GONE
                    var returnIntent = Intent()
                    card_item.setOnClickListener {
                        returnIntent.putExtra("RESULT", data[position].name.toString())
                        returnIntent.putExtra("OBJECTIVE","1")
                        activity.setResult(Activity.RESULT_OK, returnIntent)
                        activity.finish()
                    }
                }

                card_more.setOnClickListener {
                    showDialog(arrayOf("ทำสำเนา"), FirebaseAuth.getInstance().currentUser!!.uid, position, 0)
                }
            }
        } else {
            holder.apply {
                title_item.visibility = View.GONE
                card_item.visibility = View.VISIBLE
                message_area.visibility = View.GONE
                info_area.visibility = View.GONE
                histort_area.visibility = View.GONE

                card_title.text = slot.name.toString()

                val calendar = Calendar.getInstance()
                val date = Date().reDate(slot.date)

                calendar.time = date

                card_des.text = "สร้างเมื่อ ${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"
                icon_area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorText))
                card_title.setTextColor(ContextCompat.getColor(activity, R.color.colorText))
                Picasso.get().load(R.drawable.ic_new_pack_icon).into(icon_image)

                message_text.text = ""

                if (activity.type.contentEquals("0")) {
                    card_more.visibility = View.VISIBLE
                    card_item.setOnClickListener {
                        var intent = Intent(activity, DetailNotificationActivity::class.java)
                        intent.putExtra("USER_ID", FirebaseAuth.getInstance().currentUser!!.uid)
                        intent.putExtra("CARD_KEY", slot.key)
                        intent.putExtra("TYPE", "2")
                        activity.startActivity(intent)
                    }
                } else if (activity.type.contentEquals("1")) {
                    card_more.visibility = View.GONE
                    var returnIntent = Intent()
                    card_item.setOnClickListener {
                        returnIntent.putExtra("RESULT", data[position].key.toString())
                        returnIntent.putExtra("OBJECTIVE","2")
                        activity.setResult(Activity.RESULT_OK, returnIntent)
                        activity.finish()
                    }

                }

                card_more.setOnClickListener {
                    showDialog(arrayOf("แก้ไขชื่อ", "ทำสำเนา", "ลบ"), FirebaseAuth.getInstance().currentUser!!.uid, position, 1)
                }

            }


        }
    }

    fun showDialog(arr: Array<String>, userID: String, pos: Int, obj: Int) {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }

                .setItems(arr) { parent, view, position, id ->
                    var ref = Firebase.reference

                    ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รูปแบบ").child(data[pos].key)

                    if (obj == 0) {
                        showEditDialog(pos, 0)
                    } else if (obj == 1) {
                        if (position == 0) {
                            showEditDialog(pos, 1)
                        } else if (position == 1) {
                            showEditDialog(pos, 0)
                        } else if (position == 2) {
                            showDeleteDialog(ref)

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

    fun showEditDialog(position: Int, obj: Int) {
        val dialog = AlertDialog.Builder(activity)


        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        dialog.setView(dialogView)
        dialogView.dialog_add_text.text = "ตั้งชื่อรูปแบบ"

        val editText = dialogView.custom_dialog_edittext
        editText.requestFocus()

        editText.inputType = InputType.TYPE_CLASS_TEXT

        val abc = dialog.create()
        abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        abc.show()

        if (obj == 1) {
            editText.setText(data[position].name)
        }




        dialogView.dialog_add_cancel.setOnClickListener {
            abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            this.hideKeyB()
            editText.clearFocus()
            abc.cancel()
        }

        dialogView.dialog_add_confirm.setCardBackgroundColor(ContextCompat.getColor(activity, MelonTheme.from(activity).getColor()))

        dialogView.dialog_add_confirm.setOnClickListener {
            val x = editText.text.toString()

            if (x.isNotEmpty()) {

                var firebase = Firebase.reference
                var database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รูปแบบ")

                var key = ""
                if (obj == 0) {
                    key = database.push().key.toString()
                } else if (obj == 1) {
                    key = data[position].key
                }

                var db = database.child(key).child("รายละเอียด")

                val data = CustomData()
                data.apply {
                    this.name = x
                    this.key = key
                    this.date = Date().getDate()
                }

                setWaitDialog("กำลังบันทึก...")

                if (obj == 1) {
                    db.setValue(data) { p0, p1 ->
                        if (p0 != null) {
                            waitDialog.dismiss()
                            setErrorDialog()
                        } else {
                            waitDialog.dismiss()
                            setConDialog()
                        }
                    }
                } else if (obj == 0) {
                    val childMap = HashMap<String, Any>()

                    val abs = database.child(this@NewCustomPlanAdapter.data[position].key).child("รายการที่ต้องทำ")
                    val refA = database.child(key)
                    val pathA = "/รายละเอียด/"
                    childMap.put(pathA, data)

                    if (this@NewCustomPlanAdapter.data[position].key.contentEquals("example")) {
                        Log.e("SSSS", "0")
                        val name = this@NewCustomPlanAdapter.data[position].key
                        var arrs = ArrayList<Event>()
                        Log.e("B", this@NewCustomPlanAdapter.data[position].name)
                        if (this@NewCustomPlanAdapter.data[position].name.contentEquals("ไก่พ่อ-แม่พันธุ์")) {
                            arrs = TimeManager.get.breeder
                            Log.e("B", "0")
                        } else if (this@NewCustomPlanAdapter.data[position].name.contentEquals("ไก่เนื้อ")) {
                            arrs = TimeManager.get.meat
                            Log.e("B", "1")
                        } else {
                            arrs = ArrayList()
                            Log.e("B", "2")
                        }
                        var count = 0
                        Log.e("SSSS0000", arrs.size.toString())
                        for (oSlot in arrs) {
                            val keys: String = refA.push().key.toString()
                            val pathB = "/รายการที่ต้องทำ/$keys/"

                            Log.e("COUNT", count.toString())
                            Log.e("PATH", pathB.toString())

                            var slot = oSlot
                            slot.apply {
                                this.status = "ACTIVE"
                                this.cardID = keys
                                this.fromID = key
                            }

                            childMap.put(pathB, slot)
                            count += 1
                            if (count == arrs.size) {
                                refA.updateChildren(childMap) { p0, p1 ->
                                    if (p0 != null) {
                                        waitDialog.dismiss()
                                        setErrorDialog()
                                    } else {
                                        waitDialog.dismiss()
                                        setConDialog()
                                    }
                                }
                            }
                        }
                    } else {
                        Log.e("SSSS", "1")
                        abs.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.value != null) {

                                    var count = 0
                                    for (it in p0.children) {
                                        val keys: String = refA.push().key.toString()
                                        val pathB = "/รายการที่ต้องทำ/$keys/"

                                        var slot = it.getValue(Event::class.java)!!

                                        slot.apply {
                                            this.status = "ACTIVE"
                                            this.fromID = key
                                            this.cardID = keys
                                        }

                                        childMap.put(pathB, slot)

                                        count += 1
                                        if (count == p0.children.count()) {
                                            refA.updateChildren(childMap) { p0, p1 ->
                                                if (p0 != null) {
                                                    waitDialog.dismiss()
                                                    setErrorDialog()
                                                } else {
                                                    waitDialog.dismiss()
                                                    setConDialog()
                                                }
                                            }
                                        }
                                    }

                                }

                            }
                        })
                    }

                }


                abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                this.hideKeyB()
                abc.cancel()
            } else {
                setAlertDialog()
                this.hideKeyB()
                abc.cancel()
            }
        }
    }


    fun hideKeyB() {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun setErrorDialog() {
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
                }.show()


    }

    fun setAlertDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("กรุณาใส่ข้อมูลให้ครบ")
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
                }.show()


    }

    fun setConDialog() {


        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("บันทึกเรียบร้อย")
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
                }.show()


    }

    fun setConDeleteDialog() {


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
                }.show()


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
                            waitDialog.dismiss()
                            setErrorDialog()
                        } else {
                            waitDialog.dismiss()
                            setConDeleteDialog()
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

    fun setWaitDialog(str: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(str)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)

    }

}