package plugin.gemgetter.command;
import org.bukkit.World;
import org.bukkit.entity.Player;
import plugin.gemgetter.Init;
import plugin.gemgetter.data.GGData;
/**
 *プレイヤーがスタートコマンドを入力した時に実行されるクラス
 */
public class GGStart extends SuperCommand {
    private final GGData data;
    private final Init init;
    public GGStart(GGData data, Init init) {
    this.data = data;
    this.init=init;
    }
  @Override
  public boolean PlayerDoneCommand(Player player) {
    if(!data.getStatus().get(player.getName())) {
      //↓初期装備保存&&ステータス変更↓
      data.getInventory().put(player.getName(), player.getInventory().getContents());
      data.getStatus().put(player.getName(),true);

      init.initializer(player);
      player.sendMessage("ゲームスタート！");
      init.TimerStart(player);
    }else{
      player.sendMessage("ゲームはすでにはじまっている!!!");
    }
    return true;
  }
}
