package hejcz;

public class RedeemedEvt {

    private final String id;
    private final Integer amount;

    public RedeemedEvt(String id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }
}
