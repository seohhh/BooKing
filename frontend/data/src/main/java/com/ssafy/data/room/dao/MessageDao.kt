package com.ssafy.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.data.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageEntity WHERE chatroom_id = :chatroomId ORDER BY time_stamp")
    fun getAllMessage(chatroomId: Int): LiveData<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(vararg messages: MessageEntity)
    
    // 이미 저장된 메시지 찾기
    @Query("SELECT * FROM messageEntity WHERE chatroom_id = :chatroomId AND message_id = :messageId")
    fun getMessageByChatIdAndMessageId(chatroomId: Int?, messageId: Int?): MessageEntity?

    // 읽음 수 업데이트
    @Query("UPDATE messageEntity SET read_count = :newReadCount WHERE message_id = :messageId")
    suspend fun updateReadCount(messageId: Int, newReadCount: Int)

    // '안불러옴'상태의 메시지 가져오기
    @Query("SELECT * FROM messageEntity WHERE chatroom_id = :chatroomId AND used = 0")
    fun getUnusedMessage(chatroomId: Int): LiveData<List<MessageEntity>>

    // '안불러옴' -> '불러옴'
    @Query("UPDATE messageEntity SET used = 1 WHERE id = :messageId")
    suspend fun markUsedMessage(messageId: Int)
}