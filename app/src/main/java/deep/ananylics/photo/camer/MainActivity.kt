package deep.ananylics.photo.camer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val permissionGranted = isCameraPermissionGranted.collectAsState().value

            Box(modifier = Modifier.fillMaxSize()) {
                if (permissionGranted) {
                    CameraScreen()
                } else {
                    Button(
                        onClick = {
                            handleCameraPermission()
                        },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = "Start Capture")
                    }
                }
            }
        }

    }
}