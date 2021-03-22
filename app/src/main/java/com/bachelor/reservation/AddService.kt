package com.bachelor.reservation


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.selects.select


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddService : AppCompatActivity() {

    lateinit var service: Service
    private val pickImage = 100
    lateinit var imageUri: Uri
    lateinit var imageUrl : String
    lateinit var serviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        serviceId = intent.getStringExtra("serviceId")

//        servicePictureBtn.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//                startActivityForResult(gallery, pickImage)
//            }
//        })

        servicePictureBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        submitServiceBtn.setOnClickListener{
                upload()
        }

    }

    private fun upload() {

        if(imageUri != null){
            var stor = FirebaseStorage.getInstance().getReference("Pictures")
            val filePath: StorageReference = stor.child(System.currentTimeMillis().toString() + "." + getFileExtention(imageUri))
            filePath.putFile(imageUri)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "File uploaded", Toast.LENGTH_SHORT).show()
                        it.storage.downloadUrl.addOnSuccessListener { it ->
                            imageUrl = it.toString()
                            val ref = FirebaseDatabase.getInstance().getReference("Services")
                            service = Service(serviceId, serviceTitle.text.toString() ,imageUrl, serviceDescription.text.toString(),serviceDuration.text.toString())
                            ref.child(serviceTitle.text.toString()).setValue(service)
                                    .addOnFailureListener { p0->
                                        Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnCanceledListener {
                                        Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnCompleteListener {
                                        Toast.makeText(applicationContext, "completed", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnSuccessListener {
                                        Toast.makeText(applicationContext, "succes", Toast.LENGTH_SHORT).show()
                                    }
                        }
                    }
                    .addOnFailureListener { p0->
                        Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                    }
        }else {
            Toast.makeText(applicationContext, "Musíte vybrať obrázok!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getFileExtention(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(uri?.let {
            contentResolver.getType(
                    it
            )
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage && data != null) {
            imageUri = data.data!!
            serviceImage.setImageURI(imageUri)
        }
    }


}