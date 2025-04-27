package com.example.docconnetingalarmwebflux.infra.rabbitmq.handler;

import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;
import reactor.core.publisher.Mono;

public interface AlarmHandler {
    AlarmType getAlarmType();
    Mono<Void> handle(Message message);

}
