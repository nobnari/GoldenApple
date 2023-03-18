package plugin.gemgetter;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.gemgetter.data.GGData;

/**
 * ゲームの開始に関わるクラス
 */
public class Init {
  public static final int GAME_TIME = 10;
  private final Main main;
  private final GGData data;
  private final Fini fini;
  public Init(Main main,GGData data,Fini fini) {
    this.main =main;
    this.data=data;
    this.fini =fini;
  }

  /**
   * ゲーム開始時のプレイヤーの基本初期設定を行う
   * 装備フル変更(金)。
   * 体力、空腹値も全回復する
   * @param player　コマンド実行プレイヤー
   */
  public void initializer(Player player) {
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
    data.getTime().put(player.getName(), GAME_TIME);
    data.getAppleSum().put(player.getName(),0);
  }

  /**
   * プレイヤーからタイムを取得し、タイマーをスタートする。
   * その後、プレイヤータイムを消費しつつ、一定間隔で敵をランダムスポーンし、
   * プレイヤータイムが０以下になると終了メソッドを処理。
   * @param player プレイヤー
   */
  public void TimerStart(Player player) {
    Bukkit.getScheduler().runTaskTimer(main,
        run-> {
          Integer time = data.getTime().get(player.getName());
            if (time <= 0) {
              run.cancel();
              fini.Finisher(player);
              return;
            }
            RandomSpawner(player);
            data.getTime().put(player.getName(), time-5);
            },0,5*20);
  }

  /**
   * プレイヤーの近くのランダムな位置にリストの中からランダムな敵をスポーン
   * @param player　中心となるプレイヤー
   */
  public void RandomSpawner(Player player) {
    player.getWorld().spawnEntity(RandomSpawnLocation(player), RandomMonsterList());
  }

  /**
   * プレイヤーを中心に近辺にランダムの（スポーンのための）場所を取得。
   * x,zがプラスマイナス10の幅を持つ。
   * y（高さ）はプレイヤーと同じ。
   * @param player　コマンド実行プレイヤー
   * @return Location
   */
  private Location RandomSpawnLocation(Player player) {
    Location l = player.getLocation();
    int rx= new SplittableRandom().nextInt(21)-10;
    int rz= new SplittableRandom().nextInt(21)-10;
    double x =l.getX()+rx;
    double y = l.getY();
    double z = l.getZ()+rz;
    return new Location(player.getWorld(), x, y, z);
  }

  /**
   * config.yml 内の monsterList からランダムでモンスターを一体選択して返す。
   * @return EntityType
   */
  private EntityType RandomMonsterList() {
    List<String> monsterList=main.getConfig().getStringList("monsterList");
    int random= new SplittableRandom().nextInt(monsterList.size());
    return EntityType.valueOf(monsterList.get(random));
  }

}