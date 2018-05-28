package th.ac.up.agr.thai_mini_chicken.AddProgramActivity

import th.ac.up.agr.thai_mini_chicken.MainActivity
import android.support.design.widget.BottomSheetDialog
import th.ac.up.agr.thai_mini_chicken.R
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.View
import com.aigestudio.wheelpicker.WheelPicker
import com.aigestudio.wheelpicker.widgets.WheelDayPicker
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker
import com.aigestudio.wheelpicker.widgets.WheelYearPicker
import kotlinx.android.synthetic.main.input_dialog.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_add_program.view.*
import android.R.attr.textColor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.InputType
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.TextParams
import com.mylhyl.circledialog.params.TitleParams
import kotlinx.android.synthetic.main.activity_add_program.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.AddDataFirebase
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.SQLite.AppTheme
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme


class AddBottomDialog(private val activity: AddProgramActivity) : WheelPicker.OnItemSelectedListener {

    private val bottomSheetView: View = activity.layoutInflater.inflate(R.layout.input_dialog, null)
    private val bottomSheetDialog = BottomSheetDialog(activity)
    //private var bottomSheetBehavior = BottomSheetBehavior.from<CoordinatorLayout>(activity.input_bottom_sheet)

    private var yearWheel: WheelYearPicker
    private var monthWheel: WheelMonthPicker
    private var dayWheel: WheelDayPicker

    private lateinit var dataCard: CardData

    var amountMale: String = "0"
    var amountFemale: String = "0"

    var ageDay: String = "0"
    var ageWeek: String = "0"

    var monthS: String = "0"
    var yearS: String = "0"
    var dayS:String = "0"


