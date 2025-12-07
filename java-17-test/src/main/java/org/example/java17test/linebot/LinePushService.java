package org.example.java17test.linebot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class LinePushService {

    private final WebClient webClient;
    private final String supabaseSecretKey;
    private final String supabaseUrl = "https://jmqdfpguuxisjdtinyqo.supabase.co";
    private final String bucketName = "images";

    public LinePushService(@Value("${line.channel-token}") String channelToken,
                           @Value("${superbase.secret-key}")String supabaseSecretKey) {
        this.supabaseSecretKey = supabaseSecretKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.line.me/v2/bot/message")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + channelToken)
                .build();
    }

    public void pushImageMessage(String userId, String text) throws IOException {
        // 1. 將 BufferedImage 轉成 byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(new File("./output.png")), "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // 2. 上傳到 Supabase
        String fileName = "output-" + System.currentTimeMillis() + ".png";
        String imageUrl = uploadImage(imageBytes, fileName);

        System.out.println("Image uploaded: " + imageUrl);

        // 3. 呼叫 Line Push Image API
        Map<String, Object> message = Map.of(
                "type", "image",
                "originalContentUrl", imageUrl,
                "previewImageUrl", imageUrl
        );

        Map<String, Object> body = Map.of(
                "to", userId,
                "messages", List.of(message)
        );

        webClient.post()
                .uri("/push")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.err.println("Push failed: " + err.getMessage()))
                .subscribe();
    }

    public String uploadImage(byte[] imageBytes, String fileName) {

        WebClient webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/storage/v1")
                .defaultHeader("apikey", supabaseSecretKey)
                .defaultHeader("Authorization", "Bearer " + supabaseSecretKey)
                .build();

        webClient.put()
                .uri("/object/" + bucketName + "/" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(imageBytes)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return supabaseUrl + "/storage/v1/object/public/"+ bucketName + "/" + fileName;
    }
}
