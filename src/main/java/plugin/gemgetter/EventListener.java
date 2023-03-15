package plugin.gemgetter;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.command.GGStart;
import plugin.gemgetter.data.GGData;
@Getter
@Setter
public class EventListener implements Listener {
//フィールド
  private GGData data;
  private int appleSum;
//コンストラクタ
  public EventListener(GGData data) {
    this.data = data;
  }

  /**
   * エンティティ死亡時、ステータスがTRUEのプレイヤーが倒すと金のリンゴドロップ
   * @param e　エンティティ死亡時
   */
  @EventHandler
  private void onEntityDeathEvent(EntityDeathEvent e){
    Player player = e.getEntity().getKiller();
    World world = e.getEntity().getWorld();
    Location l = e.getEntity().getLocation();
    if(Objects.nonNull(player)&& data.getStatus().get(player.getName())){
     world.dropItem(l,new ItemStack(Material.GOLDEN_APPLE));
    }
  }
  /**
   * プレイヤー(ステータス：TRUE)が今得た金リンゴと所持金リンゴを足し、メッセージに出力する
   * @param e　エンティティのアイテムピック時
   */
  @EventHandler
  private void onEntityPickupItemEvent(EntityPickupItemEvent e){
    if(e.getEntity() instanceof Player player
        && data.getStatus().get(player.getName())
        && e.getItem().getItemStack().getType()==Material.GOLDEN_APPLE) {
        ItemStack[] itemStacks = player.getInventory().getContents();
      appleSum = e.getItem().getItemStack().getAmount();
        for (ItemStack item : itemStacks) {
          if (Objects.nonNull(item)
              && item.getType()==Material.GOLDEN_APPLE) {
           appleSum += item.getAmount();
          }
        }
        player.sendMessage(appleSum+ " Golden Apples Get!");
        data.getAppleSum().put(player.getName(),appleSum);
    }

  }
  /**
   * プレイヤージョイン時にステータスに名前とFALSEを追加。
   * @param e　join
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
