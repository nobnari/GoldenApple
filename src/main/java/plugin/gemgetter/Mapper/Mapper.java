package plugin.gemgetter.Mapper;

import org.apache.ibatis.annotations.Select;
import plugin.gemgetter.Mapper.PlayerScore.PlayerScore;

import java.util.List;

public interface  Mapper {

    @Select("select * from player_score")
    List<PlayerScore> playerScores();

}
