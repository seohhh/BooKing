package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.booking.BookingAcceptRequest
import com.ssafy.domain.model.booking.BookingAll
import retrofit2.Response
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingJoinRequest
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingStartRequest
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.booking.SearchItem
import com.ssafy.domain.model.booking.SearchResponse
import com.ssafy.domain.model.mypage.UserInfoResponse

interface BookingRepository {
    suspend fun postBookingCreate(request : BookingCreateRequest) : Response<Unit>
    suspend fun getAllBooking() : Response<List<BookingAll>>
    suspend fun getEachBooking(meetingId:Long) : Response<BookingDetail>
    suspend fun getParticipants(meetingId:Long) : Response<List<BookingParticipants>>
    suspend fun getWaitingList(meetingId:Long) : Response<List<BookingWaiting>>
    suspend fun getSearchList(query:String,display:Int,start:Int,sort:String) : Response<SearchResponse>
    suspend fun postBookingJoin(meetingId: Long,request : BookingJoinRequest) : Response<Unit>
    suspend fun postBookingAccept(meetingId: Long,memberId:Int,request: BookingAcceptRequest) : Response<Unit>
    suspend fun postBookingStart(request: BookingStartRequest) : Response<Unit>
}