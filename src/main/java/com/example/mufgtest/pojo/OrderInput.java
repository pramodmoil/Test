package com.example.mufgtest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class OrderInput implements Serializable {
    private static final long serialVersionUID = -6856851661362845452L;

    @JsonProperty("Position")
    private Position position;

    @JsonProperty("Move")
    private List<Move> move;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Move> getMove() {
        return move;
    }

    public void setMove(List<Move> move) {
        this.move = move;
    }
}
