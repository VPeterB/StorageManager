package hu.bme.aut.storagemanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.storagemanager.MainActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.DialogEditCategoryItemBinding

class EditCategoryDialogFragment : DialogFragment() {
    interface EditStorageItemCategoryDialogListener {
        fun onStorageItemCategoryEdited(item: StorageItem, oldCategory: String)
    }

    private lateinit var listener: EditStorageItemCategoryDialogListener

    private lateinit var binding: DialogEditCategoryItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? EditStorageItemCategoryDialogListener
            ?: throw RuntimeException("Activity must implement the NewStorageItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = this.arguments
        binding = DialogEditCategoryItemBinding.inflate(LayoutInflater.from(context))
        val item = arguments?.getSerializable(MainActivity.KEY_ITEM_CATEGORY_TO_EDIT) as StorageItem
        binding.etCategory.setText(item.category)

        return AlertDialog.Builder(requireContext())
            .setTitle(hu.bme.aut.storagemanager.R.string.edit_storage_item_category)
            .setView(binding.root)
            .setPositiveButton(hu.bme.aut.storagemanager.R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onStorageItemCategoryEdited(getEditedStorageItem(), (arguments.getSerializable(MainActivity.KEY_ITEM_CATEGORY_TO_EDIT) as StorageItem).category)
                }
            }
            .setNegativeButton(hu.bme.aut.storagemanager.R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etCategory.text.isNotEmpty()


    private fun getEditedStorageItem() = StorageItem(
        id = (arguments?.getSerializable(MainActivity.KEY_ITEM_CATEGORY_TO_EDIT) as StorageItem).id,
        name = (arguments?.getSerializable(MainActivity.KEY_ITEM_CATEGORY_TO_EDIT) as StorageItem).name,
        description = (arguments?.getSerializable(MainActivity.KEY_ITEM_CATEGORY_TO_EDIT) as StorageItem).description,
        category = binding.etCategory.text.toString()
    )

    companion object {
        const val TAG_EDIT_C = "EditStorageItemCategoryDialogFragment"
    }
}