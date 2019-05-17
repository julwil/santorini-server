package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.*;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameServiceDemo;
import ch.uzh.ifi.seal.soprafs19.service.game.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@RestController
public class GameController {

    private final GameService service;
    private final UserRepository userRepository;
    private final GameServiceDemo gameServiceDemo;



    GameController(GameService service, UserRepository userRepository, GameServiceDemo gameServiceDemo) {
        this.service = service;
        this.userRepository = userRepository;

        this.gameServiceDemo = gameServiceDemo;

    }

    // Create new Game
    @PostMapping(value = "/games",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> postCreateGame (
            @Valid @RequestBody Game newGame,
            HttpServletResponse response)
    {
        HashMap<String, String> pathToGame = new HashMap<>();
        pathToGame.put("path", this.service.postCreateGame(newGame));

        response.setStatus(201);
        return pathToGame;
    }


    @PostMapping("/games/{id}/accept")
    public Game postAcceptGameRequestByUser (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId,
            @RequestBody HashMap<String, Object> requestBody) throws ResourceNotFoundException, ResourceActionNotAllowedException
    {
        User user = this.userRepository.findByToken(token);
        String selectedGodPower = "";
        if (requestBody.containsKey("selectedGodPower")) {
            selectedGodPower = (String) requestBody.get("selectedGodPower");
        }

        return service.postAcceptGameRequestByUser(gameId, user, selectedGodPower);
    }






    // Create new Game
    @PostMapping(value = "/games/demoXWins",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> postCreateGameDemoXWins (
            @Valid @RequestBody Game newGame,
            HttpServletResponse response) throws FailedAuthenticationException, GameRuleException, ResourceNotFoundException, UsernameAlreadyExistsException, ResourceActionNotAllowedException {
        HashMap<String, String> pathToGame = new HashMap<>();
        pathToGame.put("id", this.gameServiceDemo.postCreateGameDemoXWins(newGame));

        response.setStatus(201);
        return pathToGame;
    }


        @PostMapping("/games/demo/{id}/accept")
    public Game postAcceptDemoGameRequestByUser (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId) throws ResourceNotFoundException, ResourceActionNotAllowedException, GameRuleException, FailedAuthenticationException, UsernameAlreadyExistsException {
        User user = this.userRepository.findByToken(token);

        return gameServiceDemo.postAcceptDemoGameRequestByUser(gameId, user);
    }



//    @CrossOrigin
//    @PutMapping(value= "/games/godcards")
//    Map<String, String> saveGodCards(
//    @RequestHeader("authorization") String token, @Valid @RequestBody Game newGameGodCards,
//    String godCard1, String godCard2){
//
//        HashMap<String, String> pathToGame = new HashMap<>();
//        pathToGame.put("path", this.service.postCreateGame(newGameGodCards));
//
//        return pathToGame;
//    }

    @CrossOrigin
    @PutMapping(value= "/games/godcards")
    Map<String, String> saveGodCards(
            @RequestHeader("authorization") String token,
            @Valid @RequestBody Game newGameGodCards){

        HashMap<String, String> pathToGame = new HashMap<>();

        pathToGame.put("path", this.service.postCreateGame(newGameGodCards));

        return pathToGame;
    }



//    @GetMapping(value = "/games/invitations",produces = "application/json;charset=UTF-8")
//    @ResponseStatus(HttpStatus.OK)
//
//    public ArrayList<String> godCards2OutOf10 (
//            @RequestHeader("authorization") String token,
//            @RequestBody Game newGame
//            )
//    {
//        ArrayList<String> godCards2OutOf10= godCardService.getGodcard1(newGame);
//
//
//        return "{'turns':[" +
//                "{'id':1,'createTime':'2019-04-03 12:22:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':true,'events':[]}," +
//                "{'id':2,'createTime':'2019-04-03 12:23:02','performedBy':{'id':2,'name':'testPlayer2','figures':['figure3','figure4']},'finished':true,'events':[]}," +
//                "{'id':3,'createTime':'2019-04-03 12:24:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':false,'events':[]}" +
//                "]}";
//    }









    @GetMapping(value = "/games/{id}",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public Game getGameById (
            @RequestHeader("authorization") String token,
            @PathVariable(value="id") long id)
    {
        return service.getGameById(id);
    }



    // Get turns of Game
    @GetMapping(value = "/games/{id}/turns",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getTurnsByGame (
            @PathVariable long id)
    {
        return "{'turns':[" +
                "{'id':1,'createTime':'2019-04-03 12:22:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':true,'events':[]}," +
                "{'id':2,'createTime':'2019-04-03 12:23:02','performedBy':{'id':2,'name':'testPlayer2','figures':['figure3','figure4']},'finished':true,'events':[]}," +
                "{'id':3,'createTime':'2019-04-03 12:24:02','performedBy':{'id':1,'name':'testPlayer','figures':['figure1','figure2']},'finished':false,'events':[]}" +
                "]}";
    }





    // Create new Move in Game
    @PostMapping(value = "/games/{id}/turns",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public String postCreateTurn (
            @PathVariable long id)
    {
        return "{'path':'/games/"+id+"/turns/1'}";
    }


    // Fetch all games
    @GetMapping("/games")
    public Iterable<Game> getAllGames (
            @RequestHeader("authorization") String token) throws FailedAuthenticationException
    {
        return service.getAllGames(token);
    }


    // Fetch all games of the logged in user
    @GetMapping("/games/invitations")
    public Iterable<Game> getGamesForUser2 (
            @RequestHeader("authorization") String token)
    {
        User user2 = this.userRepository.findByToken(token);
        return service.getGamesForUser2AndStatus(user2, GameStatus.INITIALIZED);
    }




    @PostMapping("/games/{id}/reject")
    void postCancelGameRequest (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId,
            HttpServletResponse response) throws ResourceNotFoundException, ResourceActionNotAllowedException
    {
        Game game = this.service.getGameById(gameId);

        if(game.getStatus()==GameStatus.STARTED){
            User loser = this.userRepository.findByToken(token);
            User winner = new User();

            if(game.getUser1().getId()==loser.getId()){
                winner = game.getUser2();
            }
            else{winner=game.getUser1();
            }
            game.setWinnerId(winner.getId());
            game.setLoserId(loser.getId());
            service.postCancelGameRequestByUser(gameId, loser);
            response.setStatus(204);
        }
        else{


        User user = this.userRepository.findByToken(token);
        service.postCancelGameRequestByUser(gameId, user);
        response.setStatus(204);}
    }

    // Get players of Game
    @GetMapping(value = "/games/{id}/players",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String getPlayersByGame (
            @PathVariable long id)
    {
        return "{'players':[" +
                "{'id':1,'name':'testPlayer','figures':['figure1','figure2']}," +
                "{'id':2,'name':'testPlayer2','figures':['figure3','figure4']}" +
                "]}";
    }
}
