package th.ac.up.agr.thai_mini_chicken.Firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class Firebase {

    val reference: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        val reference :DatabaseReference = FirebaseDatabase.getInstance().reference
    }

}