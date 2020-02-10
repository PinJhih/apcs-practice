package com.example.apcs_practice.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DetailedDBHelper(
    context: Context, name: String = database,
    factory: SQLiteDatabase.CursorFactory? = null, version: Int = v
) :

    SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        private const val database = "detailed.db"
        private const val v = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE detaileds (id text PRIMARY KEY,content text NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS detaileds")
        onCreate(db)
    }
}