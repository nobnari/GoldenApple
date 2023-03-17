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
import plugin.gemgetter.Main;
import plugin.gemgetter.data.GGData;

public class GGStart implements CommandExecutor {
  //フィールド
  private final Main main;
  private final GGData data;
  //コンストラクタ
public GGStart(Main main,GGData data) {
  this.main = main;
  this.data = data;
}
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      if(!data.getStatus().get(player.getName())) {
        World world = player.getWorld();
        //↓初期装備保存&&ステータス変更↓
        data.getInventory().put(player.getName(), player.getInventory().getContents());
        data.getStatus().put(player.getName(),true);

        GGInit(player);
        player.sendMessage("ゲームスタート！");
        //タイマースタート
        Bukkit.getScheduler().runTaskTimer(main,
            run-> {
             Integer time = data.getTime().get(player.getName());
                if (time <= 0) {
                run.cancel();
                GGFinish(player);
                return;
              }
                world.spawnEntity(RandomSpawnLocation(player, world), RandomMonsterList());
                data.getTime().put(player.getName(),time -5);
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
   * @param player　コマンド実行プレイヤー
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
    data.getTime().put(player.getName(),13);
    data.getAppleSum().put(player.getName(),0);
  }
  /**
   * ゲーム終了時、ステータス変更(false),メッセージ、タイトルメッセージ、装備返還を処理
   * @param player ゲーム終了プレイヤー
   */
  private void GGFinish(Player player) {
    data.getStatus().put(player.getName(),false);
    player.getInventory().setContents(data.getInventory().get(player.getName()));
    player.sendMessage("ゲーム終了!!!  今回の収穫"+data.getAppleSum().get(player.getName())+"個!!!");
    player.sendTitle("Finish!!!!", StarRank(player),0,80,40);
    EntityVanish(player);
  }

  /**
   * プレイヤーの近くの特定の敵と落ちてるアイテム全てを一掃
   * @param player　プレイヤー
   */
  private void EntityVanish(Player player) {
    List<Entity> entities = player.getNearbyEntities(32, 8, 32);
    for(Entity entity:entities){
      switch (entity.getType()) {
        case SLIME, MAGMA_CUBE ,DROPPED_ITEM-> entity.remove();
      }
    }
  }

  /**
   * ゲーム終了時にリンゴの個数に応じて星判定を出す。
   * @param player　ゲーム終了プレイヤー
   * @return String
   */
  private String StarRank(Player player) {
    Integer finalScore = data.getAppleSum().get(player.getName());
    if(finalScore>64){
      return "Golden Delicious★★★";
    }else if(finalScore>32){
      return "Good Taste★★";
    }else{
      return"Green Apple★";
    }
  }

  /**
   * プレイヤーを中心に近辺にランダムの（スポーンのための）場所を取得。
   * x,zがプラスマイナス10の幅を持つ。
   * y（高さ）はプレイヤーと同じ。
   * @param player　コマンド実行プレイヤー
   * @param world　コマンド実行プレイヤーの居るワールド
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
