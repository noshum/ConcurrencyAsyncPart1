package ua.shumov.asyncbitmap

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.DialogCompat
import androidx.core.graphics.drawable.toBitmap
import ua.shumov.asyncbitmap.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val progressDialog = Dialog(this@MainActivity)
        progressDialog.setContentView(R.layout.progress_bar)

        binding.apply {
            btnChange.setOnClickListener {
                val bitmap = invImage.drawable.toBitmap()
                val task = InvertBitmapAsyncTask(progressDialog, invImage.width, invImage.height)
                val invertedBitmap = task.execute(bitmap).get()
                invImage.setImageBitmap(invertedBitmap)
            }

            btnOrMe.setOnClickListener {
                progressDialog.show()

                val bitmap = invImage.drawable.toBitmap()
                val task = MyRunnable(bitmap, invImage.width, invImage.height)
                val thread = Thread(task)
                thread.start()
                thread.join()
                val invertedBitmap = task.getBitmap()
                invImage.setImageBitmap(invertedBitmap)

                progressDialog.dismiss()
            }
        }
    }

    class MyRunnable(private var bitmap: Bitmap, width: Int, height: Int) : Runnable {
        val width = width
        val height = height

        override fun run() {
            bitmap = bitmap.invertColors(width, height)
        }

        fun getBitmap(): Bitmap {
            return bitmap
        }
        // extension function to invert bitmap colors
        private fun Bitmap.invertColors(width: Int, height: Int): Bitmap {
            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            val matrixInvert = ColorMatrix().apply {
                set(
                    floatArrayOf(
                        -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                    )
                )
            }

            val paint = Paint()
            ColorMatrixColorFilter(matrixInvert).apply {
                paint.colorFilter = this
            }

            Canvas(bitmap).drawBitmap(this, 0f, 0f, paint)
            return bitmap
        }
    }
}