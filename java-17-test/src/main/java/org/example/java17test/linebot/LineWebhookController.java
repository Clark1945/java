package org.example.java17test.linebot;

import org.example.java17test.writeimage.TodoListDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
public class LineWebhookController {

    private final WebClient webClient;
    private final LinePushService linePushService;
    private final TodoListDrawer todoListDrawer; // 新增注入 TodoListDrawer

    public LineWebhookController(
            @Value("${line.channel-token}") String channelToken,
            LinePushService linePushService, // 透過建構子注入 LinePushService
            TodoListDrawer todoListDrawer) { // 透過建構子注入 TodoListDrawer

        this.linePushService = linePushService;
        this.todoListDrawer = todoListDrawer; // 初始化

        this.webClient = WebClient.builder()
                .baseUrl("https://api.line.me/v2/bot/message")
                .defaultHeader("Authorization", "Bearer " + channelToken)
                .build();
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {

        var events = (List<Map<String, Object>>) payload.get("events");
        if (events == null || events.isEmpty()) {
            return ResponseEntity.ok("ok");
        }

        var event = events.get(0);
        var replyToken = (String) event.get("replyToken");

        Map<String, Object> message = Map.of(
                "type", "text",
                "text", "Hello from your Spring Boot LineBot!"
        );

        Map<String, Object> body = Map.of(
                "replyToken", replyToken,
                "messages", List.of(message)
        );

        webClient.post()
                .uri("/reply")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/send-notification")
    public void sendNotification() throws Exception {
        String userId = "Uaeca5b988393f45b31a9a5abda13583f"; // 用戶的 Line userId
        todoListDrawer.drawImage(); // 使用注入的 todoListDrawer 實例
        linePushService.pushImageMessage(userId, "這是一則主動推播訊息！");
    }
}

