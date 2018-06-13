package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_program_main.*
import kotlinx.android.synthetic.main.nav_header_program_main.view.*
import th.ac.up.agr.thai_mini_chicken.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

class ProgramNavDrawer(private val activity: ProgramMainActivity) {

    var navView = activity.nav_view.getHeaderView(0)

    init {
        val firebase = Firebase.reference.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายละเอียด")
        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val user = p0.getValue(Information::class.java)!!

                    navView.nav_farm_name.text = user.farmName
                    navView.nav_farm_email.text = user.email
                    if (user.photoURL.isNotEmpty()) {
                        Picasso.get()
                                .load(user.photoURL)
                                .error(R.drawable.man)
                                .into(navView.nav_icon_image)
                    } else {
                        Picasso.get()
                                .load(R.drawable.man)
                                .into(navView.nav_icon_image)
                    }

                }
            }
        })

        onClick()
    }

    private fun onClick() {
        navView.nav_profile_area.setOnClickListener {
            //initIntent(ContainerActivity()).put("TITLE", "โปรไฟล์").start()
            val intent = Intent(activity, TestAuthActivity::class.java)
            activity.startActivity(intent)
        }
        navView.nav_program_area.setOnClickListener {
            hideDrawer()
        }
        navView.nav_manager_area.setOnClickListener {
            val intent = Intent(activity, CustomPlanActivity::class.java)
            intent.putExtra("TYPE", "0")
            activity.startActivity(intent)
            hideDrawer()
        }
        navView.nav_injection_area.setOnClickListener {
            val intent = Intent(activity, KnowledgeActivity::class.java)
            intent.putExtra("TITLE", "ความรู้เพิ่มเติม")
            intent.putExtra("ID", "0")
            activity.startActivity(intent)
            hideDrawer()
        }
        navView.nav_settings_area.setOnClickListener {
            initIntent(SettingActivity()).put("TITLE", "ตั้งค่า").start()
            activity.finish()
        }
        navView.nav_about_area.setOnClickListener {
            initIntent(ContainerActivity()).put("TITLE", "ติดต่อเรา").start()
        }
        navView.nav_sign_out_area.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, option)
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(activity,
                OnCompleteListener<Void> {
                    Toast.makeText(activity, "ลงชื่อออกเรียบร้อย", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                })

    }

    private fun Intent.put(id: String, str: String): Intent {
        this.putExtra(id, str)
        return this
    }

    private fun Intent.start() {
        activity.startActivity(this)
        hideDrawer()
    }

    private fun initIntent(path: AppCompatActivity): Intent {
        return Intent(activity, path::class.java)
    }

    private fun hideDrawer() {
        activity.drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun showDrawer() {
        activity.drawer_layout.openDrawer(GravityCompat.START)
    }

}