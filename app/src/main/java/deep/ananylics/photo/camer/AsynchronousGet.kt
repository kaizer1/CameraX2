package deep.ananylics.photo.camer

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException



class AsynchronousGet internal constructor(private val numberReq: Int, private val imageFile: File?,
                                           private val strDomain: String, val cont: Context? = null) {

    private var client = OkHttpClient()
    public var dataReturn : CallbackData? = null

    @Throws(Exception::class)
    fun run() {

        //val  requestBody  = RequestBody.create( JSON_MEDIA, jsonSend.toString())
      //  val autho = "Bearer $apiKey"

        val mediaType  =
            "image/jpeg".toMediaType()//"content-type\", \"multipart/form-data".toMediaType() // MediaType.parse(getMimeType(imageFile.toURI().toURL().toString()))


        val requestBody =  MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("files", imageFile!!.getName(), imageFile.asRequestBody(mediaType))
            .build()



        val request2 = when(numberReq){
            1 -> {
                Builder()
                    .url(strDomain) // ppsand.esokolov.com
                    .addHeader("content-type", "multipart/form-data")
                    .post(requestBody)
                    .build()
            }


            else -> {
                Builder()
                    .url("https://$strDomain/get-api-link")
                   // .addHeader("Authorization", autho)
                    .get()
                    .build()
            }
        }


        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("sdf", " eror this ${e.toString()}")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                println(" my response =  ${response.code}")

                if (response.isSuccessful) {
                       println(" all successful load ! ")
                    }
                }


//                when(numberReq){
//                    1 -> {}
//                    2 -> {
//
//                        //val viewModelShared22 = ViewModelProviders.of()[SharedViewModel1::class.java]
//                        //val df = ViewModelProviders.of()
//                        //viewModelShared22
//                    }
//                }

//                        if (numberReq == 1) {
//                            val stringGet: String = response.body!!.string()
//                            // println(" sdfsd string = $stringGet")
//                            val jsonParse = JSONObject(stringGet)
//
//                            val pingUrl = jsonParse.get("ping_url")
//                            val messUrl = jsonParse.get("message_url")
//                            //println(" my data isUrl = $pingUrl")
//                            //println(" my data isMes = $messUrl")
//                            dataReturn!!.returnServerAnswer(
//                                pingUrl.toString(),
//                                messUrl.toString(),
//                                7
//                            )
//                        } else if (numberReq == 3) {
//
//
//                        }else if(numberReq == 2){
//                         val intent =  Intent("my.event.calling.circle")
//                         intent.putExtra("message", 0)
//                        // LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//
//                    }else {
//                                 dataReturn!!.returnServerAnswer("", "", 1)
//                        }
//                }

           // }
        })
    }

    companion object {
        val JSON_MEDIA : MediaType =    "application/json; charset=utf-8".toMediaType()
        val myMutableStateGreenOrRedCircle = MutableLiveData<Int>()
    }
}