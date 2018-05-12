package th.ac.up.agr.thai_mini_chicken.Tools

import android.view.View
import android.widget.RelativeLayout
import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder

class Animation {

    companion object {
        val use = Animation()
    }

    fun animation(view: View) {
        AdditiveAnimator.animate(view)
                .setDuration(0)
                .alpha(0f)
                .then()
                .setDuration(400)
                .alpha(1f)
                .start()
    }

    fun itemLoadAnimation(view :RelativeLayout){
        AdditiveAnimator.animate(view)
                .setDuration(0)
                .alpha(0f)
                .then()
                .setDuration(400)
                .alpha(1f)
                .start()
    }
    fun itemHideAnimation(view :RelativeLayout){
        AdditiveAnimator.animate(view)
                .setDuration(0)
                .alpha(1f)
                .then()
                .setDuration(100)
                .alpha(0f)
                .start()
    }

    fun cardShow(holder: CardViewHolder){
        AdditiveAnimator.animate(holder.menu_dialog)
                .setDuration(0)
                .alpha(0f)
                .then()
                .setDuration(400)
                .addStartAction { cardShowAni(holder) }
                .alpha(1f)
                .start()

    }

    fun cardHide(holder: CardViewHolder){
        AdditiveAnimator.animate(holder.menu_dialog)
                .setDuration(0)
                .alpha(1f)
                .then()
                .setDuration(200)
                .alpha(0f)
                .addStartAction { cardHideAni(holder) }
                .then()
                .addStartAction {
                    holder.menu_dialog.visibility = View.GONE
                }
                .start()

    }

    private fun cardShowAni(holder: CardViewHolder){
        AdditiveAnimator.animate(holder.card_in_dialog)
                .setDuration(0)
                .scale(0f)
                .translationX(200f)
                .then()
                .setDuration(500)
                .scale(1f)
                .thenWithDelay(100)
                .setDuration(300)
                .translationX(0f)
                .start()
    }

    private fun cardHideAni(holder: CardViewHolder){
        AdditiveAnimator.animate(holder.card_in_dialog)
                .setDuration(0)
                .scale(1f)
                .then()
                .setDuration(200)
                .scale(0f)
                .start()
    }

}

