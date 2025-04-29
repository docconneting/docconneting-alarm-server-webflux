package com.example.docconnetingalarmwebflux.domain.alarm.repository;

import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AlarmHistoriesBulkRepository {

    private final DatabaseClient databaseClient;

    public Mono<Void> saveBatch(List<Long> userIdList, AlarmType alarmType, String content) {
        List<List<Long>> userIdBatches = createUserIdBatches(userIdList);
        return Flux.fromIterable(userIdBatches)
                .concatMap(batch -> save(batch, alarmType, content)) // 데이터 베이스 안정성을 위해 faltMap 대신 순차적으로 저장하는 concatMap 사용
                .then();
    }

    /*
     * db connection을 열고, statement에 여러 값을 바인딩하여 batch insert 수행
     */
    private Mono<Void> save(List<Long> userIds, AlarmType alarmType, String content) {
        return databaseClient.inConnection(connection -> {
            Statement statement = connection.createStatement(
                    "INSERT INTO alarm_histories (content, to_id, alarm_type, created_at) VALUES (?, ?, ?, ?)"
            );

            for (int i = 0; i < userIds.size(); i++) {
                // 마지막 바인딩 값에는 .add()를 붙이지 않음
                if (i == userIds.size() - 1) {
                    statement.bind(0, content)
                            .bind(1, userIds.get(i))
                            .bind(2, alarmType)
                            .bind(3, LocalDateTime.now());

                    break;
                }

                statement.bind(0, content)
                        .bind(1, userIds.get(i))
                        .bind(2, alarmType)
                        .bind(3, LocalDateTime.now())
                        .add();
            }

            return Flux.from(statement.execute())
                        .then();
        });
    }

    /*
     * batch insert를 사용하기 위해 userid 리스트를 100개씩 나누어 batch list 생성
     */
    private List<List<Long>> createUserIdBatches(List<Long> userIds) {
        List<List<Long>> userIdBatches = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i += 100) {
            userIdBatches.add(userIds.subList(i, Math.min(userIds.size(), i + 100)));
        }
        return userIdBatches;
    }

}
