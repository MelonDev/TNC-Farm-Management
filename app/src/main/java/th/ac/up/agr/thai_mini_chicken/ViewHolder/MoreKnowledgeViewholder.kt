package th.ac.up.agr.thai_mini_chicken.ViewHolder


import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.table_card.view.*
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeData
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeType

class MoreKnowledgeViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.table_card_title_text
    val manual: TextView = itemView.table_card_how_text
    val type: TextView = itemView.table_card_type_text
    val indicator: ImageView = itemView.table_card_indicator

    val secoud: TextView = itemView.table_card_second_title
    val area: LinearLayout = itemView.table_card_third_area
    val image: ImageView = itemView.table_card_image


    fun bind(type: MoreKnowledgeType, data: MoreKnowledgeData, position: Int, arrayDataSize: Int) {

        when (type) {
            MoreKnowledgeType.PROGRAM -> {
                bindingProgram(data)
                bindingIndicator(position, arrayDataSize)
            }
            MoreKnowledgeType.HERB -> bindingHerb(data)
        }

    }

    private fun bindingProgram(data: MoreKnowledgeData) {

        title.text = "อายุไก่ ${data.title_card}"
        type.text = data.type_card
        manual.text = data.manual_card
        area.visibility = View.VISIBLE
        image.visibility = View.GONE


    }

    private fun bindingHerb(data: MoreKnowledgeData) {
        this.title.text = data.title_card
        this.area.visibility = View.GONE
        this.indicator.visibility = View.GONE
        this.secoud.text = "วัตถุประสงค์"
        this.image.visibility = View.VISIBLE
        this.type.text = data.type_card
        Picasso.get().load(data.image).into(this.image)
    }

    private fun bindingIndicator(position: Int, arrayDataSize: Int) {
        if (position == arrayDataSize) {
            indicator.visibility = View.GONE
        } else {
            indicator.visibility = View.VISIBLE
        }
    }

}