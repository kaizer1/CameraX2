package deep.ananylics.photo.camer

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.compose.rememberAsyncImagePainter
import deep.ananylics.photo.camer.CameraFileUtils.takePicture
import java.nio.file.WatchEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraScreen( frontCamera : Boolean, updateSec : Int,
                  widthIma : Int, heightIma : Int, finalURl : String) {

     val context = LocalContext.current
     val lifecycleOwner = LocalLifecycleOwner.current
     val executor = remember { Executors.newSingleThreadExecutor() }
     val capturedImageUri = remember { mutableStateOf<Uri?>(null) }
     val handler = Handler(Looper.getMainLooper())
     lateinit var runnable: Runnable
     var startServ by remember { mutableStateOf(false) }

    println(" my url == ${finalURl}")

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            //cameraSelector
            if(frontCamera){
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            }else{
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
        }
    }

    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),

            factory = { ctx ->

                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    controller = cameraController
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )


        Column (modifier = Modifier.align(Alignment.BottomEnd)
                                   .align(Alignment.BottomCenter) )
        {

            Button(
                onClick = {
                    if(!startServ)
                    {
                        //handler.removeCallbacks(runnable)
                        startServ = true
                    }

                    if(startServ){


                        runnable = object : Runnable {
                            override fun run() {

                                takePicture(finalURl, widthIma, heightIma, cameraController, context, executor, { uri ->
                                    capturedImageUri.value = uri
                                }, { exception ->

                                })

                                handler.postDelayed(this, (updateSec * 1000).toLong()) // Post the Runnable again after 1 second

                            }
                        }
                        handler.post(runnable)

                    }

                },
                //modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Start picture Service !")
            }



            Button(
                onClick = {

                  //  handler.removeCallbacks(runnable)
                    startServ = false
                },
            ) {
                Text(text = "Stop Service !")
            }

        }



        capturedImageUri.value?.let { uri ->
            Image(

                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.BottomEnd),
                contentScale = ContentScale.Crop
            )
        }
    }

}






@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}