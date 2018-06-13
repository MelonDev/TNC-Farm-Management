package th.ac.up.agr.thai_mini_chicken.Adapter

import android.content.Intent
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigItems
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.*
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.AddNotiCardActivity
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Fragment.NewNotificationFragment
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.ConvertCard
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardViewHolder
import java.util.*

class NewNotificationAdapter (val fragment: NewNotificationFragment,val data :ArrayList<CardSlot>) : RecyclerView.Adapter<CardViewHolder>(){

    private var userID = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var waitDialog: DialogFragment
    private val activity = fragment.activity!!

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


        if(day.indexOf("-slot") > -1){
            holder.apply {
                title_item.visibility = View.GONE
                card_item.visibility = View.VISIBLE
                message_area.visibility = View.VISIBLE
                info_area.visibility = View.GONE
                holder.histort_area.visibility = View.GONE

                if(card.cardName.isEmpty()){
                    card_des.text = "ชื่อรายการ"
                } else {
                    card_des.text = card.cardName
                }

                message_text.text = slot.message
                card_title.text = slot.title

                setIcon(slot.title,holder.icon_image,icon_area,card_title)
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

        holder.card_more.setOnClickListener {
            showDialog(arrayOf("แก้ไข", "ทำแล้ว", "ไม่ทำ", "ลบ"), userID, position)
        }


    }

    fun showConDialog() {
        waitDialog.dismiss()
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("ลบเรียบร้อย")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()


    }

    fun showMoveDialog(action: Int) {
        waitDialog.dismiss()
        var title = ""
        if (action == 0) {
            title = "เก็บไปที่ประวัติเรียบร้อย"
        } else if (action == 1) {
            title = "กู้คืนเรียบร้อย"
        }
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(title)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()


    }

    fun showDeleteDialog(ref: DatabaseReference) {
        val arr = arrayOf("ยืนยันการลบ")

        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                        params.canceledOnTouchOutside = false
                    }
                })
                .setTitle("คุณต้องการลบรายการนี้?")
                .setTitleColor(ContextCompat.getColor(activity, R.color.colorText))
                .setItems(arr) { parent, view, position, id ->
                    setWaitDialog("กำลังลบ...")
                    ref.removeValue { p3, _ ->
                        if (p3 != null) {
                            showErrorDialog()
                        } else {
                            showConDialog()
                        }
                    }
                    //ToolReference().checkPosition(cardKey, ref)
                    //this.notifyItemRemoved(positions)
                    //showConDialog()
                }
                .configItems(object : ConfigItems() {
                    override fun onConfig(params: ItemsParams?) {
                        params!!.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                        params.backgroundColorPress = ContextCompat.getColor(activity.applicationContext, R.color.colorRed)
                    }
                })
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity.applicationContext, R.color.colorText)
                    }
                })
                .show()
    }

    fun showDialog(arr: Array<String>, userID: String, pos: Int) {
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                //.setTitle(title)
                //.setTitleColor(ContextCompat.getColor(fragment, R.color.colorPrimary))
                //.setSubTitle(sub)
                .setItems(arr) { parent, view, position, id ->
                    var ref = Firebase.reference
                    ref = Firebase.reference.child("ผู้ใช้").child(userID).child("รายการ").child("ใช้งาน").child(data[pos].event.fromID).child("รายการที่ต้องทำ").child(data[pos].event.cardID).child("status")
                    //status = data[pos].status
                    when (position) {
                        0 -> {
                            goToEdit(data[pos].event.cardID,pos)
                        }
                        1 -> {
                            setWaitDialog("กำลังบันทึก...")
                            ref.setValue("PASSED") { p0, _ ->
                                if (p0 != null) {
                                    showErrorDialog()
                                } else {
                                    showMoveDialog(0)
                                }
                            }
                        }
                        2 -> {
                            setWaitDialog("กำลังบันทึก...")
                            ref.setValue("CANCEL") { p0, _ ->
                                if (p0 != null) {
                                    showErrorDialog()
                                } else {
                                    showMoveDialog(0)
                                }
                            }
                        }
                        3 -> {
                            showDeleteDialog(ref)
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative(
                        object : ConfigButton() {
                            override fun onConfig(params: ButtonParams) {
                                params.textSize = 50
                                params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                            }
                        })
                .show()
    }

    fun goToEdit(notiKey :String,position: Int){
        val intent = Intent(activity, AddNotiCardActivity::class.java)
        intent.putExtra("ID","1")
        intent.putExtra("USER_ID", userID)
        intent.putExtra("CARD_KEY", data[position].event.fromID)
        intent.putExtra("NOTI_KEY",notiKey)
        activity.startActivity(intent)
    }

    fun setWaitDialog(str: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText(str)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)
    }

    fun showErrorDialog() {
        waitDialog.dismiss()
        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("เกิดข้อผิดพลาด")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    //fragment.onLoad()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                })

                .show()
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