package com.android.base_project.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 17/10/2021.
*/

abstract class BaseFragment <T : ViewDataBinding, VM : BaseViewModel> : Fragment(){
    protected val TAG by lazy { this::class.java.name }

    private var _binding : T? = null
    protected val binding get() = _binding!!

    abstract val viewModel : VM

    @get:LayoutRes
    abstract val layoutId : Int

    private  var jopEventReceiver : Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,layoutId,container,false)
        return _binding!!.apply { lifecycleOwner = viewLifecycleOwner }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jopEventReceiver = lifecycleScope.launch {
            viewModel.eventReceiver.collectLatest {
                when(it){
                    is AppEvent.OnNavigation -> navigateToDestination(it.destination,it.bundle)
                    AppEvent.OnCloseApp -> activity?.finish()
                    AppEvent.OnBackScreen -> onBackFragment()
                    is AppEvent.OnShowToast -> showToast(it.content,it.type)
                }
            }
        }
    }

    private fun showToast(content: String, type: Int) {
        Toast.makeText(context,content,type).show()
    }

    private fun onBackFragment() {

    }

    private fun navigateToDestination(destination: Int, bundle: Bundle?) {

    }

    override fun onDestroy() {
        jopEventReceiver?.cancel()
        _binding = null
        super.onDestroy()
    }
}