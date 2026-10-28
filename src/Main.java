import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BudgetManager manager = new BudgetManager();
        boolean run = true;

        // run == trueの間は実行
        while (run) {
            showMenu();
            if (sc.hasNextInt()) {
                int number = sc.nextInt();
                sc.nextLine();
                
                if (number == 7) { // 7の時はアプリを終了する
                    System.out.println("アプリケーションを終了します");
                    run = false;
                } else {
                    callFunc(number, sc, manager); // 処理分岐させる関数
                }
            } else {
                System.err.println("番号を数字で入力してください");
                sc.next();
            }
        }

        sc.close();
    }

    // メニューの表示
    public static void showMenu() {
        System.out.println("========================");
        System.out.println("    家計管理アプリ");
        System.out.println("========================");
        System.out.println("1. 収入を登録");
        System.out.println("2. 支出を登録");
        System.out.println("3. 取引一覧を表示");
        System.out.println("4. 取引を検索");
        System.out.println("5. 取引を削除");
        System.out.println("6. 月ごとの収支を表示");
        System.out.println("7. アプリを終了");
        System.out.println("番号を入力してください:");
    }

    // 番号で分岐して処理を実行
    public static void callFunc(int number, Scanner sc, BudgetManager manager) {
        System.out.println();

        switch (number) {
            case 1:
                registerManager(sc, manager, TransactionType.INCOME);
                break;
            case 2:
                registerManager(sc, manager, TransactionType.EXPENSE);
                break;
            case 3:
                manager.showTransaction();
                break;
            case 4:
                System.out.println("--- 取引検索 ---");
                System.out.print("検索キーワード(カテゴリ、メモ、日付等)を入力してください: ");
                String keyword = sc.nextLine();

                List<Transaction> searchResult = manager.searchTransactions(keyword);
                if (searchResult.isEmpty()) {
                    System.out.println("条件に一致する取引は見つかりませんでした。");
                } else {
                    System.out.println("--- 検索結果 (" + searchResult.size() + "件) ---");
                    for (Transaction t : searchResult) {
                        System.out.println(t);
                    }
                }
                break;
            case 5:
                deleteWithConfirm(sc, manager);
                break;
            case 6:
                System.out.print("取得したい年月をyyyy-MM形式で入力してください(例: 2026-10): ");
                try {
                    YearMonth targetYM = YearMonth.parse(sc.nextLine().trim());
                    manager.showMonthlySummary(targetYM);
                } catch (DateTimeParseException e) {
                    System.err.println("年月の形式が正しくありません(例: 2026-10)");
                }
                break;
            default:
                System.out.println("メニューに表示されている番号を入力してください");
                break;
        }
    }

    // 削除処理
    public static void deleteWithConfirm(Scanner sc, BudgetManager manager) {
        if (manager.isEmpty()) {
            System.out.println("削除できる取引がありません");
            return;
        }

        manager.showTransaction();
        System.out.println();
        System.out.println("削除したい取引のIDを指定してください");

        int id;
        try {
            id = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.err.println("IDは数字で入力してください");
            sc.nextLine(); // 不正な入力を捨て
            return;
        }

        Transaction target = manager.findById(id);
        if (target == null) {
            System.err.println("指定されたIDの取引が見つかりませんでした");
            return;
        }

        System.out.println("以下の取引を削除します:");
        System.out.println(target);
        if (!confirm(sc, "本当に削除しますか?")) {
            System.out.println("削除をキャンセルしました");
            return;
        }

        manager.deleteTransaction(id);
        System.out.println("削除しました");
    }

    // 登録処理
    public static void registerManager(Scanner sc, BudgetManager manager, TransactionType type) {
        long amount = 0;
        String category;
        String memo;
        LocalDate date;

        String typeName = type.getDisplayName(); // TransactionTypeの名前を取得
        System.out.println("--- " + typeName + "を登録します ---");

        // 金額
        while (true) {
            System.out.print("金額を入力してください: ");
            try {
                amount = sc.nextLong();
                sc.nextLine();
                
                if (amount > 0) {
                    break;
                }
                System.out.println("0より大きい数値で入力してください");
                
            } catch (java.util.InputMismatchException e) {
                System.out.println("数値を入力してください");
                sc.next();
            }
        }

        // カテゴリ(スペースを含む入力可。空は不可)
        while (true) {
            System.out.println("カテゴリーを入力してください(例: 食費)");
            category = sc.nextLine().trim();
            if (!category.isEmpty()) {
                break;
            }
            System.out.println("カテゴリーが空です。もう一度入力してください");
        }

        // 日付
        while (true) {
            System.out.print("日付をyyyy-MM-dd形式で入力してください(例: 2026-10-15): ");
            String dateStr = sc.nextLine();
            try {
                date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("日付の形式が正しくないか、実在しません。もう一度入力してください");
            }
        }
        // メモ(任意)
        while (true) {
            System.out.println("メモを入力してください(50文字以内・任意なので書かない場合はEnter):");
            memo = sc.nextLine();
            if (memo.isEmpty()) {
                memo = "-";
                break;
            } else if (memo.length() > 50) {
                System.out.println("メモは50文字以内で入力してください");
            } else {
                break;
            }
        }

        // 登録前の確認
        System.out.println();
        System.out.println("--- 登録内容の確認 ---");
        System.out.println("種別:     " + typeName);
        System.out.printf("金額:     %,d円%n", amount);
        System.out.println("カテゴリ: " + category);
        System.out.println("日付:     " + date);
        System.out.println("メモ:     " + memo);
        if (!confirm(sc, "この内容で登録しますか?")) {
            System.out.println("登録をキャンセルしました\n");
            return;
        }

        manager.addTransaction(type, amount, category, date, memo);
        System.out.println(typeName + "の登録が完了しました！\n");
    }

    // y/n の確認(yならtrue、nならfalse)
    public static boolean confirm(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String answer = sc.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                return true;
            }
            if (answer.equals("n")) {
                return false;
            }
            System.out.println("y か n で入力してください");
        }
    }
}