package com.dicoding.eventdicoding.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.data.response.ResponseEvent
import com.dicoding.eventdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModelFinished : ViewModel() {

    private val _listEvents = MutableLiveData<List<DetailData>>()
    val listEvent : LiveData<List<DetailData>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "MainViewModel"
    }
    init{
        findEventVertical()

    }

    private fun findEventVertical() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(active = 0)
        client.enqueue(object : Callback<ResponseEvent> {
            override fun onResponse(
                call: Call<ResponseEvent>,
                response : Response<ResponseEvent>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listEvents.value = response.body()?.data
                }
                else{
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }
            override fun onFailure(error : Call<ResponseEvent>, msg : Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${msg.message}")
            }
        })
    }
}