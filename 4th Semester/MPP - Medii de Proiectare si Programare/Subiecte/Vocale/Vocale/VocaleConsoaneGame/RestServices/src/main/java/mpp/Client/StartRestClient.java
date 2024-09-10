package mpp.Client;

import mpp.Configurare;

import java.util.HashMap;
import java.util.Map;

public class StartRestClient {

    public static MyRestClient myRestClient = new MyRestClient();

    public static void main(String[] args) {
        System.out.println("GET");
        show(() -> System.out.println(myRestClient.findOneGame(2)));


        System.out.println("POST");
        Configurare configurare = new Configurare(-1,"1111","2222");
        show(() -> myRestClient.save(configurare));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Exception ... " + e.getMessage());
        }
    }
}
