package ch.uzh.ifi.seal.soprafs19.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	private Long id;

	@OneToOne
	@MapsId
	private Game game;

	@Transient
	private Set<BoardItem> items = new HashSet<>();

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	@JsonIgnore
	public Long getId() {return id;	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public LocalDateTime getCreatedOn() {return createdOn;}

	@JsonIgnore
	public Game getGame() {return game;}

	public Set<BoardItem> getItems() {return items;}

	public void setItems(Set<BoardItem> items) {this.items = items;}

	public void setGame(Game game) {this.game = game;}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof GameBoard)) {
			return false;
		}
		GameBoard board = (GameBoard) o;
		return this.getId().equals(board.getId());
	}
}