    init {
        bottomSheetDialog.setContentView(bottomSheetView)

        if(activity.ID.contentEquals("0")){
            activity.dataCard.apply {
                cardName = ""
                cardID = ""
                amountMale = "0"
                amountFemale = "0"
                ageDay = "0"
                ageWeek = "0"
                userObjective = "0"
                systemFarm = "0"
                status = "ACTIVE"
                breed = ""
                notification = "true"
                notiBefore = "true"
                dateDay = Date().getDay()
                dateMonth = Date().getMonth()
                dateYear = Date().getYear()
            }
        }

        yearWheel = bottomSheetView.add_program_year_wheel
        monthWheel = bottomSheetView.add_program_month_wheel
        dayWheel = bottomSheetView.add_program_day_wheel

        yearWheel.yearEnd = 2100
        yearWheel.yearStart = 1900

        monthS = monthWheel.selectedMonth.toString()
        yearS = yearWheel.selectedYear.toString()

        val typeface = ResourcesCompat.getFont(activity, R.font.sukhumvit_light)

        //bottomSheetView.add_program_objective_wheel.typeface = typeface
        bottomSheetView.add_program_old_day_wheel.typeface = typeface
        bottomSheetView.add_program_old_week_wheel.typeface = typeface
        bottomSheetView.add_program_year_wheel_s.typeface = typeface
        bottomSheetView.add_program_month_wheel_s.typeface = typeface
        bottomSheetView.add_program_count_male_wheel.typeface = typeface
        bottomSheetView.add_program_count_female_wheel.typeface = typeface
        dayWheel.typeface = typeface

        //bottomSheetView.add_program_objective_wheel.setOnItemSelectedListener(this)
        bottomSheetView.add_program_count_male_wheel.setOnItemSelectedListener(this)
        bottomSheetView.add_program_count_female_wheel.setOnItemSelectedListener(this)
        bottomSheetView.add_program_old_day_wheel.setOnItemSelectedListener(this)
        bottomSheetView.add_program_old_week_wheel.setOnItemSelectedListener(this)
        bottomSheetView.add_program_month_wheel_s.setOnItemSelectedListener(this)
        bottomSheetView.add_program_year_wheel_s.setOnItemSelectedListener(this)

        var a = ArrayList<Int>()
        var i = 0

        while (a.size <= 999) {
            a.add(i)
            i += 1
        }

        var b = ArrayList<Int>()
        var j = 2530

        while (j <= 2590) {
            b.add(j)
            j += 1
        }

        bottomSheetDialog.setOnCancelListener {
            resetDate()
            resetAge()
            resetObjective()
            resetAmount()
        }

        bottomSheetDialog.setOnDismissListener {
            resetDate()
            resetAge()
            resetObjective()
            resetAmount()
        }

        bottomSheetView.add_program_count_male_wheel.data = a
        bottomSheetView.add_program_count_female_wheel.data = a

        bottomSheetView.add_program_old_day_wheel.data = a
        bottomSheetView.add_program_old_week_wheel.data = a

        //bottomSheetView.add_program_objective_wheel.data = ConvertCard().getObjective()

        bottomSheetView.add_program_month_wheel_s.data = ConvertCard().getArrMonth()
        bottomSheetView.add_program_year_wheel_s.data = b


        updateDay(monthWheel.currentMonth, yearWheel.currentYear)


        yearWheel.setOnItemSelectedListener { picker, data, position ->
            //updateDay(monthWheel.currentMonth,yearWheel.currentYear)
            updateDay(monthS.toInt(), yearWheel.currentYear)
        }

        monthWheel.setOnItemSelectedListener { picker, data, position ->
            updateDay(monthWheel.currentMonth, yearWheel.currentYear)
        }



        bottomSheetView.input_dialog_action_area.setOnClickListener {
            if (activity.position == 0) {
                resetDate()
            } else if (activity.position == 2) {
                resetAge()
            } else if (activity.position == 5) {
                resetAmount()
            }
        }

        bottomSheetView.input_dialog_cancal_btn.setOnClickListener {
            bottomSheetDialog.cancel()
        }

        bottomSheetView.input_dialog_confirm_btn.setOnClickListener {
            bottomSheetDialog.cancel()
            when (activity.position) {
                0 -> {
                    activity.dateTV.text = "${bottomSheetView.add_program_day_wheel.currentDay.toString()} ${ConvertCard().getArrMonth()[monthS.toInt() - 1]} ${yearS.toInt() + 543}"
                    //activity.monthS = monthS
                    //activity.yearS = yearS
                    //activity.dayS = bottomSheetView.add_program_day_wheel.currentDay.toString()
                    activity.dataCard.dateDay = bottomSheetView.add_program_day_wheel.currentDay.toString()
                    activity.dataCard.dateMonth = monthS
                    activity.dataCard.dateYear = yearS

                }
                2 -> {
                    var aw = ageWeek
                    var ad = ageDay
                    var x = (ad.toInt() / 7).toString()
                    var y = (ad.toInt() % 7).toString()
                    aw = (aw.toInt() + x.toInt()).toString()
                    activity.add_program_age_text.text = "${aw} สัปดาห์ ${y} วัน"
                    activity.dataCard.ageWeek = aw
                    activity.dataCard.ageDay = y
                }
                5 -> {
                    activity.add_program_amount_male_text.text = "เพศผู้ ${amountMale} ตัว"
                    activity.add_program_amount_female_text.text = "เพศเมีย ${amountFemale} ตัว"
                    activity.dataCard.amountMale = amountMale
                    activity.dataCard.amountFemale = amountFemale
                }
            }
        }

        activity.add_program_save_btn.setOnClickListener {
            saveData()
        }

        activity.add_program_notify_me.setOnCheckedChangeListener { view, isChecked ->
            activity.dataCard.notification = isChecked.toString()
        }

        activity.add_program_notify_before.setOnCheckedChangeListener { view, isChecked ->
            activity.dataCard.notiBefore = isChecked.toString()
        }


    }

    fun setOnStart() {
        activity.dateTV.text = "${dayWheel.selectedDay} ${ConvertCard().getArrMonth()[monthWheel.selectedMonth - 1]} ${yearWheel.selectedYear + 543}"
        activity.objectiveTV.text = ConvertCard().getObjective("0")
        activity.add_program_system_text.text = ConvertCard().getSystem("0")
    }

    fun setTitle(position: Int) {
        when (position) {
            0 -> {
                bottomSheetView.input_dialog_title.text = "เลือกวันที่รับเข้า"
                bottomSheetView.input_dialog_action_area.visibility = View.VISIBLE
                bottomSheetView.input_dialog_action_text.text = "วันนี้"
            }
            1 -> {
                bottomSheetView.input_dialog_title.text = "เลือกวัตถุประสงค์"
                bottomSheetView.input_dialog_action_area.visibility = View.GONE
            }
            2 -> {
                bottomSheetView.input_dialog_title.text = "เลือกอายุของไก่"
                bottomSheetView.input_dialog_action_area.visibility = View.VISIBLE
                bottomSheetView.input_dialog_action_text.text = "รีเซ็ต"

            }
            5 -> {
                bottomSheetView.input_dialog_title.text = "เลือกจำนวนไก่"
                bottomSheetView.input_dialog_action_area.visibility = View.VISIBLE
                bottomSheetView.input_dialog_action_text.text = "รีเซ็ต"
            }
        }
    }

