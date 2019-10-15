package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
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
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_test_auth.*

class TestAuthActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 56000
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_auth)

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //val client = GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi()
        googleSignInClient = GoogleSignIn.getClient(this, option)
        firebaseAuth = FirebaseAuth.getInstance()
        sign_in_btn.setOnClickListener {
            signIn()
        }
        sign_out_btn.setOnClickListener {
            signOut()
        }


        

    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.getCurrentUser()
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                })
    }
    private fun signOut() {
        firebaseAuth.signOut()
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void> { updateUI(null) })
    }
    private fun revokeAccess() {
        firebaseAuth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                OnCompleteListener<Void> { updateUI(null) })
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, user.uid, Toast.LENGTH_SHORT).show()
            testLogin_profile_area.visibility = View.VISIBLE
            testLogin_profile_name.text = user.displayName
            testLogin_profile_email.text = user.email
            testLogin_profile_phone.text = user.phoneNumber
            sign_in_btn.visibility = View.GONE
            sign_out_btn.visibility = View.VISIBLE
            Picasso.get().load(user.photoUrl).into(testLogin_profile_image)
        } else {
            Toast.makeText(this, "SIGNOUT", Toast.LENGTH_SHORT).show()
            sign_in_btn.visibility = View.VISIBLE
            sign_out_btn.visibility = View.GONE
            testLogin_profile_area.visibility = View.GONE
        }
    }

}
