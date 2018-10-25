package com.prangbi.android.lottobox.helper.database

import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Prangbi on 2017. 8. 19..
 * 연금복권520 DB
 */
interface IPLottoDB {
    fun insertWinResult(round: Int, jsonString: String): Long
    fun insertMyLotto(round: Int, jsonString: String): Long
    fun selectWinResults(round: Int, count: Int): List<Map<String, Any>>
    fun selectLatestWinResult(): Map<String, Any>?
    fun selectMyLottos(round: Int, count: Int): List<Map<String, Any>>
    fun deleteMyLotto(round: Int): Int
}

class PLottoDB constructor(val readableDB: SQLiteDatabase, val writableDB: SQLiteDatabase): IPLottoDB {
    companion object {
        val TABLE_PLOTTO = "pLotto"
        val TABLE_MY_PLOTTO = "myPLotto"
    }

    override fun insertWinResult(round: Int, jsonString: String): Long {
        var count = 0L

        val contentValues = ContentValues()
        contentValues.put("round", round)
        contentValues.put("jsonString", jsonString)

        try {
            count = writableDB.insertOrThrow(TABLE_PLOTTO, null, contentValues)
        } catch (e: SQLException) {
        }

        return count
    }

    override fun insertMyLotto(round: Int, jsonString: String): Long {
        var count = 0L

        val contentValues = ContentValues()
        contentValues.put("round", round)
        contentValues.put("jsonString", jsonString)

        try {
            count = writableDB.insertOrThrow(TABLE_MY_PLOTTO, null, contentValues)
        } catch (e: SQLException) {
        }

        return count
    }

    override fun selectWinResults(round: Int, count: Int): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()

        val last = round
        var first = last - if (0 < count - 1) count - 1 else 0
        if (0 > first) {
            first = 0
        }
        val query = "SELECT * FROM " + TABLE_PLOTTO +
                " WHERE round BETWEEN " + first + " AND " + last +
                " ORDER BY round DESC;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val resRound = result.getInt(1)
                val resJsonString = result.getString(2)

                val map = mutableMapOf<String, Any>()
                map.put("round", resRound)
                map.put("jsonString", resJsonString)

                list.add(map)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    override fun selectLatestWinResult(): Map<String, Any>? {
        var winResultMap: Map<String, Any>? = null
        val query = "SELECT * FROM " + TABLE_PLOTTO +
                " ORDER BY round DESC LIMIT 1;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            val resRound = result.getInt(1)
            val resJsonString = result.getString(2)

            val map = mutableMapOf<String, Any>()
            map.put("round", resRound)
            map.put("jsonString", resJsonString)
            winResultMap = map
        }
        result.close()
        return winResultMap
    }

    override fun selectMyLottos(round: Int, count: Int): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()

        val last = round
        var first = last - if (0 < count - 1) count - 1 else 0
        if (0 > first) {
            first = 0
        }
        val query = "SELECT * FROM " + TABLE_PLOTTO +
                " WHERE round BETWEEN " + first + " AND " + last +
                " ORDER BY round DESC;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val resRound = result.getInt(1)
                val resJsonString = result.getString(2)

                val map = mutableMapOf<String, Any>()
                map.put("round", resRound)
                map.put("jsonString", resJsonString)

                list.add(map)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    override fun deleteMyLotto(round: Int): Int {
        val count = writableDB.delete(TABLE_MY_PLOTTO, "round=" + round, null)
        return count
    }
}