    private fun resetObjective() {
        //bottomSheetView.add_program_objective_wheel.selectedItemPosition = 0
    }

    private fun resetDate() {
        updateDay(monthWheel.selectedMonth, yearWheel.selectedYear)
        monthWheel.selectedMonth = monthWheel.selectedMonth
        yearWheel.selectedYear = yearWheel.selectedYear
        dayWheel.selectedDay = dayWheel.selectedDay
        bottomSheetView.add_program_month_wheel_s.selectedItemPosition = monthWheel.selectedMonth - 1
        bottomSheetView.add_program_year_wheel_s.selectedItemPosition = yearWheel.selectedYear - 1987
        monthS = monthWheel.selectedMonth.toString()
        yearS = yearWheel.selectedYear.toString()
    }

    private fun resetAge() {
        bottomSheetView.add_program_old_week_wheel.selectedItemPosition = 0
        bottomSheetView.add_program_old_day_wheel.selectedItemPosition = 0

        ageDay = "0"
        ageWeek = "0"
    }

    private fun resetAmount() {
        bottomSheetView.add_program_count_male_wheel.selectedItemPosition = 0
        bottomSheetView.add_program_count_female_wheel.selectedItemPosition = 0
        amountMale = "0"
        amountFemale = "0"
    }

    private fun setData() {
        bottomSheetView.add_program_count_male_wheel.selectedItemPosition = dataCard.amountMale.toInt()
        bottomSheetView.add_program_count_female_wheel.selectedItemPosition = dataCard.amountFemale.toInt()
        bottomSheetView.add_program_old_day_wheel.selectedItemPosition = dataCard.ageDay.toInt()
        bottomSheetView.add_program_old_week_wheel.selectedItemPosition = dataCard.ageWeek.toInt()
        yearWheel.selectedItemPosition = dataCard.dateYear.toInt() - 1900
        monthWheel.selectedItemPosition = dataCard.dateMonth.toInt() - 1
        dayWheel.setYearAndMonth(dataCard.dateYear.toInt(), dataCard.dateMonth.toInt())
        dayWheel.selectedItemPosition = dataCard.dateDay.toInt() - 1

        bottomSheetView.add_program_month_wheel_s.selectedItemPosition = dataCard.dateMonth.toInt() - 1
        bottomSheetView.add_program_year_wheel_s.selectedItemPosition = dataCard.dateYear.toInt() - 1987

        amountMale = dataCard.amountMale
        amountFemale = dataCard.amountFemale
        ageDay = dataCard.ageDay
        ageWeek = dataCard.ageWeek

        monthS = dataCard.dateMonth
        yearS = dataCard.dateYear


    }

    companion object {
        fun load(activity: AddProgramActivity) = AddBottomDialog(activity)
    }

    fun action(): TakeAction {
        return TakeAction(bottomSheetDialog)
    }

    class TakeAction(private val bottomSheetDialog: BottomSheetDialog) {
        fun show() {
            bottomSheetDialog.show()
        }

        fun hide() {
            bottomSheetDialog.hide()
        }

        fun cancel() {
            bottomSheetDialog.cancel()
        }
    }

    override fun onItemSelected(picker: WheelPicker, data: Any, position: Int) {
        when (picker.id) {
            R.id.add_program_old_day_wheel -> ageDay = data.toString()
            R.id.add_program_old_week_wheel -> ageWeek = data.toString()
            R.id.add_program_count_male_wheel -> amountMale = data.toString()
            R.id.add_program_count_female_wheel -> amountFemale = data.toString()
            R.id.add_program_month_wheel_s -> {
                monthS = (position + 1).toString()
                updateDay(monthS.toInt(), yearS.toInt())
            }
            R.id.add_program_year_wheel_s -> {
                yearS = (data.toString().toInt() - 543).toString()
                //Log.e("YEAR",yearS)
                updateDay(monthS.toInt(), yearS.toInt())

            }
        }
    }

