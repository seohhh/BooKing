package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.ChatViewModel
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHome(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val navController = LocalNavigation.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    val myPageViewModel: MyPageViewModel = hiltViewModel()

    var chatId by remember { mutableStateOf("") }

    // 유저 정보 불러오기
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    var memId by remember { mutableStateOf<Long?>(null) }
    val loginId: String? = tokenDataSource.getLoginId()
    val getUserInfoResponse by myPageViewModel.getUserInfoResponse.observeAsState()
    chatViewModel.loadChatList()
    LaunchedEffect(loginId) {
        val result = loginId?.let {
            myPageViewModel.getUserInfo(loginId)
        }
    }
    LaunchedEffect(getUserInfoResponse) {
        if (getUserInfoResponse != null) {
//            Log.d("CHAT", "HOME ${getUserInfoResponse!!.body()}")
            memId = getUserInfoResponse!!.body()?.memberPk
        }
    }

    // ChatHome 자동 목록 조회 시작
    LaunchedEffect(Unit) {
        chatViewModel.setIsChatHome(true)
    }
    // ChatHome 자동 목록 조회 중지
    DisposableEffect(Unit) {
        onDispose {
            chatViewModel.setIsChatHome(false)
        }
    }

    Scaffold(
        topBar = {
            TopBar("채팅")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box {
                Column {
                    Row {
                        TextField(
                            value = chatId,
                            onValueChange = { chatId = it },
                            placeholder = { Text("채팅방 번호") }
                        )
                        Button(
                            onClick = {
                                val request =
                                    ChatCreateRequest(chatId.toInt(), memId, "${chatId}번 채팅")
                                chatViewModel.createChatRoom(request)
                            }
                        ) {
                            Text("방 생성")
                        }
                    }
                    Row {
                        Button(
                            onClick = {
                                val request = ChatJoinRequest(chatId.toInt(), memId)
                                chatViewModel.joinChatRoom(request)
                            }
                        ) {
                            Text("채팅방 참가")
                        }
                    }
                    ChatList()
                }
            }
        }
    }
}

@Composable
fun ChatList() {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatListState by chatViewModel.chatListState.observeAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chatListState) { chat ->
            ChatItem(chat) {
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatRoom,
    onRowClick: (ChatRoom) -> Unit,
) {
    val navController = LocalNavigation.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    chatViewModel.saveLocalChatId(chat.chatroomId)
    chatViewModel.getLastReadMessageId(chat.chatroomId)

    val lastReadMessageIds  by chatViewModel.lastReadMessageIds
    val lastReadMessageId = lastReadMessageIds[chat.chatroomId]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = {
                onRowClick(chat)
                val memberListString = chat.memberList.joinToString(",")
                navController.navigate("chatDetail/${chat.chatroomId}/${memberListString}/${chat.meetingTitle}")
            })
    ) {
        // 개인 채팅방 이미지
        if (chat.memberList.size <= 1) {
            Image(
                painter = painterResource(id = R.drawable.main1),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
                    .clip(RoundedCornerShape(36.dp)) // 박스를 둥글게
            )
            // 채팅방 이미지
        } else {
            Image(
                painter = painterResource(id = R.drawable.main1),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = chat.meetingTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (chat.memberList.size > 1) {
                        Text(
                            text = "${chat.memberList.size}",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.lastMessage?.let {
                        chat.lastMessage
                    } ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (chat.lastMessageIdx - (lastReadMessageId ?: 0) - 1 > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Red, shape = CircleShape)
                ) {
//                    Log.d("CHAT", "HOME ${chat}")
//                    Log.d("CHAT", "HOME ${lastReadMessageId}")
                    Text(
                        text = "${chat.lastMessageIdx - (lastReadMessageId ?: 0) -1 }",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
