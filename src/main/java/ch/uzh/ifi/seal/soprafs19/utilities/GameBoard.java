package ch.uzh.ifi.seal.soprafs19.utilities;

import ch.uzh.ifi.seal.soprafs19.entity.BoardItem;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class GameBoard {

	private final FigureRepository figureRepository;
	private final BuildingRepository buildingRepository;

	private Game game;

	private Map<Position, BoardItem> items = new HashMap<>();

	public Game getGame() {return game;}

	public void setGame(Game game) {this.game = game;}

	public Map<Position, BoardItem> getItems() {return items;}

	public void setItems(Map<Position, BoardItem> items) {this.items = items;}

	@Autowired
	public GameBoard(Game game, FigureRepository figureRepository, BuildingRepository buildingRepository)
	{
		this.game = game;
		this.figureRepository = figureRepository;
		this.buildingRepository = buildingRepository;

		// Add all figures to the items list
		figureRepository.findAllByGame(game).forEach(figure->{
			this.getItems().put(figure.getPosition(), figure);
		});

		// Add all buildings to the items list
		buildingRepository.findAllByGame(game).forEach(building->{
			this.getItems().put(building.getPosition(), building);
		});
	}
}
