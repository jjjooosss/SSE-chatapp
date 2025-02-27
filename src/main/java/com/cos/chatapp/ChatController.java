package com.cos.chatapp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController //데이터 리턴 서버
public class ChatController {

    private final ChatRepository chatRepository;

    @CrossOrigin
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatRepository .mfindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }
    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {//데이터 한건만 리턴할거면 mono, 아니면 flux. save라서 리턴 안해도되는데 리턴 데이터 보려고 mono로 설정한 것.
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);//Obeject를 리턴하면 자동으로 JSON 변환(MessageConverter)
    }

}
