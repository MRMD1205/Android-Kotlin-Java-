package com.example.mapsdemo.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceModel::class], version = 1, exportSchema = true)
abstract class PlacesDB : RoomDatabase() {
    abstract fun placesDao(): PlaceDao
}
