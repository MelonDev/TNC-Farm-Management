package th.ac.up.agr.thai_mini_chicken

import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*

import kotlinx.android.synthetic.main.activity_register_info.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener


class RegisterInfoActivity : AppCompatActivity() {

    var ID = ""

    lateinit var userInfo: Information

    var offScreen = false

    lateinit var waitDialog: DialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val bundle = intent.extras
        ID = bundle.getString("ID")

        userInfo = Information()

        val authUser = FirebaseAuth.getInstance().currentUser!!

        val firebase = Firebase.reference.child("ผู้ใช้").child(authUser.uid).child("รายละเอียด")

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    userInfo = p0.getValue(Information::class.java)!!

                    getData()
                }
            }
        })

        register_info_back_btn.setOnClickListener {
            backAction()
        }

        register_info_confirm_btn.setOnClickListener {
            if (!offScreen) {
                offScreen = true
                getDataToData()
            }
        }
/*
        register_info_image_area.setOnClickListener {
        }
*/

    }

    fun getData() {
        if (userInfo.username != null) {
            register_info_name_edittext.setText(userInfo.username)
        }
        if (userInfo.phoneNumber != null) {
            register_info_phone_edittext.setText(userInfo.phoneNumber)
        }
        if (userInfo.farmName != null) {
            register_info_farm_name_edittext.setText(userInfo.farmName)
        }
        if (userInfo.farmAddress != null) {
            register_info_farm_address_edittext.setText(userInfo.farmAddress)
        }
        if (userInfo.photoURL != null && userInfo.photoURL.isNotEmpty()) {
            Picasso.get().load(userInfo.photoURL).error(R.drawable.man).into(register_info_image_image)
        }
    }

    fun getDataToData() {
        setWaitDialog("กำลังบันทึก...")

        userInfo.apply {
            this.username = this@RegisterInfoActivity.register_info_name_edittext.text.toString()
            this.phoneNumber = register_info_phone_edittext.text.toString()
            this.farmName = register_info_farm_name_edittext.text.toString()
            this.farmAddress = register_info_farm_address_edittext.text.toString()
        }

        if (userInfo.username.isEmpty() || userInfo.farmName.isEmpty() || userInfo.farmAddress.isEmpty() || userInfo.phoneNumber.isEmpty()) {
            offScreen = false

            waitDialog.dismiss()
            setErrorDialog("ข้อมูลไม่ครบ")
        } else {
            pushToFirebase()
        }
    }

    fun pushToFirebase() {
        val firebase = Firebase.reference.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายละเอียด")

        firebase.setValue(userInfo) { p0, p1 ->
            if (p0 != null) {
                waitDialog.dismiss()
                setErrorDialog("เกิดข้อผิดพลาด")
                offScreen = false
            } else {
                waitDialog.dismiss()
                showConDialog()
            }
        }
    }

    fun setErrorDialog(string: String) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(string)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    }
                }).show()


    }

    fun showConDialog() {
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
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    offScreen = false
                    val intent = Intent(this, ProgramMainActivity::class.java)
                    startActivity(intent)
                    finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    }
                })

                .show()


    }

    override fun onBackPressed() {
        super.onBackPressed()

        backAction()

    }

    fun setWaitDialog(string: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)
    }

    fun backAction() {
        if (ID.isNotEmpty()) {
            if (ID.contentEquals("0")) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else if (ID.contentEquals("1")) {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}
