package ch.uzh.ifi.seal.soprafs19.service.game.service;


import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class GodcardService {

    private final Logger log = LoggerFactory.getLogger(FigureService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final ch.uzh.ifi.seal.soprafs19.repository.MoveRepository moveRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private String godcard1;
    private String godcard2;


@Autowired
public GodcardService(FigureRepository figureRepository,
        BuildingRepository buildingRepository, MoveRepository moveRepository,
        GameRepository gameRepository, GameService gameService) {
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

}


    public String getGodcard1(ArrayList<String> godCard1) {
        return godCard1.get(0);
    }

    public String getGodcard2(ArrayList<String> godCard1) {
        return godCard1.get(1);
    }

    public String getGodcard2() {
        return godcard2;
    }

    public void setGodcard1(String godcard2) {

        this.godcard2 = godcard1;
    }

    public void setGodcard2(String godcard2) {

        this.godcard2 = godcard2;
    }
}

