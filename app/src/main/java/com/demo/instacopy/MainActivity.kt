package com.demo.instacopy
/*
* Created by hrank8t on 03-06-2020 - 16:08:03.
*/


import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.demo.instacopy.feed.adapter.PhotoListAdapter
import com.demo.instacopy.feed.data.State
import com.demo.instacopy.feed.viewModel.UnsplashPhotoListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var photoListAdapter: PhotoListAdapter
    private lateinit var viewModel: UnsplashPhotoListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @Suppress("ConstantConditionIf")
        if (API_KEY == "REPLACE_YOUR_KEY_HERE") {
            throw Exception("Wrong key. Go to Utils.kt and update with your own unsplash API key. It's free. Here is the link https://unsplash.com/developers")
        }


        //Instead of get/set direct
        val nightModeFlags = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                clearLightStatusBar()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                setLightStatusBar()

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {

            }
        }


        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_bar)
        toolBar.title = "InstaCopy"


        setSupportActionBar(toolBar)


        initViewModel()
    }

    //Function is denoted as fun in kotlin
    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun clearLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = ContextCompat
                .getColor(this, R.color.colorPrimaryDark)

        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(UnsplashPhotoListViewModel::class.java)
        initAdapter()
        initState()


    }

    private fun initAdapter() {
        photoListAdapter = PhotoListAdapter { viewModel.retry() }

        recycler_view.adapter = photoListAdapter
        viewModel.photoList.observe(this, Observer(photoListAdapter::submitList))
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
//                As state can be null. Hence the ? operator
                photoListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}
