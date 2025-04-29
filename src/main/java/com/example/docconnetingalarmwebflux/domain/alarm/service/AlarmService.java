package com.example.docconnetingalarmwebflux.domain.alarm.service;

import com.example.docconnetingalarmwebflux.domain.alarm.entity.AlarmHistories;
import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import com.example.docconnetingalarmwebflux.domain.alarm.repository.AlarmHistoriesBulkRepository;
import com.example.docconnetingalarmwebflux.domain.alarm.repository.AlarmHistoriesRepository;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.FcmInfo;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmSenderService alarmSenderService;
    private final AlarmHistoriesRepository alarmHistoriesRepository;
    private final AlarmHistoriesBulkRepository alarmHistoriesBulkRepository;

    /*
     * 사용자가 유료 게시물을 올렸을 떄 해당 전공에 해당되는 의사들에게 알람 전송
     */
    public Mono<Void> sendPostUploadCompletedMessage(Message message) {
        List<String> fcmTokenList = message.getFcmInfos().stream()
                .map(FcmInfo::getFcmToken)
                .toList();

        List<Long> userIdList = message.getFcmInfos().stream()
                .map(FcmInfo::getUserId)
                .toList();

        AlarmType alarmType = message.getAlarmType();
        String alarmMessage = message.getMessage();

        List<List<String>> fcmTokenBatches = new ArrayList<>();
        for (int i = 0; i < fcmTokenList.size(); i += 100) {
            fcmTokenBatches.add(fcmTokenList.subList(i, Math.min(fcmTokenList.size(), i + 100)));
        }

        return Flux.fromIterable(fcmTokenBatches)
                .flatMap(fcmTokenBatche -> alarmSenderService.sendMulticastAlarm(fcmTokenBatche, alarmMessage), 4)
                .then(alarmHistoriesBulkRepository.saveBatch(userIdList, alarmType, alarmMessage))
                .then();
    }

    /*
     * 게시물에 의사가 댓글을 달았을 때 게시물 작성자에게 알람 전송
     */
    public Mono<Void> sendCommentCompletedMessage(Message message) {
        String fcmToken = message.getFcmInfos().get(0).getFcmToken();
        Long userId = message.getFcmInfos().get(0).getUserId();
        AlarmType alarmType = message.getAlarmType();
        String alarmMessage = message.getMessage();

        return alarmSenderService.sendAlarm(fcmToken, alarmMessage)
                .then(saveAlarmHistories(alarmMessage, userId, alarmType))
                .then();
    }

    /*
     * 채팅 진료 결제가 완료 됐을 때 해당 의사에게 알람 전송
     */
    public Mono<Void> sendMedicalRequestMessage(Message message) {
        String fcmToken = message.getFcmInfos().get(0).getFcmToken();
        Long userId = message.getFcmInfos().get(0).getUserId();
        AlarmType alarmType = message.getAlarmType();
        String alarmMessage = message.getMessage();

        return alarmSenderService.sendAlarm(fcmToken, alarmMessage)
                .then(saveAlarmHistories(alarmMessage, userId, alarmType))
                .then();
    }

    /*
     * 단건 알람 히스토리 저장
     */
    private Mono<AlarmHistories> saveAlarmHistories(String content, Long id, AlarmType alarmType) {
        AlarmHistories alarmHistories = AlarmHistories.of(content, id, alarmType);
        return alarmHistoriesRepository.save(alarmHistories);
    }
}
