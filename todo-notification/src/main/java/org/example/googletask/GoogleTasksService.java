package org.example.googletask;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
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

import java.io.*;
import java.time.OffsetDateTime;
import java.util.*;

public class GoogleTasksService {

    private static final String APP_NAME = "test-app";
    private static final String TOKENS_DIR = System.getProperty("user.home") + "/.g_tokens"; // 建議改成 home 下，更通用
    private static final String credentialsPath = "D:/Code/secret/credentials.json";
    private final Tasks tasks;

    public GoogleTasksService() {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleClientSecrets clientSecrets;
            try (InputStream in = new FileInputStream(credentialsPath)) {
                clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
            }

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    clientSecrets,
                    List.of(TasksScopes.TASKS))
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIR)))
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();

            // 🔥 外層只做一件事：拿「一定可用」的 Credential
            Credential credential = authorize(flow, httpTransport, jsonFactory);

            this.tasks = new Tasks.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APP_NAME)
                    .build();
        } catch (Exception e) {
            System.out.println("Google get authorization failed:");
            throw new RuntimeException("Failed to initialize GoogleTasksService Related object", e);
        }
    }

    private Credential authorize(
            GoogleAuthorizationCodeFlow flow,
            NetHttpTransport httpTransport,
            JsonFactory jsonFactory
    ) throws Exception {
        try {
            Credential credential = doAuthorization(flow);
            // 🔍 驗證 token 是否真的可用
            validateCredential(credential, httpTransport, jsonFactory);
            return credential;
        } catch (TokenResponseException e) {
            if ("invalid_grant".equals(e.getDetails().getError())) {
                // 🔴 token 存在但已失效
                clearStoredTokens(TOKENS_DIR);
                return doAuthorization(flow);
            }
            throw e;
        }
    }

    private void clearStoredTokens(String tokenPath) {
        File dir = new File(tokenPath);
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        dir.delete();
    }


    private Credential doAuthorization(GoogleAuthorizationCodeFlow flow) throws IOException {
        var receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
    }

    private void validateCredential(
            Credential credential,
            NetHttpTransport httpTransport,
            JsonFactory jsonFactory
    ) throws IOException {

        new Tasks.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APP_NAME)
                .build()
                .tasklists()
                .list()
                .setMaxResults(1)
                .execute();
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
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return taskList;
    }

    public Tasks getService() {
        return this.tasks;
    }
}

