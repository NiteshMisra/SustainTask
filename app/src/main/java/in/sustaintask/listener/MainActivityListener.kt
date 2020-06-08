package `in`.sustaintask.listener

interface MainActivityListener {

    fun refresh()

    fun progress(message: String)

    fun success()

    fun failed(message : String)

}