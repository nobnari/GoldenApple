package plugin.gemgetter.command;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import plugin.gemgetter.Main;

public class GGStartCommand implements CommandExecutor {
  private Main main;

  public GGStartCommand(Main m) {
    this.main=m;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      World world = player.getWorld();

      FullHeal(player);
      world.spawnEntity(RandomSpawnLocation(player, world), RandomMonsterList());
    }
    return false;
  }
  /**
   * 指定のプレイヤーの体力、空腹値全回復
   * @param player
   */
  private void FullHeal(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
  }
  /**
   * プレイヤーを中心に近辺にランダムの（スポーンのための）場所を取得。
   * x,zがプラスマイナス10の幅を持つ。
   * y（高さ）はプレイヤーと同じ。
   * @param player
   * @param world
   * @return Location
   */
  private Location RandomSpawnLocation(Player player, World world) {
    Location l = player.getLocation();
    int rx= new SplittableRandom().nextInt(21)-10;
    int rz= new SplittableRandom().nextInt(21)-10;
    double x =l.getX()+rx;
    double y = l.getY();
    double z = l.getZ()+rz;
    return new Location(world, x, y, z);
  }
  /**
   * config.yml 内の monsterList からランダムでモンスターを一体返す。
   * @return EntityType
   */

  private EntityType RandomMonsterList() {
    List<String> monsterList=main.getConfig().getStringList("monsterList");
    int random= new SplittableRandom().nextInt(monsterList.size());
    return EntityType.valueOf(monsterList.get(random));
  }

}
