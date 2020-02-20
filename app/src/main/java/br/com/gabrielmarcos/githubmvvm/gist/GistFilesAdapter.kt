package br.com.gabrielmarcos.githubmvvm.gist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.gabrielmarcos.githubmvvm.R
import br.com.gabrielmarcos.githubmvvm.extensions.DiffUtilCallback
import br.com.gabrielmarcos.githubmvvm.extensions.showIf
import kotlinx.android.synthetic.main.gist_file_type_item.view.*
import kotlin.properties.Delegates

class GistFilesAdapter : RecyclerView.Adapter<GistFilesAdapter.ViewHolder>() {

    var items: List<String> by Delegates.observable(emptyList()) { _, old, new ->
        val callback = DiffUtilCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        notifyItemChanged(old.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.gist_file_type_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val fileTitle: TextView = view.gistFilesTypeTitle

        fun bind(item: String) { fileTitle.text = item }
    }
}