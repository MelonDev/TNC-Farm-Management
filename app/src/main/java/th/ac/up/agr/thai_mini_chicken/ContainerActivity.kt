package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_container.*
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val bundle = intent.extras
        val title = bundle.getString("TITLE")


        container_title_name.text = title
        container_back_btn.setOnClickListener {
            val intent = Intent(this,ProgramMainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    override fun onBackPressed() {
        val intent = Intent(this,ProgramMainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
