package ua.shumov.asyncbitmap

import android.app.Dialog
import android.graphics.*
import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import ua.shumov.asyncbitmap.databinding.ProgressBarBinding
import java.lang.Exception

class InvertBitmapAsyncTask(progressBar: Dialog, val width: Int, val height: Int)
                                              : AsyncTask<Bitmap, Void, Bitmap>() {
    val progressBarDialog = progressBar

    override fun onPreExecute() {
        super.onPreExecute()
            progressBarDialog.show()
    }
    override fun doInBackground(vararg params: Bitmap): Bitmap {
        val bitmap :Bitmap = params[0]
            return bitmap.invertColors(width, height)
    }

    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
            progressBarDialog.dismiss()
    }

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
