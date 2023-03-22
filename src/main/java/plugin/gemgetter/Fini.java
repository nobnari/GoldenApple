package plugin.gemgetter;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.data.GGData;
import plugin.gemgetter.data.Rank;

/**
 * ゲームの終了に関わるクラス
 */
public class Fini {
  private final GGData data;
  public Fini(GGData data) {
    this.data =data;
  }

  /**
   * ゲーム終了時、ステータス変更(false),装備返還を処理
   * 終了メッセージ・タイトル・サウンドを出力
   * 近くの敵など消去
   * (主に正規終了用）
   * @param player ゲーム終了プレイヤー
   */
  public void Finisher(Player player) {
    data.getStatus().put(player.getName(),false);
    player.getInventory().setContents(data.getInventory().get(player.getName()));

    int appleSum=data.getAppleSum().get(player.getName());
    Rank rank=new Rank(appleSum);

    player.sendMessage("ゲーム終了!!!  今回の収穫"+appleSum+"個!!!");
    player.sendTitle(rank.getFinishColor(), rank.getResultColor(),0,80,40);
    player.playSound(player.getLocation(),rank.getResultSound(),25,20);

    EntityVanishEX(player);
    GivePrize(player,rank,appleSum);
  }

  /**
   * 近くの敵、ドロップアイテム消去を２回行う（スライムの増殖スポーンに対する備え）
   * @param player ゲーム終了プレイヤー
   */
  public void EntityVanishEX(Player player) {
    //間を置いて２回消し↓↓↓
    EntityVanish(player);
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
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
   * ランクとリンゴの合計数に応じて褒美を与える
   * 65以上は64固定
   * @param player　プレイヤー
   * @param rank ランク
   * @param appleSum　リンゴ合計数
   */
  private static void GivePrize(Player player, Rank rank,int appleSum) {
    if(appleSum >64){
      player.getWorld().dropItem(player.getLocation(),new ItemStack(rank.getPrize(),64));
    }else{
      player.getWorld().dropItem(player.getLocation(),new ItemStack(rank.getPrize(), appleSum));
    }
  }
}
