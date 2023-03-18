package plugin.gemgetter;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import plugin.gemgetter.data.GGData;

/**
 * ゲームの終了に関わるクラス
 */
public class Fini {
  private final GGData data;
  public Fini(GGData data) {
    this.data =data;
  }

  /**
   * ゲーム終了時、ステータス変更(false),メッセージ、タイトルメッセージ、装備返還を処理
   * @param player ゲーム終了プレイヤー
   */
  public void Finisher(Player player) {
    data.getStatus().put(player.getName(),false);
    player.getInventory().setContents(data.getInventory().get(player.getName()));
    player.sendMessage("ゲーム終了!!!  今回の収穫"+data.getAppleSum().get(player.getName())+"個!!!");
    player.sendTitle(FinishTitle(player), StarRank(player),0,80,40);
    EntityVanish(player);
  }

  /**
   * プレイヤーの近くの特定の敵と落ちてるアイテム全てを一掃
   * @param player　プレイヤー
   */
  public void EntityVanish(Player player) {
    List<Entity> entities = player.getNearbyEntities(32, 8, 32);
    for(Entity entity:entities){
      switch (entity.getType()) {
        case SLIME, MAGMA_CUBE ,DROPPED_ITEM-> entity.remove();
      }
    }
  }

  /**
   * ゲーム終了時にリンゴの個数に応じてFINISHタイトルの色を変える。
   * @param player　ゲーム終了プレイヤー
   * @return String
   */
  private String FinishTitle(Player player) {
    Integer finalScore = data.getAppleSum().get(player.getName());
    if(finalScore>64){
      return ChatColor.GOLD+"Finish!!!!";
    }else if(finalScore>32){
      return ChatColor.RED+"Finish!!";
    }else{
      return ChatColor.GREEN+"Finish!";
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
      return ChatColor.GOLD+"Golden Delicious ★★★";
    }else if(finalScore>32){
      return ChatColor.RED+"Good Taste ★★";
    }else{
      return ChatColor.GREEN+"Green Apple ★";
    }
  }

}
