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

public final class Main extends JavaPlugin{

  @Override
  public void onEnable() {
    saveDefaultConfig();
    GGStartCommand ggs = new GGStartCommand(this);
    Bukkit.getPluginManager().registerEvents(new EventListener(ggs),this);
    getCommand("ggstart").setExecutor(ggs);
    getCommand("ggretire").setExecutor(new GGRetireCommand(ggs));
  }


}
