package th.ac.up.agr.thai_mini_chicken.Logic.Dialog

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class ActionDialog(val mFragmentActivity: FragmentActivity) : QuickCircleDialog(mFragmentActivity) {


    override fun build(): QuickCircleDialog {


        super.mDialog.setPositive(mFragmentActivity.getString(POSITIVE)) {
            super.positiveAction()
        }.setTitle(mFragmentActivity.getString(TITLE)).configTitle { params ->
            params!!.textSize = 60
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, TITLE_TEXT_COLOR)
        }.setText(mFragmentActivity.getString(MESSAGE)).configText { params ->
            params!!.textSize = 50
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, MESSSAGE_TEXT_COLOR)
            params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
        }.configPositive { params ->
            params.textSize = 50
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, POSITIVE_TEXT_COLOR)
        }.setNegative(mFragmentActivity.getString(NEGATIVE)) {
            negativeAction()
        }.configNegative { params ->
            params.textSize = 50
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, NEGATIVE_TEXT_COLOR)
        }

        return this
    }

}