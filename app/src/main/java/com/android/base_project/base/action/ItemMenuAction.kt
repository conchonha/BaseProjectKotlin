package com.android.base_project.base.action

import android.util.Log

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 17/10/2021.
*/
private const val TAG = "ItemMenuAction"

interface ItemMenuAction {
    fun onClickClose(){
        Log.d(TAG, "onClickClose: ")
    }
    
    fun onClicked(itemTitle : String){
        Log.d(TAG, "onClicked: ")
    }

    fun onBackStack(){
        Log.d(TAG, "onBackStack: ")
    }

    fun onChecked(itemTitle: String){
        Log.d(TAG, "onChecked: ")
    }

    fun onNavigate(itemTitle: String){
        Log.d(TAG, "onNavigate: ")
    }
    
    fun onLongClick(itemTitle: String){
        Log.d(TAG, "onLongClick: ")
    }

}