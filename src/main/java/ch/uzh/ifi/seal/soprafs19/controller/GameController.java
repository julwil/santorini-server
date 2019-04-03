package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.NotRegisteredException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class GameController {


    GameController() {

    }

    // Create new Game
    @PostMapping(value = "/games",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public String pathToGame () {
        return "{'path':'/games/1'}";
    }

    // Get Game by Id
    @GetMapping(value = "/games/{id}",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getGameById (@PathVariable long id) {
        return "{'id':"+id+"," +
                "'name':'testGame'," +
                "'board':['boardItem1','boardItem2']," +
                "'status':'running',"+
                "'currentTurn':{'id':1,'name':'testPlayer','figures':['figure1','figure2']}," +
                "'isGodPower':false,"
                +"'createTime':'2019-04-03 12:22:02',"+
                "'isRunning':true}";
    }

    // Get turns of Game
    @GetMapping(value = "/games/{id}/turns",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getTurnsOfGame (@PathVariable long id) {
        return "{'turns':[" +
                "{'id':1,'createTime':'2019-04-03 12:22:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':true,'events':[]}," +
                "{'id':2,'createTime':'2019-04-03 12:23:02','performedBy':{'id':2,'name':'testPlayer2','figures':['figure3','figure4']},'finished':true,'events':[]}," +
                "{'id':3,'createTime':'2019-04-03 12:24:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':false,'events':[]}" +
                "]}";
    }

    // Create new Turn in Game
    @PostMapping(value = "/games/{id}/turns",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public String pathToTurn (@PathVariable long id) {
        return "{'path':'/games/"+id+"/turns/1'}";
    }

    // Get players of Game
    @GetMapping(value = "/games/{id}/players",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getPlayersOfGame (@PathVariable long id) {
        return "{'players':[" +
                "{'id':1,'name':'testPlayer','figures':['figure1','figure2']}," +
                "{'id':2,'name':'testPlayer2','figures':['figure3','figure4']}" +
                "]}";
    }

}
