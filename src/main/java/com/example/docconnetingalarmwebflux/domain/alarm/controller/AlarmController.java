package com.example.docconnetingalarmwebflux.domain.alarm.controller;

import com.example.docconnetingalarmwebflux.domain.alarm.entity.AlarmHistories;
import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import com.example.docconnetingalarmwebflux.domain.alarm.repository.AlarmHistoriesRepository;
import com.example.docconnetingalarmwebflux.domain.alarm.service.AlarmSenderService;
import com.example.docconnetingalarmwebflux.domain.alarm.service.AlarmService;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.FcmInfo;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmHistoriesRepository alarmHistoriesRepository;
    private final AlarmSenderService alarmSenderService;
    private final AlarmService alarmService;

    @GetMapping("/test")
    public Mono<Void> save() {
        FcmInfo fcmInfo = FcmInfo.of("f1aDjd8xuyNrNbM8QJyM3O:APA91bH9JO7sx3uCQoYNzzVWM87wu5sELEZZh5m0YkZOcHKoNFJvUZdeQM7rmYnjj_T_u4puPdqYWA8968q_F5KxC1uHPTiYGN_oRlfzt0Ghwk_sKK0-HAk", 4L);
        List<FcmInfo> fcmInfoList = List.of(fcmInfo);
        String message = "회원님의 게시물에 의사가 답변을 달았습니다";
        Message messageDto = Message.of(fcmInfoList, message, AlarmType.COMMENT);
        return alarmService.sendCommentCompletedMessage(messageDto);
    }
}
