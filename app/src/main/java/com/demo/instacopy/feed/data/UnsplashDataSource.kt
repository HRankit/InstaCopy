package com.demo.instacopy.feed.data

/*
* Created by hrank8t on 03-06-2020 - 16:08:39.
*/




import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.demo.instacopy.feed.data.State.DONE
import com.demo.instacopy.feed.data.State.ERROR
import com.demo.instacopy.feed.models.Photos
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class UnsplashDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Photos>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photos>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPhotos(1, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(DONE)
                        callback.onResult(
                            response,
                            null,
                            2
                        )
                    },
                    {
                        updateState(ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photos>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPhotos(params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(DONE)
                        callback.onResult(
                            response,
                            params.key + 1
                        )
                    },
                    {
                        updateState(ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photos>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}