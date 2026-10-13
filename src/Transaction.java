// 1件の収入/支出を表す
import java.time.LocalDate;

public class Transaction {
    private int id;
    private TransactionType type; // enum
    private long amount;
    private String category;
    private LocalDate date;
    private String memo;

    // コンストラクタ
    public Transaction(int id, TransactionType type, long amount, String category, LocalDate date, String memo) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.memo = memo;
    }

    // getter
    public TransactionType getTransactionType() {
        return type;
    }

    public String getTypeName() {
        return type.getDisplayName();
    }

    public int getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }
}