package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private GameStatus status;

	@Column(nullable = false)
	private Boolean isGodPower;

    @OneToOne
	private User currentTurn;

	// Player 1 is the challenger
    @OneToOne
	private User user1;

	// Player 2 is the challenged one
    @OneToOne
	private User user2;

	@Column(nullable = false)
	private long user1Id;

    @Column(nullable = false)
    private long user2Id;

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

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Boolean getGodPower() { return isGodPower; }

	public void setGodPower(Boolean godPower) {
		isGodPower = godPower;
	}

    public User getUser1() { return user1; }

    public void setUser1(User user1) { this.user1 = user1; }

    public User getUser2() { return user2; }

    public void setUser2(User user2) { this.user2 = user2; }

    public long getUser1Id() { return user1Id; }

    public void setUser1Id(int user1Id) { this.user1Id = user1Id; }

    public long getUser2Id() { return user2Id; }

    public void setUser2Id(int user2Id) { this.user2Id = user2Id; }

    public User getCurrentTurn() { return currentTurn; }

    public void setCurrentTurn(User currentTurn) { this.currentTurn = currentTurn; }

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Game)) {
			return false;
		}
		Game user = (Game) o;
		return this.getId().equals(user.getId());
	}
}
