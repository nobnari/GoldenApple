package plugin.gemgetter.command;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;
import plugin.gemgetter.EventListener;
import plugin.gemgetter.Main;
import plugin.gemgetter.data.GGData;

public class GGStart implements CommandExecutor {
  //フィールド
  private Main main;
  private GGData data;
  private EventListener event;
  private int time;
  //コンストラクタ
public GGStart(Main main,GGData data, EventListener event){
  this.main =main;
  this.data=data;
  this.event =event;
}
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      if(!data.getStatus().get(player.getName())) {
        World world = player.getWorld();
        //↓初期装備保存&&ステータス変更↓
        data.getInventory().put(player.getName(), player.getInventory().getContents());
        data.getStatus().put(player.getName(),true);
        data.getAppleSum().put(player.getName(),0);

        GGInit(player);
        player.sendMessage("ゲームスタート！");

        Bukkit.getScheduler().runTaskTimer(main,
            run-> {
              if (time <= 0) {
                run.cancel();
                data.getStatus().put(player.getName(),false);
                player.getInventory().setContents(data.getInventory().get(player.getName()));
                player.sendMessage("ゲーム終了!!!累計で金のリンゴ"+data.getAppleSum().get(player.getName())+"個手に入れた！");
                return;
              }
              world.spawnEntity(RandomSpawnLocation(player, world), RandomMonsterList());
              time-=5;
            },0,5*20);
      }else{
        player.sendMessage("ゲームはすでにはじまっている!!!");
      }
    }
    return false;
  }

  /**
   * GGスタート時の初期設定
   * 装備フル変更(金)。
   * 体力、空腹値も全回復する
   * @param player
   */
  private void GGInit(Player player) {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    inventory.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
    inventory.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
    inventory.setItem(0,new ItemStack(Material.GOLDEN_SWORD));
    inventory.setItem(8,new ItemStack(Material.GOLDEN_APPLE));
    player.setHealth(20);
    player.setFoodLevel(20);
    time=30;
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
