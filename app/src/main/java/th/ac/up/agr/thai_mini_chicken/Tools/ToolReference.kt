package th.ac.up.agr.thai_mini_chicken.Tools

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ToolReference {

    private var arrKey = ArrayList<String>()

    fun checkPosition(key: String, ref: DatabaseReference) {


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                arrKey.clear()
                getKey(p0)

                var i = 0
                while (i < arrKey.size) {

                    if (i != arrKey.size) {
                        if (arrKey[i].contentEquals(key)) {
                            if (i == 0) {
                                val a = arrKey[i + 1]
                                val b = a.indexOf("-99-99-99")
                                if (b > -1) {
                                    ref.child(key).removeValue()
                                    ref.child(a).removeValue()
                                } else {
                                    ref.child(key).removeValue()
                                }
                            } else {
                                val a = arrKey[i + 1]
                                val b = arrKey[i - 1]
                                val c = a.indexOf("-99-99-99")
                                val d = b.indexOf("-99-99-99")
                                if (c > -1 && d > -1) {
                                    ref.child(key).removeValue()
                                    ref.child(a).removeValue()
                                } else {
                                    ref.child(key).removeValue()
                                }
                            }
                        }
                    }

                    i += 1
                }
            }
        })

    }

    fun getKey(dataSnapshot: DataSnapshot?) {
        arrKey.clear()
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrKey) {
            it.key
        }
    }

}