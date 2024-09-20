package ru.nsu.kochanov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards;
    //Random rand = new Random();
    //private int n = rand.nextInt(1,9);

    public Deck() {

        cards = new ArrayList<Card>();

        String ranks[] = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "J", "Q", "K", "A"};
        String suits[] = new String[]{"червы", "трефы", "пики", "бубны"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card takeCard() {
        if (cards.size() > 0) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }


}
