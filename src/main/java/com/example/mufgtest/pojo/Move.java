package com.example.mufgtest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Move {
    @JsonProperty("O")
    private int order;

    @JsonProperty("L")
    private int left;

    @JsonProperty("R")
    private int right;

    @JsonProperty("F")
    private int forward;

    @JsonProperty("B")
    private int backward;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public int getBackward() {
        return backward;
    }

    public void setBackward(int backward) {
        this.backward = backward;
    }
}
