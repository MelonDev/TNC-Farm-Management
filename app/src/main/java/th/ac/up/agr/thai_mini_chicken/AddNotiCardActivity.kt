package th.ac.up.agr.thai_mini_chicken

import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.params.ProgressParams

import kotlinx.android.synthetic.main.activity_add_noti_card.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class AddNotiCardActivity : AppCompatActivity() {

    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog

    lateinit var waitDialog: DialogFragment

    private var objective = "null"
    private var day = "null"
    private var week = "null"

    private var passed = false

    private lateinit var ID: String

    private var card_key = ""
    private var user_ID = ""
    private var notiKey = ""

    private var k = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_noti_card)

        val bundle = intent.extras!!
        ID = bundle.getString("ID")!!

        if (ID.contentEquals("1")) {
            notiKey = bundle.getString("NOTI_KEY")!!

            card_key = bundle.getString("CARD_KEY")!!
            user_ID = bundle.getString("USER_ID")!!

            var firebase = Firebase.reference
            var database = firebase.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ").child(notiKey)

            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val card = p0.getValue(Event::class.java)!!
                        week = card.week.toString()
                        day = card.day.toString()

                        objective = card.title

                        add_noti_message_edittext.setText(card.message)
                        add_noti_date_text.text = "${card.week} สัปดาห์ ${card.day} วัน"
                        add_noti_objective_text.text = objective

                        k = card.cardID

                        bottomSheetView.add_program_old_week_wheel.selectedItemPosition = card.week
                        bottomSheetView.add_program_old_day_wheel.selectedItemPosition = card.day

                    }
                }
            })


        } else if (ID.contentEquals("2")) {
            card_key = bundle.getString("CARD_KEY")!!
            user_ID = bundle.getString("USER_ID")!!
        } else if (ID.contentEquals("3")) {
            card_key = bundle.getString("CARD_KEY")!!
            user_ID = bundle.getString("USER_ID")!!
            notiKey = bundle.getString("NOTI_KEY")!!

            val ref = Firebase.reference.child("ผู้ใช้").child(user_ID).child("รูปแบบ").child(card_key).child("รายการที่ต้องทำ").child(notiKey)

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val card = p0.getValue(Event::class.java)!!
                        week = card.week.toString()
                        day = card.day.toString()

                        objective = card.title

                        add_noti_message_edittext.setText(card.message)
                        add_noti_date_text.text = "${card.week} สัปดาห์ ${card.day} วัน"
                        add_noti_objective_text.text = objective

                        k = card.cardID

                        bottomSheetView.add_program_old_week_wheel.selectedItemPosition = card.week
                        bottomSheetView.add_program_old_day_wheel.selectedItemPosition = card.day

                    }
                }
            })

        } else {
            card_key = bundle.getString("CARD_KEY")!!
            user_ID = bundle.getString("USER_ID")!!
        }

        bottomSheetView = layoutInflater.inflate(R.layout.input_dialog, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val typeface = ResourcesCompat.getFont(this, R.font.sukhumvit_light)
        bottomSheetView.add_program_old_day_wheel.typeface = typeface
        bottomSheetView.add_program_old_week_wheel.typeface = typeface
        bottomSheetView.add_program_old_week_wheel.setOnItemSelectedListener { picker, data, position ->
            week = position.toString()
        }

        bottomSheetView.add_program_old_day_wheel.setOnItemSelectedListener { picker, data, position ->
            day = position.toString()
        }


        val a = (0..999).toList()

        bottomSheetView.add_program_old_day_wheel.data = a
        bottomSheetView.add_program_old_week_wheel.data = a

        add_noti_back_btn.setOnClickListener { finish() }

        add_noti_save_btn.setOnClickListener {
            val text = add_noti_message_edittext.text.toString()
            if (ID.contentEquals("0") || ID.contentEquals("1")) {
                if (week.contentEquals("null") || objective.contentEquals("null") || day.contentEquals("null") || text.isEmpty()) {
                    setAlertDialog()
                } else if (!passed) {
                    passed = true

                    setWaitDialog()

                    var card = Event()

                    var firebase = Firebase.reference
                    var database = firebase.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ")

                    var key = ""
                    if (ID.contentEquals("0")) {
                        key = database.push().key!!
                    } else if (ID.contentEquals("1")) {
                        key = k
                    }

                    var a = week.toInt() * 7
                    a += day.toInt()

                    card.apply {
                        this.title = objective
                        this.week = this@AddNotiCardActivity.week.toInt()
                        this.day = this@AddNotiCardActivity.day.toInt()
                        this.status = "ACTIVE"
                        this.message = text
                        this.fromID = card_key
                        this.cardID = key
                        this.totalDay = a
                    }

                    database.child(key).setValue(card) { p0, p1 ->
                        if (p0 != null) {
                            waitDialog.dismiss()
                            setErrorDialog()
                        } else {
                            waitDialog.dismiss()
                            setConDialog()
                        }
                    }
                }
            } else if (ID.contentEquals("2") || ID.contentEquals("3")) {

                if (week.contentEquals("null") || objective.contentEquals("null") || day.contentEquals("null") || text.isEmpty()) {
                    setAlertDialog()
                } else if (!passed) {
                    passed = true

                    setWaitDialog()

                    var card = Event()

                    val firebase = Firebase.reference
                    val dbA = firebase.child("ผู้ใช้").child(user_ID).child("รูปแบบ").child(card_key).child("รายการที่ต้องทำ")

                    var a = week.toInt() * 7
                    a += day.toInt()

                    var key = ""
                    if (ID.contentEquals("2")) {
                        key = dbA.push().key!!
                    } else if (ID.contentEquals("3")) {
                        key = k
                    }

                    var dbB = dbA.child(key)


                    card.apply {
                        this.title = objective
                        this.week = this@AddNotiCardActivity.week.toInt()
                        this.day = this@AddNotiCardActivity.day.toInt()
                        this.status = "ACTIVE"
                        this.message = text
                        this.fromID = card_key
                        this.cardID = key
                        this.totalDay = a
                    }

                    dbB.setValue(card) { p0, p1 ->
                        if (p0 != null) {
                            waitDialog.dismiss()
                            setErrorDialog()
                        } else {
                            waitDialog.dismiss()
                            setConDialog()
                        }
                    }

                    /*
                    var a = week.toInt() * 7
                    a += day.toInt()

                    passed = true
                    var intent = Intent()

                    intent.putExtra("objective", objective.toString())
                    intent.putExtra("week", this@AddNotiCardActivity.week.toString())
                    intent.putExtra("day", this@AddNotiCardActivity.day.toString())
                    intent.putExtra("totalDay", a.toString())
                    intent.putExtra("message", text.toString())


                    setResult(RESULT_OK, intent)
*/
                    finish()
                }
            }
        }

        add_noti_objective_btn.setOnClickListener {
            newObjectiveDialog()
        }

        add_noti_date_btn.setOnClickListener {
            ageDialog().show()
        }

        bottomSheetView.input_dialog_action_text.text = "รีเซ็ต"
        bottomSheetView.input_dialog_title.text = "เลือกอายุของไก่"

        bottomSheetView.input_dialog_action_area.setOnClickListener {
            resetAge()
        }

        bottomSheetView.input_dialog_cancal_btn.setOnClickListener {
            bottomSheetDialog.cancel()
        }

        bottomSheetView.input_dialog_confirm_btn.setOnClickListener {
            bottomSheetDialog.cancel()
            if (week.contentEquals("null")) {
                week = "0"
            }
            if (day.contentEquals("null")) {
                day = "0"
            }
            var aw = week
            var ad = day
            var x = (ad.toInt() / 7).toString()
            var y = (ad.toInt() % 7).toString()
            aw = (aw.toInt() + x.toInt()).toString()
            add_noti_date_text.text = "${aw} สัปดาห์ ${y} วัน"
            week = aw
            day = y


        }


    }


    private fun resetAge() {
        bottomSheetView.add_program_old_week_wheel.selectedItemPosition = 0
        bottomSheetView.add_program_old_day_wheel.selectedItemPosition = 0

        day = "0"
        week = "0"
    }

    fun newObjectiveDialog() {

        val item = ConvertCard().getNotiObjective()
        showDialog("วัตถุประสงค์", arrayOf(item[0], item[1], item[2], item[3], item[4]))

    }

    fun showDialog(title: String, arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { parent, view, position, id ->
                    val o = ConvertCard().getNotiObjective(position.toString())
                    add_noti_objective_text.text = o
                    objective = o
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, R.color.colorText)
                }
                .show()
    }

    fun ageDialog(): TakeAction {
        hideAll()
        bottomSheetView.input_dialog_age_area.visibility = View.VISIBLE

        if (!day.contentEquals("null") && !week.contentEquals("null")) {
            bottomSheetView.add_program_old_day_wheel.selectedItemPosition = day.toInt()
            bottomSheetView.add_program_old_week_wheel.selectedItemPosition = week.toInt()
        } else {
            bottomSheetView.add_program_old_week_wheel.selectedItemPosition = 0
            bottomSheetView.add_program_old_day_wheel.selectedItemPosition = 0
        }


        return TakeAction(bottomSheetDialog)
    }

    private fun hideAll() {
        bottomSheetView.input_dialog_age_area.visibility = View.GONE
        bottomSheetView.input_dialog_date_area.visibility = View.GONE
        bottomSheetView.input_dialog_objective_area.visibility = View.GONE
        bottomSheetView.input_dialog_amount_area.visibility = View.GONE
    }

    class TakeAction(private val bottomSheetDialog: BottomSheetDialog) {
        fun show() {
            bottomSheetDialog.show()
        }

        fun hide() {
            bottomSheetDialog.hide()
        }

        fun cancel() {
            bottomSheetDialog.cancel()
        }
    }

    fun setErrorDialog() {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("เกิดข้อผิดพลาด")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, MelonTheme.from(this@AddNotiCardActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, R.color.colorText)
                }.show()


    }

    fun setAlertDialog() {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("กรุณาใส่ข้อมูลให้ครบ")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, MelonTheme.from(this@AddNotiCardActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, R.color.colorText)
                }.show()


    }

    fun setConDialog() {


        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("บันทึกเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, MelonTheme.from(this@AddNotiCardActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ") {
                    this.finish()
                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@AddNotiCardActivity, R.color.colorText)
                }.show()


    }


    fun setWaitDialog() {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText("กำลังบันทึก...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(this.supportFragmentManager)

    }

}

