package controller.card;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DrawableCardModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DrawableCardController {

    private Queue<DrawableCardModel> chances = new ConcurrentLinkedQueue<>();
    private Queue<DrawableCardModel> communities = new ConcurrentLinkedQueue<>();
    private boolean shuffle = true;

    public enum Type {
        CHANCE,
        COMMUNITY_CHEST;
    }

    public DrawableCardController(boolean shuffle) {

        List<DrawableCardModel> chances = Collections.emptyList();
        List<DrawableCardModel> communities = Collections.emptyList();

        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonChancesURL = getClass().getClassLoader().getResource("chances.json");
        URL jsonCommunityChestsURL = getClass().getClassLoader().getResource("communityChests.json");
        if (jsonChancesURL != null && jsonCommunityChestsURL != null) {
            try {
                chances = jacksonMapper.readValue(new File(jsonChancesURL.toURI()), new TypeReference<>() {
                });
                communities = jacksonMapper.readValue(new File(jsonCommunityChestsURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        if (shuffle) {
            Collections.shuffle(chances);
            Collections.shuffle(communities);
        }
        this.chances.addAll(chances);
        this.communities.addAll(communities);

    }

    public DrawableCardModel draw(Type type) {
        DrawableCardModel card;
        if (Type.CHANCE.equals(type)) {
            card = chances.poll();
            if (card != null && !card.isKeep()) {
                chances.add(card);
            }
        } else {
            card = communities.poll();
            if (card != null && !card.isKeep()) {
                communities.add(card);
            }
        }
        return card;
    }

    public Queue<DrawableCardModel> getChances() {
        return chances;
    }

    public Queue<DrawableCardModel> getCommunities() {
        return communities;
    }

}
