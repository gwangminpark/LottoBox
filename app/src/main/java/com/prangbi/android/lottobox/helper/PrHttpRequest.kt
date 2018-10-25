package com.prangbi.android.lottobox.helper

import android.os.Handler
import com.google.gson.Gson
import com.prangbi.android.lottobox.base.Definition
import com.prangbi.android.lottobox.model.NLottoInfo
import com.prangbi.android.lottobox.model.PLottoInfo
import okhttp3.*
import java.io.IOException

/**
 * Created by Prangbi on 2017. 7. 15..
 */
class PrHttpRequest {
    // Definition
    enum class API {
        GET_NLOTTO_NUMBER,
        GET_PLOTTO_NUMBER
    }

    // Companion
    companion object {
        const val API_URL = Definition.SERVER_URL + "/common.do"
    }

    // Variable
    private val client = OkHttpClient()
    private val handler = Handler()

    // ResponseCallback
    private class ResponseResult {
        var returnValue: String? = null // "success", "fail"
    }

    interface ResponseCallback {
        fun onResponse(api: PrHttpRequest.API, obj: Any?)
        fun onFailure(api: PrHttpRequest.API, error: IOException)
    }

    // GET
    fun getNLottoNumber(drwNo: Int, callback: PrHttpRequest.ResponseCallback) {
        val urlStr = PrHttpRequest.API_URL + "?method=getLottoNumber&drwNo=" + if (0 < drwNo) drwNo else ""
        val request = Request.Builder().url(urlStr).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body()?.string()
                val gson = Gson()
                val result = gson.fromJson(bodyStr, PrHttpRequest.ResponseResult::class.java)
                if ("success" == result.returnValue) {
                    val winResult = gson.fromJson(bodyStr, NLottoInfo.WinResult::class.java)
                    handler.post {
                        callback.onResponse(PrHttpRequest.API.GET_NLOTTO_NUMBER, winResult)
                    }
                } else {
                    handler.post {
                        callback.onFailure(PrHttpRequest.API.GET_NLOTTO_NUMBER, IOException("나눔로또 정보를 가져오지 못했습니다."))
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    callback.onFailure(PrHttpRequest.API.GET_NLOTTO_NUMBER, e)
                }
            }
        })
    }

    fun getPLottoNumber(round: Int, callback: PrHttpRequest.ResponseCallback) {
        val urlStr = PrHttpRequest.API_URL + "?method=get520Number&drwNo=" + if (0 < round) round else ""
        val request = Request.Builder().url(urlStr).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body()?.string()
                val gson = Gson()
                val winResultMap = gson.fromJson(bodyStr, Map::class.java)
                val winResultStr = gson.toJson(winResultMap["rows"])
                val winResultArray = gson.fromJson(winResultStr, Array<PLottoInfo.WinResult>::class.java)
                handler.post {
                    callback.onResponse(PrHttpRequest.API.GET_PLOTTO_NUMBER, winResultArray)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    callback.onFailure(PrHttpRequest.API.GET_PLOTTO_NUMBER, e)
                }
            }
        })
    }
}
