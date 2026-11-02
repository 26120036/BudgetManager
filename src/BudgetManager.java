// 取引の管理

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
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

    // 取引が1件もないかの確認
    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    // 一覧や検索結果の並び順はID順に統一する
    public List<Transaction> getSortedTransactions() {
        List<Transaction> sorted = new ArrayList<>(transactions);
        sorted.sort(Comparator.comparingInt(Transaction::getId));
        return sorted;
    }

    // 指定した年月、種別の金額合計
    public long sumByType(YearMonth targetYM, TransactionType type) {
        long total = 0;

        // 該当するものだけtotalに追加
        for(Transaction t: transactions) {
            if (t.getTransactionType() == type && YearMonth.from(t.getDate()).equals(targetYM)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // 取引の検索(結果はID順)
    public List<Transaction> searchTransactions(String key) {
        List<Transaction> result = new ArrayList<>();

        for(Transaction t: getSortedTransactions()) {
            if(t.getCategory().contains(key)) {
                result.add(t);
            }
        }
        return result;
    }

    // 取引の削除
    // 削除成功→true、指定IDがない→false
    public boolean deleteTransaction(int id) {
        Transaction t = findById(id);
        if (t == null) {
            return false;
        }
        // 取引を渡して削除
        transactions.remove(t);
        return true;
    }

    // 指定されたIDの取引を探す
    public Transaction findById(int id) {
        for(Transaction t: transactions) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}