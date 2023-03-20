package plugin.gemgetter.data;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import plugin.gemgetter.Main;

@Getter
@Setter
/**
 * ゲームの難易度に関わるクラス
 */
public class Course {

  public static final String EASY = "easy";
  public static final String NORMAL = "normal";
  public static final String HARD = "hard";

  private String value;
  private int swordNumber;
  private List<String> monsterList;

  /**
   * コマンドで受け取った引数からコースごとの敵リスト、剣本数を入れるコンストラクタ
   * @param main　メイン
   * @param args　コマンド引数
   */
  public Course(Main main,String[] args) {

    if (args.length == 1&&(EASY.equals(args[0])||NORMAL.equals(args[0])|| HARD.equals(args[0]))){
      this.value=args[0];
    }else {
      this.value = NORMAL;
    }

    switch (value) {
      case EASY -> this.swordNumber = 3;
      case NORMAL -> this.swordNumber = 2;
      case HARD -> this.swordNumber = 1;
    }

    this.monsterList =  main.getConfig().getStringList(value);

  }
}