    private fun updateDay(month: Int, year: Int) {
        //Log.e("YEAR",month.toString() +"/"+year.toString())
        dayWheel.setYearAndMonth(year, month)
    }

    fun dateDialog(): TakeAction {
        hideAll()
        bottomSheetView.input_dialog_date_area.visibility = View.VISIBLE

        if (activity.dataCard.dateYear.isNotEmpty() && activity.dataCard.dateMonth.isNotEmpty() && activity.dataCard.dateDay.isNotEmpty()) {

            bottomSheetView.add_program_year_wheel_s.selectedItemPosition = activity.dataCard.dateYear.toInt() - 1987
            yearS = activity.dataCard.dateYear
            bottomSheetView.add_program_month_wheel_s.selectedItemPosition = activity.dataCard.dateMonth.toInt() - 1
            monthS = activity.dataCard.dateMonth
            bottomSheetView.add_program_day_wheel.selectedItemPosition = activity.dataCard.dateDay.toInt() - 1
            dayWheel.setYearAndMonth(activity.dataCard.dateYear.toInt(), activity.dataCard.dateMonth.toInt())
        } else {
            bottomSheetView.add_program_month_wheel_s.selectedItemPosition = monthWheel.selectedMonth - 1
            bottomSheetView.add_program_year_wheel_s.selectedItemPosition = yearWheel.selectedYear - 1987
        }

//        yearWheel.selectedItemPosition = activity.dataCard.dateYear.toInt() - 1900
        //      monthWheel.selectedItemPosition = activity.dataCard.dateMonth.toInt() - 1
        //    dayWheel.setYearAndMonth(activity.dataCard.dateYear.toInt(),activity.dataCard.dateMonth.toInt())
        //  dayWheel.selectedItemPosition = activity.dataCard.dateDay.toInt() - 1

        return TakeAction(bottomSheetDialog)
    }

    fun ageDialog(): TakeAction {
        hideAll()
        bottomSheetView.input_dialog_age_area.visibility = View.VISIBLE

        if (activity.dataCard.ageDay.isNotEmpty() && activity.dataCard.ageWeek.isNotEmpty()) {
            bottomSheetView.add_program_old_day_wheel.selectedItemPosition = activity.dataCard.ageDay.toInt()
            ageDay = activity.dataCard.ageDay
            bottomSheetView.add_program_old_week_wheel.selectedItemPosition = activity.dataCard.ageWeek.toInt()
            ageWeek = activity.dataCard.ageWeek
        } else {
            bottomSheetView.add_program_old_week_wheel.selectedItemPosition = 0
            bottomSheetView.add_program_old_day_wheel.selectedItemPosition = 0
        }

        return TakeAction(bottomSheetDialog)
    }

    fun amountDialog(): TakeAction {
        hideAll()
        bottomSheetView.input_dialog_amount_area.visibility = View.VISIBLE

        if (activity.dataCard.amountMale.isNotEmpty() && activity.dataCard.amountFemale.isNotEmpty()) {
            bottomSheetView.add_program_count_male_wheel.selectedItemPosition = activity.dataCard.amountMale.toInt()
            bottomSheetView.add_program_count_female_wheel.selectedItemPosition = activity.dataCard.amountFemale.toInt()
        } else {
            bottomSheetView.add_program_count_male_wheel.selectedItemPosition = 0
            bottomSheetView.add_program_count_female_wheel.selectedItemPosition = 0
        }


        return TakeAction(bottomSheetDialog)
    }

    fun objectiveDialog(): TakeAction {
        hideAll()
        bottomSheetView.input_dialog_objective_area.visibility = View.VISIBLE
        return TakeAction(bottomSheetDialog)
    }

    fun systemDialog() {
        val item = ConvertCard().getSystem()
        showDialog(1, "ระบบการเลี้ยง", arrayOf(item[0], item[1], item[2], item[3], item[4]))
    }

    fun newObjectiveDialog() {

        val item = ConvertCard().getObjective()
        showDialog(0, "วัตถุประสงค์การเลี้ยง", arrayOf(item[0], item[1], item[2], item[3], item[4]))

        //hideAll()
        //bottomSheetView.input_dialog_objective_area.visibility = View.VISIBLE
        //return TakeAction(bottomSheetDialog)
    }

