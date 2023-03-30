package plugin.gemgetter.command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class GGResult extends SuperCommand {

  public boolean PlayerDoneCommand(Player player, Command command, String[] args) {
      String url = "";
      String username = "root";
      String password = "";

      String sql = "select * from player_score;";

      try (Connection con = DriverManager.getConnection(url, username, password);
          Statement st = con.createStatement();
          ResultSet resultset = st.executeQuery(sql)) {
        while (resultset.next()) {
          int id = resultset.getInt("id");
          String name = resultset.getString("player_name");
          int score = resultset.getInt("score");
          String difficulty = resultset.getString("difficulty");
          LocalDateTime date = LocalDateTime.parse(resultset.getString("registered_dt"),
              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

          player.sendMessage(
              id + "  " + name + "  " + score + "個  " + difficulty + "  " + date.format(
                  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return false;
    }
  }

