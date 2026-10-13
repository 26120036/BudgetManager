import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BudgetManager manager = new BudgetManager();
        boolean run = true;

        // run == trueの間は実行
        while (run) {
            showMenu();
            if(sc.hasNextInt()) {
                int number = sc.nextInt();
                if(number == 7) { // 7の時はアプリを終了する
                    System.out.println("アプリケーションを終了します");
                    run = false;
                }

                callFunc(number, sc, manager); // 処理分岐させる関数
            } else {
                System.err.println("番号を数字で入力してください");
                sc.next(); // 入力を破棄
            }

            sc.close();
        }
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
                
                break;
            case 4:
                
                break;
            case 5:
                
                break;
            case 6:
                
                break;
            default:
                break;
        }
    }

    // 支出登録。入力処理にmainのScanner使い回すようにする
    public static void registerManager(Scanner sc, BudgetManager manager, TransactionType type) {
        long amount = 0;
        String memo = "";

        String typeNaame = type.getDisplayName(); // TransactionTypeの名前を取得
        System.out.println("---" + typeNaame + "を登録します---");

        // 金額

        // カテゴリ

        // メモ(任意)

        manager.addTransaction(type, amount, typeNaame, null, memo);
    }
}