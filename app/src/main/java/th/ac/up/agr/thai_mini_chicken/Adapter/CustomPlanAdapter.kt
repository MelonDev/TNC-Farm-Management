package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import th.ac.up.agr.thai_mini_chicken.CustomPlanActivity
import th.ac.up.agr.thai_mini_chicken.Data.CustomData
import th.ac.up.agr.thai_mini_chicken.DetailNotificationActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardCustom

class CustomPlanAdapter(val activity: CustomPlanActivity, val data: ArrayList<CustomData>) : RecyclerView.Adapter<CardCustom>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCustom {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return CardCustom(view)
    }

    override fun onBindViewHolder(holder: CardCustom, position: Int) {
        val slot = data[position]

        holder.text.text = slot.name



        holder.area.setOnClickListener {

            Log.e("NAME",slot.name)
            Log.e("KEY",slot.key)

            var intent = Intent(activity, DetailNotificationActivity::class.java)
            intent.putExtra("USER_ID", FirebaseAuth.getInstance().currentUser!!.uid)
            intent.putExtra("CARD_KEY", slot.key)
            intent.putExtra("TYPE", "2")
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}