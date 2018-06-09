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
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 56000
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var waitDialog: DialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //val client = GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi()
        googleSignInClient = GoogleSignIn.getClient(this, option)
        firebaseAuth = FirebaseAuth.getInstance()

        login_sign_in_google_btn.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                signOut()
            } else {
                signIn()
            }
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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
                        waitDialog.dismiss()
                        setErrorDialog("เกิดข้อผิดพลาด")
                        //Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                })
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
                        val intent = Intent(this@LoginActivity, RegisterInfoActivity::class.java)
                        waitDialog.dismiss()
                        finish()
                        intent.putExtra("ID", "0")
                        //setInfo()
                        firebase.removeEventListener(this)
                        startActivity(intent)
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
