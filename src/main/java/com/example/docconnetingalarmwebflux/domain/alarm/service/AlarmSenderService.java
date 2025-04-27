package com.example.docconnetingalarmwebflux.domain.alarm.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
public class AlarmSenderService {

    /*
     * 다건 알람 전송
     */
    public Mono<BatchResponse> sendMulticastAlarm(List<String> fcmTokenBatche, String content) {
        return Mono.fromCallable(() -> {
                    MulticastMessage message = MulticastMessage.builder()
                        .setNotification(Notification.builder()
                                .setTitle("Docconneting")
                                .setBody(content)
                                .build())
                        .addAllTokens(fcmTokenBatche)
                        .build();
                    return FirebaseMessaging.getInstance().sendEachForMulticast(message);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(batchResponse -> log.info("알림 전송 완료 - 성공횟수: {}, 실패횟수: {}"
                        , batchResponse.getSuccessCount(), batchResponse.getFailureCount()))
                .doOnError(error -> log.info("알림 전송 실패", error));
    }


    /*
     * 단건 알람 전송
     */
    public Mono<String> sendAlarm(String fcmToken, String content) {
        return Mono.fromCallable(() -> {
                    Message message = Message.builder()
                            .setToken(fcmToken)
                            .setNotification(Notification.builder()
                                    .setTitle("Docconneting")
                                    .setBody(content)
                                    .build())
                            .build();
                    return FirebaseMessaging.getInstance().send(message);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(response -> log.info("알림 전송 완료 - 메시지 ID: {}", response))
                .doOnError(error -> log.info("알림 전송 실패", error));
    }
}
