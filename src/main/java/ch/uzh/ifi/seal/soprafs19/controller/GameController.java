package ch.uzh.ifi.seal.soprafs19.controller;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
@RestController
public class GameController {

    private final GameService service;
    private final UserRepository userRepository;

    GameController(GameService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }


    // Create new Game
    @PostMapping(value = "/games",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createGame (@Valid @RequestBody Game newGame, HttpServletResponse response) {
        HashMap<String, String> pathToGame = new HashMap<>();
        pathToGame.put("path", this.service.createGame(newGame));

        // Upon success return the path to the created usr
        response.setStatus(201);
        return pathToGame;
    }


    @GetMapping(value = "/games/{gameId}",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    Game game (@RequestHeader("authorization") String token, @PathVariable(value="gameId") long gameId) {

        return service.getGameById(gameId);
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


    // Fetch all games
    @GetMapping("/games")
    Iterable<Game> allGames (
            @RequestHeader("authorization") String token) throws FailedAuthenticationException {
        return service.getAllGames(token);
    }

    // Fetch all games of the logged in user
    @GetMapping("/games/invitations")
    Iterable<Game> getGamesForUser2 (@RequestHeader("authorization") String token) {
        User user2 = this.userRepository.findByToken(token);
        return service.getGamesForUser2(user2);
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
