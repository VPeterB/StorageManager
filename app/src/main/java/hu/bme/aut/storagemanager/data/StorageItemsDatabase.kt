package hu.bme.aut.storagemanager.data

import android.content.Context
import androidx.room.*

@Database(entities = [StorageItem::class], version = 2)
abstract class StorageItemsDatabase : RoomDatabase() {
    abstract fun storageItemDao(): StorageItemDao

    companion object {
        fun getDatabase(applicationContext: Context): StorageItemsDatabase {
            return Room.databaseBuilder(
                applicationContext,
                StorageItemsDatabase::class.java,
                "storage-list"
            ).fallbackToDestructiveMigration().build();
        }
    }
}