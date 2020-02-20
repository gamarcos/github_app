package br.com.gabrielmarcos.githubmvvm.gist

import DividerListDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.gabrielmarcos.githubmvvm.R
import br.com.gabrielmarcos.githubmvvm.extensions.DiffUtilCallback
import br.com.gabrielmarcos.githubmvvm.extensions.hide
import br.com.gabrielmarcos.githubmvvm.extensions.show
import br.com.gabrielmarcos.githubmvvm.extensions.showIf
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.util.AnimateUtils.handleOrientationRotate
import br.com.gabrielmarcos.githubmvvm.util.AnimateUtils.rotateDownLayout
import br.com.gabrielmarcos.githubmvvm.util.bindImageFromUrl
import kotlinx.android.synthetic.main.gist_fragment.*
import kotlinx.android.synthetic.main.gist_item.view.*
import kotlin.properties.Delegates

class GistAdapter(
    private val cardClickListener: (Gist) -> Unit,
    private val favoriteClickListener: (Gist) -> Unit
) : RecyclerView.Adapter<GistAdapter.ViewHolder>() {

    var items: List<Gist> by Delegates.observable(emptyList()) { _, old, new ->
        val callback = DiffUtilCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        notifyItemChanged(old.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.gist_item,
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
        private val starIcon: ImageView = view.gistOwnerImage
        private val gistOwnerText: TextView = view.gistOwnerName
        private val gistStartIcon: ImageView = view.gistStarredIcon
        private val gistOwnerFilesList: RecyclerView = view.gistOwnerFilesList
        private val gistOpenOwnerFiles: ImageView = view.gistOpenOwnerFiles
        private val fileContent: View = view.gistFileContent

        fun bind(item: Gist) {
            setUpTypesRecycler(item)
            setUpView(item)
            setUpClick(item)
            resetAnimation()
        }

        private fun setUpView(item: Gist) {
            bindImageFromUrl(starIcon, item.owner.photo)
            gistOwnerText.text = item.owner.login
            isStarred(item)
            view.setOnClickListener { cardClickListener(item) }
        }

        private fun setUpClick(item: Gist) {
            gistStartIcon.setOnClickListener {
                item.starred = !item.starred
                isStarred(item)
                favoriteClickListener(item)
            }

            gistOpenOwnerFiles.setOnClickListener { fileTypesSetUp() }
            fileContent.setOnClickListener { fileTypesSetUp() }
        }

        private fun resetAnimation() {
            gistOwnerFilesList.hide()
            rotateDownLayout(gistOpenOwnerFiles)
        }

        private fun fileTypesSetUp() {
            handleOrientationRotate(gistOpenOwnerFiles)
            handleVisibility()
        }

        private fun handleVisibility() {
            takeIf { !gistOwnerFilesList.isVisible }?.run {
                gistOwnerFilesList.show()
            } ?: gistOwnerFilesList.hide()
        }

        private fun isStarred(item: Gist) {
            takeIf { item.starred }?.run {
                gistStartIcon.setImageResource(R.drawable.ic_completed_star)
            } ?: gistStartIcon.setImageResource(R.drawable.ic_line_start)
        }

        private fun setUpTypesRecycler(item: Gist) {
            val fileAdapter = GistFilesAdapter()
            fileAdapter.items = item.files?.map {
                it.value.filename ?: ""
            } ?: emptyList()

            gistOwnerFilesList.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    DividerListDecoration(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.shape_partition_list
                        )
                    )
                )
                adapter = fileAdapter
            }
        }
    }
}