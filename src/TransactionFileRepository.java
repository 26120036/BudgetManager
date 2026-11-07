// ファイルの読み書き等
// 1行1取引: id,種別(INCOME/EXPENSE),金額,カテゴリ,日付(yyyy-MM-dd),メモ

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFileRepository {
    private static final Path FILE_PATH = Paths.get("save", "transactions.txt");
    private static final int FIELD_COUNT = 6;

    // ファイルから全取引を読み込む
    public List<Transaction> load() {
        List<Transaction> result = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return result; // 初回の起動
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("保存ファイルの読み込みに失敗しました: " + e.getMessage());
            return result;
        }

        Set<Integer> usedIds = new HashSet<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            try {
                Transaction t = parseLine(line);

                if (!usedIds.add(t.getId())) {
                    throw new IllegalArgumentException("IDが重複しています");
                }

                result.add(t);

            } catch (RuntimeException e) {
                System.err.println(
                    (i + 1) + "行目は不正なデータのため飛ばしました("
                    + e.getMessage() + ")"
                );
            }
        }

        return result;
    }

    // 全取引をファイルに保存する。成功でtrue
    public boolean save(List<Transaction> transactions) {
        List<String> lines = new ArrayList<>();

        for (Transaction t : transactions) {
            lines.add(toLine(t));
        }

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
            return true;

        } catch (IOException e) {
            System.err.println("保存に失敗しました: " + e.getMessage());
            return false;
        }
    }

    // Transaction -> 1行のCSV
    private String toLine(Transaction t) {
        validateCsvValue(t.getCategory());
        validateCsvValue(t.getMemo());

        return String.join(",",
                String.valueOf(t.getId()),
                t.getTransactionType().name(),
                String.valueOf(t.getAmount()),
                t.getCategory(),
                t.getDate().toString(),
                t.getMemo()
        );
    }

    // 1行のCSV -> Transaction
    private Transaction parseLine(String line) {
        if (!isValidCsvValue(line)) {
            throw new IllegalArgumentException(
                "カンマ・ダブルクォーテーション以外の区切り形式が不正です"
            );
        }

        String[] f = line.split(",", -1);

        if (f.length != FIELD_COUNT) {
            throw new IllegalArgumentException("項目数が正しくありません");
        }

        int id = Integer.parseInt(f[0].trim());
        TransactionType type = TransactionType.valueOf(f[1].trim());
        long amount = Long.parseLong(f[2].trim());
        String category = f[3];
        LocalDate date = LocalDate.parse(f[4].trim());
        String memo = f[5];

        if (id <= 0) {
            throw new IllegalArgumentException("IDが正しくありません");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("金額が正しくありません");
        }

        if (category.isBlank()) {
            throw new IllegalArgumentException("カテゴリが空です");
        }

        validateCsvValue(category);
        validateCsvValue(memo);

        return new Transaction(id, type, amount, category, date, memo);
    }

    // CSVに使用できない文字が含まれていないか確認
    private boolean isValidCsvValue(String value) {
        return !value.contains("\"")
                && !value.contains("\n")
                && !value.contains("\r");
    }

    // CSVに使用できない文字があれば例外を発生させる
    private void validateCsvValue(String value) {
        if (!isValidCsvValue(value) || value.contains(",")) {
            throw new IllegalArgumentException(
                "CSVに使用できない文字が含まれています"
            );
        }
    }
}