package plugin.gemgetter.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import plugin.gemgetter.Init;
import plugin.gemgetter.Main;
import plugin.gemgetter.data.Course;
import plugin.gemgetter.data.GGData;

/**
 *プレイヤーがスタートコマンドを入力した時に実行されるクラス
 */
public class GGStart extends SuperCommand {
  private final Main main;
    private final GGData data;
    private final Init init;
    public GGStart(Main main,GGData data, Init init) {
      this.main=main;
      this.data = data;
      this.init=init;
    }

  public boolean PlayerDoneCommand(Player player , Command command,  String[] args) {
    if(!data.getStatus().get(player.getName())) {
      Course course = new Course(main,args);
      data.getCourse().put(player.getName(),course);
      //↓初期装備保存&&ステータス変更↓
        data.getInventory().put(player.getName(), player.getInventory().getContents());
        data.getStatus().put(player.getName(), true);

        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 30, 1);
        init.initializer(player,course);
        player.sendMessage("ゲームスタート！");
        init.TimerStart(player,course);

    }else{
      player.sendMessage("ゲームはすでにはじまっている!!!");
    }
    return true;
  }
}
