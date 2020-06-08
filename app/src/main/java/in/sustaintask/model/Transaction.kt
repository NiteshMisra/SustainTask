package `in`.sustaintask.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Transaction(
    var uid: String,
    var amount: String,
    var userName: String,
    @ServerTimestamp var timestamp: Date? = null
) {
    constructor() : this("", "", "", null)
}