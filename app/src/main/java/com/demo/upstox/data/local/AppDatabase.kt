package com.demo.upstox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.demo.upstox.data.local.dao.HoldingsDao
import com.demo.upstox.data.local.model.HoldingEntity
import java.math.BigDecimal

@Database(
    entities = [HoldingEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holdingsDao(): HoldingsDao
}

object BigDecimalTypeConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }
}
