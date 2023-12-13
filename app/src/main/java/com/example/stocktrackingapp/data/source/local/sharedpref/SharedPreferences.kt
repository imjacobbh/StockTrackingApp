package com.example.stocktrackingapp.data.source.local.sharedpref

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        const val STOCKS_LIST_KEY = "STOCKS_LIST"
    }

    fun saveStocks(stocksList: ArrayList<String>) {
        val editor = sharedPreferences.edit()
        val jsonList = gson.toJson(stocksList)
        editor.putString(STOCKS_LIST_KEY, jsonList)
        editor.apply()
    }

    fun getStocks(): ArrayList<String> {
        val stocksList = sharedPreferences.getString(STOCKS_LIST_KEY, null)
        return stocksList?.let {
            gson.fromJson(it, List::class.java) as ArrayList<String>
        } ?: arrayListOf()
    }
}