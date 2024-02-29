package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MaleAvatar : AppCompatActivity() {

    companion object {
        const val TAKE_AVATAR_CAMERA_REQUEST = 1
        const val TAKE_AVATAR_GALLERY_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_male_avatar)

        val avatarButton: ImageButton = findViewById(R.id.ImageButton_Avatar)

        avatarButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val strAvatarPrompt = "Take your picture to store as your avatar!"
                val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(
                    Intent.createChooser(pictureIntent, strAvatarPrompt),
                    TAKE_AVATAR_CAMERA_REQUEST
                )
            }
        })

        avatarButton.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val strAvatarPrompt = "Choose a picture to use as your avatar!"
                val pickPhoto = Intent(Intent.ACTION_PICK)
                pickPhoto.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(pickPhoto, strAvatarPrompt),
                    TAKE_AVATAR_GALLERY_REQUEST
                )
                return true
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_AVATAR_CAMERA_REQUEST -> {
                    // Handle the result from the camera
                    // The image may be available in the data parameter of onActivityResult
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    // Do something with the imageBitmap
                }
                TAKE_AVATAR_GALLERY_REQUEST -> {
                    // Handle the result from the gallery
                    // The selected image URI may be available in the data parameter of onActivityResult
                    val selectedImageUri = data?.data
                    // Do something with the selectedImageUri
                }
            }
        }
    }
}
