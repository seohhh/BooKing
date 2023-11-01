package com.booking.member.payments.controller;

import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest extends ControllerTest {

    private final String baseUrl = "/api/payments";

    @Test
    @DisplayName("결제 준비 요청")
    void t1() throws Exception {
//        ReadyPaymentResponseDto responseDto = ReadyPaymentResponseDto.builder()
//                .tid("tid")
//                .next_redirect_app_url("url")
//                .next_redirect_mobile_url("url")
//                .next_redirect_pc_url("url")
//                .android_app_scheme("url")
//                .ios_app_scheme("url")
//                .created_at(LocalDateTime.now())
//                .build();
        ReadyPaymentResponseDto responseDto = new ReadyPaymentResponseDto("tid", "url", "url", "url", "url", "url", LocalDateTime.now());

        ReadyPaymentRequestDto readyPaymentRequestDto = new ReadyPaymentRequestDto("1000");

        when(paymentService.readyPayment(any(),anyString()))
                .thenReturn(Mono.just(responseDto));

        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(readyPaymentRequestDto)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("/payment/ready",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("amount").description("금액")
                                ),
                                responseFields(
                                        fieldWithPath("tid").description("결제 승인 요청 때 필요한 tid"),
                                        fieldWithPath("next_redirect_app_url").description("redirect url"),
                                        fieldWithPath("next_redirect_mobile_url").description("redirect url"),
                                        fieldWithPath("next_redirect_pc_url").description("redirect url"),
                                        fieldWithPath("android_app_scheme").description("redirect url"),
                                        fieldWithPath("ios_app_scheme").description("redirect url"),
                                        fieldWithPath("created_at").description("시간")
                                )
                        )
                );
    }
}
