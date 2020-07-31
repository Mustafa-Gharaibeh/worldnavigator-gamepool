package com.worldnavigator.gamepoolservice.pool.model.material.gold;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class Gold implements Material, Comparable<Integer> {

    @JsonProperty("amount")
    AtomicInteger amount;

    @JsonCreator
    private Gold(@JsonProperty("amount") Integer amount) {
        this.amount = new AtomicInteger(amount);
    }

    public static Gold createGold(Integer amount) {
        if (Objects.requireNonNull(amount) >= 0) {
            return new Gold(amount);
        } else {
            return new Gold(0);
        }
    }

    public Integer getAmount() {
        return amount.get();
    }

    public void setAmount(Integer amount) {
        this.amount.addAndGet(amount);
    }

    @Override
    public String toString() {
        return "Gold{" + "amount=" + amount.get() + '}';
    }

    @Override
    public int compareTo(Integer o) {
        if (this.amount.get() == o) {
            return 0;
        } else if (this.amount.get() > o) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String name() {
        return "Gold";
    }
}
