package hejcz;

import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {
    @Bean
    EventStore eventStore() {
        return new EmbeddedEventStore(new InMemoryEventStorageEngine());
    }

    @Bean
    SnapshotTriggerDefinition everyThreeEventsSnapshotTriggerDefinition(EventStore eventStore, SpringPrototypeAggregateFactory<GiftCard> factory) {
        return new EventCountSnapshotTriggerDefinition(new AggregateSnapshotter(eventStore, factory), 3);
    }
}
