package plugin.gemgetter;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.data.GGData;

/**
 * ゲーム中のイベント、及びマインクラフトへの参加、離脱を監視するクラス
 */
public class EventListener implements Listener {
  private final GGData data;
  public EventListener(GGData data) {
    this.data = data;
  }

  /**
   * 敵が死んだ時、ステータスがTRUEのプレイヤーが倒すと金のリンゴドロップ
   * @param e　敵が死んだ時
   */
  @EventHandler
  private void onEntityDeathEvent(EntityDeathEvent e){
    Entity entity=e.getEntity();
    Player player = e.getEntity().getKiller();
    World world = e.getEntity().getWorld();
    Location l = e.getEntity().getLocation();

    if(Objects.nonNull(player)&& data.getStatus().get(player.getName())){
      ItemStack GA = new ItemStack(Material.GOLDEN_APPLE);
      GA.setAmount(SortSlimeSize(entity));
      world.dropItem(l, GA);
    }
  }

  /**
   * ステータス：TRUEのプレイヤーが今得た金リンゴをメッセージに出力し、
   * 今得た金リンゴとそのプレイヤーの既所持金リンゴを足し　データクラスのappleSumに格納。
   * @param e　エンティティのアイテムピック時
   */
  @EventHandler
  private void onEntityPickupItemEvent(EntityPickupItemEvent e){
    if(e.getEntity() instanceof Player player
        && data.getStatus().get(player.getName())
        && e.getItem().getItemStack().getType()==Material.GOLDEN_APPLE) {

      //拾った金リンゴをメッセージに出す
      int goldenApples = e.getItem().getItemStack().getAmount();
      player.sendMessage(SingularPlural(goldenApples));
      //拾った金リンゴとプレイヤー全所持金リンゴを足しデータに加える
      goldenApples = SumGoldenApple(player, goldenApples);
      data.getAppleSum().put(player.getName(),goldenApples);
    }
  }

  /**
   * スライム族の種類と大きさで振り分けドロップ数を決定する
   * @param entity　敵
   * @return int ドロップ数
   */
  private int SortSlimeSize(Entity entity) {
    int k=0;
    if(entity instanceof MagmaCube magma &&magma.getSize()==4){
       k = magma.getSize()+3;
    }else if(entity instanceof MagmaCube magma){
      k=magma.getSize()+ 1;
    }else if(entity instanceof Slime slime &&slime.getSize()==4){
       k = slime.getSize()+1;
    }else if(entity instanceof Slime slime){
      k=slime.getSize();
    }
    return k;
  }

  /**
   * プレイヤーがインベントリーで所持する全金リンゴと得た金リンゴの合計を返す
   * @param player　
   * @param goldenApples　得た金リンゴ
   * @return goldenApples 合計金リンゴ
   */
  private static int SumGoldenApple(Player player, int goldenApples) {
    ItemStack[] itemStacks = player.getInventory().getContents();
    for (ItemStack item : itemStacks) {
      if (Objects.nonNull(item)
          && item.getType()==Material.GOLDEN_APPLE) {
        goldenApples += item.getAmount();
      }
    }
    return goldenApples;
  }

  /**
   * 得たリンゴ数を入れると単数・複数分けてリンゴ獲得のメッセージを返す。
   * @param goldenApples 得たリンゴ数
   * @return String
   */
  private String SingularPlural(int goldenApples){
    if(goldenApples >1){
      return goldenApples +" Golden Apples Get!";
    }else{
      return "1 Golden Apple Get!";
    }
  }

  /**
   * プレイヤーマインクラフト自体に参加した時にステータスに名前とFALSEを追加。
   * @param e　プレイヤーjoin
   */
  @EventHandler
  private void onPlayerJoinEvent(PlayerJoinEvent e){
    data.getStatus().put(e.getPlayer().getName(),Boolean.FALSE);
  }

  /**
   * プレイヤーがゲーム中にサーバーを離れた時、持ち物を返す
   * @param e　プレイヤーがゲームを離れた時
   */
  @EventHandler
  private void onPlayerQuitEvent(PlayerQuitEvent e){
    Player player = e.getPlayer();
  if(data.getStatus().get(player.getName())){
      player.getInventory().setContents(data.getInventory().get(player.getName()));
    }
  }

}
