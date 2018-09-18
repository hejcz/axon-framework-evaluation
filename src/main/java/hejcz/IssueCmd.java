package hejcz;

public class IssueCmd {

    private final String id;
    private final Integer amount;

    public IssueCmd(String id, Integer amount) {
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
