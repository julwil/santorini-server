package ch.uzh.ifi.seal.soprafs19.entity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Move implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private Long id;


	@ManyToOne
    private Figure figure;

    private int x;

    private int y;

    private int z;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	/*
	 * Getters and Setters
	 */

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Move)) {
			return false;
		}
		Move user = (Move) o;
		return this.getId().equals(user.getId());
	}
}
