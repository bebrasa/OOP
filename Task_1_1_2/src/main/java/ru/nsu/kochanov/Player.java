package ru.nsu.kochanov;

import java.util.ArrayList;

public class Player {
    Deck deck = new Deck();
    public ArrayList<Card> playerCards;

    public Player(Deck deck) {
        this.playerCards = new ArrayList<>();
        this.deck.shuffle();
    }

    public void getPlayerCards() {
        if (playerCards.isEmpty()) {
            playerCards.add(deck.takeCard());
            playerCards.add(deck.takeCard());
        } else {
            playerCards.add(deck.takeCard());
        }
    }

    public int getPlayerCount() {
        return playerCards.size();
    }

    public String showPlayerCards() {
        StringBuilder cards = new StringBuilder();
        for (Card card : playerCards) {
            cards.append(card.getSuitRank()).append(", "); // Добавляем каждую карту и разделитель
        }

        // Удаляем последний разделитель, если коллекция не пустая
        if (cards.length() > 0) {
            cards.setLength(cards.length() - 2); // Удаляем последние ', '
        }

        return cards.toString(); // Возвращаем сформированную строку
    }

    public String showLastCard() {
        return playerCards.get(getPlayerCount() - 1).getSuitRank();
    }

    public int calcScore(ArrayList<Card> cards) {
        int score = 0;
        int acesCount = 0;

        for (Card card : cards) {
            String rank = card.getRank();
            switch (rank) {
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    score += Integer.parseInt(rank);
                    break;
                case "10":
                case "J":
                case "Q":
                case "K":
                    score += 10;
                    break;
                case "A":
                    acesCount++;
                    break;
            }
        }
        for (int i = 0; i < acesCount; i++) {
            if (score + 11 <= 21) {
                score += 11;
            } else {
                score += 1;
            }
        }
        return score;
    }

    public int playerScore() {
        return calcScore(playerCards);
    }
}

class Dealer extends Player {
    private ArrayList<Card> dealerCards;

    public Dealer(Deck deck) {
        super(deck);
    }

    @Override
    public String showPlayerCards() {
        if (getPlayerCount() > 1) {
            StringBuilder cards = new StringBuilder();
            for (int i = 0; i < getPlayerCount() - 1; i++) {
                cards.append(playerCards.get(i).getSuitRank()).append(", ");
            }
            cards.append("<скрытая карта>");
            return cards.toString();
        }
        return super.showPlayerCards();
    }

    public String showAllCards() {
        return super.showPlayerCards();
    }

}