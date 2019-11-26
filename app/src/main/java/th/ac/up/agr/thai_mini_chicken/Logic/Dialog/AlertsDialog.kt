package th.ac.up.agr.thai_mini_chicken.Logic.Dialog

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import th.ac.up.agr.thai_mini_chicken.R

class AlertsDialog(val mFragmentActivity: FragmentActivity) : QuickCircleDialog(mFragmentActivity) {

    override fun build(): QuickCircleDialog {

        super.POSITIVE_TEXT_COLOR = R.color.colorText

        super.mDialog.setPositive(mFragmentActivity.getString(POSITIVE)) {
            super.positiveAction()
        }.configPositive { params ->
            params.textSize = 50
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, POSITIVE_TEXT_COLOR)
        }

        return this
    }

}