package th.ac.up.agr.thai_mini_chicken.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.ac.up.agr.thai_mini_chicken.Data.TableData
import th.ac.up.agr.thai_mini_chicken.Fragment.KnowledgeTableFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.TableViewholder

class TableAdapter(val fragment :KnowledgeTableFragment,val ID :Int,val data :ArrayList<TableData>) :RecyclerView.Adapter<TableViewholder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.table_card,parent,false)
        return TableViewholder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TableViewholder, position: Int) {

        val slot = data[position]

        if(ID == 0){
            holder.apply {
                this.title.text = "อายุไก่ ${slot.title_card}"
                this.type.text = slot.type_card
                this.manual.text = slot.manual_card
                this.area.visibility = View.VISIBLE

            }

            if(position == data.lastIndex){
                holder.indicator.visibility = View.GONE
            }else {
                holder.indicator.visibility = View.VISIBLE

            }
        } else if(ID == 1){
            holder.apply {
                this.title.text = slot.title_card
                this.area.visibility = View.GONE
                this.indicator.visibility = View.GONE
                this.secoud.text = "วัตถุประสงค์"
                this.type.text = slot.type_card

            }
        }

    }
}