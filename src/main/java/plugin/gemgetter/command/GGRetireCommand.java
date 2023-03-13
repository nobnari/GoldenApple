package plugin.gemgetter.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GGRetireCommand implements CommandExecutor {
  //フィールド
  private GGStartCommand ggs;


  //コンストラクタ
  public GGRetireCommand(GGStartCommand ggs) {
    this.ggs=ggs;
  }



  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      ItemStack[] contents = ggs.getDefaultInventories().get(player.getName());
      player.getInventory().setContents(contents);
      ggs.getStatus().put(player.getName(),Boolean.FALSE);
    }
      return false;

  }
}
