package hu.bme.aut.storagemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.storagemanager.adapter.ItemAdapter
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemsDatabase
import hu.bme.aut.storagemanager.databinding.ActivityItemViewBinding
import hu.bme.aut.storagemanager.fragments.StorageItemDialogFragment
import kotlin.concurrent.thread

class ItemViewActivity : AppCompatActivity(), ItemAdapter.StorageItemClickListener, StorageItemDialogFragment.StorageItemDialogListener {
    companion object{
        const val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        const val KEY_ITEM = "KEY_ITEM"
    }
    private lateinit var binding: ActivityItemViewBinding

    private lateinit var database: StorageItemsDatabase
    private lateinit var adapter: ItemAdapter
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = intent.getStringExtra(MainActivity.KEY_CATEGORY).toString()

        binding = ActivityItemViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = StorageItemsDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener{
            StorageItemDialogFragment().show(
                supportFragmentManager,
                StorageItemDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    override fun onStorageItemCreated(newItem: StorageItem) {
        thread {
            database.storageItemDao().insert(newItem)
            if(newItem.category == category){
                runOnUiThread {
                    adapter.addItem(newItem)
                }
            }
        }
    }

    override fun onStorageItemEdited(item: StorageItem) {
        thread {
            database.storageItemDao().update(item)
            if(item.category == category){
                runOnUiThread {
                    adapter.editItem(item)
                }
            }else{
                runOnUiThread {
                    adapter.deleteEditedItem()
                }
            }
            Log.d("MainActivity", "StorageItem edit was successful")
        }
    }

    private fun initRecyclerView() {
        adapter = ItemAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.storageItemDao().getItemsByCategories(category)
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemDelete(item: StorageItem) {
        thread {
            database.storageItemDao().deleteItem(item)
            Log.d("ItemViewActivity", "StorageItem delete was successful")
        }
    }

    override fun onItemClicked(item: StorageItem) {
        val intentItem = Intent()

        intentItem.setClass(this, ItemActivity::class.java)
        intentItem.putExtra(KEY_ITEM, item)
        startActivity(intentItem)
    }

    fun showStorageItemDialog(itemToEdit: StorageItem){
        val editItemDialog = StorageItemDialogFragment()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle
        editItemDialog.show(supportFragmentManager, StorageItemDialogFragment.TAG_EDIT)
    }
}