package ru.nsu.kochanov;
import java.util.Scanner;

public class GameLogic {

    public void startGame(){


        Scanner in = new Scanner(System.in);



        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Сколько раундов вы хотите сыграть?");

        int rounds = in.nextInt();
        int playerW = 0;
        int dealerW = 0;

        for (int i = 1; i < rounds + 1; i++){
            Deck deck = new Deck();
            deck.shuffle();
            Player player = new Player(deck);
            Dealer dealer = new Dealer(deck);
            System.out.println("Раунд "+ i);
            player.getPlayerCards();
            dealer.getPlayerCards();
            System.out.println("Диллер раздал карты");
            System.out.print("  Ваши карты: ");
            System.out.println("[" + player.ShowPlayerCards() + "]" + "   -->  " + player.playerScore());
            if (player.playerScore() == 21){
                playerW++;
                System.out.println("Вы выиграли раунд! Счёт " + playerW + ":" + dealerW + " в вашу пользу");
                break;
            }
            System.out.print("  Карты дилера: ");
            System.out.println("[" + dealer.ShowPlayerCards() + "]");
            System.out.println("Ваш ход:\n-------");
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            int playerChoise = in.nextInt();
            while (playerChoise != 0){
                player.getPlayerCards();
                System.out.println("Вы открыли карту " + player.ShowLastCard());
                System.out.print("  Ваши карты: ");
                System.out.println("[" + player.ShowPlayerCards() + "]" + "   -->  " + player.playerScore());
                if (player.playerScore() == 21){
                    playerW++;
                    System.out.println("Вы выиграли раунд! Счёт " + playerW + ":" + dealerW + " в вашу пользу");
                    break;
                }
                if (player.playerScore() > 21){
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW + ":" + dealerW + " в пользу дилера");
                    break;
                }
                System.out.print("  Карты дилера: ");
                System.out.println("[" + dealer.ShowPlayerCards() + "]");
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
                playerChoise = in.nextInt();
            }
            if (player.playerScore() < 21){
                System.out.println("Ход дилера\n-------");
                System.out.println("Дилер открывает закрытую карту " + dealer.ShowLastCard());
                System.out.print("  Ваши карты: ");
                System.out.println("[" + player.ShowPlayerCards() + "]" + "   -->  " + player.playerScore());
                System.out.print("  Карты дилера: ");
                System.out.println("[" + dealer.ShowAllCards() + "]" + "   -->  " + dealer.playerScore());
                if (dealer.playerScore() == 21){
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW + ":" + dealerW + " в пользу дилера");
                    break;
                }

                while (dealer.playerScore() <= 17){


                    dealer.getPlayerCards();
                    System.out.println("Дилер открывает карту " + dealer.ShowLastCard());
                    System.out.print("  Ваши карты: ");
                    System.out.println("[" + player.ShowPlayerCards() + "]" + "   -->  " + player.playerScore());
                    System.out.print("  Карты дилера: ");
                    System.out.println("[" + dealer.ShowAllCards() + "]" + "   -->  " + dealer.playerScore());
                    if (dealer.playerScore() > 21)
                    {
                        playerW++;
                        System.out.println("Вы выиграли раунд! Счёт " + playerW + ":" + dealerW + " в вашу пользу");
                        break;
                    }
                }
                if ( dealer.playerScore() > player.playerScore() && dealer.playerScore() < 21){
                    dealerW++;
                    System.out.println("Вы проиграли раунд! Счёт " + playerW + ":" + dealerW + " в пользу дилера");
                    continue;
                }
                if (player.playerScore() > dealer.playerScore() && player.playerScore() < 21){
                    playerW++;
                    System.out.println("Вы выиграли раунд! Счёт " + playerW + ":" + dealerW + " в вашу пользу");
                    continue;
                }
            }
        }
    }
}
