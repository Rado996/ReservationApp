package com.bachelor.reservation.adminActivities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservationapp.R
import com.bachelor.reservationapp.classes.Picture
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.android.synthetic.main.activity_post_picture.*

class PostPicture : AppCompatActivity() {

    lateinit var picture: Picture

    private val pickImage = 100
    lateinit var imageUri: Uri
    lateinit var imageUrl : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_picture)

        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)

        postPicture.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                upload()
            }
        })
    }

    private fun upload() {
        val progressDialog = ProgressDialog(this@PostPicture)
        progressDialog.setMessage("Uploading")   // zmenit na progressbar
        progressDialog.show()

        if(imageUri != null){

            var stor = FirebaseStorage.getInstance().getReference("Pictures")
            val filePath: StorageReference = stor.child(System.currentTimeMillis().toString() + "." + getFileExtention(imageUri))
            filePath.putFile(imageUri)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    //Toast.makeText(applicationContext, "Picture uploaded", Toast.LENGTH_SHORT).show()
                    it.storage.downloadUrl.addOnSuccessListener { it ->
                        imageUrl = it.toString()
                        val ref = FirebaseDatabase.getInstance().getReference("Pics")
                        val picID = ref.push().key
                        picID?.let { it1 ->
                            picture = Picture(it1,imageUrl, imageDescription.text.toString())

                            ref.child(it1).setValue(picture)
                                    .addOnFailureListener { p0->
                                        Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnCanceledListener {
                                        Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnCompleteListener {
                                        Toast.makeText(applicationContext, "Post saved", Toast.LENGTH_SHORT).show()
                                    }
                        }
                    }
//
                }

                .addOnFailureListener { p0->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { p0->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    progressDialog.setMessage("Uploaded ${progress.toInt()}%")
                }
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
            addedImage.setImageURI(imageUri)
        }
    }
}