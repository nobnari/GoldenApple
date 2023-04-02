package plugin.gemgetter.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.gemgetter.Mapper.PlayerScore.PlayerScore;
import java.util.List;

public interface  Mapper {
    @Select("select * from player_score")
    List<PlayerScore> getPlayerScores();

    @Insert("insert player_score (player_name, score, difficulty, registered_dt) " +
            "values (#{playerName}, #{score}, #{difficulty}, now())")
     void insertPlayerScore(PlayerScore playerScore);
}
