package hu.bme.aut.storagemanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.storagemanager.ItemViewActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.DialogStorageItemBinding

class StorageItemDialogFragment : DialogFragment() {
    interface StorageItemDialogListener {
        fun onStorageItemCreated(newItem: StorageItem)
        fun onStorageItemEdited(item: StorageItem)
    }

    private lateinit var listener: StorageItemDialogListener

    private lateinit var binding: DialogStorageItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? StorageItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewStorageItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = this.arguments
        if(arguments != null && arguments.containsKey(ItemViewActivity.KEY_ITEM_TO_EDIT)){
            binding = DialogStorageItemBinding.inflate(LayoutInflater.from(context))
            val item = arguments.getSerializable(ItemViewActivity.KEY_ITEM_TO_EDIT) as StorageItem
            binding.etName.setText(item.name)
            binding.etCategory.setText(item.category)
            binding.etDescription.setText(item.description)

            return AlertDialog.Builder(requireContext())
                .setTitle(hu.bme.aut.storagemanager.R.string.edit_storage_item)
                .setView(binding.root)
                .setPositiveButton(hu.bme.aut.storagemanager.R.string.button_ok) { dialogInterface, i ->
                    if (isValid()) {
                        listener.onStorageItemEdited(getEditedStorageItem())
                    }
                }
                .setNegativeButton(hu.bme.aut.storagemanager.R.string.button_cancel, null)
                .create()
        }else{
            binding = DialogStorageItemBinding.inflate(LayoutInflater.from(context))

            return AlertDialog.Builder(requireContext())
                .setTitle(hu.bme.aut.storagemanager.R.string.new_storage_item)
                .setView(binding.root)
                .setPositiveButton(hu.bme.aut.storagemanager.R.string.button_ok) { dialogInterface, i ->
                    if (isValid()) {
                        listener.onStorageItemCreated(getStorageItem())
                    }
                }
                .setNegativeButton(hu.bme.aut.storagemanager.R.string.button_cancel, null)
                .create()
        }
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getStorageItem() = StorageItem(
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        category = binding.etCategory.text.toString()
    )

    private fun getEditedStorageItem() = StorageItem(
        id = (arguments?.getSerializable(ItemViewActivity.KEY_ITEM_TO_EDIT) as StorageItem).id,
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        category = binding.etCategory.text.toString()
    )

    companion object {
        const val TAG = "StorageItemDialogFragment"
        const val TAG_EDIT = "StorageItemEditDialogFragment"
    }
}