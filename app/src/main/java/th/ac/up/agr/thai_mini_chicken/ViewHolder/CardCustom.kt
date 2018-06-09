package th.ac.up.agr.thai_mini_chicken.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_card.view.*

class CardCustom(itemView :View) :RecyclerView.ViewHolder(itemView){

    var text :TextView = itemView.custom_card_text
    var area :LinearLayout = itemView.custom_card_area
    var indicator :LinearLayout = itemView.custom_card_indicator

}