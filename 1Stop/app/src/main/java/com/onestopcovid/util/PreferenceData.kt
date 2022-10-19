package com.onestopcovid.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.onestopcovid.OneStopApplication

class PreferenceData(context: Context) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or null.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     */
    fun getValueFromKey(key: String?): String? {
        return preferences.getString(key, "")
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or -1.  Throws
     * ClassCastException if there is a preference with this name that is not
     * an int.
     */
    fun getValueIntFromKey(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or -1.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a long.
     */
    fun getValueLongFromKey(key: String?): Long {
        return preferences.getLong(key, -1)
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     */
    fun getValueBooleanFromKey(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     * set String value
     */
    fun setValue(key: String?, value: String?): PreferenceData {
        if (value == null) preferences.edit().remove(key).apply() else preferences.edit().putString(
            key,
            value
        ).apply()
        return this
    }

    /**
     * set int value
     */
    fun setValueInt(key: String?, value: Int): PreferenceData {
        preferences.edit().putInt(key, value).apply()
        return this
    }

    /**
     * set int value
     */
    fun setValueLong(key: String?, value: Long): PreferenceData {
        preferences.edit().putLong(key, value).apply()
        return this
    }

    /**
     * set boolean value
     */
    fun setValueBoolean(key: String?, value: Boolean): PreferenceData {
        preferences.edit().putBoolean(key, value).apply()
        return this
    }

    fun logoutClearData(key: String) {
        preferences.edit().remove(key).apply()
    }

    companion object {
        /**
         * Get IMEI Number
         */
        fun getImeiNumber(): String? {
            return OneStopApplication.instance.preferenceData?.getValueFromKey(PREF_IMEI)
        }

        /**
         * Is User Logged In
         */
        fun isUserLoggedIn(): Boolean {
            return OneStopApplication.instance.preferenceData?.getValueBooleanFromKey(PREF_IS_USER_LOGGED_IN)!!
        }

    }
}