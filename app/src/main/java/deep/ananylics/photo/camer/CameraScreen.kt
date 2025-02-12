package deep.ananylics.photo.camer

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import deep.ananylics.photo.camer.CameraFileUtils.takePicture
import java.util.concurrent.Executors

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
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

        Button(
            onClick = {
                takePicture(cameraController, context, executor, { uri ->
                    capturedImageUri.value = uri
                }, { exception ->

                })
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Take Picture")
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