package com.android.base_project.base.data

import com.android.base_project.R

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 17/10/2021.
*/
data class ItemListModel(
    val startDrawable : Int? = R.drawable.ic_start,
    val title : String? = null,
    val subTitle : String? = null,
    val itemType : ItemListType,
    var isEnable : Boolean = true,
    var isChecked : Boolean = false,
    var isGone : Boolean = false
) {
}

enum class ItemListType{
    LIST_ARROW,
    LIST_ARROW_PREVIEW,
    LIST_TOGGLE_BUTTON,
    LIST_RADIO_BUTTON,
    LIST_RADIO_BUTTON_PREVIEW,
    LIST_CHECKBOX
}