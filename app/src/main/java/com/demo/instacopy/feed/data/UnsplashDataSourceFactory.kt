package com.demo.instacopy.feed.data
/*
* Created by hrank8t on 03-06-2020 - 16:09:08.
*/






import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.demo.instacopy.feed.models.Photos
import io.reactivex.disposables.CompositeDisposable

class UnsplashDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NetworkService
) : DataSource.Factory<Int, Photos>() {

    val unsplashLiveData = MutableLiveData<UnsplashDataSource>()

    override fun create(): DataSource<Int, Photos> {
        val unsplashDataSource = UnsplashDataSource(networkService, compositeDisposable)
        unsplashLiveData.postValue(unsplashDataSource)
        return unsplashDataSource
    }
}