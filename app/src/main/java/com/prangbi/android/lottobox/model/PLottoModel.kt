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
class PLottoModel {
    fun getRecommendationGroups(): IntArray {
        // NOTE: This is a FAKE logic for open source, NOT Prangbi's logic.
        // You can create your own recommendation logic.

        val random = Random()
        var groups = IntArray(2, { 0 })
        for (i in 0..(groups.size-1)) {
            var group = 0
            do {
                group = random.nextInt(7) + 1
            } while (true == groups.contains(group))
            groups[i] = group
        }
        groups.sort()
        return groups
    }

    fun getWinResult(context: Context, round: Int, handler: (Array<PLottoInfo.WinResult>?, error: IOException?) -> Unit) {
        val pLottoDB = PrDatabase.getInstance(context).pLottoDB
        val winResultList = pLottoDB.selectWinResults(round, 1)
        if (0 < winResultList.count()) {
            val winResultJsonString = winResultList[0]["jsonString"].toString()
            val winResultArray = Gson().fromJson(winResultJsonString, Array<PLottoInfo.WinResult>::class.java)
            handler(winResultArray, null)
        } else {
            PrHttpRequest().getPLottoNumber(round, object: PrHttpRequest.ResponseCallback {
                override fun onResponse(api: PrHttpRequest.API, obj: Any?) {
                    if (obj is Array<*> && 0 < obj.count() && obj[0] is PLottoInfo.WinResult) {
                        val winResultArray = obj as Array<PLottoInfo.WinResult>
                        insertWinResult(context, winResultArray[0].round!!.toInt(), winResultArray)
                        handler(winResultArray, null)
                    } else {
                        handler(null, IOException("연금복권520 정보를 가져오지 못했습니다."))
                    }
                }

                override fun onFailure(api: PrHttpRequest.API, error: IOException) {
                    handler(null, error)
                }
            })
        }
    }

    fun getLatestWinResult(context: Context, handler: (Array<PLottoInfo.WinResult>?, error: IOException?) -> Unit) {
        val latestRound = Util.latestDrawNumber(Definition.PLOTTO_START_DATE, "yyyy-MM-dd HH:mm")
        val pLottoDB = PrDatabase.getInstance(context).pLottoDB
        val winResultList = pLottoDB.selectWinResults(latestRound, 1)
        if (0 < winResultList.count()) {
            val winResultJsonString = winResultList[0]["jsonString"].toString()
            val winResultArray = Gson().fromJson(winResultJsonString, Array<PLottoInfo.WinResult>::class.java)
            handler(winResultArray, null)
        } else {
            PrHttpRequest().getPLottoNumber(latestRound, object: PrHttpRequest.ResponseCallback {
                override fun onResponse(api: PrHttpRequest.API, obj: Any?) {
                    if (obj is Array<*> && 0 < obj.count() && obj[0] is PLottoInfo.WinResult) {
                        val winResultArray = obj as Array<PLottoInfo.WinResult>
                        insertWinResult(context, winResultArray[0].round!!.toInt(), winResultArray)
                        handler(winResultArray, null)
                    } else {
                        handler(null, IOException("연금복권520 정보를 가져오지 못했습니다."))
                    }
                }

                override fun onFailure(api: PrHttpRequest.API, error: IOException) {
                    val winResultMap = pLottoDB.selectLatestWinResult()
                    val winResultJsonString = if (null != winResultMap) winResultMap["jsonString"].toString() else null
                    if (null != winResultJsonString) {
                        val winResultArray = Gson().fromJson(winResultJsonString, Array<PLottoInfo.WinResult>::class.java)
                        handler(winResultArray, null)
                    } else {
                        handler(null, error)
                    }
                }
            })
        }
    }

    fun insertWinResult(context: Context, round: Int, winResult: Array<PLottoInfo.WinResult>): Long {
        val winResultJsonString = Gson().toJson(winResult)
        return PrDatabase.getInstance(context).pLottoDB.insertWinResult(round, winResultJsonString)
    }
}
