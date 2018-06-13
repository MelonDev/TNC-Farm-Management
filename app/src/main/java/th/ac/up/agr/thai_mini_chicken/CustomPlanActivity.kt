package th.ac.up.agr.thai_mini_chicken

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams

import kotlinx.android.synthetic.main.activity_custom_plan.*
import kotlinx.android.synthetic.main.activity_detail_notification.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.CustomPlanAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.DetailNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.NewCustomPlanAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CustomData
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView

class CustomPlanActivity : AppCompatActivity() {

    var user = FirebaseAuth.getInstance().currentUser!!.uid

    //lateinit var adapter: CustomPlanAdapter
    lateinit var adapter: NewCustomPlanAdapter

    lateinit var waitDialog: DialogFragment

    //private lateinit var arrEvent :ArrayList<Event>
    //private var arrPassed = ArrayList<Event>()
    lateinit var recyclerView: RecyclerView

    private lateinit var arrCustom: ArrayList<CustomData>

    lateinit var ref: DatabaseReference

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        //setTheme(MelonTheme.from(this).getStyle())
        setTheme(R.style.DetailTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_plan)

        val bundle = intent.extras
        //val card_key = bundle.getString("CARD_KEY")
        //val user_ID = bundle.getString("USER_ID")
        type = bundle.getString("TYPE")

