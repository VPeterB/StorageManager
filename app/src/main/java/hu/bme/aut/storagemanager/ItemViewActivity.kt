package hu.bme.aut.storagemanager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onStorageItemEdited(item: StorageItem, oldCategory: String) {
        TODO("Not yet implemented")
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

    override fun onItemChanged(item: StorageItem, index: Int) {
        thread {
            database.storageItemDao().update(item)
            Log.d("MainActivity", "StorageItem update was successful")
        }
    }

    override fun onItemDelete(item: StorageItem) {
        thread {
            database.storageItemDao().deleteItem(item)
            Log.d("MainActivity", "StorageItem delete was successful")
        }
    }
}