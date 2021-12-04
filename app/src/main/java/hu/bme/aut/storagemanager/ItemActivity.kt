package hu.bme.aut.storagemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.storagemanager.ItemViewActivity.Companion.KEY_ITEM
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.ActivityItemBinding


class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getSerializableExtra(KEY_ITEM) as StorageItem

        binding = ActivityItemBinding.inflate(layoutInflater)

        binding.tvName.text = item.name
        binding.tvCategory.text = item.category
        binding.tvDescription.text = item.description

        setContentView(binding.root)
    }
}