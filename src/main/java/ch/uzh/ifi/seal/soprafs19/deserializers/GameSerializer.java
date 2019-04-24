package ch.uzh.ifi.seal.soprafs19.deserializers;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GameSerializer extends StdSerializer<Game> {
    public GameSerializer() {
        this(null);
    }

    public GameSerializer(Class<Game> t) {
        super(t);
    }

    @Override
    public void serialize(Game game, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", game.getId());
        jgen.writeStringField("status", game.getStatus().toString());
        jgen.writeBooleanField("isGodPower", game.getGodPower());
        jgen.writeNumberField("user1", game.getUser1().getId());
        jgen.writeNumberField("user2", game.getUser2().getId());
        jgen.writeNumberField("currentTurn", game.getCurrentTurn().getId());
        jgen.writeStringField("createdOn", game.getCreatedOn().toString());
        jgen.writeEndObject();
    }
}
