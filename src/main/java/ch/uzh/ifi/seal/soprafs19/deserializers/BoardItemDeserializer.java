//package ch.uzh.ifi.seal.soprafs19.deserializers;
//
//import ch.uzh.ifi.seal.soprafs19.constant.BoardItemType;
//import ch.uzh.ifi.seal.soprafs19.entity.*;
//import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
//import ch.uzh.ifi.seal.soprafs19.utilities.Position;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.ObjectCodec;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//
//public class BoardItemDeserializer extends JsonDeserializer<Game> {
//    @Autowired
//    public BoardItemDeserializer() {}
//
//    @Override
//    public Figure deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//        /**
//         * deserializes JSON body to a board item.
//         */
//        try {
//            ObjectCodec oc = jp.getCodec();
//            JsonNode node = oc.readTree(jp);
//
//            final int x = node.at("/position/x").asInt();
//            final int y = node.at("/position/y").asInt();
//            final int z = node.at("/position/z").asInt();
//            final int gameId = node.at("game").asInt();
//            final String type = node.get("type").asText();
//
//            Position position = new Position(x, y, z);
//            Game game = gameRepository.findById(gameId);
//            Figure item = type.equals(BoardItemType.FIGURE) ? new Figure() : new Building();
//            item.setPosition(position);
//            item.setGame(game);
//
//            return item;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
