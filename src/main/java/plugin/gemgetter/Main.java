package plugin.gemgetter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.gemgetter.command.GGRecord;
import plugin.gemgetter.command.GGRetire;
import plugin.gemgetter.command.GGStart;
import plugin.gemgetter.data.GGData;

public final class Main extends JavaPlugin{

  @Override
  public void onEnable() {
    saveDefaultConfig();

    GGData data = new GGData();
    Fini fini =new Fini(data);
    Init init =new Init(this,data,fini);

    Bukkit.getPluginManager().registerEvents(new EventListener(data,fini),this);
    getCommand("ggstart").setExecutor(new GGStart(this,data,init));
    getCommand("ggretire").setExecutor(new GGRetire(data,fini));
    getCommand("ggrecord").setExecutor(new GGRecord());
  }


}
