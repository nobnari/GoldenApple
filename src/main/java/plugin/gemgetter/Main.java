package plugin.gemgetter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gemgetter.command.GGRetire;
import plugin.gemgetter.command.GGStart;
import plugin.gemgetter.data.GGData;

public final class Main extends JavaPlugin{

  @Override
  public void onEnable() {
    saveDefaultConfig();
    GGData data = new GGData();

    Bukkit.getPluginManager().registerEvents(new EventListener(data),this);
    getCommand("ggstart").setExecutor(new GGStart(this,data,new EventListener(data)));
    getCommand("ggretire").setExecutor(new GGRetire(data));
  }


}
