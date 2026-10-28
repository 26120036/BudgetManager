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
    private List<Transaction> getSortedTransactions() {
        List<Transaction> sorted = new ArrayList<>(transactions);
        sorted.sort(Comparator.comparingInt(Transaction::getId));
        return sorted;
    }

    // 取引一覧を表示
    public void showTransaction() {
        System.out.println("---取引一覧---");
        if(transactions.isEmpty()) {
            System.out.println("取引が登録されていません");
        } else {
            for(Transaction t : getSortedTransactions()) {
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
    public void showMonthlySummary(YearMonth targetYM) {
        long totalIncome = 0; // 収入合計
        long totalExpense = 0; // 支出合計

        for(Transaction t: transactions) {
            YearMonth tsYearMonth = YearMonth.from(t.getDate());

            // 指定された年月(yyyy-mmの形式)と同じ物だけ通す
            if (tsYearMonth.equals(targetYM)) {
                // 収支の分岐
                if (t.getTransactionType() == TransactionType.INCOME) {
                    totalIncome += t.getAmount();
                } else if(t.getTransactionType() == TransactionType.EXPENSE) {
                    totalExpense += t.getAmount();
                }
            }
        }

        // 一月の収支
        long balance = totalIncome - totalExpense;

        // 出力
        System.out.printf("--- %d年%d月の収支 ---%n", targetYM.getYear(), targetYM.getMonthValue());
        System.out.printf("収入合計: %,d円\n", totalIncome);
        System.out.printf("支出合計: %,d円\n", totalExpense);
        System.out.printf("収支:     %,d円\n", balance);
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
    public void deleteTransaction(int id) {
        Transaction t = findById(id);
        if (t != null) {
            // インデックス指定になってたから修正
            transactions.remove(t);
        } else {
            System.err.println("指定されたIDの取引がありませんでした");
        }
    }

    // 指定されたIDの取引を探す
    public Transaction findById(int id) {
        for(Transaction t: transactions) {
            if (t.getId() == id) {
                return t;
            }
        }
        return  null;
    }
}