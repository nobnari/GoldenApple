package plugin.gemgetter.data;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * GGで手に入れるデータを扱うオブジェクト。
 * プレイヤー名、総所持金リンゴ数、日時
 */
@Getter
@Setter
public class GGData {

  /**
   * プレイヤーごとのGGのon/off変数を管理するマップ
   */
  private Map<String, Boolean> status = new HashMap<>();

  /**
   * プレイヤーごとのGG参加前のItemStack[](配列)を預かるマップ
   */
  private Map<String, ItemStack[]> inventory= new HashMap<>();

  /**
   * プレイヤーごとのゲームの難易度を管理するマップ
   */
  private Map<String,Course> course = new HashMap<>();

  /**
   * プレイヤーごとの最終リンゴピック時の所持数を管理するマップ
   */
  private Map<String,Integer> appleSum =new HashMap<>();

  /**
   * プレイヤーごとの持ち時間を預かるマップ
   */
  private Map<String,Integer>time =new HashMap<>();
}
