package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.MealType
import com.example.data.model.StorageLocation

class Converters {
    @TypeConverter
    fun fromStorageLocation(value: StorageLocation?): String? {
        return value?.name
    }

    @TypeConverter
    fun toStorageLocation(value: String?): StorageLocation? {
        return value?.let { enumValueOf<StorageLocation>(it) }
    }

    @TypeConverter
    fun fromMealType(value: MealType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toMealType(value: String?): MealType? {
        return value?.let { enumValueOf<MealType>(it) }
    }
}
