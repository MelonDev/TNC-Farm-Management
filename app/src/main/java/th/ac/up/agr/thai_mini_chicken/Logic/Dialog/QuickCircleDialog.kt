package th.ac.up.agr.thai_mini_chicken.Logic.Dialog


import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mylhyl.circledialog.CircleDialog
import th.ac.up.agr.thai_mini_chicken.R

open class QuickCircleDialog(private val mFragmentActivity: FragmentActivity) {

    var mDialog: CircleDialog.Builder

    var NEGATIVE_TEXT_COLOR: Int = R.color.colorText
    var POSITIVE_TEXT_COLOR: Int = R.color.colorPrimary
    var TITLE_TEXT_COLOR: Int = R.color.colorPrimary
    var MESSSAGE_TEXT_COLOR: Int = R.color.colorText

    var MESSAGE: Int = R.string.dialog_message_text_default
    var TITLE: Int = R.string.dialog_title_text_default
    var NEGATIVE: Int = R.string.dialog_negative_text_default
    var POSITIVE: Int = R.string.dialog_positive_text_default


    var positiveAction: () -> Unit = {}
    var negativeAction: (() -> Unit) = {}


    fun positive(lambda: () -> Unit): QuickCircleDialog {
        positiveAction = lambda
        return this
    }

    fun negative(lambda: () -> Unit): QuickCircleDialog {
        negativeAction = lambda
        return this
    }


    fun item(lambda: () -> Unit): QuickCircleDialog {
        return this
    }


    init {
        mDialog = CircleDialog.Builder().configDialog { params ->
            params.canceledOnTouchOutside = false
        }.setTitle(mFragmentActivity.getString(TITLE)).configTitle { params ->
            params!!.textSize = 60
            params.textColor = ContextCompat.getColor(mFragmentActivity.applicationContext, TITLE_TEXT_COLOR)
        }
    }

    open fun build(): QuickCircleDialog {
        return this
    }

    fun show() {
        mDialog.show(mFragmentActivity.supportFragmentManager)
    }

}