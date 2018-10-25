package com.prangbi.android.lottobox.helper.database

import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Prangbi on 2017. 8. 19..
 * 나눔로또 DB
 */
interface INLottoDB {
    fun insertWinResult(drwNo: Int, jsonString: String): Long
    fun insertMyLotto(drwNo: Int, jsonString: String): Long
    fun selectWinResults(drwNo: Int, count: Int): List<Map<String, Any>>
    fun selectLatestWinResult(): Map<String, Any>?
    fun selectMyLottos(drwNo: Int, count: Int): List<Map<String, Any>>
    fun deleteMyLotto(drwNo: Int): Int
}

class NLottoDB constructor(val readableDB: SQLiteDatabase, val writableDB: SQLiteDatabase): INLottoDB {
    companion object {
        val TABLE_NLOTTO = "nLotto"
        val TABLE_MY_NLOTTO = "myNLotto"
    }

    override fun insertWinResult(drwNo: Int, jsonString: String): Long {
        var count = 0L

        val contentValues = ContentValues()
        contentValues.put("drwNo", drwNo)
        contentValues.put("jsonString", jsonString)

        try {
            count = writableDB.insertOrThrow(TABLE_NLOTTO, null, contentValues)
        } catch (e: SQLException) {
        }

        return count
    }

    override fun insertMyLotto(drwNo: Int, jsonString: String): Long {
        var count = 0L

        val contentValues = ContentValues()
        contentValues.put("drwNo", drwNo)
        contentValues.put("jsonString", jsonString)

        try {
            count = writableDB.insertOrThrow(TABLE_MY_NLOTTO, null, contentValues)
        } catch (e: SQLException) {
        }

        return count
    }

    override fun selectWinResults(drwNo: Int, count: Int): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()

        val last = drwNo
        var first = last - if (0 < count - 1) count - 1 else 0
        if (0 > first) {
            first = 0
        }
        val query = "SELECT * FROM " + TABLE_NLOTTO +
                " WHERE drwNo BETWEEN " + first + " AND " + last +
                " ORDER BY drwNo DESC;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val resDrwNo = result.getInt(1)
                val resJsonString = result.getString(2)

                val map = mutableMapOf<String, Any>()
                map.put("drwNo", resDrwNo)
                map.put("jsonString", resJsonString)

                list.add(map)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    override fun selectLatestWinResult(): Map<String, Any>? {
        var winResultMap: Map<String, Any>? = null
        val query = "SELECT * FROM " + TABLE_NLOTTO +
                " ORDER BY drwNo DESC LIMIT 1;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            val resDrwNo = result.getInt(1)
            val resJsonString = result.getString(2)

            val map = mutableMapOf<String, Any>()
            map.put("drwNo", resDrwNo)
            map.put("jsonString", resJsonString)
            winResultMap = map
        }
        result.close()
        return winResultMap
    }

    override fun selectMyLottos(drwNo: Int, count: Int): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()

        val last = drwNo
        var first = last - if (0 < count - 1) count - 1 else 0
        if (0 > first) {
            first = 0
        }
        val query = "SELECT * FROM " + TABLE_NLOTTO +
                " WHERE drwNo BETWEEN " + first + " AND " + last +
                " ORDER BY drwNo DESC;"
        val result = readableDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val resDrwNo = result.getInt(1)
                val resJsonString = result.getString(2)

                val map = mutableMapOf<String, Any>()
                map.put("drwNo", resDrwNo)
                map.put("jsonString", resJsonString)

                list.add(map)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    override fun deleteMyLotto(drwNo: Int): Int {
        val count = writableDB.delete(TABLE_MY_NLOTTO, "drwNo=" + drwNo, null)
        return count
    }
}
