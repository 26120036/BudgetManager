public enum TransactionType {
    INCOME("収入"),
    EXPENSE("支出");

    private final String displayName;

    // コンストラクタ
    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    // 表示名を取得
    public String getDisplayName() {
        return displayName;
    }
}