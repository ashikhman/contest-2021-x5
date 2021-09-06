package com.ashikhman.x5.eventcollector;

import com.ashikhman.x5.event.EventInterface;
import com.ashikhman.x5.event.UnemployedCheckoutLineEvent;
import com.ashikhman.x5.repository.CheckoutLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnemployedCheckoutLineEventCollector implements EventCollectorInterface {

    private final CheckoutLineRepository checkoutLineRepository;

    @Override
    public List<EventInterface> execute() {
        var events = new ArrayList<EventInterface>();
        for (var line : checkoutLineRepository.getAll()) {
            if (null == line.getEmployeeId()) {
                var event = new UnemployedCheckoutLineEvent()
                        .setLine(line);

                events.add(event);
            }
        }

        return events;
    }
}
