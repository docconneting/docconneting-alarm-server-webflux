package com.example.docconnetingalarmwebflux.domain.alarm.repository;

import com.example.docconnetingalarmwebflux.domain.alarm.entity.AlarmHistories;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AlarmHistoriesRepository extends R2dbcRepository<AlarmHistories, Long> {
}
