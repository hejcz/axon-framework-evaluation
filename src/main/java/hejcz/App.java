package hejcz;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class App implements CommandLineRunner {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    public App(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        commandGateway.sendAndWait(new IssueCmd("gc1", 100));
        commandGateway.sendAndWait(new IssueCmd("gc2", 50));
        commandGateway.sendAndWait(new RedeemCmd("gc1", 10));
        commandGateway.sendAndWait(new RedeemCmd("gc2", 20));

        queryGateway.query(new FetchCardSummariesQuery(2, 0), ResponseTypes.multipleInstancesOf(CardSummary.class))
            .get()
            .forEach(System.out::println);
    }

    @Bean
    EventStore eventStore() {
        return new EmbeddedEventStore(new InMemoryEventStorageEngine());
    }
}
