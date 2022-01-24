package it.monopoly.controller.board.card;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.model.DrawableCardModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DrawableCardController {

    private Queue<DrawableCardModel> chancesCards = new ConcurrentLinkedQueue<>();
    private Queue<DrawableCardModel> communitiesCards = new ConcurrentLinkedQueue<>();

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
                chances = Collections.emptyList();
                communities = Collections.emptyList();
            }
        }

        if (shuffle) {
            Collections.shuffle(chances);
            Collections.shuffle(communities);
        }
        this.chancesCards.addAll(chances);
        this.communitiesCards.addAll(communities);

    }

    public DrawableCardModel draw(Type type) {
        DrawableCardModel card;
        if (Type.CHANCE.equals(type)) {
            card = chancesCards.poll();
            if (card != null && !card.isKeep()) {
                chancesCards.add(card);
            }
        } else {
            card = communitiesCards.poll();
            if (card != null && !card.isKeep()) {
                communitiesCards.add(card);
            }
        }
        return card;
    }

    public Queue<DrawableCardModel> getChancesCards() {
        return chancesCards;
    }

    public Queue<DrawableCardModel> getCommunitiesCards() {
        return communitiesCards;
    }

}
