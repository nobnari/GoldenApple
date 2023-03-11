package plugin.gemgetter.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.Main;

public class GGRetireCommand implements CommandExecutor {
  //フィールド
  private Main main;


  //コンストラクタ
  public GGRetireCommand(Main m) {
    this.main=m;
  }



  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      ItemStack[] contents = main.defaultInventories.get(player.getName());
      player.getInventory().setContents(contents);
    }
      return false;

  }
}