    fun showDialog(ID: Int, title: String, arr: Array<String>) {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                .setTitle(title)
                .setTitleColor(ContextCompat.getColor(activity, MelonTheme.from(activity).getColor()))
                .setItems(arr) { parent, view, position, id ->
                    when (ID) {
                        0 -> {
                            activity.add_program_objective_text.text = ConvertCard().getObjective()[position]
                            activity.dataCard.userObjective = position.toString()
                        }
                        1 -> {
                            activity.add_program_system_text.text = ConvertCard().getSystem()[position]
                            activity.dataCard.systemFarm = position.toString()
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })
                .show()
    }

    fun showConDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("บันทึกเรียบร้อย")
                .configText(object :ConfigText(){
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0,0,0,0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    activity.finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()



    }



    fun onEdit(){
        activity.dateTV.text = "${activity.dataCard.dateDay} ${ConvertCard().getMonth(activity.dataCard.dateMonth)} ${ConvertCard().getYear(activity.dataCard.dateYear)}"
        activity.objectiveTV.text = ConvertCard().getObjective(activity.dataCard.userObjective)
        activity.add_program_system_text.text = ConvertCard().getSystem(activity.dataCard.systemFarm)
        activity.add_program_edittext.setText(activity.dataCard.cardName)
        activity.add_program_amount_male_text.text = "เพศผู้ ${activity.dataCard.amountMale} ตัว"
        activity.add_program_amount_female_text.text = "เพศเมีย ${activity.dataCard.amountFemale} ตัว"
        activity.add_program_notify_me.isChecked = activity.dataCard.notification.toBoolean()
        activity.add_program_notify_before.isChecked = activity.dataCard.notiBefore.toBoolean()
        activity.add_program_age_text.text = "${activity.dataCard.ageWeek} สัปดาห์ ${activity.dataCard.ageDay} วัน"

        if (activity.dataCard.breed.isNotEmpty()) {
            activity.add_program_breed_text.text = activity.dataCard.breed
        } else {
            activity.add_program_breed_text.text = "ไม่ระบุ"
        }
    }

    fun showEditDialog() {
        val dialog = AlertDialog.Builder(activity)


        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        dialog.setView(dialogView)
        //dialog.setTitle(Title);
        //dialog.setMessage(Message);
        val editText = dialogView.custom_dialog_edittext
        editText.requestFocus()

        if (activity.dataCard.breed.isNotEmpty()) {
                   editText.setText(activity.dataCard.breed)
        }

        editText.inputType = InputType.TYPE_CLASS_TEXT

        val abc = dialog.create()
        //abc.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        abc.show()

        dialogView.dialog_add_cancel.setOnClickListener {
            abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            activity.hideKeyB()
            editText.clearFocus()
            abc.cancel()
        }

        dialogView.dialog_add_confirm.setOnClickListener {
            val x = editText.text.toString()
            if (x.isNotEmpty()) {
                activity.add_program_breed_text.text = x
                activity.dataCard.breed = x
            } else {
                activity.add_program_breed_text.text = "ไม่ระบุ"
            }

            abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            activity.hideKeyB()
            abc.cancel()
        }
    }


    private fun hideAll() {
        bottomSheetView.input_dialog_age_area.visibility = View.GONE
        bottomSheetView.input_dialog_date_area.visibility = View.GONE
        bottomSheetView.input_dialog_objective_area.visibility = View.GONE
        bottomSheetView.input_dialog_amount_area.visibility = View.GONE
    }

    private fun saveData(){
        val cardData = activity.dataCard
        /*val cardData = CardData()
        cardData.apply {
            cardName = activity.add_program_edittext.text.toString()
            dateDay = bottomSheetView.add_program_day_wheel.currentDay.toString()
        }
*/

        val d = Date().getDate()

        if(activity.ID.contentEquals("0")){
            cardData.apply {
                cardID = d
                createDate = d
                lastUpdate = d
                status = "ACTIVE"
                cardName = activity.add_program_edittext.text.toString()
                showConDialog()
            }
        }else{
            cardData.apply {
                lastUpdate = d
                cardName = activity.add_program_edittext.text.toString()
                showConDialog()
            }
        }




        AddDataFirebase.from(activity).setDataToActive(cardData)

    }

}