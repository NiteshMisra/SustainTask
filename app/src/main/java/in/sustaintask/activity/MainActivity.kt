@file:Suppress("DEPRECATION")

package `in`.sustaintask.activity

import `in`.sustaintask.R
import `in`.sustaintask.adapter.MyAdapter
import `in`.sustaintask.databinding.ActivityMainBinding
import `in`.sustaintask.extra.MyFactory
import `in`.sustaintask.extra.Preferences
import `in`.sustaintask.listener.MainActivityListener
import `in`.sustaintask.model.GetTransaction
import `in`.sustaintask.model.Transaction
import `in`.sustaintask.repository.MyRepository
import `in`.sustaintask.viewmodel.MyViewModel
import android.app.ProgressDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(),MainActivityListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var transactionsList : ArrayList<GetTransaction>
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        mAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        transactionsList = ArrayList()

        val myRepository = MyRepository()
        val myFactory = MyFactory(myRepository)
        myViewModel = ViewModelProviders.of(this,myFactory).get(MyViewModel::class.java)

        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.setHasFixedSize(true)

        binding.hello.text = ("Hello ${Preferences.userName()!!.substringBefore(" ")} !!")

        binding.create.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.create_trans_dialog,null)
            val editText : EditText = view.findViewById(R.id.edt)
            val create : TextView = view.findViewById(R.id.create)
            val cancel : TextView = view.findViewById(R.id.cancel)
            builder.setView(view)
            val dialog = builder.create()
            create.setOnClickListener {
                if (editText.text.toString().isNotEmpty()){
                    val transaction = Transaction(mAuth.currentUser!!.uid,editText.text.toString().trim(), Preferences.userName()!!)
                    myViewModel.createTrans(transaction,this)
                    dialog.dismiss()
                }else{
                    editText.error = ("enter amount")
                    editText.requestFocus()
                }
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        getList()

    }

    private fun getList() {
        myViewModel.getTransList(this).observe(this, Observer { list ->
            if (list.isNotEmpty()){
                transactionsList.clear()
                transactionsList.addAll(list)
                val myAdapter = MyAdapter(transactionsList,this,this)
                binding.rcv.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun refresh() {
        getList()
    }

    override fun progress(message : String) {
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    override fun success() {
        progressDialog.dismiss()
    }

    override fun failed(message : String) {
        progressDialog.dismiss()
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }


}
