package plugin.gemgetter.Mapper.PlayerScore;

import lombok.Getter;
import lombok.Setter;

/**
 * プレイヤーのスコアを扱うオブジェクト。
 * データベースのプレイヤースコアテーブルと連動する。
 */
@Getter
@Setter
public class PlayerScore {
    private int id;
    private String playerName;
    private int score;
    private String difficulty;
    private String registeredDt;

}
