package com.octocat.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.octocat.R
import coil.transform.CircleCropTransformation
import com.octocat.databinding.AdapterItemRepoBinding
import com.octocat.databinding.AdapterItemUserBinding
import com.octocat.domain.models.HomeData
import com.octocat.domain.models.RepoInfo
import com.octocat.domain.models.UserInfo

private const val USER_COUNT = 1

private const val VIEW_TYPE_USER = 20
private const val VIEW_TYPE_REPO = 30

@SuppressLint("NotifyDataSetChanged")
class HomeAdapter(
    private val context: Context,
    private val onRepoClick: (AdapterItemRepoBinding, RepoInfo) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var data: HomeData? = null

    fun setData(data: HomeData?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data?.repos?.size?.let { USER_COUNT + it } ?: 0

    override fun getItemViewType(position: Int): Int {
        return when (position == 0) {
            true -> VIEW_TYPE_USER
            else -> VIEW_TYPE_REPO
        }
    }

    override fun getItemId(position: Int): Long {
        val data = this.data ?: return RecyclerView.NO_ID
        return when (position == 0) {
            true -> Long.MAX_VALUE
            else -> {
                val repoPosition = position - USER_COUNT
                val repo = data.repos[repoPosition]
                return repo.repoId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return when (viewType == VIEW_TYPE_USER) {
            true -> UserViewHolder.create(context, parent)
            else -> RepoViewHolder.create(context, parent, onRepoClick)
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data = this.data ?: return
        if (holder is UserViewHolder) {
            holder.bind(data.user)
        } else if (holder is RepoViewHolder) {
            val repoPosition = position - USER_COUNT
            val repo = data.repos[repoPosition]
            holder.bind(repo)
        }
    }

}

abstract class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private class UserViewHolder(
    private val binding: AdapterItemUserBinding,
) : HomeViewHolder(binding.root) {

    companion object {
        fun create(context: Context, parent: ViewGroup) = UserViewHolder(
            AdapterItemUserBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    fun bind(user: UserInfo) {
        binding.avatar.load(user.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        binding.userName.text = user.name
    }
}

private class RepoViewHolder(
    private val binding: AdapterItemRepoBinding,
    private val onClick: (AdapterItemRepoBinding, RepoInfo) -> Unit
) : HomeViewHolder(binding.root) {

    companion object {
        fun create(
            context: Context,
            parent: ViewGroup,
            onClick: (AdapterItemRepoBinding, RepoInfo) -> Unit
        ) = RepoViewHolder(
            AdapterItemRepoBinding.inflate(LayoutInflater.from(context), parent, false),
            onClick
        )
    }

    fun bind(repo: RepoInfo) {
        itemView.transitionName = itemView.context.getString(R.string.details_transition, repo.repoId)
        binding.repoTitle.text = repo.name
        binding.repoDesc.text = repo.description
        itemView.setOnClickListener {
            onClick(binding, repo)
        }
    }
}