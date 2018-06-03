package th.ac.up.agr.thai_mini_chicken.Adapter

import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.Data.ArraySlot
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class PageNotificationAdapter(val activity: FragmentActivity,val data :ArrayList<CardSlot>) :RecyclerView.Adapter<CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_card,parent,false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val slot = data[position].event
        val card = data[position].dataCard
        val day = data[position].day

        //Log.e("NO",day)

        if(day.indexOf("-slot") > -1){
            holder.apply {
                title_item.visibility = View.GONE
                card_item.visibility = View.VISIBLE
                message_area.visibility = View.VISIBLE
                info_area.visibility = View.GONE
                holder.histort_area.visibility = View.GONE

                //card_des.text = "อายุ ${data[position].week} สัปดาห์ ${data[position].day} วัน"

                if(card.cardName.isEmpty()){
                    card_des.text = "ชื่อรายการ"
                } else {
                    card_des.text = card.cardName
                }

                message_text.text = slot.message
                card_title.text = slot.title

                setIcon(slot.title,holder.icon_image,icon_area,card_title)

                //card_title.setTextColor(color)
                //card_item.setCardBackgroundColor(color)
                //icon_image.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_parasite_icon))

            }
        }else {
            holder.apply {
                title_item.visibility = View.VISIBLE
                card_item.visibility = View.GONE
                message_area.visibility = View.GONE
                info_area.visibility = View.GONE
                holder.histort_area.visibility = View.GONE

                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR,day.toInt())

                if(day.toInt() == 0){
                    holder.title_item.text = "วันนี้"
                } else if(day.toInt() == 1){
                    holder.title_item.text = "พรุ่งนี้"
                }else {
                    holder.title_item.text = "${calendar.get(Calendar.DAY_OF_MONTH).toString()} ${ConvertCard().getMonth((calendar.get(Calendar.MONTH) + 1).toString())} ${calendar.get(Calendar.YEAR) + 543}"

                }




            }
        }


    }

    private fun setIcon(objective: String, imageView: ImageView, area: CardView, textView: TextView) {
        //area.setCardBackgroundColor(ContextCompat.getColor(activity, MelonTheme.from(activity).getColor()))
        //textView.setTextColor(ContextCompat.getColor(activity, MelonTheme.from(activity).getColor()))
        when (objective) {
            "ฉีดวัคซีน" -> {
                Picasso.get().load(R.drawable.ic_inject_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightBlue))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorLightBlue))
            }
            "ถ่ายพยาธิ" -> {
                Picasso.get().load(R.drawable.ic_parasite_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorLightGreen))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorLightGreen))
            }
            "ตัดปาก" -> {
                Picasso.get().load(R.drawable.ic_cut_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorRed))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorRed))
            }
            "ให้แสงสว่าง" -> {
                Picasso.get().load(R.drawable.ic_sun_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorAmber))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorAmber))
            }
            else -> {
                Picasso.get().load(R.drawable.ic_chicken_icon).into(imageView)
                area.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorDeepPurple))
                textView.setTextColor(ContextCompat.getColor(activity, R.color.colorDeepPurple))
            }
        }
    }
}