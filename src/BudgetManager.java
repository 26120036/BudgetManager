// 取引の管理

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private List<Transaction> transactions;
    private int nextId; 

    // コンストラクタ
    public BudgetManager() {
        this.transactions = new ArrayList<>();
        this.nextId = 1;
    }
    
    // 取引の追加(収入/支出)
    public void addTransaction(TransactionType type, long amount, String category, LocalDate date, String memo) {
        Transaction transaction = new Transaction(nextId, type, amount, category, date, memo);
        transactions.add(transaction);
        nextId++; // idを増やす
    }

    // 取引一覧を表示
    public void showTransaction() {
        System.out.println("---取引一覧---");
        if(transactions.isEmpty()) {
            System.out.println("取引が登録されていません");
        } else {
            for(Transaction t : transactions) {
                System.out.printf("[%d] %s | %s | %s | %,d円 | %s%n",
                t.getId(),
                t.getDate(),
                t.getTypeName(),
                t.getCategory(),
                t.getAmount(),
                t.getMemo());
            }
        }
    }

    // 収支(月ごと)を表示

    // 取引の検索

    // 取引の削除
}
