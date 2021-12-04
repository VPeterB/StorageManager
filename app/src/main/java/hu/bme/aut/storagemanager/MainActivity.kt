package hu.bme.aut.storagemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.storagemanager.adapter.StorageAdapter
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemsDatabase
import hu.bme.aut.storagemanager.databinding.ActivityMainBinding
import hu.bme.aut.storagemanager.fragments.EditCategoryDialogFragment
import hu.bme.aut.storagemanager.fragments.EditCategoryDialogFragment.Companion.TAG_EDIT_C
import hu.bme.aut.storagemanager.fragments.StorageItemDialogFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), StorageAdapter.StorageItemClickListener, StorageItemDialogFragment.StorageItemDialogListener, EditCategoryDialogFragment.EditStorageItemCategoryDialogListener {
    companion object{
        const val KEY_ITEM_CATEGORY_TO_EDIT = "KEY_ITEM_CATEGORY_TO_EDIT"
        const val KEY_CATEGORY = "KEY_CATEGORY"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var database: StorageItemsDatabase
    private lateinit var adapter: StorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onStorageItemEdited(item: StorageItem) {
        //MainActivity does nothing with StorageItem
    }

    private fun initRecyclerView() {
        adapter = StorageAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.storageItemDao().getCategories()
            val allItems = database.storageItemDao().getAll()
            runOnUiThread {
                adapter.update(items, allItems)
            }
        }
    }

    override fun onItemDelete(item: StorageItem) {
        thread {
            database.storageItemDao().deleteItemsByCategory(item.category)
            Log.d("MainActivity", "Category delete was successful")
        }
    }

    override fun onItemClicked(item: StorageItem) {
        val intentItems = Intent()

        intentItems.setClass(this, ItemViewActivity::class.java)
        intentItems.putExtra(KEY_CATEGORY, item.category)
        startActivity(intentItems)
    }

    fun showEditItemCategoryDialog(itemToEdit: StorageItem){
        val editCategoryDialog = EditCategoryDialogFragment()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_CATEGORY_TO_EDIT, itemToEdit)
        editCategoryDialog.arguments = bundle
        editCategoryDialog.show(supportFragmentManager, TAG_EDIT_C)
    }

    override fun onStorageItemCategoryEdited(item: StorageItem, oldCategory: String) {
        thread {
            database.storageItemDao().updateCategories(item.category, oldCategory)
            runOnUiThread {
                adapter.editItem(item, oldCategory)
            }
            Log.d("MainActivity", "Category edit was successful")
        }
    }

    override fun onResume() {
        super.onResume()
        loadItemsInBackground()
    }
}