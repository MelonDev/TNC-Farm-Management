package th.ac.up.agr.thai_mini_chicken.Firebase


import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mylhyl.circledialog.CircleDialog

import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.LoadTime
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import kotlin.collections.HashMap
import com.mylhyl.circledialog.params.ProgressParams
import th.ac.up.agr.thai_mini_chicken.Data.Event


class AddDataFirebase(val activity: FragmentActivity) {

    private var firebase: DatabaseReference = Firebase.reference

    private val userRef: DatabaseReference
    private val container: DatabaseReference

    lateinit var waitDialog: DialogFragment
    lateinit var conDialog: CircleDialog.Builder
    lateinit var mainDialog: CircleDialog.Builder
    lateinit var errorDialog: CircleDialog.Builder

    init {
        userRef = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid)
        container = userRef.child("รายการ")

    }

    companion object {
        fun from(activity: FragmentActivity) = AddDataFirebase(activity)
    }

    fun setDataToActive(dataCard: CardData, objective: String, result: String, change: Boolean) {

        val database = FirebaseDatabase.getInstance().reference

        val man = container.child("ใช้งาน").child(dataCard.cardID)

        val b = man.child("รายการที่ต้องทำ")
        val c = man.child("รายละเอียด").child("userObjective")
        val ref = man.child("รายละเอียด")

        b.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                setWaitDialog()
                if (p0.value == null) {


                    setData(objective, result, dataCard, man)

                } else {

                    Log.e("CHANGE",change.toString())
                    if (change) {
                        b.removeValue()
                        setData(objective, result, dataCard, man)


                    } else {
                        val childMap = HashMap<String, Any>()
                        childMap.put("/รายละเอียด/", dataCard)
                        man.updateChildren(childMap) { p0, _ ->
                            if (p0 != null) {
                                waitDialog.dismiss()
                                setErrorDialog()
                                //showErrorDialog()
                            } else {
                                waitDialog.dismiss()
                                setConDialog()
                                //showConDialog()
                            }
                        }
                    }

                }
            }
        })

    }

    fun setData(objective: String, result: String, dataCard: CardData, man: DatabaseReference) {
        val childMap = HashMap<String, Any>()

        if(objective.contentEquals("1") && result.contentEquals("ว่างเปล่า")){
            dataCard.managerName = result
            dataCard.managerObjective = objective
            childMap.put("/รายละเอียด/", dataCard)

            man.updateChildren(childMap) { p0, _ ->
                if (p0 != null) {
                    waitDialog.dismiss()
                    setErrorDialog()
                } else {
                    waitDialog.dismiss()
                    setConDialog()
                }
            }
        } else if (objective.contentEquals("1")) {
            val arr = LoadTime().getTable(result)
            Log.e("COUNT","${arr.size}")
            for (i in arr) {

                val key: String = man.push().key.toString()

                val pathA = "/รายการที่ต้องทำ/$key"

                i.fromID = dataCard.cardID
                i.cardID = key


                if (i.status.isEmpty()) {
                    i.status = "ACTIVE"
                }


                childMap.put(pathA, i)

            }

            //val wer = AddSlot(null,dataCard)
            dataCard.managerName = result
            dataCard.managerObjective = objective
            childMap.put("/รายละเอียด/", dataCard)

            man.updateChildren(childMap) { p0, _ ->
                if (p0 != null) {
                    waitDialog.dismiss()
                    setErrorDialog()
                    //showErrorDialog()
                } else {
                    waitDialog.dismiss()
                    setConDialog()
                    //showConDialog()
                }
            }
        } else if (objective.contentEquals("2")) {
            var firebase = Firebase.reference
            var database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รูปแบบ")
            val abs = database.child(result).child("รายการที่ต้องทำ")
            val refA = database

            dataCard.managerName = result
            dataCard.managerObjective = objective
            childMap.put("/รายละเอียด/", dataCard)

            val pathA = "/รายละเอียด/"

            abs.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {

                        var count = 0
                        for (it in p0.children) {
                            val keys: String = refA.push().key.toString()
                            val pathB = "/รายการที่ต้องทำ/$keys/"

                            var slot = it.getValue(Event::class.java)!!

                            slot.apply {
                                this.status = "ACTIVE"
                                this.fromID = result
                                this.cardID = keys
                            }

                            childMap.put(pathB, slot)

                            count += 1
                            if (count == p0.children.count()) {

                                man.updateChildren(childMap) { p0, _ ->
                                    if (p0 != null) {
                                        waitDialog.dismiss()
                                        setErrorDialog()
                                    } else {
                                        waitDialog.dismiss()
                                        setConDialog()
                                    }
                                }
                            }
                        }

                    } else {
                        man.updateChildren(childMap) { p0, _ ->
                            if (p0 != null) {
                                waitDialog.dismiss()
                                setErrorDialog()
                            } else {
                                waitDialog.dismiss()
                                setConDialog()
                            }
                        }
                    }

                }
            })

        }
    }


    fun setErrorDialog() {
        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("เกิดข้อผิดพลาด")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }.show()


    }

    fun setConDialog() {


        CircleDialog.Builder(activity
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("บันทึกเรียบร้อย")
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                    activity.finish()
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                }.show()


    }

    fun setWaitDialog() {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText("กำลังบันทึก...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)

    }

    fun setDataToInactive(dataCard: CardData) {
        val ref = container.push().child("ข้อมูล").child("ไม่ได้ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

    fun setData(key: String, dataCard: CardData) {

        val ref = container.push().child("ข้อมูล").child("ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

}