package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import th.ac.up.agr.thai_mini_chicken.Data.KnowledgeData
import th.ac.up.agr.thai_mini_chicken.KnowledgeActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.KnowledgeViewHolder

class KnowledgeAdapter(val activity: KnowledgeActivity, val ID: String, val data: ArrayList<KnowledgeData>) : RecyclerView.Adapter<KnowledgeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeViewHolder = KnowledgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.knowledge_card, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: KnowledgeViewHolder, position: Int) = holder.bind(data[position])
}