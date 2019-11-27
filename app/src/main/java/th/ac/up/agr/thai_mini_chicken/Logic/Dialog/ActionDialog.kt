package th.ac.up.agr.thai_mini_chicken.Logic.Dialog

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import th.ac.up.agr.thai_mini_chicken.R

class ActionDialog(val mFragmentActivity: FragmentActivity) : QuickCircleDialog(mFragmentActivity) {


    override fun build(): QuickCircleDialog {

        val resource = mFragmentActivity.resources

        super.mDialog.setPositive(mFragmentActivity.getString(POSITIVE)) {
            super.positiveAction()
        }.setTitle(mFragmentActivity.getString(TITLE)).configTitle { params ->
            params?.let {
                it.textSize = resource.getInteger(R.integer.dialog_message_text)
                it.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, TITLE_TEXT_COLOR)
            }
        }.setText(mFragmentActivity.getString(MESSAGE)).configText { params ->
            params?.let {
                it.textSize = resource.getInteger(R.integer.dialog_positive_text_button)
                it.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, MESSSAGE_TEXT_COLOR)
                it.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
            }
        }.configPositive { params ->
            params?.let {
                it.textSize = resource.getInteger(R.integer.dialog_positive_text_button)
                it.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, POSITIVE_TEXT_COLOR)
            }

        }.setNegative(mFragmentActivity.getString(NEGATIVE)) {
            negativeAction()
        }.configNegative { params ->
            params?.let {
                it.textSize = resource.getInteger(R.integer.dialog_positive_text_button)
                it.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, NEGATIVE_TEXT_COLOR)
            }

        }

        return this
    }

}