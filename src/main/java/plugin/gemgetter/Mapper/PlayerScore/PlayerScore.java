package plugin.gemgetter.Mapper.PlayerScore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * プレイヤーのスコアを扱うオブジェクト。
 * データベースのプレイヤースコアテーブルと連動する。
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerScore {
    private int id;
    private String playerName;
    private int score;
    private String difficulty;
    private LocalDateTime registeredDt;

    public PlayerScore(String playerName, int appleSum, String courseValue){
        this.playerName = playerName;
        this.score = appleSum;
        this.difficulty = courseValue;
    }
}
