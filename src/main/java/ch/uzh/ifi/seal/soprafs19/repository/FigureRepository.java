package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface FigureRepository extends CrudRepository<Figure, Long> {
	Figure findById(long id);
	boolean existsById(long id);
	Iterable<Figure> findAllByGame(Game game);

    Collection<Figure> findAllByGameAndOwnerId(Game game, long ownerId);
}
