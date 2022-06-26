package com.mudrax.fitu

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("select * from `history-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>

    //@Query("select * from `history-table` where ")





}