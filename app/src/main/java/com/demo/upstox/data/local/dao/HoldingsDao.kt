package com.demo.upstox.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.demo.upstox.data.local.model.HoldingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingsDao {
    @Query(value = "SELECT * FROM holdings")
    fun getHoldings(): Flow<List<HoldingEntity>>

    @Insert
    suspend fun singleInsertHolding(entity: HoldingEntity)

    @Upsert
    suspend fun upsertHoldings(entities: List<HoldingEntity>)

    @Query(value = "DELETE FROM holdings")
    suspend fun deleteAllHoldings()
}
