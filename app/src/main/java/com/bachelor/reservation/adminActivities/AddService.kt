package com.bachelor.reservation.adminActivities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bachelor.reservation.classes.Service
import com.bachelor.reservationapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_service.*


//Aktivita pridávania novej služby

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddService : AppCompatActivity() {

    val SAVED_ID_BUNDLE_KEY = "SERVICE_ID"
    val SAVED_TITLE_BUNDLE_KEY = "SERVICE_TITLE"
    val SAVED_DESC_BUNDLE_KEY = "SERVICE_DESC"
    val SAVED_DURATION_BUNDLE_KEY = "SERVICE_DURATION"


    lateinit var service: Service
    private val pickImage = 100
    var imageUri: Uri? = null
    lateinit var imageUrl : String
    lateinit var serviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        serviceId = intent.getStringExtra("serviceId")

        setupButtons()

    }

    private fun setupButtons() {
        servicePictureBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        submitServiceBtn.setOnClickListener{
            uploadImage()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {   // ak mam uložene dáta tak ich ziskam z bundlu
        super.onRestoreInstanceState(savedInstanceState)

        serviceId = savedInstanceState.getString(SAVED_ID_BUNDLE_KEY).toString()
        serviceTitle.setText(savedInstanceState.getString(SAVED_TITLE_BUNDLE_KEY))
        serviceDescription.setText(savedInstanceState.getString(SAVED_DESC_BUNDLE_KEY))
        serviceDuration.setText(savedInstanceState.getString(SAVED_DURATION_BUNDLE_KEY))
    }

    override fun onSaveInstanceState(outState: Bundle) {        // uložím dáta ak sa ide aktivita ukončiť
        super.onSaveInstanceState(outState)

        val servID = serviceId
        val servTitle = serviceTitle.text.toString()
        val servDesc = serviceDescription.text.toString()
        val servDuratiom = serviceDuration.text.toString()

        outState.putString(SAVED_ID_BUNDLE_KEY, servID)
        outState.putString(SAVED_TITLE_BUNDLE_KEY, servTitle)
        outState.putString(SAVED_DESC_BUNDLE_KEY, servDesc)
        outState.putString(SAVED_DURATION_BUNDLE_KEY, servDuratiom)

    }

    private fun uploadImage() {                 //nahravanie obrázka

        if(imageUri != null){
            var stor = FirebaseStorage.getInstance().getReference("Pictures")       //vytvorim referenciu na miesto v databáze
            val filePath: StorageReference = stor.child(System.currentTimeMillis().toString() + "." + getFileExtention(imageUri))   // referencia na filestorage databazu, id podla aktualneho času
            filePath.putFile(imageUri!!)        // nahram do databazy.
                .addOnSuccessListener {

                    it.storage.downloadUrl.addOnSuccessListener { it ->     // po uspešnom nahrati si z databázy získam URL adresu obrázka
                        imageUrl = it.toString()
                        uploadService(imageUrl)     // uložím službu do databázy
                    }
                }

                .addOnFailureListener { p0->
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                }
        }else {
            Toast.makeText(applicationContext, "Musíte vybrať obrázok!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadService(imageUrl: String) {           //vytvorím inštanciu triedy Servce a nahram do databázy
        val ref = FirebaseDatabase.getInstance().getReference("Services")
        service = Service(serviceId, serviceTitle.text.toString() ,imageUrl, serviceDescription.text.toString(),serviceDuration.text.toString(),servicePrice.text.toString())
        ref.child(serviceTitle.text.toString()).setValue(service)
            .addOnFailureListener { p0->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Uložené", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getFileExtention(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(uri?.let {
            contentResolver.getType(
                    it
            )
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {       // výsledok aktivity na výber obrazka
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage && data != null) {
            imageUri = data.data!!
            serviceImage.setImageURI(imageUri)
        }
    }


}