package com.prangbi.android.lottobox.base

/**
 * Created by Prangbi on 2017. 7. 15..
 */
class Definition {
    companion object {
        // Server Info
        const val SERVER_URL = "http://m.nlotto.co.kr"
        const val TERMS_URL = "http://bgm1112.cafe24.com/logobox/te.txt"
        const val NLOTTO_START_DATE = "2002-12-07 21:00" // 20시 40분 추첨 시작
        const val PLOTTO_START_DATE = "2011-07-06 20:00" // 19시 40분 추첨 시작
        // Database
        const val DB_NAME = "lottopop.db"
        const val DB_VERSION = 1

        const val COUNT_PER_PAGE = 10
    }
}
