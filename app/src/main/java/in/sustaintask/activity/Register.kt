@file:Suppress("DEPRECATION")

package `in`.sustaintask.activity

import `in`.sustaintask.R
import `in`.sustaintask.databinding.ActivityRegisterBinding
import `in`.sustaintask.extra.MyFactory
import `in`.sustaintask.extra.Preferences
import `in`.sustaintask.listener.LoginListener
import `in`.sustaintask.repository.MyRepository
import `in`.sustaintask.viewmodel.MyViewModel
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

class Register : AppCompatActivity(), LoginListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        progressDialog = ProgressDialog(this)

        val myRepository = MyRepository()
        val myFactory = MyFactory(myRepository)
        myViewModel = ViewModelProviders.of(this, myFactory).get(MyViewModel::class.java)

        binding.register.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {

        if (binding.email.text.toString().isEmpty() || binding.userName.text.toString()
                .isEmpty() || binding.password.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Empty Data", Toast.LENGTH_SHORT).show()
            return
        }

        myViewModel.register(
            binding.email.text.toString(),
            binding.password.text.toString(),
            binding.userName.text.toString(),
            this
        )
    }

    override fun loginInProgress() {
        progressDialog.setTitle("Please wait...")
        progressDialog.setMessage("Creating User...")
        progressDialog.show()
    }

    override fun loginSuccess() {
        Preferences.userName(binding.userName.text.toString().trim())
        progressDialog.dismiss()
        Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun loginFailed() {
        progressDialog.dismiss()
        Toast.makeText(this,"Registration Failed,Try Again later",Toast.LENGTH_SHORT).show()
    }
}
