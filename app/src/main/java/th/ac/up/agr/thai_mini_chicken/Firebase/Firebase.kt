package th.ac.up.agr.thai_mini_chicken.Firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Firebase {

    companion object {
        val reference :DatabaseReference = FirebaseDatabase.getInstance().reference
    }

}