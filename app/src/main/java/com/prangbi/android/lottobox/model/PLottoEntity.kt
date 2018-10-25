package com.prangbi.android.lottobox.model

/**
 * Created by Prangbi on 2017. 10. 9..
 */
class PLottoInfo {
    data class WinResult(
            var pensionDrawDate: String? = null,
            var rankClass: String? = null,
            var rank: String? = null,
            var rankNo: String? = null,
            var rankAmt: String? = null,
            var round: String? = null,
            var drawDate: String? = null
    )

    data class MyLotto(
            var rankClass: String? = null,
            var rankNo: String? = null,
            var round: String? = null
    )
}
