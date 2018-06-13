package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.activity_login.*
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import com.google.android.gms.tasks.Task
import android.R.attr.password
import android.R.attr.password
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.WindowManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 56000
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var waitDialog: DialogFragment


    companion object {
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //window.statusBarColor = ContextCompat.getColor(this,R.color.colorStatusBarOverlay)

        login_overlay.setImageDrawable(ContextCompat.getDrawable(this,MelonTheme.from(this).getOverlay()))

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //val client = GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi()
        googleSignInClient = GoogleSignIn.getClient(this, option)
        firebaseAuth = FirebaseAuth.getInstance()

        login_help_area.setOnClickListener {
            showHelpDialog(0,"เมนูช่วยเหลือ", arrayOf("ลงทะเบียน", "คู่มือการใช้งาน", "ลืมรหัส", "ติดต่อผู้ดูแล"))
        }

        login_sign_in_google_btn.setOnClickListener {


            /*val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val uri = Uri.fromParts ("package", packageName, null)
            intent.data = uri
            startActivity(intent)
*/
/*
            Log.e("PER",ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE).toString())


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("PERMISION", "FAIL")
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            } else {
                Log.e("PER", "0")
            }
*/

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
            //setQuestionDialog(2,"คุณต้องการรีเซ็ตรหัสผ่าน?","ใช่",true,"ไม่",false)
            showEditDialog()
        }

        login_sign_in_email_buttons.setOnClickListener {
            val username = login_sign_in_email_email.text.toString()
            val password = login_sign_in_email_password.text.toString()

            if (username.isNotEmpty() && password.length >= 6) {
                firebaseAuthWithEmail(username, password)
            } else {
                if (username.isEmpty() || password.isEmpty()) {
                    setErrorDialog("ข้อมูลไม่ครบ")
                } else if (password.length < 6) {
                    setErrorDialog("รหัสอย่างน้อย 6 ตัว")
                }
            }


        }

/*

        login_sign_in_google_btn.setOnLongClickListener {
            val intent = Intent(this, ProgramMainActivity::class.java)
            startActivity(intent)
            //finish()
            return@setOnLongClickListener false
        }
*/
        login_register_btn.setOnClickListener {
            toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString())
        }

    }

    fun showColorDialog(ID: Int, title: String, arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { parent, view, position, id ->
                    saveData(position)
                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                    }
                })
                .show()
    }

    fun showHelpDialog(ID: Int, title: String, arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { parent, view, position, id ->
                    //saveData(position)
                    when(position){
                        0 -> {
                            toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString())
                        }
                        1 -> {
                            Log.e("","")
                        }
                        2 -> {
                            showEditDialog()
                        }
                        3 -> {
                            Log.e("","")
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                    }
                })
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
        val signInIntent = googleSignInClient.signInIntent
        setWaitDialog("กำลังดำเนินการ...")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        //Log.e("TEST", "TEST")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                waitDialog.dismiss()
                setErrorDialog("คำขอถูกยกเลิก")
                //Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
        }
    }

    private fun firebaseAuthWithEmail(userName: String, password: String) {
        setWaitDialog("กำลังดำเนินการ...")

        firebaseAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        //waitDialog.dismiss()
                        startProcess()
                        //val user = firebaseAuth.getCurrentUser()
                        //updateUI(user)
                        //Log.w(FragmentActivity.TAG, "signInWithEmail", task.exception!!.message)
                        //Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    } else {
                        val error = task.exception!!.localizedMessage.toString()

                        //val a = setErrorDialog("s")

                        waitDialog.dismiss()
                        if (error.indexOf("invalid") > -1) {
                            setQuestionDialog(1, "รหัสผ่านไม่ถูกต้อง", "ลืมรหัส", true, "ปิด", false)
                        } else if (error.indexOf("badly formatted") > -1) {
                            setErrorDialog("รูปแบบอีเมลไม่ถูกต้อง")
                        } else if (error.indexOf("no user") > -1) {
                            setQuestionDialog(0, "ลีเมลนี้ยังไม่มีการลงทะเบียน", "ลงทะเบียน", true, "ปิด", false)
                        } else if (error.indexOf("network error") > -1) {
                            setErrorDialog("กรุณาเช็คการเชื่อมต่่ออินเทอร์เน็ต")
                        }
                        //setErrorDialog("ลีเมลนี้ยังไม่มีการลงทะเบียน")
                        //Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                })
    }

    fun toRegister(userName: String, password: String) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("USER", userName)
        intent.putExtra("PASS", password)
        startActivity(intent)
    }

    fun toForget() {
        Log.e("TEST", "FORGET")
        sendEmail(login_sign_in_email_email.text.toString())
    }


    fun sendEmail(email: String) {
        setWaitDialog("กำลังดำเนินการ")
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    waitDialog.dismiss()
                    setErrorDialog("อีเมลขั้นตอนการรีเซ็ตได้ถูกส่งไปให้คุณเรียบร้อย")
                }.addOnFailureListener {
                    //Log.e("LOG",it.localizedMessage.toString())
                    waitDialog.dismiss()
                    val error = it.localizedMessage.toString()

                    if (error.indexOf("badly formatted") > -1) {
                        setErrorDialog("รูปแบบอีเมลไม่ถูกต้อง")
                    } else if (error.indexOf("no user") > -1) {
                        setErrorDialog("ลีเมลนี้ยังไม่มีการลงทะเบียน")
                    } else if (error.indexOf("network error") > -1) {
                        setErrorDialog("กรุณาเช็คการเชื่อมต่่ออินเทอร์เน็ต")
                    } else {
                        setErrorDialog("เกิดข้อผิดพลาด")
                    }
                }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        //waitDialog.dismiss()

                        startProcess()

                        //Log.e("PROVIDER",firebaseAuth.currentUser!!.providerId)
                        //Log.e("PROVIDER",firebaseAuth.currentUser!!.uid)

                        //val user = firebaseAuth.getCurrentUser()
                        //updateUI(user)
                    } else {
                        waitDialog.dismiss()
                        setErrorDialog("เกิดข้อผิดพลาด")
                        //updateUI(null)
                    }
                })
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
                        params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //activity.finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                    }
                })

                .show()


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
                        params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                    }
                }).show()


    }

    fun setQuestionDialog(ID: Int, title: String, positive: String, positiveLight: Boolean, negative: String, negativeLight: Boolean) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(title)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive(positive, {
                    if (ID == 0) {
                        toRegister(login_sign_in_email_email.text.toString(), login_sign_in_email_password.text.toString())
                    } else if (ID == 1) {
                        sendEmail(login_sign_in_email_email.text.toString())
                    } else if (ID == 2) {
                        showEditDialog()
                    }
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        if (positiveLight) {
                            params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())

                        } else {
                            params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                        }
                    }
                })
                .setNegative(negative, {

                })
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        if (negativeLight) {
                            params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())

                        } else {
                            params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                        }
                    }
                })
                .show()


    }

    fun startProcess() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setErrorDialog("คำขอถูกยกเลิก")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val info = p0.getValue(Information::class.java)!!

                    if (info.farmName.isEmpty() || info.username.isEmpty() || info.phoneNumber.isEmpty() || info.farmAddress.isEmpty()) {
                        setQuestionDialogss()
                    } else {
                        val intent = Intent(this@LoginActivity, ProgramMainActivity::class.java)
                        waitDialog.dismiss()
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
                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                        params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)

                    }
                })
                .setTitle("คำอธิบาย")
                .configTitle(object : ConfigTitle() {
                    override fun onConfig(params: TitleParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                    }
                })
                .setPositive("ตกลง", {
                    newStartProcess()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@LoginActivity, MelonTheme.from(this@LoginActivity).getColor())
                    }
                })
                .setNegative("ข้าม", {
                    val intent = Intent(this@LoginActivity, ProgramMainActivity::class.java)
                    startActivity(intent)
                    finish()
                })
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50

                        params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)

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
                waitDialog.dismiss()

                setErrorDialog("ลงทะเบียนเรียบร้อย")
            }
        }


    }

    fun showEditDialog() {
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
                setErrorDialog("คุณไม่ได้กรอกอีเมล")
                abc.cancel()
            }
        }
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

    private fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void> { updateUI(null) })
    }

}
