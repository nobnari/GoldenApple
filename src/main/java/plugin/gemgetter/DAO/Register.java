package plugin.gemgetter.DAO;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Register {
    SqlSessionFactory sqlSessionFactory;
    public Register() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis_config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * ランキング表示メソッド(総合版)
     *　
     * @param player コマンド入力プレイヤー
     */
    public void displayScores(Player player) {
        SqlSession session = sqlSessionFactory.openSession();
        Mapper mapper = session.getMapper(Mapper.class);
        List<PlayerScore> playerScores = mapper.getPlayerScores();
        scoreRanking(player, playerScores);
        session.close();
    }
    /**
     * ランキング表示メソッド(難易度別)
     *
     * @param player コマンド入力プレイヤー
     */
    public void displayScores(Player player, String difficulty) {
        SqlSession session = sqlSessionFactory.openSession();
        Mapper mapper = session.getMapper(Mapper.class);
        List<PlayerScore> playerScores = mapper.getPlayerScoresByDifficulty(difficulty);
        scoreRanking(player, playerScores);
        session.close();
    }

    /**
     * プレイヤーのスコアをデータベースに登録
     * @param player プレイヤー
     * @param appleSum 最終的に得たリンゴの数
     * @param courseValue 難易度
     */
    public void insertPlayerScore(Player player, int appleSum, String courseValue) {
        SqlSession session = sqlSessionFactory.openSession(true);
        Mapper mapper = session.getMapper(Mapper.class);
        PlayerScore playerScore = new PlayerScore(player.getName(), appleSum, courseValue);
        mapper.insertPlayerScore(playerScore);
        session.close();
    }
    
    /**
     * ランキング表示メソッド
     *　タイムは非表示、日付のみ表示
     * @param player コマンド入力プレイヤー
     * @param playerScores プレイヤースコアリスト
     */
    private static void scoreRanking(Player player, List<PlayerScore> playerScores) {
        int i= 1;
        for (PlayerScore playerScore : playerScores) {
            player.sendMessage(i+"位   "+playerScore.getScore() + "個   "+
                    playerScore.getPlayerName() + "   "
                    + playerScore.getDifficulty() + "   "
                    + playerScore.getRegisteredDt()
                    .format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            i++;
        }
    }
}
