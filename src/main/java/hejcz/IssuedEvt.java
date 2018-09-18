package hejcz;

public class IssuedEvt {

    private final String id;
    private final Integer amount;

    public IssuedEvt(String id, Integer amount) {
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