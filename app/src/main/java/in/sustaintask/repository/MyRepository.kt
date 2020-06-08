package `in`.sustaintask.repository

import `in`.sustaintask.extra.Constants
import `in`.sustaintask.listener.LoginListener
import `in`.sustaintask.listener.MainActivityListener
import `in`.sustaintask.model.GetTransaction
import `in`.sustaintask.model.Transaction
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MyRepository {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun register(email: String, password: String, userName: String, loginListener: LoginListener) {

        loginListener.loginInProgress()
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["name"] = userName
            db.collection(Constants.userCollection).document(mAuth.currentUser!!.uid).set(
                hashMap,
                SetOptions.merge()
            )
                .addOnSuccessListener {
                    loginListener.loginSuccess()
                }.addOnFailureListener { exception ->
                    Log.e("loginError", exception.message.toString())
                    mAuth.signOut()
                    loginListener.loginFailed()
                }
        }.addOnFailureListener { exception ->
            Log.e("loginError", exception.message.toString())
            loginListener.loginFailed()
        }
    }

    fun createTrans(transaction: Transaction, mainActivityListener: MainActivityListener) {
        mainActivityListener.progress("Creating Transaction...")
        val id = db.collection(Constants.transCollection).document().id
        db.collection(Constants.transCollection).document(id).set(transaction)
            .addOnSuccessListener {
                mainActivityListener.refresh()
            }.addOnFailureListener { exception ->
                Log.e("Transaction", exception.message.toString())
                mainActivityListener.failed("Transaction Failed, Please try again later")
            }
    }

    fun getTransList(mainActivityListener: MainActivityListener): MutableLiveData<ArrayList<GetTransaction>> {
        val data: MutableLiveData<ArrayList<GetTransaction>> = MutableLiveData()
        val transactionList: ArrayList<GetTransaction> = ArrayList()

        db.collection(Constants.transCollection).whereEqualTo("uid", mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapShot ->
                for (item in querySnapShot) {
                    val transaction = item.toObject(Transaction::class.java)
                    val getTransaction = GetTransaction(
                        item.id,
                        transaction.uid,
                        transaction.amount,
                        transaction.userName,
                        transaction.timestamp
                    )
                    transactionList.add(getTransaction)
                }
                mainActivityListener.success()
                data.postValue(transactionList)
            }
            .addOnFailureListener { exception ->
                Log.e("Transaction", exception.message.toString())
                mainActivityListener.failed("Failed to load data from server")
            }

        return data
    }


    fun deleteTransaction(documentId: String, mainActivityListener: MainActivityListener) {
        mainActivityListener.progress("Deleting Transaction...")

        db.collection(Constants.transCollection).document(documentId).delete()
            .addOnSuccessListener {
                mainActivityListener.refresh()
            }.addOnFailureListener { exception ->
                Log.e("Delete Error", exception.message.toString())
                mainActivityListener.failed("Failed to delete, Please try again later")
            }
    }

    fun updateTransaction(
        documentId: String,
        amount: String,
        mainActivityListener: MainActivityListener
    ) {
        mainActivityListener.progress("Updating Data...")

        db.collection(Constants.transCollection).document(documentId).update("amount", amount)
            .addOnSuccessListener {
                mainActivityListener.refresh()
            }.addOnFailureListener { exception ->
                Log.e("Update Error", exception.message.toString())
                mainActivityListener.failed("Failed to update, Please try again later")
            }
    }

}