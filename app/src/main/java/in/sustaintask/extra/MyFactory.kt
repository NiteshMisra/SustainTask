package `in`.sustaintask.extra

import `in`.sustaintask.repository.MyRepository
import `in`.sustaintask.viewmodel.MyViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyFactory(
    private var myRepository: MyRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyViewModel(myRepository) as T
    }


}