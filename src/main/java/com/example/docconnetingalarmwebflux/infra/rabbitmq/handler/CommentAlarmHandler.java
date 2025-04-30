package com.example.docconnetingalarmwebflux.infra.rabbitmq.handler;

import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import com.example.docconnetingalarmwebflux.domain.alarm.service.AlarmService;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentAlarmHandler implements AlarmHandler{

    private final AlarmService alarmService;

    @Override
    public AlarmType getAlarmType() {
        return AlarmType.COMMENT;
    }

    @Override
    public Mono<Void> handle(Message message) {
        return alarmService.sendCommentCompletedMessage(message);
    }
}
