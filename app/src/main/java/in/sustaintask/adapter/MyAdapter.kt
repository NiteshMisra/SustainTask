@file:Suppress("DEPRECATION")

package `in`.sustaintask.adapter

import `in`.sustaintask.R
import `in`.sustaintask.extra.MyFactory
import `in`.sustaintask.listener.MainActivityListener
import `in`.sustaintask.model.GetTransaction
import `in`.sustaintask.repository.MyRepository
import `in`.sustaintask.viewmodel.MyViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private var list: ArrayList<GetTransaction>,
    private var context: FragmentActivity,
    private var mainActivityListener: MainActivityListener
) :
    RecyclerView.Adapter<MyAdapter.VH>() {

    private val myRepository = MyRepository()
    private val myFactory = MyFactory(myRepository)
    private val myViewModel = ViewModelProviders.of(context, myFactory).get(MyViewModel::class.java)

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.userName)
        val amount: TextView = view.findViewById(R.id.amount)
        val date: TextView = view.findViewById(R.id.date)
        val layout: RelativeLayout = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_transaction, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentItem = list[position]
        holder.name.text = currentItem.userName
        holder.amount.text = ("\u20b9 " + currentItem.amount)
        holder.date.text = currentItem.timestamp.toString()

        holder.layout.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val gender = arrayOf("Edit", "Delete")
            builder.setItems(gender)
            { dialog, which ->
                when (which) {
                    0 -> {
                        dialog.dismiss()
                        val builder2 = AlertDialog.Builder(context)
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.create_trans_dialog, null)
                        val title: TextView = view.findViewById(R.id.title)
                        title.text = ("Edit Transaction")
                        val editText: EditText = view.findViewById(R.id.edt)
                        editText.setText(currentItem.amount)
                        val create: TextView = view.findViewById(R.id.create)
                        create.text = ("Update")
                        val cancel: TextView = view.findViewById(R.id.cancel)
                        builder2.setView(view)
                        val dialog2 = builder2.create()
                        create.setOnClickListener {
                            if (editText.text.toString().isNotEmpty()) {
                                dialog2.dismiss()
                                myViewModel.updateTransaction(
                                    currentItem.documentId,
                                    editText.text.toString().trim(),
                                    mainActivityListener
                                )
                                dialog2.dismiss()
                            } else {
                                editText.error = ("enter amount")
                                editText.requestFocus()
                            }
                        }
                        cancel.setOnClickListener {
                            dialog2.dismiss()
                        }
                        dialog2.show()
                    }

                    1 -> {
                        dialog.dismiss()
                        val builder2 = AlertDialog.Builder(context)
                        builder2.setTitle("Delete Transaction")
                        builder2.setMessage("Are you sure to delete Transaction of \u20b9 ${currentItem.amount}")
                        builder2.setPositiveButton(
                            "Delete"
                        ) { dialog2, _ ->
                            dialog2.dismiss()
                            myViewModel.deleteTransaction(
                                currentItem.documentId,
                                mainActivityListener
                            )
                        }
                        builder2.setNegativeButton("Cancel") { dialog2, _ ->
                            dialog2.dismiss()
                        }
                        val dialog2 = builder2.create()
                        dialog2.show()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

}