package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.BoardItemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DynamicUpdate
public class Building extends BoardItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("type")
	public BoardItemType getType() {
		return BoardItemType.BUILDING;
	}

}
