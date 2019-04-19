package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
abstract class BoardItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	private Long id;

	//private Position position;

	@Column(nullable = false)
	private GameBoard board;

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

	public GameBoard getGameBoard() {return board;}

	public void setGameBoard(GameBoard board) {this.board = board;}

//	public Position getPosition() {return position;}
//
//	public void setPosition(Position position) {this.position = position;}

	public GameBoard getBoard() {return board;}

	public void setBoard(GameBoard board) {	this.board = board;}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof BoardItem)) {
			return false;
		}
		BoardItem user = (BoardItem) o;
		return this.getId().equals(user.getId());
	}
}
