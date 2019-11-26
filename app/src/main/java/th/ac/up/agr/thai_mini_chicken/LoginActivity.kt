package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.activity_login.*
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

import android.content.pm.PackageManager
import android.os.Build

import android.text.InputType
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog

import com.mylhyl.circledialog.params.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.ActionDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.AlertsDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.QuickProgressDialog
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 56000
    private lateinit var mGoogleSigninCliend: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var progressDialog: DialogFragment


    companion object {
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<ImageView>(R.id.login_overlay).setImageDrawable(ContextCompat.getDrawable(this, MelonTheme.from(this).getOverlay()))

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSigninCliend = GoogleSignIn.getClient(this, option)
        firebaseAuth = FirebaseAuth.getInstance()


        login_help_area.setOnClickListener {
            showHelpDialog(0, "เมนูช่วยเหลือ", arrayOf("ลงทะเบียน", "คู่มือการใช้งาน", "ลืมรหัส", "ติดต่อผู้ดูแล"))
        }

        login_sign_in_google_btn.setOnClickListener {

            if (FirebaseAuth.getInstance().currentUser != null) {
                signOut()
            } else {
                signIn()
            }


        }

        login_logo_image.setOnClickListener {
            showColorDialog(0, "เลือกสีของแอป", arrayOf("เหลือง (ค่าเริ่มต้น)", "แดง", "เขียวอ่อน", "ฟ้าอ่อน", "ม่วง"))
        }

        login_forget_btn.setOnClickListener {
            showEditDialog()
        }

        login_sign_in_email_buttons.setOnClickListener {
            val username = login_sign_in_email_email.text.toString()
            val password = login_sign_in_email_password.text.toString()

            if (username.isNotEmpty() && password.length >= 6) {
                firebaseAuthWithEmail(username, password)
            } else {
                if (username.isEmpty() || password.isEmpty()) {
                    showAlertDialog(R.string.message_data_not_complete)
                } else if (password.length < 6) {
                    showAlertDialog(R.string.password_lower_message)
                }
            }


        }

        login_register_btn.setOnClickListener {
            toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString())
        }

    }

    fun showColorDialog(ID: Int, title: String, arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { parent, view, position, id ->
                    saveData(position)
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }
                .show()
    }

    fun showHelpDialog(ID: Int, title: String, arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { parent, view, position, id ->

                    when (position) {
                        0 -> {
                            toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString())
                        }
                        2 -> {
                            showEditDialog()
                        }
                        3 -> {
                            this@LoginActivity.startActivity(Intent(this@LoginActivity, HelpActivity::class.java))
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }
                .show()
    }

    private fun saveData(position: Int) {
        val database = AppTheme(this)
        val sqLiteDatabase = database.writableDatabase
        when (position) {
            0 -> {
                database.update(sqLiteDatabase, R.style.MelonTheme_Amber_Material)
            }
            1 -> {
                database.update(sqLiteDatabase, R.style.MelonTheme_Red_Material)
            }
            2 -> {
                database.update(sqLiteDatabase, R.style.MelonTheme_LightGreen_Material)
            }
            3 -> {
                database.update(sqLiteDatabase, R.style.MelonTheme_LightBlue_Material)
            }
            4 -> {
                database.update(sqLiteDatabase, R.style.MelonTheme_DeepPurple_Material)
            }
        }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("PERMISSION", "GRANTED")
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.e("PERMISSION", "DENIED")
                } else {
                    Log.e("PERMISSION", "NULL")
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    Log.e("SHOW", showRationale.toString())
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSigninCliend.signInIntent
        showProgressDialog()

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                progressDialog.dismiss()
                showAlertDialog(R.string.request_has_cancel)

            }
        }
    }

    private fun firebaseAuthWithEmail(userName: String, password: String) {
        showProgressDialog()

        firebaseAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startProcess()

                    } else {
                        task.exception?.localizedMessage?.let { error ->
                            progressDialog.dismiss()
                            when {
                                error.indexOf("invalid") > -1 -> ActionDialog(this).setTitle(R.string.alert_message).setMessage(R.string.wrong_password_message).positive(R.string.forget_password_message) {
                                    sendEmail(login_sign_in_email_email.text.toString())
                                }.negative(R.string.close_message_response).build().show()
                                error.indexOf("badly formatted") > -1 -> showAlertDialog(R.string.email_not_found_message)
                                error.indexOf("no user") > -1 -> ActionDialog(this).setTitle(R.string.alert_message).setMessage(R.string.email_cant_register_message).positive(R.string.register_message) { toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString()) }.negative(R.string.close_message_response).build().show()
                                error.indexOf("network error") > -1 -> showAlertDialog(R.string.internet_not_found_message)
                                else -> showAlertDialog(R.string.error_message)
                            }
                        }


                    }
                }
    }

    private fun toRegister(userName: String, password: String) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("USER", userName)
        intent.putExtra("PASS", password)
        startActivity(intent)
    }


    fun sendEmail(email: String) {
        showProgressDialog()
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    showAlertDialog(R.string.reset_email_send_complete_message)
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    it.localizedMessage?.let { error ->
                        when {
                            error.indexOf("badly formatted") > -1 -> showAlertDialog(R.string.email_not_found_message)
                            error.indexOf("no user") > -1 -> showAlertDialog(R.string.email_cant_register_message)
                            error.indexOf("network error") > -1 -> showAlertDialog(R.string.internet_not_found_message)
                            else -> showAlertDialog(R.string.error_message)
                        }
                    }

                }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        startProcess()

                    } else {
                        progressDialog.dismiss()
                        setErrorDialog("เกิดข้อผิดพลาด")
                        //updateUI(null)
                    }
                }
    }


    private fun updateUI(user: FirebaseUser? = null) {
        if (user != null) {
            Toast.makeText(this, user.uid, Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "SIGNOUT", Toast.LENGTH_SHORT).show()
        }
    }

    fun showConDialog() {

        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("บันทึกเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ") {
                    //activity.finish()
                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }

                .show()


    }

    fun setErrorDialog(string: String) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(string)
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }.show()


    }

    fun showAlertDialog(resource: Int) {
        AlertsDialog(this).setTitle(resource).positive(R.string.ok_message_response).build().show()
    }

    private fun startProcess() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setErrorDialog("คำขอถูกยกเลิก")
                Log.e("TEST-2", "CANCEL")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val info = p0.getValue(Information::class.java)!!

                    if (info.farmName.isEmpty() || info.username.isEmpty() || info.phoneNumber.isEmpty() || info.farmAddress.isEmpty()) {

                        ActionDialog(this@LoginActivity).setTitle(R.string.alert_message).setMessage(R.string.inital_information_question_message).positive(R.string.yes_message_response) {
                            newStartProcess()
                        }.negative(R.string.skip_message_response) {
                            val intent = Intent(this@LoginActivity, ProgramMainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.build().show()
                    } else {
                        val intent = Intent(this@LoginActivity, ProgramMainActivity::class.java)
                        progressDialog.dismiss()
                        //setInfo()
                        startActivity(intent)
                        firebase.removeEventListener(this)
                        finish()
                    }

                } else {
                    setInfo()
                }
            }
        })

    }

    private fun newStartProcess() {
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

            if (user.photoUrl.toString().isNotEmpty() && user.photoUrl != null) {
                this.photoURL = user.photoUrl.toString()
            }
            if (user.displayName.toString().isNotEmpty() && user.displayName != null) {
                this.username = user.displayName.toString()
            }
        }

        val firebase = Firebase.reference.child("ผู้ใช้").child(info.masterKey).child("รายละเอียด")
        firebase.setValue(info) { p0, p1 ->
            if (p0 != null) {
                setErrorDialog("เกิดข้อผิดพลาด")
            } else {
                progressDialog.dismiss()

                setErrorDialog("ลงทะเบียนเรียบร้อย")
            }
        }


    }

    private fun showEditDialog() {
        val dialog = AlertDialog.Builder(this)


        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        dialog.setView(dialogView)
        dialogView.dialog_add_text.text = "ใส่อีเมลที่ลงทะเบียนไว้"
        //dialog.setTitle(Title);
        //dialog.setMessage(Message);
        val editText = dialogView.custom_dialog_edittext
        editText.hint = "อีเมล"
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorText))
        editText.requestFocus()

        editText.inputType = InputType.TYPE_CLASS_TEXT

        val abc = dialog.create()
        abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        abc.show()

        dialogView.dialog_add_cancel.setOnClickListener {
            abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            editText.clearFocus()
            abc.cancel()
        }

        dialogView.dialog_add_confirm.setCardBackgroundColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))

        dialogView.dialog_add_confirm.setOnClickListener {
            val x = editText.text.toString()
            if (x.isNotEmpty()) {
                sendEmail(x)
                abc.cancel()
            } else {
                //setErrorDialog("คุณไม่ได้กรอกอีเมล")
                abc.cancel()
            }
        }
    }

    fun showProgressDialog() {
        progressDialog = QuickProgressDialog(this).build().show()
    }

    private fun signOut() {
        firebaseAuth.signOut()
        mGoogleSigninCliend.signOut().addOnCompleteListener(this) { updateUI() }
    }

}
