package com.ssafy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "chatEntity")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "chatroom_id") val chatroomId : Int?,
    @ColumnInfo(name = "lastReadMessage_idx") val lastMessageIdx : Int = 0,
)

