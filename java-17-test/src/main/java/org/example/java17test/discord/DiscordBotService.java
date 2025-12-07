package org.example.java17test.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;

//@Service
//public class DiscordBotService {
//
//    private final GatewayDiscordClient client;
//
//    public DiscordBotService() {
//        this.client = DiscordClientBuilder.create("你的BotToken")
//                .build()
//                .login()
//                .block();
//
//        // 註冊事件監聽
//        client.on(MessageCreateEvent.class)
//                .subscribe(event -> {
//                    String content = event.getMessage().getContent();
//                    if (content.equalsIgnoreCase("!ping")) {
//                        event.getMessage()
//                                .getChannel()
//                                .block()
//                                .createMessage("pong!")
//                                .block();
//                    }
//                });
//    }
//}

// 1445414466299756646
// ec4ab609539236647878bc5572888458bf62cb9a4aa6f9e85fcb691ef219c767