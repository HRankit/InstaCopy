package com.demo.instacopy.feed.viewModel
/*
* Created by hrank8t on 03-06-2020 - 16:11:33.
*/



import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.instacopy.feed.data.NetworkService
import com.demo.instacopy.feed.data.State
import com.demo.instacopy.feed.data.UnsplashDataSource
import com.demo.instacopy.feed.data.UnsplashDataSourceFactory
import com.demo.instacopy.feed.models.Photos
import io.reactivex.disposables.CompositeDisposable


class UnsplashPhotoListViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var photoList: LiveData<PagedList<Photos>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val unsplashDataSourceFactory: UnsplashDataSourceFactory

    init {
        unsplashDataSourceFactory = UnsplashDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        photoList = LivePagedListBuilder(unsplashDataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap(
        unsplashDataSourceFactory.unsplashLiveData,
        UnsplashDataSource::state
    )

    fun retry() {
//        Safe call if the Live-data is null
        unsplashDataSourceFactory.unsplashLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
//        Safe call if the photo list is null
        return photoList.value?.isEmpty() ?: true
    }

//    Call this function to refresh. This is an example of single line return function
//    fun invalidateDataSource() =
//        unsplashDataSourceFactory.unsplashLiveData.value?.invalidate()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}