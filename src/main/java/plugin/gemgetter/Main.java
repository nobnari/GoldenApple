package plugin.gemgetter;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gemgetter.command.GGRetireCommand;
import plugin.gemgetter.command.GGStartCommand;

public final class Main extends JavaPlugin implements Listener {
  public Map<String, ItemStack[]> defaultInventories= new HashMap<>();
  @Override
  public void onEnable() {
    saveDefaultConfig();
    Bukkit.getPluginManager().registerEvents(this, this);
    getCommand("ggstart").setExecutor(new GGStartCommand(this));
    getCommand("ggretire").setExecutor(new GGRetireCommand(this));
  }


}
