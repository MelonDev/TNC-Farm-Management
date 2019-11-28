package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {

    var email: String = "saengwong1111@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        help_email_area.visibility = View.GONE

        help_back_btn.setOnClickListener {
            finish()
        }

        help_confirm_btn.setOnClickListener {

            val message: String = help_message_edittext.text.toString()
            sendEmail(email, message)


        }


    }

    private fun sendEmail(email: String, message: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            this.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            this.putExtra(Intent.EXTRA_SUBJECT, "ติดต่อสอบถาม จาก TNC Farm Manager")
            this.putExtra(Intent.EXTRA_TEXT, message)

            this.type = "text/email"


        }

        startActivity(Intent.createChooser(intent, "เลือกแอปอีเมลที่ต้องการส่ง :"))
        finish()

    }

}
