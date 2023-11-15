package com.zilaneleftoz.sportwavestore.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zilaneleftoz.sportwavestore.data.model.response.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductRoomDB: RoomDatabase() {
    abstract fun productDao():ProductDao
}