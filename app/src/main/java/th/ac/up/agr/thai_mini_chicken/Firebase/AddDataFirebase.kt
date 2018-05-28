package th.ac.up.agr.thai_mini_chicken.Firebase

import android.support.v4.app.FragmentActivity
import com.google.firebase.database.DatabaseReference
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardDate
import th.ac.up.agr.thai_mini_chicken.Tools.Date

class AddDataFirebase(activity: FragmentActivity) {

    private var firebase :DatabaseReference = Firebase.reference

    private val userRef :DatabaseReference
    private val container :DatabaseReference

    init {
        userRef = firebase.child("ผู้ใช้").child("melondev_icloud_com")
        container = userRef.child("รายการ")
    }

    companion object {
        fun from(activity: FragmentActivity) = AddDataFirebase(activity)
    }

    fun setDataToActive(dataCard :CardData){
        val d = CardDate()
        d.dateTime = Date().getDateNull()
        d.IDCard = Date().getDateFakeID()
        val refs = container.child("ใช้งาน").child(d.IDCard)
        refs.setValue(d)

        val ref = container.child("ใช้งาน").child(dataCard.cardID)
        ref.child("รายละเอียด").setValue(dataCard)
    }

    fun setDataToInactive(dataCard :CardData){
        val ref = container.push().child("ข้อมูล").child("ไม่ได้ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

    fun setData(key :String,dataCard :CardData){
        //var ref = container.child(key)
        //var ref = container.push()
        val ref = container.push().child("ข้อมูล").child("ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

    fun testGet(){

    }

}