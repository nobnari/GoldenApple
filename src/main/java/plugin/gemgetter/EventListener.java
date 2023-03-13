package plugin.gemgetter;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import plugin.gemgetter.command.GGStartCommand;

public class EventListener implements Listener {

  private GGStartCommand ggs;
  private int appleSum;

  public EventListener(GGStartCommand ggs) {
    this.ggs = ggs;
  }
  @EventHandler
  public void onPlayerJoinEvent(PlayerJoinEvent e){
    ggs.getStatus().put(e.getPlayer().getName(),Boolean.FALSE);
  }
  @EventHandler
  public void onEntityDeathEvent(EntityDeathEvent e){
    Player player = e.getEntity().getKiller();
    World world = e.getEntity().getWorld();
    Location l = e.getEntity().getLocation();
    if(Objects.nonNull(player)&&ggs.getStatus().get(player.getName())){
     world.dropItem(l,new ItemStack(Material.GOLDEN_APPLE));
    }
  }
  @EventHandler
  public void onEntityPickupItemEvent(EntityPickupItemEvent e){
    if(e.getEntity() instanceof Player player
        &&ggs.getStatus().get(player.getName())
        && e.getItem().getItemStack().getType()==Material.GOLDEN_APPLE) {
        ItemStack[] itemStacks = player.getInventory().getContents();
        appleSum = e.getItem().getItemStack().getAmount();
        for (ItemStack item : itemStacks) {
          if (Objects.nonNull(item)
              && item.getType()==Material.GOLDEN_APPLE) {
            appleSum += item.getAmount();
          }
        }
        player.sendMessage("ただいま金のリンゴ" + appleSum + "個所持");

    }

  }

}
