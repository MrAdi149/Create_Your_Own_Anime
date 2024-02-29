package com.example.loginsignup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginsignup.databinding.ActivitySelectedCharacterBinding

import java.io.IOException

class SelectedCharacter : AppCompatActivity() {

    private lateinit var binding: ActivitySelectedCharacterBinding
    private val selectedCharacters = mutableListOf<Selected>()
    private val saveInterval: Long = 5000 // 5 seconds
    private val handler = Handler(Looper.getMainLooper())
    private val pdfDocument = PdfDocument()

    companion object {
        private const val SAVE_PDF_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvSelectedLists.layoutManager = layoutManager

        val adapter = RecyclerViewSelectedAdapter(selectedCharacters)
        binding.rvSelectedLists.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            showCharacterSelectionDialog()
        }

        binding.downloadButton.setOnClickListener {
            createPdf()
        }

        // Start the repeating task to save text every 5 seconds
        handler.postDelayed(saveTextTask, saveInterval)
    }

    private fun createPdf() {
        // Create a PageInfo object
        val pageInfo = PdfDocument.PageInfo.Builder(
            612, 792, 1
        ).create()

        // Start a page
        val page = pdfDocument.startPage(pageInfo)

        // Draw the contents of the RecyclerView on the PDF page
        drawRecyclerViewOnPdf(page.canvas, binding.rvSelectedLists)

        // Finish the page
        pdfDocument.finishPage(page)

        // Open a system file picker to choose the destination directory
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "selected_characters.pdf")
        }

        startActivityForResult(intent, SAVE_PDF_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SAVE_PDF_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                        pdfDocument.close()
                        showNotification("PDF saved at ${uri.path}")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showNotification(filePath: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create NotificationChannel for Android Oreo and higher
            val channel = NotificationChannel(
                "pdf_channel",
                "PDF Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "pdf_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("PDF Downloaded")
            .setContentText("PDF saved at $filePath")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun drawRecyclerViewOnPdf(canvas: Canvas, view: View) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                view.width,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        view.layout(
            0,
            0,
            view.measuredWidth,
            view.measuredHeight
        )

        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        val recyclerCanvas = Canvas(bitmap)
        view.draw(recyclerCanvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private val saveTextTask = object : Runnable {
        override fun run() {
            saveTextsToInternalStorage()
            handler.postDelayed(this, saveInterval)
        }
    }

    private fun showCharacterSelectionDialog() {
        val maleList: List<Male> = getMaleCharacters()
        val femaleList: List<Female> = getFemaleCharacters()

        val characterNames = maleList.map { it.name } + femaleList.map { it.name }

        AlertDialog.Builder(this)
            .setTitle("Select a Character")
            .setItems(characterNames.toTypedArray()) { _, which ->
                val selectedCharacter = if (which < maleList.size) {
                    Selected(maleList[which].image, maleList[which].name)
                } else {
                    Selected(
                        femaleList[which - maleList.size].image,
                        femaleList[which - maleList.size].name
                    )
                }

                // Handle the selected character
                val position = selectedCharacters.size
                selectedCharacters.add(selectedCharacter)
                binding.rvSelectedLists.adapter?.notifyItemInserted(position)
            }
            .show()
    }

    private fun saveTextsToInternalStorage() {
        for (selectedCharacter in selectedCharacters) {
            saveTextToInternalStorage(selectedCharacter)
        }
    }

    private fun saveTextToInternalStorage(selectedCharacter: Selected) {
        val fileName = "selected_character_${selectedCharacter.image}.txt"
        val textToSave = selectedCharacter.editTextContent

        try {
            openFileOutput(fileName, MODE_PRIVATE).use {
                it.write(textToSave.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the callback when the activity is destroyed
        handler.removeCallbacks(saveTextTask)
    }

    private fun getMaleCharacters(): List<Male> {
        // Replace this with your logic to fetch male characters
        // Example: return a list of Male objects
        return listOf(
            Male("Angry Hero", R.drawable.angry_hero),
            Male("Simple Hero", R.drawable.simple_hero),
            Male("Silent Hero", R.drawable.silent_character),
            Male("Confident Hero", R.drawable.confident_hero),
            Male("Side Character 1", R.drawable.side_character_1),
            Male("Side Character 2", R.drawable.side_character_2),
            Male("Side Character 3", R.drawable.side_character_3),
            Male("Master Of Hero", R.drawable.master_of_hero),
            Male("Villian 1", R.drawable.villian_1),
            Male("Villian 2", R.drawable.villian_2),
            Male("Villian 3", R.drawable.villian_3),
            Male("Villian 4", R.drawable.villian_4),
            Male("Villian 5", R.drawable.villian_5)
        )
    }

    private fun getFemaleCharacters(): List<Female> {
        // Replace this with your logic to fetch female characters
        // Example: return a list of Female objects
        return listOf(
            Female("Simple Heroine", R.drawable.simplefemale),
            Female("Strong Heroine 1", R.drawable.strongfemale1),
            Female("Strong Heroine 2", R.drawable.strongfemale2),
            Female("Confused Heroine", R.drawable.confusedfemale),
            Female("Female Side character 1", R.drawable.femalesidecharacter1),
            Female("Female Side character 2", R.drawable.femalesidecharacter2),
            Female("Female Side character 3", R.drawable.femalesidecharacter3),
            Female("Female Side character 4", R.drawable.femalesidecharacter4),
            Female("Innocent Heroine", R.drawable.innocentfemale),
            Female("female villian 1 ", R.drawable.fvillian1),
            Female("female villian 2 ", R.drawable.fvillian2),
            Female("female villian 3 ", R.drawable.fvillian3),
            Female("female villian 4 ", R.drawable.fvillian4),
            Female("female villian 5 ", R.drawable.fvillian5)
        )
    }
}
