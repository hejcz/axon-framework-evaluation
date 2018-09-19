package hejcz;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class App implements CommandLineRunner {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    private final EventStore eventStore;

    public App(CommandGateway commandGateway, QueryGateway queryGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.eventStore = eventStore;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        commandGateway.sendAndWait(new IssueCmd("gc1", 100));
        commandGateway.sendAndWait(new IssueCmd("gc2", 50));
        commandGateway.sendAndWait(new RedeemCmd("gc1", 10));
        commandGateway.sendAndWait(new RedeemCmd("gc1", 10));
        commandGateway.sendAndWait(new RedeemCmd("gc2", 20));

        queryGateway.query(new FetchCardSummariesQuery(2, 0), ResponseTypes.multipleInstancesOf(CardSummary.class))
            .get()
            .forEach(System.out::println);

        eventStore.readEvents("gc1")
            .asStream()
            .map(Message::getPayload)
            .forEach(System.out::println);
    }
}
