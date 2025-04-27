package com.example.docconnetingalarmwebflux.infra.rabbitmq.consumer;

import com.example.docconnetingalarmwebflux.infra.rabbitmq.dto.Message;
import com.example.docconnetingalarmwebflux.infra.rabbitmq.handler.AlarmHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmServerReceiver {

    private final List<AlarmHandler> handlers;
    private static final String QUEUE = "alarm-queue";

    @RabbitListener(queues = QUEUE)
    public Mono<Void> receive(Message message) {
        return Flux.fromIterable(handlers)
                .filter(handler -> handler.getAlarmType().equals(message.getAlarmType()))
                .next()
                .flatMap(handler -> handler.handle(message));
    }
}
