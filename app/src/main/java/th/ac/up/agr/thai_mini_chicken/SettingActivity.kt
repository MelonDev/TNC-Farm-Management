package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams

import kotlinx.android.synthetic.main.activity_setting.*
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class SettingActivity : AppCompatActivity() {

    private lateinit var database: AppTheme
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private var material = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        checkColor(MelonTheme.from(this).getStyle())

        database = AppTheme(this)
        sqLiteDatabase = database.writableDatabase

        setting_back_btn.setOnClickListener {
            val intent = Intent(this, ProgramMainActivity::class.java)
            startActivity(intent)
            finish()
        }


        setting_theme_color_area.setOnClickListener {
            showDialog()
        }

        setting_theme_color_switch.setOnCheckedChangeListener { view, isChecked ->
            material = isChecked
            saveData(checkPosition(MelonTheme.from(this).getStyle()))
        }


    }

    fun showDialog() {
        CircleDialog.Builder()
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setTitle(getString(R.string.color_menu_title))
                .setTitleColor(ContextCompat.getColor(this, MelonTheme.from(this).getColor()))
                .setItems(arrayOf(R.string.color_menu_amber, R.string.color_menu_red, R.string.color_menu_light_green, R.string.color_menu_light_blue, R.string.color_munu_purple).map { getString(it) }.toTypedArray()) { parent, view, position, id ->
                    saveData(position)
                }
                .setNegative(getString(R.string.cancel_message_response), null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@SettingActivity, R.color.colorText)
                }
                .show(supportFragmentManager)
    }

    override fun onBackPressed() {
        val intent = Intent(this, ProgramMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkColor(style: Int) {
        val arr = arrayOf(R.string.color_menu_amber, R.string.color_menu_red, R.string.color_menu_light_green, R.string.color_menu_light_blue, R.string.color_munu_purple).map { getString(it) }.toTypedArray()
        when (style) {
            R.style.MelonTheme_Amber_Material -> {
                setting_theme_color_text.text = arr[0]
                setting_theme_color_switch.isChecked = true
            }
            R.style.MelonTheme_Red_Material -> {
                setting_theme_color_text.text = arr[1]
                setting_theme_color_switch.isChecked = true
            }
            R.style.MelonTheme_LightGreen_Material -> {
                setting_theme_color_text.text = arr[2]
                setting_theme_color_switch.isChecked = true
            }
            R.style.MelonTheme_LightBlue_Material -> {
                setting_theme_color_text.text = arr[3]
                setting_theme_color_switch.isChecked = true
            }
            R.style.MelonTheme_DeepPurple_Material -> {
                setting_theme_color_text.text = arr[4]
                setting_theme_color_switch.isChecked = true
            }
            R.style.MelonTheme_Amber_Flat -> {
                setting_theme_color_text.text = arr[0]
                setting_theme_color_switch.isChecked = false
            }
            R.style.MelonTheme_Red_Flat -> {
                setting_theme_color_text.text = arr[1]
                setting_theme_color_switch.isChecked = false
            }
            R.style.MelonTheme_LightGreen_Flat -> {
                setting_theme_color_text.text = arr[2]
                setting_theme_color_switch.isChecked = false
            }
            R.style.MelonTheme_LightBlue_Flat -> {
                setting_theme_color_text.text = arr[3]
                setting_theme_color_switch.isChecked = false
            }
            R.style.MelonTheme_DeepPurple_Flat -> {
                setting_theme_color_text.text = arr[4]
                setting_theme_color_switch.isChecked = false
            }
        }
        material = setting_theme_color_switch.isChecked
    }

    private fun saveColor(string: String) {
        setting_theme_color_text.text = string
    }

    private fun checkPosition(style: Int) :Int {
        return when (style) {
            R.style.MelonTheme_Amber_Material -> {
                0
            }
            R.style.MelonTheme_Red_Material -> {
                1
            }
            R.style.MelonTheme_LightGreen_Material -> {
                2
            }
            R.style.MelonTheme_LightBlue_Material -> {
                3
            }
            R.style.MelonTheme_DeepPurple_Material -> {
                4
            }
            R.style.MelonTheme_Amber_Flat -> {
                0
            }
            R.style.MelonTheme_Red_Flat -> {
                1
            }
            R.style.MelonTheme_LightGreen_Flat -> {
                2
            }
            R.style.MelonTheme_LightBlue_Flat -> {
                3
            }
            R.style.MelonTheme_DeepPurple_Flat -> {
                4
            }
            else -> {
                0
            }
        }
    }

    private fun saveData(position :Int) {
        val arr = arrayOf(R.string.color_menu_amber, R.string.color_menu_red, R.string.color_menu_light_green, R.string.color_menu_light_blue, R.string.color_munu_purple).map { getString(it) }.toTypedArray()
        saveColor(arr[position])
        if (material) {
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
        } else {
            when (position) {
                0 -> {
                    database.update(sqLiteDatabase, R.style.MelonTheme_Amber_Flat)
                }
                1 -> {
                    database.update(sqLiteDatabase, R.style.MelonTheme_Red_Flat)
                }
                2 -> {
                    database.update(sqLiteDatabase, R.style.MelonTheme_LightGreen_Flat)
                }
                3 -> {
                    database.update(sqLiteDatabase, R.style.MelonTheme_LightBlue_Flat)
                }
                4 -> {
                    database.update(sqLiteDatabase, R.style.MelonTheme_DeepPurple_Flat)
                }
            }
        }
        startActivity(Intent(this,SettingActivity::class.java))
        finish()
    }

}
