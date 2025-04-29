package com.example.docconnetingalarmwebflux.domain.alarm.entity;

import com.example.docconnetingalarmwebflux.domain.alarm.enums.AlarmType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("alarm_histories")
public class AlarmHistories {

    @Id
    private Long id;

    private String content;

    private Long toId;

    private AlarmType alarmType;

    @CreatedDate
    private LocalDateTime createdAt;

    private AlarmHistories(String content, Long toId, AlarmType alarmType) {
        this.content = content;
        this.toId = toId;
        this.alarmType = alarmType;
    }

    public static AlarmHistories of(String content, Long toId, AlarmType alarmType) {
        return new AlarmHistories(content, toId, alarmType);
    }

}
