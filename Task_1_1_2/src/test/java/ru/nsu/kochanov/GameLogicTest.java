package ru.nsu.kochanov;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLogicTest {

    private GameLogic game;
    private Player player;
    private Dealer dealer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        Deck deck = new Deck(); // Создаем колоду
        deck.shuffle();         // Перемешиваем
        player = new Player();
        dealer = new Dealer();
        game = new GameLogic();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testPlayerCardsCount() {
        Deck deck = new Deck();
        player.getPlayerCards(deck);
        assertEquals(2, player.getPlayerCount());
    }

    @Test
    void testPlayerScore() {
        Deck deck = new Deck();
        player.getPlayerCards(deck);
        int score = player.playerScore();
        assertTrue(score > 0 && score <= 21);
    }

    @Test
    void testDealerScore() {
        Deck deck = new Deck();
        dealer.getPlayerCards(deck);
        int score = dealer.playerScore();
        assertTrue(score > 0 && score <= 21);
    }

    @Test
    void testStartGame() {
        // Подготавливаем ввод для симуляции игры
        String simulatedInput = "1\n1\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Запускаем игру
        GameLogic game = new GameLogic();
        game.startGame();

        // Проверяем, что нужные строки были выведены
        String output = outContent.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"), "Ожидается вывод приветствия");
        assertTrue(output.contains("Сколько раундов вы хотите сыграть?"),
                "Ожидается вывод про количество раундов");
        assertTrue(output.contains("Ваши карты:"), "Ожидается вывод карт игрока");
        assertTrue(output.contains("Карты дилера:"), "Ожидается вывод карт дилера");
    }
}