package th.ac.up.agr.thai_mini_chicken.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.data_card.view.*

class CardNotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title_item : TextView = itemView.data_card_title_item
    val card_item : CardView = itemView.data_card_card_item
    val icon_area : CardView = itemView.data_card_card_icon_area
    val icon_image : ImageView = itemView.data_card_card_icon_image
    val card_title : TextView = itemView.data_card_title_text_view
    val card_des : TextView = itemView.data_card_title_description_text_view
    val card_more : CardView = itemView.data_card_card_more_area
    val info_date : TextView = itemView.data_card_card_info_date_text_view
    val info_age : TextView = itemView.data_card_card_info_age_text_view
    val info_objective : TextView = itemView.data_card_card_info_objective_text_view
    val info_area : LinearLayout = itemView.data_card_card_info_area
    val message_area : RelativeLayout = itemView.data_card_card_message_area
    val message_text : TextView = itemView.data_card_card_message_text_view

    val menu_dialog : RelativeLayout = itemView.data_card_card_menu_dialog
    var dialogOpen :Boolean = false
    val card_in_dialog : CardView = itemView.data_card_card_menu_dialog_card

    val layout : RelativeLayout = itemView.data_card_layout

}