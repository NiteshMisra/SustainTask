package `in`.sustaintask.extra

import android.app.Activity
import android.content.SharedPreferences


class Preferences {

    companion object {

        private val preferences: SharedPreferences
            get() = Master.applicationContext().getSharedPreferences(Constants.APP_NAME, Activity.MODE_PRIVATE)

        //// Add following lines to store and retrieve String

        fun userName(value: String) {
            val editor = preferences.edit()
            editor.putString("USER_NAME", value)
            editor.apply()
        }

        fun userName(): String? {
            val mSharedPreferences = preferences
            return mSharedPreferences.getString("USER_NAME", "")
        }
    }
}