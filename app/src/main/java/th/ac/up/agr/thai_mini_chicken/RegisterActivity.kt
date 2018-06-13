package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.*

import kotlinx.android.synthetic.main.activity_register.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class RegisterActivity : AppCompatActivity() {

    lateinit var waitDialog: DialogFragment

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val bundle = intent.extras
        val u = bundle.getString("USER")
        val p = bundle.getString("PASS")

        register_email_edittext.setText(u)
        register_password_edittext.setText(p)

        firebaseAuth = FirebaseAuth.getInstance()

        register_back_btn.setOnClickListener {
            finish()
        }

        register_confirm_btn.setOnClickListener {
            onRegister()
        }

    }

    fun onRegister() {
        val email = register_email_edittext.text.toString()
        val passwordA = register_password_edittext.text.toString()
        val passwordB = register_password_again_edittext.text.toString()

        if (email.isNotEmpty() && passwordA.length >= 6 && passwordA.contentEquals(passwordB)) {
            setWaitDialog("กำลังดำเนินการ...")
            firebaseAuthCreateWithEmail(email, passwordA)
        } else {
            //Toast.makeText(this, "ข้อมูลไม่ครบ", Toast.LENGTH_SHORT).show()
            if (email.isEmpty() || passwordA.isEmpty() || passwordB.isEmpty()) {
                setErrorDialog("ข้อมูลไม่ครบ")
            } else if (passwordA.length < 6) {
                setErrorDialog("รหัสผ่านอย่างน้อย 6 ตัว")
            } else if (!passwordA.contentEquals(passwordB)) {
                setErrorDialog("รหัสผ่านไม่ตรงกัน")
            } else {
                setErrorDialog("เกิดข้อผิดพลาด")
            }

        }
    }

    private fun firebaseAuthCreateWithEmail(userName: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(this, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                        startProcess()
                    } else {
                        waitDialog.dismiss()
                        setErrorDialog("อีเมลนี้ได้ลงทะเบียนแล้ว")

                        //Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                })
    }

    fun startProcess() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")
        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setErrorDialog("คำขอถูกยกเลิก")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val info = p0.getValue(Information::class.java)!!

                    if(info.farmName.isEmpty() || info.username.isEmpty() || info.phoneNumber.isEmpty() || info.farmAddress.isEmpty()){
                        setQuestionDialogss()
                    } else {
                        val intent = Intent(this@RegisterActivity, ProgramMainActivity::class.java)
                        waitDialog.dismiss()
                        startActivity(intent)
                        finish()
                    }

                } else {
                    setInfo()
                }
            }
        })

    }

    fun setQuestionDialogss() {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("บัญชีของคุณยังไม่ได้ใส่ข้อมูลพื้นฐานต่างๆ คุณต้องการไปใส่ข้อมูลตอนนี้เลยไหม?")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, R.color.colorText)
                        params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)

                    }
                })
                .setTitle("คำอธิบาย")
                .configTitle(object : ConfigTitle() {
                    override fun onConfig(params: TitleParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, MelonTheme.from(this@RegisterActivity).getColor())
                    }
                })
                .setPositive("ตกลง", {
                    newStartProcess()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, MelonTheme.from(this@RegisterActivity).getColor())
                    }
                })
                .setNegative("ข้าม", {
                    val intent = Intent(this@RegisterActivity, ProgramMainActivity::class.java)
                    startActivity(intent)
                    finish()
                })
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50

                        params.textColor = ContextCompat.getColor(this@RegisterActivity, R.color.colorText)

                    }
                })
                .show()


    }

    fun newStartProcess() {
        val intent = Intent(this, RegisterInfoActivity::class.java)
        finish()
        intent.putExtra("ID", "0")
        startActivity(intent)
        finish()
    }

    fun setInfo() {
        val info = Information()
        val user = FirebaseAuth.getInstance().currentUser!!

        info.apply {
            this.masterKey = user.uid.toString()
            this.email = user.email.toString()

            if(user.photoUrl.toString().isNotEmpty()){
                this.photoURL = user.photoUrl.toString()
            }
            if(user.displayName.toString().isNotEmpty()){
                this.username = user.displayName.toString()
            }
        }

        val firebase = Firebase.reference.child("ผู้ใช้").child(info.masterKey).child("รายละเอียด")
        firebase.setValue(info){p0,p1 ->
            if(p0 != null){
                setErrorDialog("เกิดข้อผิดพลาด")
            } else {
                waitDialog.dismiss()
                setConDialog("ลงทะเบียนเรียบร้อย")
            }
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, user.uid, Toast.LENGTH_SHORT).show()
            /*   testLogin_profile_area.visibility = View.VISIBLE
               testLogin_profile_name.text = user.displayName
               testLogin_profile_email.text = user.email
               testLogin_profile_phone.text = user.phoneNumber
               sign_in_btn.visibility = View.GONE
               sign_out_btn.visibility = View.VISIBLE
               Picasso.get().load(user.photoUrl).into(testLogin_profile_image)
               */
        } else {
            Toast.makeText(this, "SIGNOUT", Toast.LENGTH_SHORT).show()
            /*
            sign_in_btn.visibility = View.VISIBLE
            sign_out_btn.visibility = View.GONE
            testLogin_profile_area.visibility = View.GONE
            */
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
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, MelonTheme.from(this@RegisterActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, R.color.colorText)
                    }
                }).show()


    }

    fun setConDialog(string: String) {
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
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, MelonTheme.from(this@RegisterActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("ต่อไป", {
                    setWaitDialog("กำลังดำเนินการต่อ")
                    startProcess()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterActivity, R.color.colorText)
                    }
                }).show()


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


}
