package com.example.mapsdemo.localStorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
class PlaceModel : java.io.Serializable {
    constructor(address : String,latitude : Double,longitude : Double){
        this.address = address
        this.latitude = latitude
        this.longitude = longitude
    }

    @ColumnInfo(name = "address")
    var address: String? = ""
    @ColumnInfo(name = "id")
    var id: String = ""
    @ColumnInfo(name = "lat")
    var latitude: Double = 0.0
    @ColumnInfo(name = "long")
    var longitude: Double = 0.0
    @PrimaryKey(autoGenerate = true)
    var srno: Int = 0

}
