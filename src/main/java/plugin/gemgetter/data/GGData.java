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

  //ステータス(ON/OFF)
  private Map<String, Boolean> status = new HashMap<>();

  //ゲーム開始前の持ち物
  private Map<String, ItemStack[]> inventory= new HashMap<>();

  //持ちリンゴ
  private Map<String,Integer> appleSum =new HashMap<>();

  //持ち時間
  private Map<String,Integer>time =new HashMap<>();
}
