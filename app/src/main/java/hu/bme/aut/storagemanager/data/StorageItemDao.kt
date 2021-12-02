package hu.bme.aut.storagemanager.data

import androidx.room.*

@Dao
interface StorageItemDao {
    @Query("SELECT * FROM storageitem")
    fun getAll(): List<StorageItem>

    @Query("SELECT * FROM storageitem GROUP BY Category HAVING COUNT(Name) >= 0")
    fun getCategories(): List<StorageItem>

    @Insert
    fun insert(storageItems: StorageItem): Long

    @Update
    fun update(storageItem: StorageItem)

    @Delete
    fun deleteItem(storageItem: StorageItem)

    @Query("DELETE FROM storageitem WHERE Category = :category")
    fun deleteItemsByCategory(category: String)

    @Query("SELECT * FROM storageitem WHERE Category = :category")
    fun getItemsByCategories(category: String): List<StorageItem>

    @Query("UPDATE storageitem SET Category = :category WHERE Category = :oldCategory")
    fun updateCategories(category: String, oldCategory: String)
}