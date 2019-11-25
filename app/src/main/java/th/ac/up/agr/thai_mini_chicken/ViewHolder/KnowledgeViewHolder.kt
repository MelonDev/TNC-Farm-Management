package th.ac.up.agr.thai_mini_chicken.ViewHolder


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.knowledge_card.view.*

class KnowledgeViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    val textView :TextView = itemView.knowledge_card_text
    val area: CardView = itemView.knowledge_card_item
    val imageView :ImageView = itemView.knowledge_card_image

}