package com.example.docconnetingalarmwebflux.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class AlarmSenderService {

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
