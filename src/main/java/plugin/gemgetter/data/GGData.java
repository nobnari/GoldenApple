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


  private Map<String, ItemStack[]> inventory= new HashMap<>();
  private Map<String, Boolean> status = new HashMap<>();

  private String playerName;
  private int appleSumPast;

}
