package mpp.Server;


import mpp.Configurare;
import mpp.Repo.RepoConfigurare;
import mpp.Repo.RepoJucatorGame;
import mpp.RepoORM.RepoConfigurareHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping( "/jucatoriGames")
@ComponentScan("mpp")
public class ControllerRest {

    private static final String template = "Hello, %s!";
    @Autowired
    private RepoJucatorGame repoJucatorGame;
    @Autowired
    private RepoConfigurareHibernate repoConfigurare;

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
    public ResponseEntity<?> save(@RequestBody Configurare configuratie){
        System.out.println("Saving concurs ...");
        try{
            repoConfigurare.save(configuratie);
            return new ResponseEntity<Configurare>(configuratie, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}