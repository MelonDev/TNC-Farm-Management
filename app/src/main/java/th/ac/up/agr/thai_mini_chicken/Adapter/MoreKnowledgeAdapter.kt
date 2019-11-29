package th.ac.up.agr.thai_mini_chicken.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeData
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeType
import th.ac.up.agr.thai_mini_chicken.Fragment.MoreKnowledgeFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.MoreKnowledgeViewholder

class MoreKnowledgeAdapter(
        val fragment: MoreKnowledgeFragment,
        val type: MoreKnowledgeType,
        val data: ArrayList<MoreKnowledgeData>
) : RecyclerView.Adapter<MoreKnowledgeViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreKnowledgeViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.table_card, parent, false)
        return MoreKnowledgeViewholder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MoreKnowledgeViewholder, position: Int) {
        holder.bind(type, data[position], position, data.lastIndex)
    }
}