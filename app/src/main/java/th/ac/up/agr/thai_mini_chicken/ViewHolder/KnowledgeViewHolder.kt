package th.ac.up.agr.thai_mini_chicken.ViewHolder


import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.knowledge_card.view.*
import th.ac.up.agr.thai_mini_chicken.ContainerActivity
import th.ac.up.agr.thai_mini_chicken.Data.KnowledgeData
import th.ac.up.agr.thai_mini_chicken.KnowledgeActivity

class KnowledgeViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    private val textView: TextView = itemView.knowledge_card_text
    private val area: CardView = itemView.knowledge_card_item
    private val imageView: ImageView = itemView.knowledge_card_image

    fun bind(data: KnowledgeData) {

        textView.text = data.name
        Picasso.get().load(data.image).into(imageView)

        area.setOnClickListener {
            if (/*ID.contentEquals("0") && */data.name.contentEquals("วิธีการทำน้ำหมักเอนไซน์แทนการใช้สารเคมี")) {
                val intent = Intent(itemView.context, KnowledgeActivity::class.java)
                intent.putExtra("TITLE", data.name)
                intent.putExtra("ID", "1")
                itemView.context.startActivity(intent)
            } else {
                val intent = Intent(itemView.context, ContainerActivity::class.java)
                intent.putExtra("TITLE", data.name)
                itemView.context.startActivity(intent)
            }

        }
    }

}