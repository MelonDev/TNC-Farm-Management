package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.content.SharedPreferences
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

import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.ActionDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.AlertsDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.QuickProgressDialog


class LoginActivity : AppCompatActivity() {

    private val RCSIGNIN = 56000
    private lateinit var mGoogleSigninCliend: GoogleSignInClient
    private lateinit var mFirebaseAuth: FirebaseAuth

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
        mFirebaseAuth = FirebaseAuth.getInstance()


        login_help_area.setOnClickListener {
            showHelpDialog(getString(R.string.help_menu_title), arrayOf(R.string.help_menu_register, R.string.help_menu_manual, R.string.help_menu_forget_password, R.string.help_menu_contact_admin).map { getString(it) }.toTypedArray())
        }

        login_sign_in_google_btn.setOnClickListener {

            mFirebaseAuth.currentUser?.let {
                signOut()
            } ?: run {
                signIn()
            }

        }

        login_logo_image.setOnClickListener {
            showColorDialog(arrayOf(R.string.color_menu_amber, R.string.color_menu_red, R.string.color_menu_light_green, R.string.color_menu_light_blue, R.string.color_munu_purple).map { getString(it) }.toTypedArray())
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

    private fun showColorDialog(arr: Array<String>) {
        CircleDialog.Builder(
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(getString(R.string.color_menu_title))
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { _, _, position, _ ->
                    saveData(position)
                }
                .setNegative(getString(R.string.cancel_message_response), null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }
                .show(supportFragmentManager)
    }

    private fun showHelpDialog(title: String, arr: Array<String>) {
        CircleDialog.Builder(
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arr) { _, _, position, _ ->

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
                .setNegative(getString(R.string.cancel_message_response), null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@LoginActivity, R.color.colorText)
                }
                .show(supportFragmentManager)
    }

    private fun saveData(position: Int) {


        val style = when (position) {
            0 -> {
                R.style.MelonTheme_Amber_Material
            }
            1 -> {
                R.style.MelonTheme_Red_Material
            }
            2 -> {
                R.style.MelonTheme_LightGreen_Material
            }
            3 -> {
                R.style.MelonTheme_LightBlue_Material
            }
            4 -> {
                R.style.MelonTheme_DeepPurple_Material
            }
            else -> {
                -1
            }
        }

        savingStyle(style)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun savingStyle(style: Int) {
        val sharedPref: SharedPreferences = getSharedPreferences("MELON_THEME", 0)
        sharedPref.edit().putInt("STYLE", style).apply()
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

        startActivityForResult(signInIntent, RCSIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RCSIGNIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    firebaseAuthWithGoogle(account)

                }
            } catch (e: ApiException) {
                progressDialog.dismiss()
                showAlertDialog(R.string.request_has_cancel)

            }
        }
    }

    private fun firebaseAuthWithEmail(userName: String, password: String) {
        showProgressDialog()

        mFirebaseAuth.signInWithEmailAndPassword(userName, password)
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


    private fun sendEmail(email: String) {
        showProgressDialog()
        mFirebaseAuth.sendPasswordResetEmail(email)
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
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        startProcess()

                    } else {
                        progressDialog.dismiss()
                        showAlertDialog(R.string.error_message)
                    }
                }
    }


    private fun updateUI(user: FirebaseUser? = null) {

        user?.let {
            Toast.makeText(this, user.uid, Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, getString(R.string.sign_out_message), Toast.LENGTH_SHORT).show()
        }

    }


    private fun showAlertDialog(resource: Int) {
        AlertsDialog(this).setTitle(resource).positive(R.string.ok_message_response).build().show()
    }

    private fun startProcess() {
        mFirebaseAuth.currentUser?.let { user ->

            val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")
            firebase.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    showAlertDialog(R.string.request_has_cancel)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.getValue(Information::class.java)?.let { info ->
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
                            startActivity(intent)
                            firebase.removeEventListener(this)
                            finish()
                        }
                    } ?: run {
                        setInfo()
                    }
                }
            })

        }


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
        mFirebaseAuth.currentUser?.let { user ->
            info.apply {
                this.masterKey = user.uid
                this.email = user.email.toString()

                if (user.photoUrl.toString().isNotEmpty() && user.photoUrl != null) {
                    this.photoURL = user.photoUrl.toString()
                }
                if (user.displayName.toString().isNotEmpty() && user.displayName != null) {
                    this.username = user.displayName.toString()
                }
            }

            val firebase = Firebase.reference.child("ผู้ใช้").child(info.masterKey).child("รายละเอียด")
            firebase.setValue(info) { p0, _ ->

                p0?.let {
                    showAlertDialog(R.string.error_message)
                } ?: run {
                    progressDialog.dismiss()
                    showAlertDialog(R.string.register_successful_message)
                }

            }
        }


    }

    private fun showEditDialog() {
        val dialog = AlertDialog.Builder(this)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)

        dialog.setView(dialogView)
        dialogView.dialog_add_text.text = getString(R.string.registed_email_message)

        val editText = dialogView.custom_dialog_edittext
        editText.hint = getString(R.string.email_message)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorText))
        editText.requestFocus()

        editText.inputType = InputType.TYPE_CLASS_TEXT

        val abc = dialog.create()

        abc.window?.let {
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        abc.show()

        dialogView.dialog_add_cancel.setOnClickListener {

            abc.window?.let {
                it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                editText.clearFocus()
                abc.cancel()
            }

        }

        dialogView.dialog_add_confirm.setCardBackgroundColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))

        dialogView.dialog_add_confirm.setOnClickListener {
            val x = editText.text.toString()
            if (x.isNotEmpty()) {
                sendEmail(x)
                abc.cancel()
            } else {
                abc.cancel()
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = QuickProgressDialog(this).build().show()
    }

    private fun signOut() {
        mFirebaseAuth.signOut()
        mGoogleSigninCliend.signOut().addOnCompleteListener(this) { updateUI() }
    }


}
