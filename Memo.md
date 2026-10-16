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