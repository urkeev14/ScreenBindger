package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val upcomingViewState: MutableLiveData<UpcomingViewState>
) : ViewModel() {

    fun fetchData() {
        CoroutineScope(IO).launch {
            remoteDataSource.getUpcoming(upcomingViewState)
        }
    }

}