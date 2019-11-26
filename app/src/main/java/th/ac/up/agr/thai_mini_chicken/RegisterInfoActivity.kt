package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

import com.mylhyl.circledialog.CircleDialog

import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_register_info.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.ActionDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.AlertsDialog
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.QuickProgressDialog
import java.io.ByteArrayOutputStream


class RegisterInfoActivity : AppCompatActivity() {

    var ID = ""

    private lateinit var userInfo: Information

    private var offScreen = false

    lateinit var builder: CircleDialog.Builder


    private var filepath: Uri? = null

    private var imageBitmap: Bitmap? = null

    private lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var progressDialog: DialogFragment


    companion object {
        const val REQUEST_PERMISSION_CAMERA = 56000
        const val REQUEST_PERMISSION_GALLERY = 56001
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        intent.extras?.let { bundle ->
            bundle.getString("ID")?.let {
                ID = it
            }

        }

        userInfo = Information()

        mFirebaseAuth = FirebaseAuth.getInstance()

        mFirebaseAuth.currentUser?.let { user ->
            val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")

            firebase.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.getValue(Information::class.java)?.let {
                        getData()
                    }
                }
            })

        }


        register_info_back_btn.setOnClickListener {
            backAction()
        }

        register_info_confirm_btn.setOnClickListener {
            if (!offScreen) {
                offScreen = true
                getDataToData()
            }
        }

        register_info_image_area.setOnClickListener {

            showOptionDialog(arrayOf("เปิดกล้อง", "เลือกรูปจากคลังภาพ"))

        }


    }

    private fun showOptionDialog(arr: Array<String>) {
        CircleDialog.Builder(
        )
                .configDialog { params -> params.animStyle = R.style.dialogWindowAnim }
                .setItems(arr) { _, _, position, _ ->

                    when (position) {
                        0 -> {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                setQuestionDialog(0, "คำอธิบาย", "หากคุณต้องการเปิดกล้องให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_CAMERA, "ขอสิทธิ์", "ยกเลิก")
                            } else {
                                goToCamera()
                            }

                        }
                        1 -> {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                setQuestionDialog(0, "คำอธิบาย", "หากคุณต้องการเปิดคลังภาพให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_GALLERY, "ขอสิทธิ์", "ยกเลิก")
                            } else {
                                goToGallery()
                            }
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                }
                .show(this.supportFragmentManager)
    }

    private fun goToCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(Intent.createChooser(intent, "ถ่ายรูปจาก"), REQUEST_PERMISSION_CAMERA)

    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปจาก"), REQUEST_PERMISSION_GALLERY)
    }

    private fun upload() {

        filepath?.let { uri ->


            val bmp = if (Build.VERSION.SDK_INT >= 29) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            val data = imageCalculate(bmp)

            showProgressDialog(R.string.dialog_upload_message)

            mFirebaseAuth.currentUser?.let { user ->

                val ref = FirebaseStorage.getInstance().reference.child("profile").child(user.uid)
                ref.putBytes(data)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            showAlertDialog(R.string.upload_complete_message)
                            ref.downloadUrl.addOnSuccessListener {
                                userInfo.photoURL = uri.toString()
                            }
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            showAlertDialog(R.string.error_message)
                        }.addOnCanceledListener {
                            progressDialog.dismiss()
                        }

            }

        }

    }

    private fun imageCalculate(bitmap: Bitmap): ByteArray {
        val maxSize = 100

        var data = bitmapToByteArray(bitmap, 100)

        var count = maxSize
        var passed = false


        if ((data.size) / 1000 <= maxSize) {
            passed = true
        }

        while (((data.size) / 1000 > maxSize || !passed)) {
            when {
                (bitmapToByteArray(bitmap, count / 2).size) / 1000 < 100 -> {
                    data = bitmapToByteArray(bitmap, count / 2)
                    passed = true
                }
                (bitmapToByteArray(bitmap, count / 4).size) / 1000 < 100 -> {
                    data = bitmapToByteArray(bitmap, count / 4)
                    passed = true
                }
                else -> count /= 4
            }
        }

        return data
    }

    private fun bitmapToByteArray(bitmap: Bitmap, per: Int): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, per, ByteArrayOutputStream())
        return baos.toByteArray()
    }

    private fun uploadC() {
        imageBitmap?.let { image ->
            showProgressDialog(R.string.dialog_upload_message)
            val ref = FirebaseStorage.getInstance().reference.child("profile").child(FirebaseAuth.getInstance().currentUser!!.uid)

            val data = imageCalculate(image)

            ref.putBytes(data)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        showAlertDialog(R.string.upload_complete_message)

                        ref.downloadUrl.addOnSuccessListener {
                            //Picasso.get().load(it).error(R.drawable.man).into(this@RegisterInfoActivity.register_info_image_image)
                            userInfo.photoURL = it.toString()
                        }
                        //Log.e("URI",it.uploadSessionUri.toString())
                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        showAlertDialog(R.string.error_message)
                    }.addOnCanceledListener {
                        progressDialog.dismiss()
                        Log.e("UPLOAD", "CANCEL")
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                        Log.e("PROGRESS", progress.toString())
                    }
        }
    }


    private fun showProgressDialog(message: Int?) {

        message?.let {
            progressDialog = QuickProgressDialog(this).build().setProgressText(it).show()
        } ?: run {
            progressDialog = QuickProgressDialog(this).build().show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PERMISSION_GALLERY && resultCode == RESULT_OK) {

            data?.let { intent ->
                intent.data?.let {
                    filepath = it

                    Picasso.get().load(it).error(R.drawable.man).into(this@RegisterInfoActivity.register_info_image_image)

                    upload()
                }
            }

        } else if (requestCode == REQUEST_PERMISSION_CAMERA && resultCode == RESULT_OK) {

            data?.let { intent ->
                intent.extras?.let { extras ->
                    val image = extras.get("data") as Bitmap

                    imageBitmap = Bitmap.createScaledBitmap(image, (image.width * 0.8).toInt(), (image.height * 0.8).toInt(), true)

                    imageBitmap.let {
                        register_info_image_image.setImageBitmap(it)

                        uploadC()
                    }

                }
            }

        }

    }

    private fun getPermision(permission: String, requestCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            } else {
                Log.e("PER", "0")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (requestCode) {
                REQUEST_PERMISSION_CAMERA -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        goToCamera()
                    } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                        if (!showRationale) {
                            setQuestionDialog(1, "คำอธิบาย", "เนื่องจากคุณได้ทำการกด \"ไม่ต้องแสดงอีก\" ในหน้าขอสิทธิ์ หากคุณต้่องการจะขอสิทธิ์อีกครั้ง ให้กด \"ไปที่ตั้งค่า\" แล้วกดเลือก \"สิทธิ์ของแอป\" จากนั้นกดเปิดสิทธิ์ที่ต้องการ ", REQUEST_PERMISSION_CAMERA, "ไปที่ตั้งค่า", "ยกเลิก")
                        }
                    } else {

                        showAlertDialog(R.string.error_message)

                    }

                }
                REQUEST_PERMISSION_GALLERY -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        goToGallery()
                    } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        if (!showRationale) {
                            setQuestionDialog(1, "คำอธิบาย", "เนื่องจากคุณได้ทำการกด \"ไม่ต้องแสดงอีก\" ในหน้าขอสิทธิ์ หากคุณต้่องการจะขอสิทธิ์อีกครั้ง ให้กด \"ไปที่ตั้งค่า\" แล้วกดเลือก \"สิทธิ์ของแอป\" จากนั้นกดเปิดสิทธิ์ที่ต้องการ ", REQUEST_PERMISSION_GALLERY, "ไปที่ตั้งค่า", "ยกเลิก")
                        }
                    } else {
                        showAlertDialog(R.string.error_message)
                    }

                }
            }
        }
    }


    private fun setQuestionDialog(ID: Int, title: String, sub: String, requestCode: Int, positive: String, negative: String) {
        CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(sub)
                .configText { params ->
                    params!!.textSize = 50
                    params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
                }
                .setTitle(title)
                .configTitle { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                }
                .setPositive(positive) {
                    if (ID == 0) {
                        when (requestCode) {
                            REQUEST_PERMISSION_CAMERA -> {
                                getPermision(android.Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA)
                            }
                            REQUEST_PERMISSION_GALLERY -> {
                                getPermision(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_GALLERY)
                            }
                        }
                    } else if (ID == 1) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                }
                .setNegative(negative) {
                }
                .configNegative { params ->
                    params.textSize = 50

                    params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                }
                .show(supportFragmentManager)


    }


    fun getData() {

        if (!userInfo.username.contentEquals("null")) {
            register_info_name_edittext.setText(userInfo.username)
        }
        if (!userInfo.phoneNumber.contentEquals("null")) {
            register_info_phone_edittext.setText(userInfo.phoneNumber)
        }
        if (!userInfo.farmName.contentEquals("null")) {
            register_info_farm_name_edittext.setText(userInfo.farmName)
        }
        if (!userInfo.farmAddress.contentEquals("null")) {
            register_info_farm_address_edittext.setText(userInfo.farmAddress)
        }
        if (userInfo.photoURL.isNotEmpty()) {
            Picasso.get().load(userInfo.photoURL).error(R.drawable.man).into(register_info_image_image)
        }
    }

    private fun getDataToData() {
        showProgressDialog(R.string.dialog_saving_message)

        userInfo.apply {
            this.username = this@RegisterInfoActivity.register_info_name_edittext.text.toString()
            this.phoneNumber = register_info_phone_edittext.text.toString()
            this.farmName = register_info_farm_name_edittext.text.toString()
            this.farmAddress = register_info_farm_address_edittext.text.toString()
        }

        if (userInfo.username.isEmpty() || userInfo.farmName.isEmpty() || userInfo.farmAddress.isEmpty() || userInfo.phoneNumber.isEmpty()) {
            offScreen = false

            progressDialog.dismiss()
            showAlertDialog(R.string.message_data_not_complete)
        } else {
            pushToFirebase()
        }
    }

    private fun pushToFirebase() {
        val firebase = Firebase.reference.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายละเอียด")

        firebase.setValue(userInfo) { p0, _ ->
            if (p0 != null) {
                progressDialog.dismiss()
                showAlertDialog(R.string.error_message)
                offScreen = false
            } else {
                progressDialog.dismiss()
                ActionDialog(this).setMessage(R.string.saved_message).positive(R.string.ok_message_response) {
                    val intent = Intent(this, ProgramMainActivity::class.java)
                    this.startActivity(intent)
                    this.finish()
                }.build().show()
            }
        }
    }

    private fun showAlertDialog(resource: Int) {
        AlertsDialog(this).setTitle(resource).positive(R.string.ok_message_response).build().show()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        backAction()

    }

    private fun backAction() {

        if (ID.isNotEmpty()) {
            val intent = Intent(this, ProgramMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
