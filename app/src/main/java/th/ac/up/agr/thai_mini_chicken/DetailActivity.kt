package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle

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
import th.ac.up.agr.thai_mini_chicken.Tools.Animation
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.ToolReference
import java.util.*
import kotlin.collections.ArrayList


class DetailActivity : AppCompatActivity() {

    var fab_status = false
    lateinit var path: DatabaseReference

    var type = ""

    var card_key = ""
    var user_ID = ""

    lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bundle = intent.extras!!
        card_key = bundle.getString("CARD_KEY")!!
        user_ID = bundle.getString("USER_ID")!!

        //var count = 0

        val database = FirebaseDatabase.getInstance().reference
        path = database.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key)
        val data = database.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key).child("รายละเอียด")

        data.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {

                    val data = p0.getValue(CardData::class.java)
                    setText(data!!)
                }
            }
        })

        window.statusBarColor = resources.getColor(MelonTheme.from(this).getStatusBarOverlay())

        //Log.e("DATA",AppTheme(this).read().toString())

        val userRef = Firebase.reference.child("ผู้ใช้").child(user_ID)
        val container = userRef.child("รายการ")

        ref = container.child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ")

        val refs = container.child("ใช้งาน").child(card_key).child("รายละเอียด")

        activity_detail_indicator_text.text = "0"

        refs.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val slot = p0.getValue(CardData::class.java)!!

                    detail_fab_card.setOnClickListener {
                        val a = data.child("status")
                        if (slot.status.contentEquals("ACTIVE")) {
                            a.setValue("INACTIVE")
                        } else if (slot.status.contentEquals("INACTIVE")) {
                            a.setValue("ACTIVE")
                        }
                    }

                    if(slot.managerObjective.contentEquals("1") || slot.managerObjective.contentEquals("0")){
                       /* if(slot.managerObjective.contentEquals("1") && slot.managerName.contentEquals("ว่างเปล่า")){
                            detail_manager_text.text = slot.managerName
                        }else {
                            val zz = ConvertCard().getObjective()
                            val yy = zz.indexOf()
                        }
                        */
                        detail_manager_text.text = slot.managerName
                    }else if(slot.managerObjective.contentEquals("2")){
                        val firebase = Firebase.reference.child("ผู้ใช้").child(user_ID).child("รูปแบบ").child(slot.managerName).child("รายละเอียด").child("name")
                        firebase.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.value != null){
                                    detail_manager_text.text = p0.value.toString()
                                }else {
                                    detail_manager_text.text = "ชุดรูปแบบถูกลบไปแล้ว"
                                }
                            }
                        })
                    }

                    if (slot.status.contentEquals("ACTIVE")) {
                        detail_fab_text.text = "เก็บประวัติ"
                        detail_fab_card.setCardBackgroundColor(ContextCompat.getColor(this@DetailActivity, MelonTheme.from(this@DetailActivity).getColor()))
                        detail_feature_reminder.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, MelonTheme.from(this@DetailActivity).getColor()))
                        activity_detail_indicator_text.setTextColor(ContextCompat.getColor(this@DetailActivity, MelonTheme.from(this@DetailActivity).getColor()))

                    } else if (slot.status.contentEquals("INACTIVE")) {
                        detail_fab_text.text = "กู้คืน"
                        detail_fab_card.setCardBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.colorText))
                        detail_feature_reminder.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.colorText))
                        activity_detail_indicator_text.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.colorText))
                    }

                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("","")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.value != null) {
                                getEvents(p0, slot)
                                //recyclerView.layoutManager.smoothScrollToPosition(recyclerView, RecyclerView.State(), 0)

                            }else {
                                activity_detail_indicator_text.text = "0"

                            }
                        }
                    })

                }


            }
        })


        detail_back_btn.setOnClickListener {
            finish()
        }

        detail_delete_btn.setOnClickListener {
            showDialog()
        }

        detail_edit_btn.setOnClickListener {
            val intent = Intent(this, AddProgramActivity::class.java)
            intent.putExtra("ID", "1")
            intent.putExtra("USER_ID", user_ID)
            intent.putExtra("CARD_KEY", card_key)
            startActivity(intent)
        }

        //OverScrollDecoratorHelper.setUpOverScroll(detail_scroll_view)

        //showDialog(0, "หัวข้อ","รหัส xxxxxxxxxx", arrayOf("แก้ไข","เก็บประวัติ", "ลบ"))

        detail_feature_reminder.setOnClickListener {
            var intent = Intent(this, DetailNotificationActivity::class.java)
            intent.putExtra("USER_ID", user_ID)
            intent.putExtra("CARD_KEY", card_key)
            intent.putExtra("TYPE", "0")
            startActivity(intent)
        }

        val layouts = detail_title_area
        val v = detail_title_area.viewTreeObserver
        v.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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

                    if (detail_scroll_view.scrollY >= height) {
                        detail_line.visibility = View.VISIBLE
                    } else {
                        detail_line.visibility = View.GONE
                    }
                }

            }
        })


    }

    fun setText(slot: CardData) {
        if (slot.cardName.isEmpty()) {
            detail_title_text.text = "ชื่อรายการ"
        } else {
            detail_title_text.text = slot.cardName
        }
        if (slot.breed.isEmpty()) {
            detail_breed_text.text = "ไม่ระบุ"
        } else {
            detail_breed_text.text = slot.breed
        }

        type = slot.userObjective


        val dat = Date().reDate(slot.lastUpdate)

        detail_description_text.text = "อัปเดตล่าสุดวันที่ ${dat.day}"
        detail_amount_male_text.text = "${slot.amountMale} ตัว"
        detail_amount_female_text.text = "${slot.amountFemale} ตัว"
        detail_age_text.text = "${slot.ageWeek} สัปดาห์ ${slot.ageDay} วัน"
        detail_date_text.text = "${slot.dateDay} ${ConvertCard().getMonth(slot.dateMonth)} ${ConvertCard().getYear(slot.dateYear)}"
        detail_objective_text.text = ConvertCard().getObjective(slot.userObjective)
        detail_system_text.text = ConvertCard().getSystem(slot.systemFarm)

        detail_notify_me.text = ConvertCard().getBool(slot.notification.toBoolean())
        detail_notify_before.text = ConvertCard().getBool(slot.notiBefore.toBoolean())

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

            detail_age_current_text.text = "$week สัปดาห์ $day วัน"
        }else {
            detail_age_current_text.text = "ยังไม่ถึงวันรับเข้า"

        }



        //Log.e("day",days.toString())
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
                .configDialog { params ->
                    params.animStyle = R.style.dialogWindowAnim
                    params.canceledOnTouchOutside = false
                }
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(this, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    //path.removeValue()
                    path.removeValue()
                    showConDialog()
                }
                .configItems { params ->
                    params!!.textColor = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRed)
                    params.backgroundColorPress = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorRed)
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@DetailActivity.applicationContext, R.color.colorText)
                }
                .show()
    }

    fun showConDialog() {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("ลบเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@DetailActivity, MelonTheme.from(this@DetailActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                    this.finish()
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@DetailActivity, R.color.colorText)
                }

                .show()


    }

    fun getEvents(dataSnapshot: DataSnapshot?, cardData: CardData) {
/*
        val z = Event()
        z.apply {
            title = "INACTIVE_BUTTON"
            day = -1
            week = -1
            message = "BUTTON"
        }
        arrEvent.add(z)
*/
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())

        calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
        calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

        val arrEvent = ArrayList<Event>()
        arrEvent.clear()

        activity_detail_indicator_text.text = arrEvent.size.toString()
        //activity_detail_indicator_text.text = dataSnapshot!!.children.count().toString()


        dataSnapshot!!.children.forEach {
            //Log.e("KEY",it.key.toString())

            val slot = it.getValue(Event::class.java)!!

            if (slot.cardID.isEmpty()) {
                slot.cardID = it.key.toString()

                val a = ref.child(it.key.toString())
                a.setValue(slot)
            }

            val x = calendar.clone() as Calendar

            x.add(Calendar.WEEK_OF_YEAR, slot.week)
            x.add(Calendar.DAY_OF_YEAR, slot.day)

            val difference = x.timeInMillis - today.timeInMillis
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()

            //Log.e("DAY", days.toString())


            if (days >= 0 && slot.status.contentEquals("ACTIVE")) {
                arrEvent.add(slot)
            }


            activity_detail_indicator_text.text = arrEvent.size.toString()

            //Log.e("arr",arrEvent.size.toString())


        }

    }

}
