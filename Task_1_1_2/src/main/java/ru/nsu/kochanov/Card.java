package ru.nsu.kochanov;

public class Card {
    private String rank;
    private String suit;


    public  Card(String rank, String suit){
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank(){
        return rank;
    }

    public String getSuitRank(){
        return rank + " " + suit;
    }
}
