package com.example.apcs_practice.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HistoryDBHelper(
    context: Context, name: String = database,
    factory: SQLiteDatabase.CursorFactory? = null, version: Int = v
) :

    SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        private const val database = "histories.db"
        private const val v = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE histories (id text PRIMARY KEY,date text NOT NULL,session integer NOT NULL, myAnswer text NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS histories")
        onCreate(db)
    }
}