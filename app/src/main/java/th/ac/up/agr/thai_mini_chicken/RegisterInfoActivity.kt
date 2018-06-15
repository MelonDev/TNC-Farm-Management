package th.ac.up.agr.thai_mini_chicken

import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*

import kotlinx.android.synthetic.main.activity_register_info.*
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.*
import java.io.ByteArrayOutputStream


class RegisterInfoActivity : AppCompatActivity() {

    var ID = ""

    lateinit var userInfo: Information

    var offScreen = false

    lateinit var waitDialog: DialogFragment
    lateinit var uploadDialog: DialogFragment
    lateinit var builder: CircleDialog.Builder


    var filepath: Uri? = null

    var imageBitmap: Bitmap? = null

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 56000
        const val REQUEST_PERMISSION_GALLERY = 56001
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val bundle = intent.extras
        ID = bundle.getString("ID")

        userInfo = Information()

        val authUser = FirebaseAuth.getInstance().currentUser!!

        val firebase = Firebase.reference.child("ผู้ใช้").child(authUser.uid).child("รายละเอียด")

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    userInfo = p0.getValue(Information::class.java)!!

                    getData()
                }
            }
        })

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

    fun showOptionDialog(arr: Array<String>) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.animStyle = R.style.dialogWindowAnim
                    }
                })
                .setItems(arr) { parent, view, position, id ->

                    when (position) {
                        0 -> {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                setQuestionDialog(0, "คำอธิบาย", "หากคุณต้องการเปิดกล้องให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_CAMERA, "ขอสิทธิ์", "ยกเลิก")
                            } else {
                                goToCamera()
                                //setErrorDialog("ได้สิทธิ์แล้ว")
                            }
                            //setQuestionDialog(0,"คำอธิบาย","หากคุณต้องการเปิดกล้องให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_CAMERA,"ขอสิทธิ์","ยกเลิก")
                            //getPermision(android.Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA)
                        }
                        1 -> {
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                setQuestionDialog(0, "คำอธิบาย", "หากคุณต้องการเปิดคลังภาพให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_GALLERY, "ขอสิทธิ์", "ยกเลิก")
                            } else {
                                goToGallery()
                                //setErrorDialog("ได้สิทธิ์แล้ว")
                            }
                            //getPermision(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_GALLERY)
                        }
                    }
                }
                .setNegative("ยกเลิก", null)
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    }
                })
                .show()
    }

    fun goToCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(Intent.createChooser(intent, "ถ่ายรูปจาก"), REQUEST_PERMISSION_CAMERA)

    }

    fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปจาก"), REQUEST_PERMISSION_GALLERY)
    }

    fun upload() {
        if (filepath != null) {

            val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filepath)


            //val baos = ByteArrayOutputStream()
            //bmp.compress(Bitmap.CompressFormat.JPEG, 97, baos)
            //val data = baos.toByteArray()

            val data = imageCalculate(bmp)

            Log.e("DATA",data.size.toString())

            setUploadDialog("กำลังอัพโหลด")
            val ref = FirebaseStorage.getInstance().reference.child("profile").child(FirebaseAuth.getInstance().currentUser!!.uid)
            ref.putBytes(data)
                    .addOnSuccessListener { it ->
                        uploadDialog.dismiss()
                        setErrorDialog("อัพโหลดเสร็จสิ้น")
                        ref.downloadUrl.addOnSuccessListener {
                            //Picasso.get().load(it).error(R.drawable.man).into(this@RegisterInfoActivity.register_info_image_image)
                            userInfo.photoURL = it.toString()
                        }
                        //Log.e("URI",it.uploadSessionUri.toString())
                    }.addOnFailureListener {
                        uploadDialog.dismiss()
                        setErrorDialog("เกิดข้้อผิดพลาด")
                    }.addOnCanceledListener {
                        uploadDialog.dismiss()
                        Log.e("UPLOAD", "CANCEL")
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                        Log.e("PROGRESS", progress.toString())
                    }
        }
    }

    fun imageCalculate(bitmap: Bitmap) :ByteArray{
        val maxSize = 100

        var data = bitmapToByteArray(bitmap,100)

        var count = maxSize
        var passed = false


        if((data.size)/1000 <= maxSize){
            passed = true
        }

        while (((data.size)/1000 > maxSize || !passed)){
            if((bitmapToByteArray(bitmap,count/2).size)/1000 < 100){
                data = bitmapToByteArray(bitmap,count/2)
                passed = true
            }else if((bitmapToByteArray(bitmap,count/4).size)/1000 < 100){
                data = bitmapToByteArray(bitmap,count/4)
                passed = true
            } else {
                count /= 4
            }
        }

        return data
    }

    fun bitmapToByteArray(bitmap: Bitmap,per :Int) :ByteArray{
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos)
        return baos.toByteArray()
    }

    fun uploadC() {
        if (imageBitmap != null) {

            setUploadDialog("กำลังอัพโหลด")
            val ref = FirebaseStorage.getInstance().reference.child("profile").child(FirebaseAuth.getInstance().currentUser!!.uid)

            val data = imageCalculate(imageBitmap!!)

            ref.putBytes(data)
                    .addOnSuccessListener { it ->
                        uploadDialog.dismiss()
                        setErrorDialog("อัพโหลดเสร็จสิ้น")
                        ref.downloadUrl.addOnSuccessListener {
                            //Picasso.get().load(it).error(R.drawable.man).into(this@RegisterInfoActivity.register_info_image_image)
                            userInfo.photoURL = it.toString()
                        }
                        //Log.e("URI",it.uploadSessionUri.toString())
                    }.addOnFailureListener {
                        uploadDialog.dismiss()
                        setErrorDialog("เกิดข้้อผิดพลาด")
                    }.addOnCanceledListener {
                        uploadDialog.dismiss()
                        Log.e("UPLOAD", "CANCEL")
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                        Log.e("PROGRESS", progress.toString())
                    }
        }
    }


    fun setUploadDialog(string: String) {
        builder = CircleDialog.Builder()
        uploadDialog = builder
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_GALLERY && resultCode == RESULT_OK) {
            filepath = data!!.data

            Picasso.get().load(filepath).error(R.drawable.man).into(this@RegisterInfoActivity.register_info_image_image)

            upload()
            /*
            try {
                bitmap = Media.getBitmap(this.getContentResolver(), uri);
                imageView1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        } else if (requestCode == REQUEST_PERMISSION_CAMERA && resultCode == RESULT_OK) {
            //Log.e("IMAGE", data.toString())

            val extras = data!!.extras
            val image = extras.get("data") as Bitmap?

            //val matrix = Matrix()
            //matrix.postScale(0.5f, 0.5f)
            //imageBitmap = Bitmap.createBitmap(image, 100, 100, 100, 100, matrix, true)

            imageBitmap = Bitmap.createScaledBitmap(image,(image!!.width*0.8).toInt(),(image!!.height*0.8).toInt(),true)

            //Picasso.get().load(imageBitmap).into(register_info_image_image)
            imageBitmap.let {
                register_info_image_image.setImageBitmap(it)

                uploadC()
            }

            //filepath = data!!.data
            //Log.e("IMAGE",filepath.toString())

            //upload()
        }

    }

    fun getPermision(permission: String, requestCode: Int) {

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
                        setErrorDialog("เกิดข้อผิดพลาด")
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
                        setErrorDialog("เกิดข้อผิดพลาด")
                    }

                }
            }
        }
    }


    fun setQuestionDialog(ID: Int, title: String, sub: String, requestCode: Int, positive: String, negative: String) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(sub)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                        params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)

                    }
                })
                .setTitle(title)
                .configTitle(object : ConfigTitle() {
                    override fun onConfig(params: TitleParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                    }
                })
                .setPositive(positive, {
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

                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                    }
                })
                .setNegative(negative, {
                })
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50

                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)

                    }
                })
                .show()


    }


    fun getData() {
        if (userInfo.username != null && !userInfo.username.contentEquals("null")) {
            register_info_name_edittext.setText(userInfo.username)
        }
        if (userInfo.phoneNumber != null && !userInfo.phoneNumber.contentEquals("null")) {
            register_info_phone_edittext.setText(userInfo.phoneNumber)
        }
        if (userInfo.farmName != null && !userInfo.farmName.contentEquals("null")) {
            register_info_farm_name_edittext.setText(userInfo.farmName)
        }
        if (userInfo.farmAddress != null && !userInfo.farmAddress.contentEquals("null")) {
            register_info_farm_address_edittext.setText(userInfo.farmAddress)
        }
        if (userInfo.photoURL != null && userInfo.photoURL.isNotEmpty()) {
            Picasso.get().load(userInfo.photoURL).error(R.drawable.man).into(register_info_image_image)
        }
    }

    fun getDataToData() {
        setWaitDialog("กำลังบันทึก...")

        userInfo.apply {
            this.username = this@RegisterInfoActivity.register_info_name_edittext.text.toString()
            this.phoneNumber = register_info_phone_edittext.text.toString()
            this.farmName = register_info_farm_name_edittext.text.toString()
            this.farmAddress = register_info_farm_address_edittext.text.toString()
        }

        if (userInfo.username.isEmpty() || userInfo.farmName.isEmpty() || userInfo.farmAddress.isEmpty() || userInfo.phoneNumber.isEmpty()) {
            offScreen = false

            waitDialog.dismiss()
            setErrorDialog("ข้อมูลไม่ครบ")
        } else {
            pushToFirebase()
        }
    }

    fun pushToFirebase() {
        val firebase = Firebase.reference.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายละเอียด")

        firebase.setValue(userInfo) { p0, p1 ->
            if (p0 != null) {
                waitDialog.dismiss()
                setErrorDialog("เกิดข้อผิดพลาด")
                offScreen = false
            } else {
                waitDialog.dismiss()
                showConDialog()
            }
        }
    }

    fun setErrorDialog(string: String) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(string)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    }
                }).show()


    }

    fun showConDialog() {
        CircleDialog.Builder(this
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
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, MelonTheme.from(this@RegisterInfoActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                    offScreen = false
                    val intent = Intent(this@RegisterInfoActivity, ProgramMainActivity::class.java)
                    this@RegisterInfoActivity.startActivity(intent)
                    this@RegisterInfoActivity.finish()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@RegisterInfoActivity, R.color.colorText)
                    }
                })

                .show()


    }

    override fun onBackPressed() {
        super.onBackPressed()

        backAction()

    }

    fun setWaitDialog(string: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)
    }

    fun backAction() {



        if (ID.isNotEmpty()) {
            val intent = Intent(this, ProgramMainActivity::class.java)
            startActivity(intent)
            finish()
            /*
            if (ID.contentEquals("0")) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else if (ID.contentEquals("1")) {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            */
        }
    }

}
