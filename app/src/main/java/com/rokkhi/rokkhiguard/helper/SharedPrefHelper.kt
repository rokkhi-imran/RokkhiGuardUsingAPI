package com.rokkhi.rokkhiguard.helper

import android.app.Application
import android.content.Context
import com.rokkhi.rokkhiguard.Model.api.AllFlatsModelClass

class SharedPrefHelper(application: Context) {
    private val sharedPreferences = application.getSharedPreferences("live.qtec.mpos",0)
    fun putInt(key:String,value:Int){
        val editor = sharedPreferences.edit()
        editor.putInt(key,value)
        editor.apply()
    }

    fun putFloat(key:String,value:Float){
        val editor = sharedPreferences.edit()
        editor.putFloat(key,value)
        editor.apply()
    }


    fun putBoolean(key:String,value:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun putString(key:String,value:String){
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun removeData(key: String){
        val editor = sharedPreferences.edit()
        editor.remove(key).commit()
        editor.apply()

    }

    fun getInt(key: String):Int = sharedPreferences.getInt(key,-1)
    fun getFloat(key: String):Float = sharedPreferences.getFloat(key,-1.0f)
    fun getBoolean(key: String):Boolean = sharedPreferences.getBoolean(key,false)
    fun getString(key: String):String = sharedPreferences.getString(key,"")!!

    fun clearAllData(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}