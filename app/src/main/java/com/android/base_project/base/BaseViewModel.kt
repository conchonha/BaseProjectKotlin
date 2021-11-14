package com.android.base_project.base

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.base_project.base.action.ItemMenuAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 17/10/2021.
*/
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    ItemMenuAction {
    protected val TAG by lazy { this::class.java.name }
    protected val evenSender = Channel<AppEvent>()
    val eventReceiver = evenSender.receiveAsFlow().conflate()

    override fun onClickClose() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnCloseApp)
        }
    }

    override fun onBackStack() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnBackScreen)
        }
    }

    fun getString(idString: Int) = getApplication<Application>().resources.getString(idString)
}

sealed class AppEvent {
    class OnNavigation(val destination: Int, val bundle: Bundle? = null) : AppEvent()
    object OnCloseApp : AppEvent()
    object OnBackScreen : AppEvent()
    class OnShowToast(val content: String, val type: Int = Toast.LENGTH_SHORT) : AppEvent()
}