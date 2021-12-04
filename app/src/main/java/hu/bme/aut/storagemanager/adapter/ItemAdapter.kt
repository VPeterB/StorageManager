package hu.bme.aut.storagemanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.storagemanager.ItemViewActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.ItemStorageBinding

class ItemAdapter (private val listener: StorageItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.StorageViewHolder>() {

    private val items = mutableListOf<StorageItem>()
    private var editIndex = -1
    private lateinit var editedItem: StorageItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StorageViewHolder(
        ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        val storageItem = items[position]

        holder.binding.tvText.text = storageItem.name
        holder.itemView.setOnClickListener{
            listener.onItemClicked(storageItem)
        }
        holder.binding.ibEdit.setOnClickListener{
            setEditIndex(position)
            setEditItem(storageItem)
            (holder.itemView.context as ItemViewActivity).showStorageItemDialog(storageItem)
        }
        holder.binding.ibRemove.setOnClickListener{
            delete(storageItem)
            listener.onItemDelete(storageItem)
        }
    }

    private fun setEditItem(item: StorageItem) {
        editedItem = item
    }

    private fun setEditIndex(i: Int) {
        editIndex = i
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

    fun editItem(item: StorageItem){
        val index = editIndex
        items[index] = item
        notifyItemChanged(index)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteEditedItem(){
        items.remove(editedItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface StorageItemClickListener {
        fun onItemDelete(item: StorageItem)
        fun onItemClicked(item: StorageItem)
    }

    inner class StorageViewHolder(val binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root)
}