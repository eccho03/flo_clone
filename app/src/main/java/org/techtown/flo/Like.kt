package org.techtown.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Likes(
    var userId : Int,
    var albumId : Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
