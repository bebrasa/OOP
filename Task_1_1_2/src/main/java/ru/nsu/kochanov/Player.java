package ru.nsu.kochanov;

import java.util.ArrayList;

/**
 * Represents a Player in the game, having a deck of cards and the ability to manage their hand.
 */
public class Player {
  Deck deck = new Deck();
  public ArrayList<Card> playerCards;

  /**
   * Constructs a Player with a given deck and shuffles the deck.
   *
   * @param deck The deck of cards used by the player.
   */
  public Player(Deck deck) {
    this.playerCards = new ArrayList<>();
    this.deck.shuffle();
  }

  /**
   * Deals cards to the player. If no cards are dealt yet, deals two cards;
   * otherwise, deals one card.
   */
  public void getPlayerCards() {
    if (playerCards.isEmpty()) {
      playerCards.add(deck.takeCard());
      playerCards.add(deck.takeCard());
    } else {
      playerCards.add(deck.takeCard());
    }
  }

  /**
   * Returns the number of cards the player has.
   *
   * @return The count of player's cards.
   */
  public int getPlayerCount() {
    return playerCards.size();
  }

  /**
   * Displays the player's cards in a string format.
   *
   * @return A string representing the player's cards, separated by commas.
   */
  public String showPlayerCards() {
    StringBuilder cards = new StringBuilder();
    for (Card card : playerCards) {
      cards.append(card.getSuitRank()).append(", "); // Adds each card and a separator
    }

    // Removes the last separator if the collection is not empty
    if (cards.length() > 0) {
      cards.setLength(cards.length() - 2); // Remove the last ', '
    }

    return cards.toString(); // Returns the constructed string
  }

  /**
   * Shows the last card of the player.
   *
   * @return A string representation of the last card's suit and rank.
   */
  public String showLastCard() {
    return playerCards.get(getPlayerCount() - 1).getSuitRank();
  }

  /**
   * Calculates the score based on the provided cards.
   *
   * @param cards The cards to calculate the score for.
   * @return The calculated score.
   */
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

  /**
   * Gets the current score of the player based on their cards.
   *
   * @return The player's score.
   */
  public int playerScore() {
    return calcScore(playerCards);
  }
}

/**
 * Represents a Dealer, who is also a Player, with additional functionalities
 * specific to the dealer's behavior.
 */
class Dealer extends Player {
  private ArrayList<Card> dealerCards;

  /**
   * Constructs a Dealer with a given deck.
   *
   * @param deck The deck of cards used by the dealer.
   */
  public Dealer(Deck deck) {
    super(deck);
  }

  /**
   * Displays the dealer's cards. Only shows the first card and a hidden card if there are more than one card.
   *
   * @return A string representation of the dealer's cards with the last card hidden.
   */
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

  /**
   * Shows all the dealer's cards.
   *
   * @return A string representation of all dealer's cards.
   */
  public String showAllCards() {
    return super.showPlayerCards();
  }
}