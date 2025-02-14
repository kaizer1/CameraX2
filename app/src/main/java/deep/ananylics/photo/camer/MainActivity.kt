package deep.ananylics.photo.camer

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KeepScreenOn()

            val cont = LocalContext.current
            var mainData = SimpleData()
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val screenWidth = configuration.screenWidthDp.dp

            var pxValueHeight = with(LocalDensity.current) { screenHeight.toPx() }
            var pxValueWidth = with(LocalDensity.current) { screenWidth.toPx() }
            println(" my values == ${pxValueWidth} and ${pxValueHeight}")

            val sharedPref = cont.getSharedPreferences("simpl", MODE_PRIVATE)
            if(sharedPref.contains("url")){
                mainData = getShaderData(sharedPref, pxValueWidth.toInt(), pxValueHeight.toInt())

            }else {

                mainData.SecondsTime = 15
                mainData.Url = "https://e8c877af25d6.vps.myjino.ru/images/upload"
                mainData.widthPicture = pxValueWidth.toInt()
                mainData.heightPicture = pxValueHeight.toInt()
                mainData.frontCamera = false

            }

//            text = mainData.SecondsTime.toString()
//            count = mainData.SecondsTime
//            textURL = mainData.Url
            //textWidth = mainData.widthPicture.toString()
            //textHeight= mainData.heightPicture.toString()


            val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            var backPressHandled by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            var text1212 by remember { mutableIntStateOf(mainData.SecondsTime) }
            var textURL12 by remember { mutableStateOf(mainData.Url) }
            var textWidth12 by remember { mutableIntStateOf(mainData.widthPicture) }
            var textHeight12 by remember { mutableIntStateOf(mainData.heightPicture) }
            var frontCamera12 by remember { mutableStateOf(mainData.frontCamera) }
            val permissionGranted = isCameraPermissionGranted.collectAsState().value


            Box(modifier = Modifier.fillMaxSize()) {
                if (permissionGranted && !backPressHandled) {
                    CameraScreen(frontCamera12, text1212.toInt(), textWidth12.toInt(), textHeight12.toInt(), textURL12 )
                } else {

                    Column() {
                        Row(modifier = Modifier.padding(top = 40.dp)){
                            // enterText seconds send
                            Text("Seconds send period: ")
                            Spacer(Modifier.width(20.dp))

                            TextField(
                                value = text1212.toString(),
                                onValueChange = {
                                    if(it.isEmpty()){
                                        mainData.SecondsTime = 0
                                        text1212 = 0
                                    }else{
                                        mainData.SecondsTime = it.toInt()
                                        text1212 = it.toInt()

                                    }


                                                },
                                singleLine = true,
                                modifier = Modifier.width(110.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                        }

                        Spacer(Modifier.height(10.dp))

                        Row(){
                            TextField(
                                value = textURL12,
                                onValueChange = { textURL12 = it },
                                label = { Text("send URL to server: ") },
                                singleLine = true,
                                modifier = Modifier.padding(horizontal = 20.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(modifier = Modifier.padding(horizontal = 30.dp)){

                            // enter height and width send photo

                            TextField(
                                value = textHeight12.toString(),
                                onValueChange = {

                                    if(it.isEmpty()){
                                        mainData.heightPicture = 0
                                        textHeight12 = 0
                                    }else{
                                        mainData.heightPicture = it.toInt()
                                        textHeight12 = it.toInt()
                                    }
                                                },
                                label = { Text("Height:") },
                                singleLine = true,
                                modifier = Modifier.width(110.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                            )

                            Spacer(Modifier.width(20.dp))

                            TextField(
                                value = textWidth12.toString(),
                                onValueChange = {

                                    if(it.isEmpty()){
                                        mainData.widthPicture = 0
                                        textWidth12 = 0
                                    }else{
                                        mainData.widthPicture = it.toInt()
                                        textWidth12 = it.toInt()
                                    }


                                                },
                                label = { Text("Width:") },
                                singleLine = true,
                                modifier = Modifier.width(110.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                        }

                        Spacer(Modifier.height(10.dp))

                        Row(){

                            //RadioButtonSingleSelection(frontCamera12)

                            val radioOptions = listOf("Back", "Front")

                            var numberF: Int = 0
                            if(frontCamera12){
                                numberF = 1
                                println(" front camera is true ")
                            }else{
                                numberF = 0
                                println(" front camera is false")
                            }

                            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[numberF]) }
                            // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
                            Column(modifier = Modifier.selectableGroup()) {
                                radioOptions.forEach { text ->
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .selectable(
                                                selected = (text == selectedOption),
                                                onClick = {
                                                    onOptionSelected(text)
                                                    frontCamera12 = text != "Back"

                                                    println(" my front ${frontCamera12}")
                                                },
                                                role = Role.RadioButton
                                            )
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (text == selectedOption),
                                            onClick = null // null recommended for accessibility with screen readers
                                        )
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                }
                            }


                         }


                        Button(
                            onClick = {
                                saveAllData(text1212.toInt(), textURL12, textWidth12.toInt(), textHeight12.toInt(), frontCamera12, sharedPref)
                                handleCameraPermission()

                                backPressHandled = false
                            },
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 40.dp)
                                .padding(horizontal = 20.dp)
                            ,

                        ) {
                            Text(text = "Start Work ")
                        }


                    }

                }
                BackHandler(enabled = !backPressHandled) {
                    println("back pressed L ")
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                       // onBackPressedDispatcher?.onBackPressed()
                       // backPressHandled = false
                    }
                }
            }
        }

    }


    private fun saveAllData(tex : Int, textUrl : String, textW : Int, textH : Int, frontCamera12 : Boolean, sharedP : SharedPreferences){

       // println(" my data is ${mainD.SecondsTime} anad ${mainD.Url} a ${mainD.widthPicture}  aa ${mainD.heightPicture}")

        println(" my seconds get =${tex}")
        val df =  sharedP.edit()

        df.putString("url", textUrl )
        df.putInt("seconds", tex)
        df.putInt("wid", textW)
        df.putInt("hei", textH)
        df.putBoolean("front", frontCamera12)

        df.apply()
    }

    private fun getShaderData(pref : SharedPreferences, wid_01: Int, hei_01: Int) : SimpleData{

      val aSeconds =   pref.getInt("seconds", 15)
      val aUrl =    pref.getString("url", "")
      val aWidth =  pref.getInt("wid", wid_01)
      var aHeight =  pref.getInt("hei", hei_01)

      var aFron   = pref.getBoolean("front", false)

    //    println(" this data = ${aWidth}")

        return SimpleData(aSeconds, aUrl!!, aWidth, aHeight, aFron)

    }

}
