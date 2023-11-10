package com.ssafy.domain.model.booking

import java.time.LocalDateTime

// 보낼 떄는 상관없는데, 받을 때는 serializeName이 필요함. -> gson의 매칭을 위해서.
data class BookingCreateRequest (
    val bookIsbn : String,
    val meetingTitle: String,
    val description : String,
    val maxParticipants : Number,
    val hashtagList : List<String>,
)

// 모임 신청하기
data class BookingJoinRequest (
    val meetingId : Long,
)

// 모임 수락하기
data class BookingAcceptRequest (
    val meetingId : Long,
    val memberId : Int,
)

// 모임 확정하기
data class BookingStartRequest (
    val meetingId : Long,
    val date : LocalDateTime,
    val location : String,
    val fee : Integer
)