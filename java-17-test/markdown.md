# Project Report: To-Do List Snapshot Service

## 1. Summary

This project is a Spring Boot application built with Java 17, designed to function as a "To-Do List Snapshot" service. Its core purpose is to fetch a user's to-do lists from Google Tasks, render these tasks into a PNG image, and deliver the image to the user via the LINE messaging platform.

## 2. Core Workflow

The application follows a precise, event-driven workflow:

1.  **Task Fetching**: The `GoogleTasksService` authenticates with the Google Tasks API using an OAuth 2.0 flow designed for installed applications. This requires a one-time manual user login to grant access. Once authorized, it fetches all available tasks.

2.  **Image Generation**: The `TodoListDrawer` service takes the fetched tasks. Using Java's Abstract Window Toolkit (AWT), it draws the task list onto a predefined template image (`todolist1.jpg`) and saves the final output as `output.png` in the local filesystem.

3.  **Image Hosting & Delivery**: The `LinePushService` reads the generated `output.png` file. It then uploads this image to a configured Supabase Storage bucket via its REST API, obtaining a public URL. Finally, it sends a push message to a specified LINE user, with the message containing the public URL of the newly hosted image.

## 3. Architecture & Integrations

### Framework
*   **Spring Boot**: The application is built on the Spring Boot framework.
*   **Webflux**: It utilizes `WebClient` from the Spring Webflux module for making reactive, non-blocking HTTP requests to external services (Supabase).

### Integrations
*   **Google Tasks**: Serves as the primary data source for user tasks.
*   **LINE**: Used as the user-facing communication channel for both receiving commands (via a webhook) and dispatching image notifications.
*   **Supabase**: Leveraged as a simple and quick solution for hosting the generated image files, making them accessible via a public URL.
*   **Discord**: A module for a Discord bot (`DiscordBotService`) exists within the codebase, but it is currently inactive as its `@Service` annotation is commented out.

### Configuration
The project uses a hybrid configuration model:
*   `application.yml`: Contains general application settings and configuration for external services.
*   `.env` file: Used to store sensitive secrets and credentials. These are loaded into System Properties at application startup.

## 4. Key Architectural Points

*   **Single-User Focus**: The Google OAuth "installed app" flow is designed for a single-user context and is not suited for a multi-tenant web application.
*   **Filesystem Dependency**: The workflow relies on the local filesystem as an intermediary (`output.png`) to pass the generated image between the drawing and pushing services. This could present a bottleneck or race conditions if the service were to handle concurrent requests.
*   **Direct API Integration**: The Supabase integration is implemented with raw `WebClient` calls to its REST API endpoints, rather than using a dedicated client library.

## 5. Key Project Files

*   `pom.xml`: Defines all project dependencies, including Spring Boot, Google Tasks API, Discord4j, and the `dotenv-java` loader.
*   `src/main/resources/application.yml`: Configures the LINE, Google, and Supabase integrations.
*   `src/main/java/org/example/java17test/Java17TestApplication.java`: The main application entry point, which includes the logic for loading the `.env` file.
*   `src/main/java/org/example/java17test/linebot/LineWebhookController.java`: Defines the primary API endpoints for interacting with the LINE bot and triggering the core workflow.
*   `src/main/java/org/example/java17test/writeimage/TodoListDrawer.java`: Contains the core business logic for fetching tasks and rendering them into an image.
*   `src/main/java/org/example/java17test/googletask/GoogleTasksService.java`: Manages the Google OAuth 2.0 flow and data fetching from the Google Tasks API.
*   `src/main/java/org/example/java17test/linebot/LinePushService.java`: Handles uploading the final image to Supabase and sending the notification to LINE.
