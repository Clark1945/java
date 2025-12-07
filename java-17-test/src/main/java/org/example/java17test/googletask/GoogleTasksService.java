package org.example.java17test.googletask;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class GoogleTasksService {

    private static final String APP_NAME = "test-app";
    private static final String TOKENS_DIR = System.getProperty("user.home") + "/.g_tokens"; // 建議改成 home 下，更通用

    private final Tasks tasks;

    public GoogleTasksService(@Value("${google.credentials.path}") String credentialsPath) {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            // 從外部檔案路徑讀取 credentials.json
            com.google.api.client.auth.oauth2.Credential credential;
            try (InputStream in = new FileInputStream(credentialsPath)) {
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport,
                        jsonFactory,
                        clientSecrets,
                        List.of(TasksScopes.TASKS))
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIR)))
                        .setAccessType("offline")
                        .build();

                var receiver = new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver.Builder()
                        .setPort(8888)
                        .build();

                credential = new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(flow, receiver)
                        .authorize("user");
            }

            this.tasks = new Tasks.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APP_NAME)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GoogleTasksService Related object", e);
        }
    }

    public List<Task> getGoogleTasks() throws Exception {
        List<Task> taskList = new ArrayList<>();
        this.tasks.tasklists()
                .list()
                .execute()
                .getItems()
                .forEach((tasklist) -> {
                    String taskListTitle = tasklist.getTitle();
                    try {
                        List<Task> tasks = this.tasks.tasks()
                                .list(tasklist.getId())
//                                .setShowHidden(true)
                                .execute()
                                .getItems();
                        if (tasks == null || tasks.isEmpty()) {
                            System.out.printf("%s清單沒有待辦事項%n", taskListTitle);
                            return;
                        }
                        List<Task> taskswithoutDue = tasks.stream()
                                .filter(task -> task.getDue() == null)
                                .toList();
                        List<Task> tasksWithDue = tasks.stream()
                                .filter(task -> task.getDue() != null)
                                .sorted(Comparator.comparing(task -> OffsetDateTime.parse(task.getDue())))
                                .toList();
                        taskList.addAll(tasksWithDue);
                        taskList.addAll(taskswithoutDue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return taskList;
    }

    public Tasks getService() {
        return this.tasks;
    }
}

