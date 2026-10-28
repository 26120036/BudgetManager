・getMonthはenum型のためJANUARYなどが入ってる。数字で取得したいときはgetMonthValueを使う  
・hasNextInt()で早めに拾うのもいいんだけど、毎回if文かくから冗長になりがち。今回はためしにnextLongで入れてエラーが出たらInputMismatchExceptionで拾うようにしてみた。    
nextLine()とかでなんでもいいからとりあえず入力→longやintにparse→失敗はNumberFormatExceptionで拾うとかでもよさそう。
  
・deleteTransactionの元コード  
```java
public void deleteTransaction(int id) {
    transactions.removeIf(t -> t.getId() == id);
}
```  
removeifで探して見つかれば消すだけにしていたんだけど、これだと見つからなかったときにﾇﾙﾎﾟ出るから、別で探索用メソッド用意してチェックを行うようにした
  
10/28  
・削除処理のバグ修正  
インデックスを渡していたのをTransactionを渡して正しく削除できるように修正。  
・表示の修正  
TransactionにtoString()を追加  
・集計の見出し修正  
1行になっていたのを%n入れて修正  
・取引が0の時用のisEmpty()追加  
・入力の修正  
文字が入力されても落ちないようにし、最初に戻るようにした  
・カテゴリの入力の修正  
nextLine()にしたことでスペースが入っていても登録できるように修正  
・登録、削除前の確認処理追加  
confirm()でy/n表示するようにした  
・並び順の統一  
ID順になるようにした  