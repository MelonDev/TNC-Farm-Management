package th.ac.up.agr.thai_mini_chicken.Firebase

import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.TextParams
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
        //val d = CardDate()
        //d.dateTime = Date().getDateNull()
        //d.IDCard = Date().getDateFakeID()
        //val refs = container.child("ใช้งาน").child(d.IDCard)
        //refs.setValue(d)

        val database = FirebaseDatabase.getInstance().reference

        val man = container.child("ใช้งาน").child(dataCard.cardID)

        val b = man.child("รายการที่ต้องทำ")
        val c = man.child("รายละเอียด").child("userObjective")
        val ref = man.child("รายละเอียด")

        //val mainJson = JSONObject()
        //val manJson = JSONObject()

        //val m = HashMap<>

        b.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                setWaitDialog()
                if (p0.value == null) {

                    //val childMap = HashMap<String, Any>()

                    setData(objective, result, dataCard, man)

                    //val arr = LoadTime().getTable(dataCard.userObjective)

                    //val objectiveJson = Gson()

                    //val salaries = HashMap<>()
                    //salaries.put("salary", salary(10, 20))
                    //val s = Gson().toJson(salaries)

                    //val w = HashMap<String,CardData>()


                    //val reminderMap = HashMap<String, AddSlot>()


                    //objectiveJson.toJson(arr)
                    //ref.setValue(dataCard)
                    //val a = Gson()
                    //a.toJson(dataCard)
                    //a.toJson(objectiveJson)
                    //man.setValue(a)
                } else {

                    Log.e("CHANGE",change.toString())
                    if (change) {
                        b.removeValue()
                        setData(objective, result, dataCard, man)

                        /*
                        val arr = LoadTime().getTable(dataCard.userObjective)
                        b.removeValue()
                        val childMap = HashMap<String, Any>()
                        for (i in arr) {
                            val key: String = man.push().key.toString()

                            val pathA = "/รายการที่ต้องทำ/$key"
                            i.cardID = key
                            i.fromID = dataCard.cardID
                            if (i.status.isEmpty()) {
                                i.status = "ACTIVE"
                            }
                            childMap.put(pathA, i)
                            //b.push().setValue(i)
                            //Log.e("ADD","TEST")
                        }
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
                        */
                        //ref.setValue(dataCard)
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
                        //ref.setValue(dataCard)
                    }
                    /*

                    c.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.value != null) {
                                val z = p0.value.toString()
                                if (!z.contentEquals(dataCard.userObjective)) {
                                    val arr = LoadTime().getTable(dataCard.userObjective)
                                    b.removeValue()
                                    val childMap = HashMap<String, Any>()
                                    for (i in arr) {
                                        val key: String = man.push().key.toString()

                                        val pathA = "/รายการที่ต้องทำ/$key"
                                        i.cardID = key
                                        i.fromID = dataCard.cardID
                                        if (i.status.isEmpty()) {
                                            i.status = "ACTIVE"
                                        }
                                        childMap.put(pathA, i)
                                        //b.push().setValue(i)
                                        //Log.e("ADD","TEST")
                                    }
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
                                    //ref.setValue(dataCard)
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
                                    //ref.setValue(dataCard)
                                }
                            }
                        }
                    })*/
                }
            }
        })

    }

    fun setData(objective: String, result: String, dataCard: CardData, man: DatabaseReference) {
        val childMap = HashMap<String, Any>()

        //Log.e("$objective","$result")

        if(objective.contentEquals("1") && result.contentEquals("ว่างเปล่า")){
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

                //val a = AddSlot(i,null)

                childMap.put(pathA, i)

                //w.put()

                //Log.e("ADD","TEST")
                //b.push().setValue(i)
                //val oj = Gson()
                //oj.toJson(i)
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
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                                        //showErrorDialog()
                                    } else {
                                        waitDialog.dismiss()
                                        setConDialog()
                                        //showConDialog()
                                    }
                                }
                            }
                        }

                    } else {
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
            })

        }
    }


    fun setErrorDialog() {
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
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                }).show()


    }

    fun setConDialog() {


        CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("บันทึกเรียบร้อย")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, MelonTheme.from(activity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    activity.finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                    }
                }).show()


    }

    fun setWaitDialog() {
        waitDialog = CircleDialog.Builder()
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText("กำลังบันทึก...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(activity.supportFragmentManager)
        //waitDialog.dismiss()
        //                        .setProgressDrawable(R.drawable.bg_progress_s)
        /*waitDialog = CircleDialog.Builder(activity
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText("登录中...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .setText("กำลังบันทึก")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(activity, R.color.colorText)
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })

*/
    }

    fun setDataToInactive(dataCard: CardData) {
        val ref = container.push().child("ข้อมูล").child("ไม่ได้ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

    fun setData(key: String, dataCard: CardData) {
        //var ref = container.child(key)
        //var ref = container.push()
        val ref = container.push().child("ข้อมูล").child("ใช้งาน")
        ref.child("Information").setValue(dataCard)
    }

    fun testGet() {

    }

}