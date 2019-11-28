
package th.ac.up.agr.thai_mini_chicken

import com.google.firebase.database.FirebaseDatabase

class OfflineClient :android.app.Application(){

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

    }

}