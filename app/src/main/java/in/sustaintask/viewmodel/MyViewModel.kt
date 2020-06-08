package `in`.sustaintask.viewmodel

import `in`.sustaintask.listener.LoginListener
import `in`.sustaintask.listener.MainActivityListener
import `in`.sustaintask.model.GetTransaction
import `in`.sustaintask.model.Transaction
import `in`.sustaintask.repository.MyRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MyViewModel(
    private var myRepository: MyRepository
) : ViewModel() {

    fun register(email : String,password : String,userName : String,loginListener: LoginListener){
        myRepository.register(email,password,userName,loginListener)
    }

    fun createTrans(transaction: Transaction,mainActivityListener: MainActivityListener){
        myRepository.createTrans(transaction,mainActivityListener)
    }

    fun getTransList(mainActivityListener: MainActivityListener) : LiveData<ArrayList<GetTransaction>> {
        return myRepository.getTransList(mainActivityListener)
    }

    fun deleteTransaction(documentId : String,mainActivityListener: MainActivityListener){
        myRepository.deleteTransaction(documentId,mainActivityListener)
    }

    fun updateTransaction(documentId: String,amount : String,mainActivityListener: MainActivityListener){
        myRepository.updateTransaction(documentId,amount,mainActivityListener)
    }

}