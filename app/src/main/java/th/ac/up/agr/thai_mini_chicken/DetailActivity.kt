package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
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

import kotlinx.android.synthetic.main.activity_detail.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardDate
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme
import th.ac.up.agr.thai_mini_chicken.Tools.Animation
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import android.os.Build
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity



class DetailActivity : AppCompatActivity() {

    var fab_status = false
    lateinit var path :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bundle = intent.extras
        val card_key = bundle.getString("CARD_KEY")
        val user_ID = bundle.getString("USER_ID")

        val database = FirebaseDatabase.getInstance().reference
        path = database.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key)
        val data = database.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key).child("รายละเอียด")

        data.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null){
                    val data = p0.getValue(CardData::class.java)
                    setText(data!!)
                }
            }
        })

        window.statusBarColor = resources.getColor(MelonTheme.from(this).getStatusBarOverlay())

        //Log.e("DATA",AppTheme(this).read().toString())



        detail_back_btn.setOnClickListener {
            finish()
        }

        detail_delete_btn.setOnClickListener {
            showDialog()
        }

        detail_edit_btn.setOnClickListener {
            val intent = Intent(this, AddProgramActivity::class.java)
            intent.putExtra("ID","1")
            intent.putExtra("USER_ID",user_ID)
            intent.putExtra("CARD_KEY",card_key)
            startActivity(intent)
        }

        //OverScrollDecoratorHelper.setUpOverScroll(detail_scroll_view)

        //showDialog(0, "หัวข้อ","รหัส xxxxxxxxxx", arrayOf("แก้ไข","เก็บประวัติ", "ลบ"))

        detail_feature_reminder.setOnClickListener {
            var intent = Intent(this,DetailNotificationActivity::class.java)
            startActivity(intent)
        }

        val layouts = detail_title_area
        val v = detail_title_area.viewTreeObserver
        v.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    layouts.viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    layouts.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                //val width = layouts.measuredWidth
                val height = layouts.measuredHeight

                detail_scroll_view.viewTreeObserver.addOnScrollChangedListener {
                    //Log.e("Sc",detail_scroll_view.scrollY.toString())
/*
            if(detail_scroll_view.scrollY >= 120){
                if (!fab_status){
                    Animation.use.cardHideAnimation(this,detail_fab_card)
                }
            }else {
                if(fab_status){
                    Animation.use.cardLoadAnimation(this,detail_fab_card)
                }
            }
            */

                    if(detail_scroll_view.scrollY >= height){
                        detail_line.visibility = View.VISIBLE
                    }else {
                        detail_line.visibility = View.GONE
                    }
                }

            }
        })




    }

    fun setText(slot: CardData){
        if(slot.cardName.isEmpty()){
            detail_title_text.text = "ชื่อรายการ"
        }else{
            detail_title_text.text = slot.cardName
        }
        if (slot.breed.isEmpty()){
            detail_breed_text.text = "ไม่ระบุ"
        }else {
            detail_breed_text.text = slot.breed
        }
        detail_description_text.text = slot.cardID
        detail_amount_male_text.text = "${slot.amountMale} ตัว"
        detail_amount_female_text.text = "${slot.amountFemale} ตัว"
        detail_age_text.text = "${slot.ageWeek} สัปดาห์ ${slot.ageDay} วัน"
        detail_date_text.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
        detail_objective_text.text = ConvertCard().getObjective(slot.userObjective)
        detail_system_text.text = ConvertCard().getSystem(slot.systemFarm)

        detail_notify_me.text = ConvertCard().getBool(slot.notification.toBoolean())
        detail_notify_before.text = ConvertCard().getBool(slot.notiBefore.toBoolean())
    }

    fun showDialog() {
        val arr = arrayOf("ยืนยันการลบ")
/*
.setText("")
                .configText(object : ConfigText() {
                    override fun onConfig(params : TextParams) {
params.textSize = 10
                    }
                })
                .setPositive("ยืนยัน",null)
                .configPositive(object : ConfigButton(){
                    override fun onConfig(params: ButtonParams?) {
                        params!!.textSize = 50
                        params.textColor = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRed)
                        params.backgroundColorPress = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRedP)
                    }
                })

*/
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                        params.canceledOnTouchOutside = false
                    }
                })
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(this, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    path.removeValue()
                    showConDialog()
                }
                .configItems(object : ConfigItems(){
                    override fun onConfig(params: ItemsParams?) {
                        params!!.textColor = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRed)
                        params.backgroundColorPress = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRed)
                    }
                })
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorText)
                    }
                })
                .show()
    }

    fun showConDialog() {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("ลบเรียบร้อย")
                .configText(object :ConfigText(){
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@DetailActivity, MelonTheme.from(this@DetailActivity).getColor())
                        params.padding = intArrayOf(0,0,0,0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    this.finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@DetailActivity, R.color.colorText)
                    }
                })

                .show()



    }

}
