package hu.bme.aut.storagemanager.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.storagemanager.MainActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.CategoryStorageBinding

class StorageAdapter(private val listener: StorageItemClickListener) :
    RecyclerView.Adapter<StorageAdapter.StorageViewHolder>() {

    private val items = mutableListOf<StorageItem>()
    private val allItems = mutableListOf<StorageItem>()
    private val categories = mutableListOf<String>()
    private var editIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StorageViewHolder(
        CategoryStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        val storageItem = items[position]
        categories.add(storageItem.category)

        holder.binding.tvCategory.text = storageItem.category
        holder.itemView.setOnClickListener{
            listener.onItemClicked(storageItem)
        }
        holder.binding.ibEdit.setOnClickListener{
            setEditIndex(position)
            (holder.itemView.context as MainActivity).showEditItemCategoryDialog(storageItem)
        }
        holder.binding.ibRemove.setOnClickListener {
            delete(storageItem)
            listener.onItemDelete(storageItem)
        }
    }

    fun addItem(item: StorageItem) {
        if(!categories.contains(item.category)){
            items.add(item)
            notifyItemInserted(items.size - 1)
        }
        allItems.add(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(storageItems: List<StorageItem>, allStorageItems: List<StorageItem>) {
        items.clear()
        allItems.clear()
        categories.clear()
        items.addAll(storageItems)
        allItems.addAll(allStorageItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(item: StorageItem){
        val cat = item.category
        categories.remove(cat)
        items.remove(item)
        for(i in allItems){
            if(i.category == cat)
                allItems.remove(i)
        }
        notifyDataSetChanged()
    }

    fun editItem(item: StorageItem, oldCategory: String){
        categories.remove(oldCategory)
        categories.add(item.category)
        for(i in allItems){
            if(i.category == oldCategory)
                i.category = item.category
        }
        val index = editIndex
        items[index] = item
        notifyItemChanged(index)
    }

    private fun setEditIndex(index: Int){
        editIndex = index
    }

    override fun getItemCount(): Int = items.size

    interface StorageItemClickListener {
        fun onItemDelete(item: StorageItem)
        fun onItemClicked(item: StorageItem)
    }

    inner class StorageViewHolder(val binding: CategoryStorageBinding) : RecyclerView.ViewHolder(binding.root)
}
