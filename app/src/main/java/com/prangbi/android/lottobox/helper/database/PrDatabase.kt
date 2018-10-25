package com.prangbi.android.lottobox.helper.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.prangbi.android.lottobox.base.Definition

/**
 * Created by Prangbi on 2017. 1. 5..
 */
class PrDatabase private constructor(context: Context): SQLiteOpenHelper(context, Definition.DB_NAME, null, Definition.DB_VERSION) {
    // Variable
    var nLottoDB: NLottoDB // 나눔로또
    var pLottoDB: PLottoDB // 연금복권520

    // Companion
    companion object {
        // Instance
        private var prDatabase: PrDatabase? = null

        fun getInstance(context: Context): PrDatabase {
            if (null == prDatabase) {
                synchronized(PrDatabase::javaClass) {
                    if (null == prDatabase) {
                        prDatabase = PrDatabase(context.applicationContext)
                    }
                }
            }
            return prDatabase!!
        }
    }

    // Lifecycle
    init {
        nLottoDB = NLottoDB(readableDatabase, writableDatabase)
        pLottoDB = PLottoDB(readableDatabase, writableDatabase)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 나눔로또
        val nLottoTableName = NLottoDB.TABLE_NLOTTO
        var query = "CREATE TABLE IF NOT EXISTS $nLottoTableName (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                ", drwNo INTEGER UNIQUE NOT NULL" +
                ", jsonString TEXT" +
                ");"
        db.execSQL(query)

        val myNLottoTableName = NLottoDB.TABLE_MY_NLOTTO
        query = "CREATE TABLE IF NOT EXISTS $myNLottoTableName (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                ", drwNo INTEGER UNIQUE NOT NULL" +
                ", jsonString TEXT" +
                ");"
        db.execSQL(query)

        // 연금복권520
        val pLottoTableName = PLottoDB.TABLE_PLOTTO
        query = "CREATE TABLE IF NOT EXISTS $pLottoTableName (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                ", round INTEGER UNIQUE NOT NULL" +
                ", jsonString TEXT" +
                ");"
        db.execSQL(query)

        val myPLottoTableName = PLottoDB.TABLE_MY_PLOTTO
        query = "CREATE TABLE IF NOT EXISTS $myPLottoTableName (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                ", round INTEGER UNIQUE NOT NULL" +
                ", jsonString TEXT" +
                ");"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        // 나눔로또
        val nLottoTableName = NLottoDB.TABLE_NLOTTO
        var query = "DROP TABLE IF EXISTS $nLottoTableName;"
        db.execSQL(query)

        val myNLottoTableName = NLottoDB.TABLE_MY_NLOTTO
        query = "DROP TABLE IF EXISTS $myNLottoTableName;"
        db.execSQL(query)

        // 연금복권520
        val pLottoTableName = PLottoDB.TABLE_PLOTTO
        query = "DROP TABLE IF EXISTS $pLottoTableName;"
        db.execSQL(query)

        val myPLottoTableName = PLottoDB.TABLE_MY_PLOTTO
        query = "DROP TABLE IF EXISTS $myPLottoTableName;"
        db.execSQL(query)

        onCreate(db)
    }
}
