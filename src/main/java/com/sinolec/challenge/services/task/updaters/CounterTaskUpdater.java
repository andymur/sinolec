package com.sinolec.challenge.services.task.updaters;

import com.sinolec.challenge.model.CounterTask;
import org.springframework.stereotype.Component;

@Component
public class CounterTaskUpdater implements GeneralTaskUpdater<CounterTask> {

    @Override
    public CounterTask update(CounterTask existing, CounterTask task) {
        existing.setCreationDate(task.getCreationDate());
        existing.setName(task.getName());
        existing.setX(task.getX());
        existing.setY(task.getY());
        return existing;
    }
}
