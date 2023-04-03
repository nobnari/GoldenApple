package plugin.gemgetter.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import plugin.gemgetter.DAO.Register;
/**
 * スコアなどを表示するコマンドの実行内容
 */
public class GGRecord extends SuperCommand {
    public static final String EASY = "easy";
    public static final String NORMAL = "normal";
    public static final String HARD = "hard";
    Register regi = new Register();

    /**
     * 引数ごとにランキングを分ける
     * 引数なしの時は全ての中からランキングを表示
     *
     * @param player　プレイヤー
     * @param args　引数
     * @return 処理の実行有無判定
     */
    public boolean PlayerDoneCommand(Player player, Command command, String[] args){
        if(args.length==1&&(EASY.equals(args[0])||NORMAL.equals(args[0])|| HARD.equals(args[0]))){
            regi.displayScores(player, args[0]);
        }else{
            regi.displayScores(player);
        }
        return false;
    }
  }

