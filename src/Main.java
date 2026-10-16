import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                break;
            case 5:
                manager.showTransaction();
                System.out.println();
                System.out.println("削除したい取引のIDを指定してください");
                
                int id = sc.nextInt();
                manager.deleteTransaction(id);
                break;
            case 6:
                int year;
                int month;

                try {
                    System.out.print("取得したい年を入力してください(例: 2026): ");
                    year = sc.nextInt();

                    System.out.print("取得したい月を入力してください(1~12): ");
                    month = sc.nextInt();
                    sc.nextLine();

                    YearMonth targetYM = YearMonth.of(year, month);
                    manager.showMonthlySummary(targetYM);
                    
                } catch (java.util.InputMismatchException e) {
                    System.err.println("数値を入力してください");
                    sc.next();
                } catch (DateTimeException e) {
                    System.err.println("有効な年月を入力してください");
                }

                break;
            default:
                System.out.println("メニューに表示されている番号を入力してください");
                break;
        }
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
                System.out.println("0以上の数値を入力してください");
            } catch (java.util.InputMismatchException e) {
                System.out.println("数値を入力してください");
                sc.next();
            }
        }

        // カテゴリ
        System.out.println("カテゴリーを入力してください(例: 食費)");
        category = sc.next();
        sc.nextLine();

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

        manager.addTransaction(type, amount, category, date, memo);
        System.out.println(typeName + "の登録が完了しました！\n");
    }
}