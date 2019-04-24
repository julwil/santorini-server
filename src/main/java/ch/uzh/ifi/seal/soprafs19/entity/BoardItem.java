package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BoardItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	private Long id;

	private int x;

	private int y;

	private int z;

	@ManyToOne
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

	public Position getPosition()
    {
        return new Position(this.x, this.y, this.z);
    }

	public void setPosition(Position position)
    {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }

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