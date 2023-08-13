package com.hifacy.nelikeme.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hifacy.nelikeme.R
import com.hifacy.nelikeme.database.SharedPreference
import com.hifacy.nelikeme.databinding.ActivityMainBinding
import com.hifacy.nelikeme.webservice.*
import com.presentid.sdk.IdentityVerificationSDK
import com.presentid.sdk.IdentityVerificationSDK_EventListener
import com.presentid.sdk.IdentityVerificationSDK_EventListener.enmResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException


//ADD VIEWBIDING
class MainActivity : AppCompatActivity() {

    /**
     * -> ADs
     */
    private var interstitialAd: InterstitialAd? = null

    ////////Ads functions/////////////////

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.interstitial_ad_unit_id) , adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {

                    interstitialAd = ad
                    ad.setFullScreenContentCallback(
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                interstitialAd = null
                            }

                            override fun onAdShowedFullScreenContent() {

                                Handler(Looper.getMainLooper()).postDelayed({
//                                    lyTop?.visibility = View.GONE
//                                    lyRate?.visibility = View.VISIBLE
                                }, 100)
                            }
                        })
                    showInterstitial()

                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {

                    Log.e("addfail", loadAdError.toString())
                    interstitialAd = null

                }
            })
    }

    private fun showInterstitial() {
        if (interstitialAd != null ) {
            Handler(Looper.getMainLooper()).postDelayed({
                interstitialAd!!.show(this)
            }, 0)

        }
    }

    var genderValue: Int? = null

    private var sdkKey: String = "My Sdk Key"
    private var idvSDK = IdentityVerificationSDK()
    private var idvSDK_EventListener: IdentityVerificationSDK_EventListener =
        object : IdentityVerificationSDK_EventListener {
            override fun LiveSDK_onResult(p0: enmResult) {
                mainBinding!!.lyResult.visibility = View.VISIBLE
                when(p0){
                    enmResult.FaceVerifyError, enmResult.Success ->{
                        Log.e("HadiResponse", genderValue.toString())
                        mainBinding!!.lyResult.visibility = View.VISIBLE
                        userResponse(idvSDK.image_Liveness_SimapleFrame, genderValue!!/*,999*/)
                    }
                    enmResult.Fail -> {
                        Log.e("HadiResponse", "enmResult.Back.name.toString()fail")
                        mainBinding!!.lyResult.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "FAIL", Toast.LENGTH_LONG).show()
                    }
                    enmResult.Error -> {
                        Log.e("HadiResponse", "enmResult.Back.name.toString()error")
                        mainBinding!!.lyResult.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_LONG).show()
                    }
                    enmResult.Back-> {
                        mainBinding!!.lyResult.visibility = View.GONE
                    }
                }
            }

            override fun LiveSDK_onSendDataState(p0: IdentityVerificationSDK_EventListener.enmSendDataState?) {
                //Get state of request ....
            }

            override fun LiveSDK_onReadMRZ(
                p0: enmResult?,
                p1: String?,
                p2: String?,
                p3: String?,
                p4: String?,
                p5: String?
            ) {
                //Get MRZ data ....
            }

            override fun toString(): String {
                return super.toString()
            }

        }

    private var mainBinding: ActivityMainBinding? = null
    private var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding!!.root)

        idvSDK.Init(this, sdkKey, idvSDK_EventListener)
        idvSDK.checkVPN = false
        idvSDK.showSendDialog = false

        mainBinding!!.lyResult.visibility = View.GONE

        Glide.with(this).asGif().load(R.raw.twinceleb_loading).into(mainBinding!!.imgLoading)

        serviceBuilder = ServiceBuilder()
        sharedPreference = SharedPreference(this)
        sharedPreference?.removeValue("twinImage")
        sharedPreference?.removeValue("userImage")

        genderValue = 1

        mainBinding!!.lyMale.setOnClickListener {
            mainBinding!!.chkmale.setCheckMarkDrawable(R.drawable.checkbox_selected)
            mainBinding!!.chkFemale.setCheckMarkDrawable(R.drawable.checkbox_unselected)
            mainBinding!!.lyMale.setBackgroundResource(R.drawable.back_ly_male)
            mainBinding!!.lyFemale.setBackgroundResource(R.drawable.back_ly_female)
            genderValue = 1
        }

        mainBinding!!.lyFemale.setOnClickListener {
            mainBinding!!.chkFemale.setCheckMarkDrawable(R.drawable.checkbox_selected)
            mainBinding!!.chkmale.setCheckMarkDrawable(R.drawable.checkbox_unselected)
            mainBinding!!.lyMale.setBackgroundResource(R.drawable.back_ly_female)
            mainBinding!!.lyFemale.setBackgroundResource(R.drawable.back_ly_male)
            genderValue = 0
        }

        val firstRun: Boolean = sharedPreference!!.getValueBoolean("firstRun")
        val first: Int = sharedPreference!!.getValueInt("first")

        mainBinding!!.btnFind.setOnClickListener {
            idvSDK.clearModules()
            idvSDK.addModule(IdentityVerificationSDK_EventListener.Workflow.Liveness_Detect)
            idvSDK.Start()

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED ) {
                Handler().postDelayed({
                    mainBinding!!.lyResult.visibility = View.VISIBLE
                },1000)
            }


        }

    }

    private var webServiceMethods: RetroService? = null
    private var serviceBuilder: ServiceBuilder? = null

    private fun userResponse(photo: Bitmap, /*nation: Int,*/ gender: Int) {

        Log.e("here","In Response")
        mainBinding!!.btnFind.isActivated = false
        mainBinding!!.btnFind.isClickable = false
        mainBinding!!.btnFind.isEnabled = false

        serviceBuilder = ServiceBuilder()

        ///////////////////////
        val map = HashMap<String, RequestBody>()
        var str =  encodeImageBitmapToBase64(photo)
        Log.e("dfdf", str.toString())
        str = "data:image/jpeg;base64," + str
        sharedPreference?.removeValue("userImage")
        sharedPreference?.saveString("userImage", str)


//        map["nation"] = nation.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["gender"] = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["imageBase64"] = str.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        ///////////////////////


        webServiceMethods = serviceBuilder!!.getRetrofit(false, this)?.create(RetroService::class.java)

        val userResponse: Call<FaceSimilarModel?>? = webServiceMethods?.getSimilarFace(map)

        userResponse?.enqueue(object : Callback<FaceSimilarModel?> {

            override fun onFailure(call: Call<FaceSimilarModel?>, t: Throwable) {

                Toast.makeText(
                    applicationContext,
                    "Something wrong!" + t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Something Wrong", t.localizedMessage.toString())
                if (t is HttpException) {
                    try {
                        Log.e("Something Wrong", "t.localizedMessage.toString()")

                        val errorStringRaw: String? = t.response()?.errorBody()?.string()
                        //Parse error message; format is api specific; we can't make a generic approach for this as of the moment
                        val type = object : TypeToken<ResponseBody?>() {}.type
                        val response: ResponseBody = Gson().fromJson(errorStringRaw, type)
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onResponse(call: Call<FaceSimilarModel?>, response: Response<FaceSimilarModel?>) {

                try {
                    System.out.println(response.body())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                var userModel: FaceSimilarModel? = null

                try {
                    userModel = response.body()
                    Log.e("hhhh", userModel?.data.toString())
                    sharedPreference?.removeValue("twinImage")
                    sharedPreference?.removeValue("twinName")
                    sharedPreference?.saveString("twinImage", userModel?.data?.imageBase64.toString())
                    sharedPreference?.saveString("twinName", userModel?.data?.name.toString())

                } catch (e: Exception) {
                    Log.d("TAG", "onResponse: error parsing$e")
                }

                if (userModel?.data?.imageBase64 != null) {
                    loadInterstitialAd()
                    startActivity(Intent(this@MainActivity, ResultActivity::class.java))
                    Handler().postDelayed({
                        mainBinding!!.lyResult.visibility = View.GONE
                    },1000)
                }


                if (!response.isSuccessful) {
                    mainBinding!!.lyResult.visibility = View.GONE
                    Log.e("hadiiii", response.message().toString())
                    Log.e("hadiiii", response.body().toString())
                    Log.e("hadiiii", response.errorBody().toString())
                    Log.e("hadiiii", response.raw().toString())


                }
            }
            //هتل اپاتمان آیسان خیابان ویلا - نوشته ۱ شب - خیابن نجات الهی - ویلا مابین سمیه و طالقانی -
        })

        Handler().postDelayed({
//            mainBinding!!.lyResult.visibility = View.GONE
        },3500)
        mainBinding!!.btnFind.isActivated = true
        mainBinding!!.btnFind.isClickable = true
        mainBinding!!.btnFind.isEnabled = true

    }

    private fun encodeImageBitmapToBase64(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mainBinding!!.lyResult.visibility == View.VISIBLE){
            mainBinding!!.lyResult.visibility = View.GONE
        } else {
            finishAffinity() // or finish();
        }
    }


}
