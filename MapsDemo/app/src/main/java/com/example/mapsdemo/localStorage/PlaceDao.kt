package com.example.mapsdemo.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.android.libraries.places.api.model.Place

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getAll(): List<PlaceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg place: PlaceModel)

    @Delete
    fun remove(place: PlaceModel)

}