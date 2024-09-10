package rest.Server;


import client.Domain_REST.Concurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Repo_REST.ConcursDBRepository_REST;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping( "/concurs")
@ComponentScan("server")
public class Concurs_RestController {
    private static final String template = "Hello, %s!";

    @Autowired
    private ConcursDBRepository_REST concursDBRepository;

    @RequestMapping("/test")
    public String greeting(@RequestParam(value="name", defaultValue = "World") String name){
        return String.format(template,name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Concurs[] getAll(){
        System.out.println("FindALl Concurs...");
        Iterable<Concurs> concursuri = concursDBRepository.findAll();
        ArrayList<Concurs> new_concursuri = new ArrayList<>();
        for(var concurs: concursuri)
            new_concursuri.add(concurs);
        Concurs[] concursuriArray = new Concurs[new_concursuri.size()];
        return new_concursuri.toArray(concursuriArray);
    }

    
    @RequestMapping(value="/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> findOne(@PathVariable Integer id){
        System.out.println("FindOne, id = " + id + "...");
        Optional<Concurs> concurs = concursDBRepository.findOne(id);
        if(concurs.isEmpty())
            return new ResponseEntity<String>("Concurs not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<Concurs>(concurs.get(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> save(@RequestBody Concurs concurs){
        System.out.println("Saving concurs ...");
        try{
            concursDBRepository.save(concurs);
            return new ResponseEntity<Concurs>(concurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting concurs ... " + id);
        try {
            concursDBRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Concurs entity){
        System.out.println("Updating concurs ... " + id);
        try {
            concursDBRepository.update(id, entity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
