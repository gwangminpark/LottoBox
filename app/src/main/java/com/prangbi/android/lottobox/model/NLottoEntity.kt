package com.prangbi.android.lottobox.model

/**
 * Created by Prangbi on 2017. 10. 9..
 */
class NLottoInfo {
    data class WinResult(
            var drwNo: Int = 0,
            var drwNoDate: String? = null,
            var totSellamnt: Long = 0,
            var firstWinamnt: Long = 0,
            var firstPrzwnerCo: Int = 0,
            var drwtNo1: Short = 0,
            var drwtNo2: Short = 0,
            var drwtNo3: Short = 0,
            var drwtNo4: Short = 0,
            var drwtNo5: Short = 0,
            var drwtNo6: Short = 0,
            var bnusNo: Short = 0
    )

    // 02 03 08 18 23 32 + 45
    data class MyLotto(
            var id: Int = 0,
            var drwNo: Int = 0,
            var drwtNo1: Short = 0,
            var drwtNo2: Short = 0,
            var drwtNo3: Short = 0,
            var drwtNo4: Short = 0,
            var drwtNo5: Short = 0,
            var drwtNo6: Short = 0,
            var bnusNo: Short = 0
    )
}
