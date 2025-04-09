//package ppd.server.notUsed;
//
//import ppd.common.CompetitorBlock;
//
//import java.util.Set;
//
//// Worker pentru consumatori
//public class MyWorker implements Runnable {
//    private final MyQueue queue;
//    private final LinkedList globalRankings;
//    private final Set<Integer> blacklist;
//
//    public MyWorker(MyQueue queue, LinkedList globalRankings, Set<Integer> blacklist) {
//        this.queue = queue;
//        this.globalRankings = globalRankings;
//        this.blacklist = blacklist;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                CompetitorBlock[] competitor = queue.take();
//                int id = competitor[0].getCompetitors().get(0)[0];
//                int score = competitor[0].getCompetitors().get(1)[0];
//                String countryId = competitor[0].getCountryName();
//
//                // Dacă ID-ul este în blacklist, îl ignorăm
//                if (blacklist.contains(id)) {
//                    System.out.println("[CONSUMER] Competitor " + id + " is blacklisted and will be ignored.");
//                    continue;
//                }
//
//                // Adăugăm sau actualizăm concurentul în lista globală
//                globalRankings.addOrUpdate(id, score, countryId, blacklist);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
