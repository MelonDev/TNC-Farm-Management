package th.ac.up.agr.thai_mini_chicken.AddProgramActivity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.aigestudio.wheelpicker.widgets.WheelDayPicker
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker
import com.aigestudio.wheelpicker.widgets.WheelYearPicker

import kotlinx.android.synthetic.main.activity_add_program.*
import th.ac.up.agr.thai_mini_chicken.R
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.AddDataFirebase
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme


class AddProgramActivity : AppCompatActivity() {

    var position = -1

    lateinit var dateTV :TextView
    lateinit var objectiveTV :TextView
    lateinit var ageTV :TextView

    lateinit var dataCard: CardData

    lateinit var ID :String


    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.MelonTheme_Amber_Material)
        setTheme(MelonTheme.from(this).getStyle())

        //AddDataFirebase.from(this).setDataToActive(CardData())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_program)

        val bundle = intent.extras
        ID = bundle.getString("ID")

        dataCard = CardData()

        val dialog = AddBottomDialog.load(this)

        dateTV = add_program_date_text
        objectiveTV = add_program_objective_text
        ageTV = add_program_age_text

        if(ID.contentEquals("0")){
            dialog.setOnStart()
            add_program_title_name.text = "เพิ่มรายการข้อมูล"
        }else if(ID.contentEquals("1")){
            val card_key = bundle.getString("CARD_KEY")
            val user_ID = bundle.getString("USER_ID")

            add_program_title_name.text = "แก้ไขรายการข้อมูล"

            val database = FirebaseDatabase.getInstance().reference
            val data = database.child("ผู้ใช้").child(user_ID).child("รายการ").child("ใช้งาน").child(card_key).child("รายละเอียด")

            data.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.value != null){
                        dataCard = p0.getValue(CardData::class.java)!!
                        dialog.onEdit()
                    }
                }
            })
        }


        //add_program_edittext.clearFocus()

        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        hideKeyB()

        add_program_back_btn.setOnClickListener { finish() }

        add_program_date_area.setOnClickListener {
            dialog.dateDialog().show()
            position = 0
            dialog.setTitle(position)
        }

        add_program_objective_area.setOnClickListener {
            //dialog.objectiveDialog().show()
            dialog.newObjectiveDialog()
            position = 1
            //dialog.setTitle(position)
        }

        add_program_amount_area.setOnClickListener {
            dialog.amountDialog().show()
            position = 5
            dialog.setTitle(position)
        }

        add_program_system_area.setOnClickListener {
            dialog.systemDialog()
            position = 3
        }

        add_program_breed_area.setOnClickListener {
            dialog.showEditDialog()
            position = 4
        }

        add_program_age_area.setOnClickListener {
            dialog.ageDialog().show()
            position = 2
            dialog.setTitle(position)
        }

    }

    fun hideKeyB(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }


}
