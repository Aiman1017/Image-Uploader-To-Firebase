package com.example.bottomnavbardemo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadFragment(activity: Activity) : Fragment(R.layout.fragment_upload) {

    var resolver = activity.contentResolver
    lateinit var filePath: Uri
    lateinit var myUploadImageView: ImageView
    lateinit var myUploadTextView: TextView
    lateinit var myUploadButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initiate the var outside of the method
        //So the onActivity can access the var
        myUploadImageView = view.findViewById<ImageView>(R.id.imageView)
        myUploadTextView = view.findViewById<TextView>(R.id.textView2)
        myUploadButton = view.findViewById<Button>(R.id.button)
        myUploadImageView.setOnClickListener {
            Log.i("Upload", "Image clicked")
            var i = Intent()
            //Get the image file
            i.action = Intent.ACTION_GET_CONTENT
            i.type = "image/*"
            //Knowing where the request is coming from
            startActivityForResult(i, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            //Get the file path
            filePath = data.data!!
            //Create an Image from the file location using ImageDecoder
            //ImageDecoder needs createSource, contentResolver and filePath
            var source = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                ImageDecoder.createSource(resolver, filePath)
            } else {
                TODO("VERSION.SDK_INT < P")
            }
            var bitmap = ImageDecoder.decodeBitmap(source)
            myUploadImageView.setImageBitmap(bitmap)
            myUploadImageView.setBackgroundColor(Color.WHITE)
            myUploadTextView.text = "Tap to select other photos"
            myUploadTextView.setBackgroundColor(Color.DKGRAY)
            myUploadButton.visibility = View.VISIBLE
            myUploadButton.setOnClickListener{
                uploadToFirebase()
            }
        }
    }

    fun uploadToFirebase(){
        //Generate string sequence
        var imageFileName = UUID.randomUUID().toString()
        var storageRef = FirebaseStorage.getInstance().reference.child("/images/$imageFileName")
        storageRef.putFile(filePath)
                //If image successful upload
                .addOnSuccessListener {
                    myUploadTextView.text = "Uploaded, Tap to select another"
                    Log.i("Image name", "${it.metadata?.path}")
                }
                //If image fail upload
                .addOnFailureListener{
                    myUploadTextView.text = "Upload failed"
                }
    }
}