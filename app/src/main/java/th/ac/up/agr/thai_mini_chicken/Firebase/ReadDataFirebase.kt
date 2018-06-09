package th.ac.up.agr.thai_mini_chicken.Firebase

import android.support.v4.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import java.util.ArrayList

class ReadDataFirebase(val activity: FragmentActivity) {

    private val ID = FirebaseAuth.getInstance().currentUser!!.uid


}