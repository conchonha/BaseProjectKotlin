package com.android.base_project.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.base_project.base.BaseViewModel
import com.android.base_project.base.action.ItemMenuAction
import com.android.base_project.base.data.ItemListModel
import com.android.base_project.base.data.ItemListType
import com.android.base_project.databinding.ItemListArrowBindingBinding
import com.android.base_project.databinding.ItemListArrowPreviewBindingBinding

/*
    Copyright © 2021 UITS CO.,LTD
    Create by SangTB on 17/10/2021.
*/

class MenuAdapter(private val itemMenuAction: ItemMenuAction) :
    LifecycleAdapter<LifecycleViewHolder>() {
    private val differ = AsyncListDiffer(this, DIFF_UTILS)

    fun submit(dataList: List<ItemListModel>) {
        differ.submitList(dataList)
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].itemType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        return when (viewType) {
            ItemListType.LIST_ARROW.ordinal -> ItemListArrowViewHolder(parent)
            ItemListType.LIST_ARROW_PREVIEW.ordinal -> ItemListArrowViewPreViewHolder(parent)
            else -> ItemListArrowViewHolder(parent)
        }
    }

    override fun getItemCount() = differ.currentList.size

    class ItemListArrowViewHolder(
        parent: ViewGroup,
        private val binding: ItemListArrowBindingBinding = ItemListArrowBindingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : LifeCycleMenuViewHolder<ItemListModel, BaseViewModel>(binding.root) {
        override fun onBind(data: ItemListModel?, viewModel: BaseViewModel) {
            binding.apply {
                this.action = viewModel
                this.data = data
            }
        }
    }

    class ItemListArrowViewPreViewHolder(
        parent: ViewGroup,
        private val binding: ItemListArrowPreviewBindingBinding = ItemListArrowPreviewBindingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : LifeCycleMenuViewHolder<ItemListModel, BaseViewModel>(binding.root) {
        override fun onBind(data: ItemListModel?, viewModel: BaseViewModel) {
            binding.apply {
                this.action = viewModel
                this.data = data
            }
        }
    }

    companion object {
        private val DIFF_UTILS = object : DiffUtil.ItemCallback<ItemListModel>() {
            override fun areItemsTheSame(oldItem: ItemListModel, newItem: ItemListModel) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: ItemListModel,
                newItem: ItemListModel
            ) =
                oldItem.title == newItem.title
                        && oldItem.subTitle == newItem.subTitle
                        && oldItem.isEnable == newItem.isEnable
                        && oldItem.isGone == newItem.isGone
                        && oldItem.isChecked == newItem.isChecked
        }
    }

    override fun onBindViewHolder(holder: LifecycleViewHolder, position: Int) {

    }
}

abstract class LifecycleAdapter<T : LifecycleViewHolder> : RecyclerView.Adapter<T>() {
    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        holder.onCreate()
    }

    override fun onViewDetachedFromWindow(holder: T) {
        super.onViewDetachedFromWindow(holder)
        holder.onDestroy()
    }
}

abstract class LifecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    LifecycleOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun onCreate() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}

abstract class LifeCycleMenuViewHolder<T : Any?, VM : BaseViewModel?>(itemView: View) :
    LifecycleViewHolder(itemView) {
    abstract fun onBind(data: T?, viewModel: VM)
}