        if (type.contentEquals("0")) {

            custom_fab_btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))

            custom_back_btn.setOnClickListener { finish() }

            custom_fab_btn.setOnClickListener { showEditDialog() }

            arrCustom = ArrayList()

            //val arr = LoadTime().getTable(type)

            recyclerView = QuickRecyclerView(this
                    , custom_plan_recycler_view
                    , "linear"
                    , 1
                    , "vertical"
                    , false
                    , "alway"
                    , "high")
                    .recyclerView()
            //adapter = CustomPlanAdapter(this, arrCustom)
            adapter = NewCustomPlanAdapter(this, arrCustom)

            recyclerView.adapter = adapter

            getCustom(type)
        } else if (type.contentEquals("1")) {
            //custom_fab_btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))

            custom_fab_btn.visibility = View.GONE

            custom_back_btn.setOnClickListener {
                var returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }

            //custom_fab_btn.setOnClickListener { showEditDialog() }

            arrCustom = ArrayList()

            //val arr = LoadTime().getTable(type)

            recyclerView = QuickRecyclerView(this
                    , custom_plan_recycler_view
                    , "linear"
                    , 1
                    , "vertical"
                    , false
                    , "alway"
                    , "high")
                    .recyclerView()
            //adapter = CustomPlanAdapter(this, arrCustom)
            adapter = NewCustomPlanAdapter(this, arrCustom)

            recyclerView.adapter = adapter

            getCustom(type)
        }

    }

    fun getCustom(type: String) {
        val firebase = Firebase.reference
        val ref = firebase.child("ผู้ใช้").child(user).child("รูปแบบ")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    arrCustom.clear()

                    var check = false

                    if (p0.children.count() == 0) {
                        if (type.contentEquals("1")) {
                            arrCustom.add(CustomData().init("ว่างเปล่า", "example", ""))
                        }
                        arrCustom.apply {
                            add(CustomData().init("มาตรฐาน", "title", ""))
                            add(CustomData().init("ไก่พ่อ-แม่พันธุ์", "example", ""))
                            add(CustomData().init("ไก่เนื้อ", "example", ""))
                        }
                        recyclerView.adapter.notifyDataSetChanged()
                    } else {

                        p0.children.forEach {
                            val key = it.key.toString()
                            val refss = firebase.child("ผู้ใช้").child(user).child("รูปแบบ").child(key).child("รายละเอียด")

                            if (!check) {
                                if (type.contentEquals("1")) {
                                    arrCustom.add(CustomData().init("ว่างเปล่า", "example", ""))
                                }
                                arrCustom.apply {
                                    add(CustomData().init("มาตรฐาน", "title", ""))
                                    add(CustomData().init("ไก่พ่อ-แม่พันธุ์", "example", ""))
                                    add(CustomData().init("ไก่เนื้อ", "example", ""))
                                    add(CustomData().init("ของฉัน", "title", ""))
                                }

                                check = true
                                recyclerView.adapter.notifyDataSetChanged()

                            }

                            refss.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e("","")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val a = p0.getValue(CustomData::class.java)!!
                                    arrCustom.add(a)
                                    recyclerView.adapter.notifyDataSetChanged()
                                }
                            })

                        }
                    }
                } else {
                    arrCustom.clear()

                    if (type.contentEquals("1")) {
                        arrCustom.add(CustomData().init("ว่างเปล่า", "example", ""))
                    }
                    arrCustom.apply {
                        add(CustomData().init("มาตรฐาน", "title", ""))
                        add(CustomData().init("ไก่พ่อ-แม่พันธุ์", "example", ""))
                        add(CustomData().init("ไก่เนื้อ", "example", ""))
                    }
                    recyclerView.adapter.notifyDataSetChanged()

                }
            }
        })

        //val ref = firebase.child("ผู้ใช้").child(user).child("รูปแบบ").child(card_key).child("รายการที่ต้องทำ")

    }

    fun showEditDialog() {
        val dialog = AlertDialog.Builder(this)


        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        dialog.setView(dialogView)
        dialogView.dialog_add_text.text = "ตั้งชื่อรูปแบบ"
        //dialog.setTitle(Title);
        //dialog.setMessage(Message);
        val editText = dialogView.custom_dialog_edittext
        editText.requestFocus()

        /*
        if (this.dataCard.breed.isNotEmpty()) {
            editText.setText(activity.dataCard.breed)
        }
*/
        editText.inputType = InputType.TYPE_CLASS_TEXT

        val abc = dialog.create()
        //abc.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        abc.show()

        dialogView.dialog_add_cancel.setOnClickListener {
            abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            this.hideKeyB()
            editText.clearFocus()
            abc.cancel()
        }

        dialogView.dialog_add_confirm.setCardBackgroundColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))

        dialogView.dialog_add_confirm.setOnClickListener {
            val x = editText.text.toString()
            /*if (x.isNotEmpty()) {
                activity.add_program_breed_text.text = x
                activity.dataCard.breed = x
            } else {
                activity.add_program_breed_text.text = "ไม่ระบุ"
            }
*/
            if (x.isNotEmpty()) {

                var firebase = Firebase.reference
                var database = firebase.child("ผู้ใช้").child(user).child("รูปแบบ")
                var key = database.push().key.toString()

                var db = database.child(key).child("รายละเอียด")

                val data = CustomData()
                data.apply {
                    this.name = x
                    this.key = key
                    this.date = Date().getDate()
                }

                setWaitDialog()

                db.setValue(data) { p0, p1 ->
                    if (p0 != null) {
                        waitDialog.dismiss()
                        setErrorDialog()
                    } else {
                        waitDialog.dismiss()
                        setConDialog(key)
                    }
                }

                abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                this.hideKeyB()
                abc.cancel()
            } else {
                setAlertDialog()
                this.hideKeyB()
                abc.cancel()
            }
        }
    }

    fun setAlertDialog() {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("คุณไม่ได้ใส่ข้อมูล")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, MelonTheme.from(this@CustomPlanActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, R.color.colorText)
                    }
                }).show()


    }

    fun setWaitDialog() {
        waitDialog = CircleDialog.Builder()
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText("กำลังบันทึก...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(this.supportFragmentManager)
        //waitDialog.dismiss()
        //                        .setProgressDrawable(R.drawable.bg_progress_s)
        /*waitDialog = CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText("登录中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .setText("กำลังบันทึก")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })

*/
    }


    fun setConDialog(key: String) {


        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("บันทึกเรียบร้อย")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, MelonTheme.from(this@CustomPlanActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //this.finish()
                    var intent = Intent(this, DetailNotificationActivity::class.java)
                    intent.putExtra("USER_ID", user)
                    intent.putExtra("CARD_KEY", key)
                    intent.putExtra("TYPE", "2")
                    startActivity(intent)
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, R.color.colorText)
                    }
                }).show()


    }

    fun setErrorDialog() {
        CircleDialog.Builder(this
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
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, MelonTheme.from(this@CustomPlanActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@CustomPlanActivity, R.color.colorText)
                    }
                }).show()


    }

    fun hideKeyB() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

}
