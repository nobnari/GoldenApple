package plugin.gemgetter.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

@Getter
@Setter
public class Rank {
  private final int MIDDLE_BORDER=64;
  private final int LOW_BORDER=32;

private int stars;
private String finishColor;
private String resultColor;
private Sound resultSound;
private Material prize;

  /**
   * 獲得リンゴに合わせて星分け。
   * 星に合わせて、メッセージ、色、サウンド変更
   * @param appleSum　最終リンゴ数
   */
  public Rank(Integer appleSum){
  if(appleSum>MIDDLE_BORDER){
    this.stars=3;
  }else if(appleSum>LOW_BORDER){
    this.stars=2;
  }else{
    this.stars=1;
  }
  switch (stars) {
    case 3 -> {
      this.finishColor = ChatColor.GOLD + "Finish!!!!";
      this.resultColor = ChatColor.GOLD + "Golden Delicious ★★★";
      this.resultSound = Sound.ENTITY_GOAT_SCREAMING_AMBIENT;
      this.prize=Material.GOLDEN_APPLE;
    }
    case 2 -> {
      this.finishColor = ChatColor.DARK_RED + "Finish!!";
      this.resultColor = ChatColor.DARK_RED + "Good Taste ★★";
      this.resultSound = Sound.ENTITY_WOLF_AMBIENT;
      this.prize=Material.MAGMA_CREAM;
    }
    case 1 -> {
      this.finishColor = ChatColor.DARK_GREEN + "Finish!";
      this.resultColor = ChatColor.DARK_GREEN + "Green Apple ★";
      this.resultSound = Sound.ENTITY_LLAMA_AMBIENT;
      this.prize=Material.SLIME_BALL;
    }
  }

}

}
