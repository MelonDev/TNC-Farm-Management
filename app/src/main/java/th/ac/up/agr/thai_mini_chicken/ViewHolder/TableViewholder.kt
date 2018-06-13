package th.ac.up.agr.thai_mini_chicken.ViewHolder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.table_card.view.*

class TableViewholder(itemView:View) :RecyclerView.ViewHolder(itemView){
    val title :TextView = itemView.table_card_title_text
    val manual :TextView = itemView.table_card_how_text
    val type :TextView = itemView.table_card_type_text
    val indicator :ImageView = itemView.table_card_indicator

    val secoud :TextView = itemView.table_card_second_title
    val area :LinearLayout = itemView.table_card_third_area
}