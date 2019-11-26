package th.ac.up.agr.thai_mini_chicken.Logic.Dialog

import androidx.fragment.app.FragmentActivity
import com.mylhyl.circledialog.params.ProgressParams
import th.ac.up.agr.thai_mini_chicken.R

class QuickProgressDialog(val mFragmentActivity: FragmentActivity) : QuickCircleDialog(mFragmentActivity) {

    override fun build(): QuickCircleDialog {

        super.mDialog.setProgressText(mFragmentActivity.getString(super.WAIT)).setProgressStyle(ProgressParams.STYLE_SPINNER)

        return this

    }
}