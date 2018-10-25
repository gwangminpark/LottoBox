package com.prangbi.android.lottobox.model

import android.content.Context
import com.google.gson.Gson
import com.prangbi.android.lottobox.base.Definition
import com.prangbi.android.lottobox.helper.PrHttpRequest
import com.prangbi.android.lottobox.helper.Util
import com.prangbi.android.lottobox.helper.database.PrDatabase
import java.io.IOException
import java.util.*

/**
 * Created by Prangbi on 2017. 8. 21..
 */
class NLottoModel {
    fun getRecommendationNumbers(): IntArray {
        // NOTE: This is a FAKE logic for open source, NOT Prangbi's logic.
        // You can create your own recommendation logic.

        val random = Random()
        var numbers = IntArray(6, { 0 })
        for (i in 0..(numbers.size-1)) {
            var number = 0
            do {
                number = random.nextInt(45) + 1
            } while (true == numbers.contains(number))
            numbers[i] = number
        }
        numbers.sort()
        return numbers
    }

    fun getWinResult(context: Context, drwNo: Int, handler: (NLottoInfo.WinResult?, error: IOException?) -> Unit) {
        val nLottoDB = PrDatabase.getInstance(context).nLottoDB
        val winResultList = nLottoDB.selectWinResults(drwNo, 1)
        if (0 < winResultList.count()) {
            val winResultJsonString = winResultList[0]["jsonString"].toString()
            val winResult = Gson().fromJson(winResultJsonString, NLottoInfo.WinResult::class.java)
            handler(winResult, null)
        } else {
            PrHttpRequest().getNLottoNumber(drwNo, object: PrHttpRequest.ResponseCallback {
                override fun onResponse(api: PrHttpRequest.API, obj: Any?) {
                    if (obj is NLottoInfo.WinResult) {
                        insertWinResult(context, obj.drwNo, obj)
                        handler(obj, null)
                    } else {
                        handler(null, IOException("나눔로또 정보를 가져오지 못했습니다."))
                    }
                }

                override fun onFailure(api: PrHttpRequest.API, error: IOException) {
                    handler(null, error)
                }
            })
        }
    }

    fun getLatestWinResult(context: Context, handler: (NLottoInfo.WinResult?, error: IOException?) -> Unit) {
        val latestDrawNumber = Util.latestDrawNumber(Definition.NLOTTO_START_DATE, "yyyy-MM-dd HH:mm")
        val nLottoDB = PrDatabase.getInstance(context).nLottoDB
        val winResultList = nLottoDB.selectWinResults(latestDrawNumber, 1)
        if (0 < winResultList.count()) {
            val winResultJsonString = winResultList[0]["jsonString"].toString()
            val winResult = Gson().fromJson(winResultJsonString, NLottoInfo.WinResult::class.java)
            handler(winResult, null)
        } else {
            PrHttpRequest().getNLottoNumber(latestDrawNumber, object: PrHttpRequest.ResponseCallback {
                override fun onResponse(api: PrHttpRequest.API, obj: Any?) {
                    if (obj is NLottoInfo.WinResult) {
                        insertWinResult(context, obj.drwNo, obj)
                        handler(obj, null)
                    } else {
                        handler(null, IOException("나눔로또 정보를 가져오지 못했습니다."))
                    }
                }

                override fun onFailure(api: PrHttpRequest.API, error: IOException) {
                    val winResultMap = nLottoDB.selectLatestWinResult()
                    val winResultJsonString = if (null != winResultMap) winResultMap["jsonString"].toString() else null
                    if (null != winResultJsonString) {
                        val winResult = Gson().fromJson(winResultJsonString, NLottoInfo.WinResult::class.java)
                        handler(winResult, null)
                    } else {
                        handler(null, error)
                    }
                }
            })
        }
    }

    fun insertWinResult(context: Context, drwNo: Int, winResult: NLottoInfo.WinResult): Long {
        val winResultJsonString = Gson().toJson(winResult)
        return PrDatabase.getInstance(context).nLottoDB.insertWinResult(drwNo, winResultJsonString)
    }
}
