package com.wonrmizo.manager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wonrmizo.model.Note

class PrefsManager(context: Context) {
    private var sharedPreferences: SharedPreferences? =
        context.getSharedPreferences("Note Preference", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val key = "note"

    fun saveNote(notes: ArrayList<Note>) {
        val json = gson.toJson(notes)
        sharedPreferences!!.edit().putString(key, json).apply()
    }

    fun getNote(): ArrayList<Note> {
        val json = sharedPreferences!!.getString(key, arrayListOf<Note>().toString())
        val type = object : TypeToken<ArrayList<Note>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearAllNote() {
        sharedPreferences!!.edit().clear().apply()
    }

}