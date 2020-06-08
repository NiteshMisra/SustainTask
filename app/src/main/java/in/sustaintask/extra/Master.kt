package `in`.sustaintask.extra

import android.app.Application

class Master : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this@Master
    }

    companion object {

        @get:Synchronized
        var instance: Master? = null
            private set

        fun applicationContext() : Master {
            return instance!!.applicationContext as Master
        }
    }
}