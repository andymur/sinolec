package com.sinolec.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import java.util.Optional;

@Entity
public class CounterTask extends GeneralTask {

    // time stamp in seconds(!)
    private long startedAt = 0;

    // time stamp in seconds(!)
    private long cancelledAt = 0;

    private int x;

    private int y;

    public long getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(long cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    @JsonIgnore
    public boolean isCancelled() {
        return cancelledAt > 0;
    }

    @JsonIgnore
    public boolean isExecuted() {
        return startedAt > 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Optional<String> validate() {
        final Optional<String> generalTaskValidationResult = super.validate();
        if (generalTaskValidationResult.isPresent()) {
            return generalTaskValidationResult;
        }
        if (x < 0) {
            return Optional.of("Y of the counter task must be greater or equal to zero");
        }
        if (y <= 0) {
            return Optional.of("Y of the counter task must be greater than zero");
        }
        if (x >= y) {
            return Optional.of("X of the counter task must be lesser than Y");
        }
        return Optional.empty();
    }
}
