package hejcz;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;

import java.util.concurrent.ExecutionException;

class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CardSummaryProjection projection = new CardSummaryProjection();
        EventHandlingConfiguration eventHandlingConfiguration = new EventHandlingConfiguration();
        eventHandlingConfiguration.registerEventHandler(c -> projection);

        Configuration configuration = DefaultConfigurer.defaultConfiguration()
            .configureAggregate(GiftCard.class) // (1)
            .configureEventStore(c -> new EmbeddedEventStore(new InMemoryEventStorageEngine())) //(2)
            .registerModule(eventHandlingConfiguration) // (3)
            .registerQueryHandler(c -> projection) // (4)
            .buildConfiguration(); // (5)

        configuration.start();

        CommandGateway commandGateway = configuration.commandGateway();
        QueryGateway queryGateway = configuration.queryGateway();

        commandGateway.sendAndWait(new IssueCmd("gc1", 100));
        commandGateway.sendAndWait(new IssueCmd("gc2", 50));
        commandGateway.sendAndWait(new RedeemCmd("gc1", 10));
        commandGateway.sendAndWait(new RedeemCmd("gc2", 20));

        queryGateway.query(new FetchCardSummariesQuery(2, 0), ResponseTypes.multipleInstancesOf(CardSummary.class))
            .get()
            .forEach(System.out::println);
    }
}
