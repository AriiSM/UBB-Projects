package com.example.socialnetwork_1v.controller.Mess;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ChatManager {

    private static List<Stage> openWindows = Collections.synchronizedList(new ArrayList<>());
    private static List<ChatController> messageControllers = Collections.synchronizedList(new ArrayList<>());

    public static void openWindow(Stage window, ChatController messageController) {
        Platform.runLater(() -> {
            openWindows.add(window);
            messageControllers.add(messageController);
            window.show(); // Deschide fereastra
        });
    }

    public static void closeWindow(Stage window, ChatController messageController) {
        Platform.runLater(() -> {
            openWindows.remove(window);
            messageControllers.remove(messageController);
            window.close(); // Închide fereastra
        });
    }

//    public static void notifyMessageControllers() {
//        for (ChatController controller : messageControllers) {
//            controller.refresh();
//        }
//    }


    // ... alte metode existente

}
