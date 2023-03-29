package plugin.gemgetter;

import java.util.Objects;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.data.GGData;

/**
 * ゲーム中のイベント、及びマインクラフトへの参加、離脱を監視するクラス
 */
public class EventListener implements Listener {
  private final GGData data;
  private final Fini fini;
  public EventListener(GGData data,Fini fini) {
    this.data = data;
    this.fini=fini;
  }

  /**
   * スライムとマグマキューブが死んだ時、ステータスがTRUEのプレイヤーのKillだと金のリンゴドロップ
   * @param e　敵が死んだ時
   */
  @EventHandler
  private void onEntityDeathEvent(EntityDeathEvent e){
    LivingEntity entity=e.getEntity();
    Player player = entity.getKiller();
    World world = entity.getWorld();
    Location l = entity.getLocation();
    if (Objects.nonNull(player) && data.getStatus().get(player.getName())) {
      switch (entity.getType()) {
          case SLIME, MAGMA_CUBE -> {
          ItemStack GA = new ItemStack(Material.GOLDEN_APPLE);
          GA.setAmount(SortSlimeSize(entity));
          world.dropItem(l, GA);
          }
      }
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
   * スライムボール、マグマクリームでガチャ
   * @param e アイテムドロップ時(true)
   */
  @EventHandler
  private  void onPlayerDropItemEvent(PlayerDropItemEvent e){
    Player player = e.getPlayer();
    Item item = e.getItemDrop();
    if(data.getStatus().get(player.getName())){
      switch (item.getItemStack().getType()) {
        case MAGMA_CREAM -> {
          item.remove();
          int random = new SplittableRandom().nextInt(8);
          switch (random) {
            case 0 -> {
              item.getWorld().dropItem(item.getLocation(), new ItemStack(Material.DIAMOND_SWORD));
              player.sendMessage("SSR!!! ダイヤモンドの剣Get!!!!");
            }
            case 1, 2-> {
              item.getWorld().dropItem(item.getLocation(), new ItemStack(Material.GOLDEN_SWORD));
              player.sendMessage("SR!! 金の剣Get!!!");
            }
          }
        }
        case SLIME_BALL -> {
          item.remove();
          int random = new SplittableRandom().nextInt(8);
          switch (random) {
            case 0 -> {
              Integer time = data.getTime().get(player.getName());
              data.getTime().put(player.getName(), (time + 10));
              player.playSound(player, Sound.BLOCK_GLASS_BREAK, 30, 45);
              player.sendMessage("大当たり!! タイム10秒追加!!!");
            }
            case 1,2,3 -> {
              Integer time = data.getTime().get(player.getName());
              data.getTime().put(player.getName(), (time + 5));
              player.playSound(player, Sound.BLOCK_GLASS_BREAK, 30, 45);
              player.sendMessage("当たり! タイム５秒追加!!");
            }

          }
        }
      }
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

  /**
   * ステータスtrueのプレイヤーが死んだタイミングで行う処理(リスポーンとセット)
   * タイム−100でゲーム強制終了、周囲の敵消去、ドロップするはずの金装備消去
   * @param e プレイヤー(true)死亡時
   */
  @EventHandler
  private void onPlayerDeathEvent(PlayerDeathEvent e) {
    Player player = e.getEntity();
    if (data.getStatus().get(player.getName())) {
      data.getTime().put(player.getName(),-100);
      fini.EntityVanishEX(player);
      e.getDrops().clear();
    }
  }

  /**
   * ステータスtrueプレイヤーが死んだのちリスポーンタイミングで行う処理(デスとセット)
   * ステータス変更、持ち物返却、メッセージ
   * @param e プレイヤー(true)リスポーン時
   */
  @EventHandler
  private void onPlayerRespawnEvent(PlayerRespawnEvent e) {
    Player player = e.getPlayer();
    if (data.getStatus().get(player.getName())) {
      data.getStatus().put(player.getName(), false);
      player.getInventory().setContents(data.getInventory().get(player.getName()));
      player.sendMessage("このゲームは死んだら終わり…何も無い…");
    }
  }

}
