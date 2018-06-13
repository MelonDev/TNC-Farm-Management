package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.ContainerActivity
import th.ac.up.agr.thai_mini_chicken.Data.KnowledgeData
import th.ac.up.agr.thai_mini_chicken.KnowledgeActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.KnowledgeViewHolder

class KnowledgeAdapter(val activity: KnowledgeActivity,val ID :String, val data: ArrayList<KnowledgeData>) : RecyclerView.Adapter<KnowledgeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.knowledge_card, parent, false)
        return KnowledgeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: KnowledgeViewHolder, position: Int) {

        val slot = data[position]

        holder.textView.text = slot.name
        Picasso.get().load(slot.image.toInt()).into(holder.imageView)

        holder.area.setOnClickListener {
            if(ID.contentEquals("0") && data[position].name.contentEquals("วิธีการทำน้ำหมักเอนไซน์แทนการใช้สารเคมี")){
                val intent = Intent(activity, KnowledgeActivity::class.java)
                intent.putExtra("TITLE",data[position].name)
                intent.putExtra("ID","1")
                activity.startActivity(intent)
            }else {
                initIntent(ContainerActivity()).put("TITLE", slot.name).start()
            }
            //initIntent(ContainerActivity()).put("TITLE", slot.name).start()

        }

    }

    private fun Intent.put(id: String, str: String): Intent {
        this.putExtra(id, str)
        return this
    }

    private fun Intent.start() {
        activity.startActivity(this)
    }

    private fun initIntent(path: AppCompatActivity): Intent {
        return Intent(activity, path::class.java)
    }
}