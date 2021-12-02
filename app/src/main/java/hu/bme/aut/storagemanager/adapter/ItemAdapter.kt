package hu.bme.aut.storagemanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.ItemStorageBinding

class ItemAdapter (private val listener: StorageItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.StorageViewHolder>() {

    private val items = mutableListOf<StorageItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StorageViewHolder(
        ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        val storageItem = items[position]

        holder.binding.tvName.text = storageItem.name
        holder.binding.tvDescription.text = storageItem.description
        holder.binding.tvCategory.text = storageItem.category
        holder.binding.ibEdit.setOnClickListener{
            edit(storageItem)
            listener.onItemChanged(storageItem, position)
        }
        holder.binding.ibRemove.setOnClickListener{
            delete(storageItem)
            listener.onItemDelete(storageItem)
        }
    }

    fun addItem(item: StorageItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(storageItems: List<StorageItem>) {
        items.clear()
        items.addAll(storageItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(item: StorageItem){
        items.remove(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun edit(oldItem: StorageItem){
        val index = items.indexOf(oldItem)
        items.remove(oldItem)
        notifyItemInserted(index)
    }

    override fun getItemCount(): Int = items.size

    interface StorageItemClickListener {
        fun onItemChanged(item: StorageItem, index: Int)
        fun onItemDelete(item: StorageItem)
    }

    inner class StorageViewHolder(val binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root)
}