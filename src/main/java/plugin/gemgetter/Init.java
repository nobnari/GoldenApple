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
import plugin.gemgetter.data.Course;
import plugin.gemgetter.data.GGData;

/**
 * ゲームの開始に関わるクラス
 * タイマーも扱うため終了にも少し関わる
 */
public class Init {
  public static final int GAME_TIME = 35;
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
   * 装備フル変更(金)。難易度に合わせて剣の本数変えて支給。
   * 体力、空腹値も全回復する
   * @param player　コマンド実行プレイヤー
   * @param course 選択難易度
   */
  public void initializer(Player player, Course course) {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    inventory.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
    inventory.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
    for(int i=0;i<course.getSwordNumber();i++) {
      inventory.setItem(i, new ItemStack(Material.GOLDEN_SWORD));
    }
    inventory.setItem(8,new ItemStack(Material.GOLDEN_APPLE));
    player.setHealth(20);
    player.setFoodLevel(20);
    data.getTime().put(player.getName(), GAME_TIME);
    data.getAppleSum().put(player.getName(),0);
  }

  /**
   * プレイヤーからタイムを取得し、タイマーをスタートする。
   * その後、プレイヤータイムを消費しつつ、一定間隔で敵をランダムスポーンし続ける。
   * プレイヤータイムが０以下になると終わる。
   * プレイヤーステータスがfalseになっても終了。
   * @param player プレイヤー
   * @param course 難易度
   */
  public void TimerStart(Player player , Course course){
    Bukkit.getScheduler().runTaskTimer(main,
        run-> {
          Integer time = data.getTime().get(player.getName());
            if(!data.getStatus().get(player.getName())||time<-55){
              run.cancel();
              return;
            }else if (time <= 0) {
              run.cancel();
              fini.Finisher(player);
              return;
            }
            RandomSpawner(player,course);
            data.getTime().put(player.getName(), time-5);
            },0,5*20);
  }

  /**
   * プレイヤーの近くのランダムな位置にリストの中からランダムな敵をスポーン
   * @param player　中心となるプレイヤー
   * @param course 難易度
   */
  public void RandomSpawner(Player player ,Course course) {
    player.getWorld().spawnEntity(RandomSpawnLocation(player), RandomMonsterList(course));
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
   * @param course　難易度
   * @return EntityType
   */
  private EntityType RandomMonsterList(Course course) {
    List<String> monsterList=course.getMonsterList();
    int random= new SplittableRandom().nextInt(monsterList.size());
    return EntityType.valueOf(monsterList.get(random));
  }

}
