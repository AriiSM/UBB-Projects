package mpp;


import mpp.Repo.RepoConfiguratie;
import mpp.Repo.RepoJucatorGame;
import mpp.RepoORM.RepoConfiguratieHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping( "/jucatoriGames")
@ComponentScan("mpp")
public class JucatorGamesRestController {

    private static final String template = "Hello, %s!";

    @Autowired
    private RepoConfiguratieHibernate repoConfiguratie;
    @Autowired
    private RepoJucatorGame repoJucatorGame;


    @RequestMapping("/test")
    public String greeting(@RequestParam(value="name", defaultValue = "World") String name){
        return String.format(template,name);
    }

    @RequestMapping(value="/jucator/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> findOne(@PathVariable Integer id){
        System.out.println("FindOne, id = " + id + "...");
        String cerinta = repoJucatorGame.findCerinta(id);
        if(cerinta.isEmpty())
            return new ResponseEntity<String>("Concurs not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cerinta, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> save(@RequestBody Configuratie configuratie){
        System.out.println("Saving concurs ...");
        try{
            repoConfiguratie.save(configuratie);
            return new ResponseEntity<Configuratie>(configuratie, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
