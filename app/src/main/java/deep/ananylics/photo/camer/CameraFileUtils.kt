package deep.ananylics.photo.camer

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import com.squareup.picasso.Picasso
import deep.ananylics.photo.camer.R
import okio.source
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService


object CameraFileUtils {

    fun takePicture(
        urlM: String,
        width : Int,
        heightL : Int,
        cameraController: CameraController,
        context: Context,
        executor: ExecutorService,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val photoFile = createPhotoFile(context)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        cameraController.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Uri.fromFile(photoFile).let(onImageCaptured)
                    println(" my files size is ${photoFile.length()/1024}")

                    Picasso
                        .get()
                        .load(Uri.fromFile(photoFile))
                        .resize(width, heightL)
                        .fit()



                    // urlM
                    if(URLUtil.isValidUrl(urlM))
                        {
                            AsynchronousGet(1, photoFile, urlM).run()

                        }


                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }

    private fun createPhotoFile(context: Context): File {
        val outputDirectory = getOutputDirectory(context)
        return File(outputDirectory, photoFileName()).apply {
            parentFile?.mkdirs()
        }
    }

    private fun photoFileName() =
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"

    private fun getOutputDirectory(context: Context): File {
        val mediaDir = context.getExternalFilesDir(null)?.let {
            File(it, "my_save_files").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }
}