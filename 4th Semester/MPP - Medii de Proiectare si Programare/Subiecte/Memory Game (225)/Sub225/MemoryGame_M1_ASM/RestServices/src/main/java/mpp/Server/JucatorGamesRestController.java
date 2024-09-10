package mpp.Server;


import mpp.Repo.RepoGame;
import mpp.RepoORM.RepoConfigurareHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping( "/jucatoriGames")
@ComponentScan("mpp")
public class JucatorGamesRestController {

    private static final String template = "Hello, %s!";

    @Autowired
    private RepoGame repoGame;
    @Autowired
    private RepoConfigurareHibernate repoConfigurare;


    @RequestMapping("/test")
    public String greeting(@RequestParam(value="name", defaultValue = "World") String name){
        return String.format(template,name);
    }

    @RequestMapping(value="/configurare/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, String> updates){
        System.out.println("Updating configurare ... " + id);
        try {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                repoConfigurare.updateWithId(id, entry.getKey(), entry.getValue());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/game/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> findOne(@PathVariable Integer id){
        System.out.println("FindOne, id = " + id + "...");
        String cerinta = repoGame.findCerinta(id);
        if(cerinta.isEmpty())
            return new ResponseEntity<String>("Concurs not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cerinta, HttpStatus.OK);
    }

}
