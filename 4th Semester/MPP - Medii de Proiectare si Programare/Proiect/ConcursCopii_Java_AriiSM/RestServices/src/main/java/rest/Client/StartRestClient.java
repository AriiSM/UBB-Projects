package rest.Client;

import client.Domain_REST.Categorie;
import client.Domain_REST.Concurs;
import client.Domain_REST.Proba;

public class StartRestClient {
    public static MyRestClient myRestClient = new MyRestClient();

    public static void main(String[] args) {
        System.out.println("===============================================================================================================\n");
        System.out.println("\nFind all concurs: ");
        show(() -> {
            Concurs[] res = myRestClient.findAll();
            for (Concurs u : res) {
                System.out.println(u);
            }
        });
        System.out.println("===============================================================================================================\n");

        System.out.println("\nFind one id=2");
        show(() -> System.out.println(myRestClient.findOne(2)));
        System.out.println("===============================================================================================================\n");

        System.out.println("\nAdding a new concurs: ");
        Concurs savedone = new Concurs(Categorie.VARSTA_REST, Proba.PROBA_REST);
        show(()-> myRestClient.save(savedone));
        System.out.println("===============================================================================================================\n");

        System.out.println("\nModifing concurs");
        Concurs concurs_modify = myRestClient.findOne(10);
        concurs_modify.setCategorie(Categorie.VARSTA_REST_MODIFICAT);
        show(() -> myRestClient.update(10, concurs_modify));

        System.out.println("\nDeleting concurs...");
        show(()-> myRestClient.delete(10));
        System.out.println("===============================================================================================================\n");


    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Exception ... " + e.getMessage());
        }
    }
}
