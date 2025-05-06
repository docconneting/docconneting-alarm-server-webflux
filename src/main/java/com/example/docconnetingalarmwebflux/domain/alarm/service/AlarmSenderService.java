package com.example.docconnetingalarmwebflux.domain.alarm.service;

import com.example.docconnetingalarmwebflux.common.util.FirebaseUtils;
import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AlarmSenderService {

    /*
     * 다건 알람 전송
     */
    public Mono<BatchResponse> sendMulticastAlarm(List<String> fcmTokenBatche, String content) {
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("Docconneting")
                        .setBody(content)
                        .build())
                .addAllTokens(fcmTokenBatche)
                .build();

        ApiFuture<BatchResponse> apiFuture = FirebaseMessaging.getInstance().sendEachForMulticastAsync(message);

        return Mono.fromFuture(FirebaseUtils.toCompletableFuture(apiFuture))
                .doOnNext(response -> log.info("알림 전송 완료 - 성공: {}, 실패: {}",
                        response.getSuccessCount(), response.getFailureCount()))
                .doOnError(error -> log.error("알림 전송 실패", error));
    }


    /*
     * 단건 알람 전송
     */
    public Mono<String> sendAlarm(String fcmToken, String content) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("Docconneting")
                        .setBody(content)
                        .build())
                .build();

        ApiFuture<String> apiFuture = FirebaseMessaging.getInstance().sendAsync(message);

        return Mono.fromFuture(FirebaseUtils.toCompletableFuture(apiFuture))
                .doOnNext(response -> log.info("알림 전송 완료 - 메시지 ID: {}", response))
                .doOnError(error -> log.error("알림 전송 실패", error));
    }
}
