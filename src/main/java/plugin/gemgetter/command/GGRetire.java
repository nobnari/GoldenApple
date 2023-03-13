package plugin.gemgetter.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.data.GGData;

public class GGRetire implements CommandExecutor {
  //フィールド
  private GGData data;

  //コンストラクタ
  public GGRetire(GGData data) {
    this.data =data;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      if(data.getStatus().get(player.getName())) {
        player.getInventory().setContents(data.getInventory().get(player.getName()));
        data.getStatus().put(player.getName(), Boolean.FALSE);
        player.sendMessage("ゲームをリタイアしました");
      }else{
        player.sendMessage("ゲームはまだはじまっていない…");
      }
    }
      return false;

  }
}
