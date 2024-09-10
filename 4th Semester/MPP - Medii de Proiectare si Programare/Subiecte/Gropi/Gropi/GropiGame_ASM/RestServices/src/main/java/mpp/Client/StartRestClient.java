package mpp.Client;

import java.util.HashMap;
import java.util.Map;

public class StartRestClient {

    public static MyRestClient myRestClient = new MyRestClient();

    public static void main(String[] args) {
        System.out.println("GET");
        show(() -> System.out.println(myRestClient.findOneJucator(1)));


        System.out.println("PUT");
        Map<String, String> updates = new HashMap<>();
        updates.put("coef00", "X");
        show(() -> myRestClient.updateConfigurare(1, updates));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Exception ... " + e.getMessage());
        }
    }
}
