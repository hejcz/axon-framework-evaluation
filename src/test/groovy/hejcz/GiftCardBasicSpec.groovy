package hejcz

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import spock.lang.Specification

class GiftCardBasicSpec extends Specification {

    private FixtureConfiguration<GiftCard> fixture;

    void setup() {
        fixture = new AggregateTestFixture<>(GiftCard.class);
    }

    def "gift card issued"() {
        expect:
        fixture.given()
            .when(new IssueCmd(id, initialValue))
            .expectEvents(new IssuedEvt(id, initialValue))

        where:
        id << ["card 1", "some another id"]
        initialValue << [100, 200]
    }

    def "initial value redeemed"() {
        expect:
        fixture.given(new IssuedEvt("C1", 50))
            .when(new RedeemCmd("C1", 50))
            .expectSuccessfulHandlerExecution()
    }

    def "more than initial value redeemed"() {
        expect:
        fixture.given(new IssuedEvt("C1", 50), new RedeemedEvt("C1", 50))
            .when(new RedeemCmd("C1", 1))
            .expectException(IllegalStateException)
            .expectExceptionMessage("amount > remaining value")
    }

    def "negative amount redeemed"() {
        expect:
        fixture.given(new IssuedEvt("C1", 50))
            .when(new RedeemCmd("C1", -1))
            .expectException(IllegalArgumentException)
            .expectExceptionMessage("amount <= 0")
    }

    def "zero amount redeemed"() {
        expect:
        fixture.given(new IssuedEvt("C1", 50))
            .when(new RedeemCmd("C1", 0))
            .expectException(IllegalArgumentException)
            .expectExceptionMessage("amount <= 0")
    }

}
