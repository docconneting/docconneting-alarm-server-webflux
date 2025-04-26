package com.example.docconnetingalarmwebflux.infra.rabbitmq.handler;

import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;

public interface AlarmHandler {
    AlarmType getAlarmType();
    void handle(Message message);

}
