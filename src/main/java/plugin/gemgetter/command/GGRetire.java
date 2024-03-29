package plugin.gemgetter.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import plugin.gemgetter.Fini;
import plugin.gemgetter.data.GGData;
/**
 *プレイヤーがリタイアコマンドを入力した時に実行されるクラス
 */
public class GGRetire extends SuperCommand {
  private final GGData data;
  private final Fini fini;
  public GGRetire(GGData data, Fini fini) {
    this.data =data;
    this.fini=fini;
  }

  public boolean PlayerDoneCommand(Player player , Command command,  String[] args) {
    if(data.getStatus().get(player.getName())) {

      player.getInventory().setContents(data.getInventory().get(player.getName()));
      data.getStatus().put(player.getName(),false);
      fini.EntityVanishEX(player);

      player.sendMessage("ゲームをリタイアしました");
    }else{
      player.sendMessage("ゲームはまだはじまっていない…");
    }
    return true;
  }
}
