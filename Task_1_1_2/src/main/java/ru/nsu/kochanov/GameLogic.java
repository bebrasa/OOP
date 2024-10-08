package ru.nsu.kochanov;

import java.util.Scanner;

public class GameLogic {

    public void startGame() {

        Scanner in = new Scanner(System.in);

        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Сколько раундов вы хотите сыграть?");

        int rounds = in.nextInt();
        int playerW = 0;
        int dealerW = 0;
        int draws = 0;  // Счетчик ничьих

        for (int i = 1; i < rounds + 1; i++) {
            Deck deck = new Deck();
            deck.shuffle();
            Player player = new Player();
            Dealer dealer = new Dealer();
            System.out.println("Раунд " + i);
            player.getPlayerCards(deck);
            dealer.getPlayerCards(deck);
            System.out.println("Дилер раздал карты");
            System.out.print("  Ваши карты: ");
            System.out.println("[" + player.showPlayerCards() + "]"
                    + "   -->  " + player.playerScore());
            if (player.playerScore() == 21) {
                playerW++;
                System.out.println("Вы выиграли раунд! Счёт " + playerW + ":"
                        + dealerW + " в вашу пользу");
                break;
            }
            System.out.print("  Карты дилера: ");
            System.out.println("[" + dealer.showPlayerCards() + "]");
            System.out.println("Ваш ход:\n-------");
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            int playerChoice = in.nextInt();
            while (playerChoice != 0) {
                player.getPlayerCards(deck);
                System.out.println("Вы открыли карту " + player.showLastCard());
                System.out.print("  Ваши карты: ");
                System.out.println("[" + player.showPlayerCards() + "]"
                        + "   -->  " + player.playerScore());
                if (player.playerScore() == 21) {
                    playerW++;
                    System.out.println("Вы выиграли раунд! Счёт " + playerW
                            + ":" + dealerW + " в вашу пользу");
                    break;
                }
                if (player.playerScore() > 21) {
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW
                            + ":" + dealerW + " в пользу дилера");
                    break;
                }
                System.out.print("  Карты дилера: ");
                System.out.println("[" + dealer.showPlayerCards() + "]");
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
                playerChoice = in.nextInt();
            }
            if (player.playerScore() < 21) {
                System.out.println("Ход дилера\n-------");
                System.out.println("Дилер открывает закрытую карту " + dealer.showLastCard());
                System.out.print("  Ваши карты: ");
                System.out.println("[" + player.showPlayerCards() + "]"
                        + "   -->  " + player.playerScore());
                System.out.print("  Карты дилера: ");
                System.out.println("[" + dealer.showAllCards() + "]"
                        + "   -->  " + dealer.playerScore());
                if (dealer.playerScore() == 21) {
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW
                            + ":" + dealerW + " в пользу дилера");
                    break;
                }

                while (dealer.playerScore() <= 17) {
                    dealer.getPlayerCards(deck);
                    System.out.println("Дилер открывает карту " + dealer.showLastCard());
                    System.out.print("  Ваши карты: ");
                    System.out.println("[" + player.showPlayerCards() + "]"
                            + "   -->  " + player.playerScore());
                    System.out.print("  Карты дилера: ");
                    System.out.println("[" + dealer.showAllCards() + "]"
                            + "   -->  " + dealer.playerScore());
                    if (dealer.playerScore() > 21) {
                        playerW++;
                        System.out.println("Вы выиграли раунд! Счёт " + playerW
                                + ":" + dealerW + " в вашу пользу");
                        break;
                    }
                    if (dealer.playerScore() == 21) {
                        dealerW++;
                        System.out.println("Вы проиграли раунд! Счёт " + playerW
                                + ":" + dealerW + " в пользу дилера");
                        break;
                    }
                }

                if (dealer.playerScore() == player.playerScore()) {
                    draws++;
                    System.out.println("Ничья в раунде! Счёт " + playerW + ":"
                            + dealerW + " (ничьи: " + draws + ")");
                } else if (dealer.playerScore() > player.playerScore() && dealer.playerScore() < 21) {
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW
                            + ":" + dealerW + " в пользу дилера");
                } else if (player.playerScore() > dealer.playerScore() && player.playerScore() < 21) {
                    playerW++;
                    System.out.println("Вы выиграли раунд! Счёт " + playerW
                            + ":" + dealerW + " в вашу пользу");
                }
            }
        }
    }
}