package red.man10.mappstore.apps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;
import red.man10.mappstore.MappApp;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;

import java.awt.*;

public class labyrinth {

    ////////////////////////////////
    //     Data
    ///////////////////////////////////
    static class MappData{
        int         data;

        //   Add your data here
        //   マップごとに保存するデータはここに追加
    }


    ////////////////////////////////////////////
    //      App name (must be unique key)
    //      アプリ名：ユニークな必要があります
    final  static String appName = "labyrinth";

    static int mode = 0;
    static int blocks[][] = new int[30][30];
    static Graphics2D g;

    ////////////////////////////////////////////
    //     Draw refresh Cycle:描画割り込み周期
    //     appTickCycle = 1 -> 1/20 sec
    static public void register(){


        /////////////////////////////////////////////////
        //      Button (nearby map) clicked event
        //      ボタン押された時の処理
        DynamicMapRenderer.registerButtonEvent(appName, (String key, int mapId, Player player) -> {
            if (mode==0){
                mode=1;

                for(int x=0;x!=3;x++){
                    for(int y=0;y!=3;y++){
                        Bukkit.broadcastMessage("a");
                        blocks[x][y]=1;
                        labyrinth.drawblock(x,y);
                    }
                }
                mode=0;
            }

            return true;
        });

        //////////////////////////////////////////////////
        //      DisplayTouch Event
        DynamicMapRenderer.registerDisplayTouchEvent(appName, (String key, int mapId,Player player,int x,int y) -> {
            //////////////////////////////////////////////
            //  Get Graphics context for drawing
            //  描画用コンテキスト取得
            Graphics2D gr = DynamicMapRenderer.getGraphics(mapId);
            if(gr == null){
                return false;
            }

            gr.setColor(Color.RED);
            gr.drawLine(x,y,x,y);

            //    true -> updateView:描画更新
            return true;
        });

        /////////////////////////////////////////////////
        //      rendering logic 描画ロジックをここに書く
        DynamicMapRenderer.register( appName, 1, (String key, int mapId,Graphics2D g) -> {
            return true;
        });
    }
    static void drawblock(int x,int y){
        Bukkit.broadcastMessage(""+x+"/"+y);
        g.setColor(Color.WHITE);
        g.fillRect(x*8,y*8,8,8);
    }
}


