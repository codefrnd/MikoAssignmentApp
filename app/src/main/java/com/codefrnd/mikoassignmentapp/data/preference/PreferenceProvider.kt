package com.codefrnd.mikoassignmentapp.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val RUN_TIME = "run_time"

class PreferenceProvider(
    context: Context
) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun setRunTime(savedAt: Long) {
        preference.edit().putLong(RUN_TIME, savedAt).apply()
    }

    fun getRunTime(): Long = preference.getLong(RUN_TIME, 0)
}