package se.example.mushroommapper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*


//Image upload tutorial: On step AndroidManifest.xml
//https://rrtutors.com/tutorials/how-to-upload-image-to-firebase-storage-in-android-using-kotlin

private fun selectImageFromGallery(){
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(
        Intent.createChooser(
            intent,
            "Please select..."
        ),
        GALLERY_REQUEST_CODE
    )
}

override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
){
    super.onActivityResult(
        requestCode,
        resultCode,
        data
    )

    if(requestCode ==  GALLERY_REQUEST_CODE
        && resultCode == Activity.RESULT_OK
        && data != null
        && data.data != null
    ){
        //Get URI of data
        val file_uri = data.data
        if (file_uri != null) {
            uploadImageToFirebase(file_uri)
        }
    }
}

private fun uploadImageToFirebase(fileURI: Uri) {
    if (fileURI != null){
        val fileName = UUID.randomUUID().toString() + ".jpg"

        val database = FirebaseDatabase.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")

        storageRef.putFile(fileURI)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        val imageURL = it.toString()
                    }
                })
            ?.addOnFailureListener(OnFailureListener { e ->
                print(e.message)
            })
    }
}