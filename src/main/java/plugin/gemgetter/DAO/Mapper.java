package plugin.gemgetter.DAO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface  Mapper {
    /**
     * プレイヤースコアテーブルからスコアの高い順に最大八人分のデータを取得
     *
     * @return プレイヤースコアテーブルの全てのデータ
     */
    @Select("select * from player_score order by score desc limit 8")
    List<PlayerScore> getPlayerScores();

    /**
     * プレイヤースコアテーブルから同じ難易度でスコアの高い順に最大八人分のデータを取得
     *
     * @return プレイヤースコアテーブルの全てのデータ
     */
    @Select("select * from player_score where difficulty = #{difficulty} order by score desc limit 8")
    List<PlayerScore> getPlayerScoresByDifficulty(String courseValue);


    /**
     * プレイヤースコアテーブルにプレイヤーのスコアを登録
     *
     * @param playerScore プレイヤースコアオブジェクト
     */
    @Insert("insert player_score (player_name, score, difficulty, registered_dt) " +
            "values (#{playerName}, #{score}, #{difficulty}, now())")
     void insertPlayerScore(PlayerScore playerScore);
}
