package com.agusw.test_github_api.pages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agusw.test_github_api.databinding.HomePageBinding
import com.agusw.test_github_api.databinding.RepoItemBinding
import com.agusw.test_github_api.models.Repos
import com.agusw.test_github_api.utils.LinearSpaceDecoration
import com.agusw.test_github_api.viewmodels.HomeViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePage : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by lazy { HomePageBinding.inflate(layoutInflater) }
    private val adapter by lazy { Adapter() }
    private val glide by lazy { Glide.with(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.apply {
            btnSearch.setOnClickListener { getData() }
            swipeRefresh.setOnRefreshListener { getData("refresh") }
            rvRepos.apply {
                layoutManager = LinearLayoutManager(this@HomePage)
                adapter = this@HomePage.adapter
                addItemDecoration(LinearSpaceDecoration())
            }
        }

        viewModel.apply {
            errorString.observe(this@HomePage, Observer(this@HomePage::toast))
            loading.observe(this@HomePage) {
                binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            }
            refreshing.observe(this@HomePage, Observer(binding.swipeRefresh::setRefreshing))
            listData.observe(this@HomePage, Observer(this@HomePage.adapter::submitList))
        }
    }

    private fun getData(type: String = "search") = viewModel.getRepo(binding.inQuery.text.toString(), type)

    private fun toast(msg: String?) {
        if (!msg.isNullOrEmpty()) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private inner class Adapter :
        ListAdapter<Repos, ViewHolder>(object : DiffUtil.ItemCallback<Repos>() {
            override fun areItemsTheSame(oldItem: Repos, newItem: Repos): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repos, newItem: Repos): Boolean =
                oldItem == newItem
        }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent)

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(getItem(position), glide)
    }

    private inner class ViewHolder(
        parent: ViewGroup, val binding: RepoItemBinding = RepoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repos, glide: RequestManager) {
            binding.apply {
                root.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.html_url)))
                }

                glide.load(item.owner?.avatar_url).centerCrop().into(imgRepo)
                title.text = item.full_name
                author.text = item.owner?.login
                desc.text = item.description
            }
        }
    }
}