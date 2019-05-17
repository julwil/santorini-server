package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.deserializers.GameDeserializer;
import ch.uzh.ifi.seal.soprafs19.deserializers.GameSerializer;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.turn.Turn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@DynamicUpdate @JsonDeserialize(using = GameDeserializer.class) @JsonSerialize(using = GameSerializer.class)
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

//	@Column
//	private ArrayList<String> godCards;

	@OneToOne
	private User currentTurn;

    @Column
	private long lastActiveFigureId;

	// Player 1 is the challenger
    @OneToOne
	private User user1;

	// Player 2 is the challenged one
    @OneToOne
	private User user2;

    @Column
	private String god1;

    @Column
	private String god2;

    @Column
    private long winner;

	@Column
	private long loser;

	@Transient
	private Turn turn;

	@Column
	private long demo;

	@Column
	private long athenaMoveUp;

	@Column
	private ArrayList<String> godCardsList;

//	@OneToOne
//	private User winner;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	/*
	 * Getters and Setters
	 */

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

	public void setDemo(long demoLong) { this.demo=demoLong; }

	public long checkIfDemo(){
		return demo;
	}

    public User getUser2() { return user2; }

    public void setUser2(User user2) { this.user2 = user2; }

    public User getCurrentTurn() { return currentTurn; }

    public void setCurrentTurn(User currentTurn) { this.currentTurn = currentTurn; }

//	public User getWinner() {return winner;	}


	public Turn getTurn() {	return turn;}

	public void setTurn(Turn turn) {this.turn = turn;}

	public void deactivateAthenaMovedUp(){
		this.athenaMoveUp=0;
	}

	public void activateAthenaMovedUp(){
		this.athenaMoveUp=1;
	}

	public long statusAthenaMovedUp(){
		return athenaMoveUp;
	}

	public User checkForAthena(){
			if (god1.equals("Athena")){
		return user1;}
				else{return user2;
	}
	}



	@JsonIgnore
	public long getLastActiveFigureId() {return lastActiveFigureId;}

	@JsonIgnore
	public void setLastActiveFigureId(long lastActiveFigureId) {this.lastActiveFigureId = lastActiveFigureId; }

	@JsonIgnore
    public boolean isMoveAllowedByUserId(long userId)
    {
        return turn.isMoveAllowedByUserId(userId);
    }

	@JsonIgnore
	public boolean isPlaceFigureAllowedByUserId(long userId)
	{
		return turn.isPlaceFigureAllowedByUserId(userId);
	}

    @JsonIgnore
    public boolean isBuildAllowedByUserId(long userId)
    {
        return turn.isBuildAllowedByUserId(userId);
    }

	public ArrayList<String> getGodCardsList() {
		return godCardsList;
	}

	public void setGodCardsList(ArrayList<String> godCardsList) {
		this.godCardsList = godCardsList;
	}

	@JsonIgnore
	public void swapTurns()
	{
		turn.swap();
	}

    @Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Game)) {
			return false;
		}
		Game user = (Game) o;
		return this.getId().equals(user.getId());
	}

	public void setWinnerId(long ownerId){

				this.winner=ownerId;}

	public void setLoserId(long ownerId){

		this.loser=ownerId;}

	public long getWinner(){
		return winner;
	}
	public long getLoser(){
		return loser;
	}
	public void setGod2(String selectedGodPower) {
		this.god2 = selectedGodPower;
	}

	public String getGod1(){
		return god1;
	}
	public String getGod2(){
		return god2;
	}

	public void setGod1(String remainginGodPower) {
		this.god1 = remainginGodPower;
	}


//	public long getWinnerId()
//	{
//		return winnerId;
//	}

//	public void setWinnerId(User winner) {this.winner = winner;}
}