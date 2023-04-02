package plugin.gemgetter.command;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import plugin.gemgetter.Mapper.Mapper;
import plugin.gemgetter.Mapper.PlayerScore.PlayerScore;

public class GGResult extends SuperCommand {
private final SqlSessionFactory sqlSessionFactory;
public GGResult() {
    try {
      InputStream inputStream = Resources.getResourceAsStream("mybatis_config.xml");
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }

  public boolean PlayerDoneCommand(Player player, Command command, String[] args){

    try (SqlSession session = sqlSessionFactory.openSession()) {
        Mapper mapper = session.getMapper(Mapper.class);
        List<PlayerScore> playerScores = mapper.playerScores();
        for (PlayerScore playerScore : playerScores) {
            LocalDateTime date = LocalDateTime.parse(playerScore.getRegisteredDt(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            player.sendMessage(playerScore.getId() + "  "
                    + playerScore.getPlayerName() + "  "
                    + playerScore.getScore() + "個  "
                    + playerScore.getDifficulty() + "  "
                    + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        }
    }

      return false;
    }
  }

