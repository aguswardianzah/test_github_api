package com.agusw.test_github_api.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agusw.test_github_api.api.ApiService
import com.agusw.test_github_api.models.Repos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {

    val errorString by lazy { MutableLiveData("") }
    val loading by lazy { MutableLiveData(false) }
    val refreshing by lazy { MutableLiveData(false) }
    val listData by lazy { MutableLiveData<List<Repos>>() }

    private var fetchJob: Job? = null

    fun getRepo(query: String, type: String = "search") {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch { fetchRepo(query, type) }
    }

    private suspend fun fetchRepo(query: String, type: String) {
        if (type == "search")
            loading.postValue(true)
        else
            refreshing.postValue(true)

        try {
            listData.postValue(api.searchRepo(query).items)
        } catch (e: Exception) {
            Timber.e(e)
            if (e !is CancellationException)
                errorString.postValue(e.message)
        } finally {
            if (type == "search")
                loading.postValue(false)
            else
                refreshing.postValue(false)
        }
    }
}