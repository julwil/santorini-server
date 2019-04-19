package ch.uzh.ifi.seal.soprafs19.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private Game game;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {return createdOn;}

	public Game getGame() {return game;}

	public void setGame(Game game) {this.game = game;}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof GameBoard)) {
			return false;
		}
		GameBoard user = (GameBoard) o;
		return this.getId().equals(user.getId());
	}
}
