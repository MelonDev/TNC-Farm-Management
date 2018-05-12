package th.ac.up.agr.thai_mini_chicken.AddProgramActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

import kotlinx.android.synthetic.main.activity_add_program.*
import th.ac.up.agr.thai_mini_chicken.R

class AddProgramActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_program)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        add_program_back_btn.setOnClickListener { finish() }



    }

}
