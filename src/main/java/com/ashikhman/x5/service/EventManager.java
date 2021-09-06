package com.ashikhman.x5.service;

import com.ashikhman.x5.event.EventInterface;
import com.ashikhman.x5.eventcollector.EventCollectorInterface;
import com.ashikhman.x5.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EventManager {

    private final List<EventCollectorInterface> collectors;

    private final StateRepository stateRepository;

    private final Map<Integer, List<EventInterface>> history = new HashMap<>();

    public void execute() {
        var events = new ArrayList<EventInterface>();
        collectors.forEach(collector -> events.addAll(collector.execute()));

        if (!events.isEmpty()) {
            history.computeIfAbsent(stateRepository.get().getCurrentTick(), k -> new ArrayList<>())
                    .addAll(events);
        }
    }

    public void printAll() {
        var output = new StringBuilder();
        output.append("EVENTS: ");

        history.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    output.append(String.format("[%s]", entry.getKey()));

                    var joiner = new StringJoiner(";");
                    for (var event : entry.getValue()) {
                        joiner.add(event.toString());
                    }
                    output.append(joiner);
                });

        output.append("@");

        System.out.println(output);
    }
}
