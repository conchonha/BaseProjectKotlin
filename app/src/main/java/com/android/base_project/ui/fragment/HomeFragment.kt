package com.android.base_project.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base_project.R
import com.android.base_project.base.BaseFragment
import com.android.base_project.databinding.FragmentHomeBinding
import com.android.base_project.ui.viewmodel.HomeViewModel

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 10/11/2021.
*/
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            action = viewModel
            title = "Sang Thai"
        }
    }
}