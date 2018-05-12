package red.man10.mappstore.apps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;
import red.man10.mappstore.MappApp;
import java.awt.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;
import red.man10.mappstore.MappApp;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.Sound;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;
import red.man10.mappstore.MappApp;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.Sound;


/////////////////////////////////////////////////////////
//          Hello map app example
/////////////////////////////////////////////////////////



public class Bounens_Slots extends MappApp {
    ////////////////////////////////////////////
    //      App name (must be unique key)
    //      アプリ名：ユニークな必要があります
    final  static String appName = "bounenslots";
    static int amount=50;//⭐︎
    static String massege;
    static Player clickplayer;//⭐︎

    static int hit;//0=未当選 1=当選　//⭐︎
    static int slot_mode;//⭐︎ 0=回されていない 1=回されている 10~回された後
    static int slot_seconds;//⭐︎ スロットのロールが回った時間計測
    static int hit_number;//⭐︎ 当たり判定が出た時に揃う絵柄
    static int in_amount;
    static int mapId;

    //設定に使われる項目
    static String slot_name;//⭐︎
    static int hit_chance;//⭐︎
    static int use_coin;//⭐︎
    static int hit_coin;//⭐︎
    static String picture[] = new String[3];//⭐︎
    static int rool_time[] = new int[3];//⭐︎
    static int slot_picture_amount;//⭐︎
    static String slot_images[] = new String[30];//⭐︎
    static String slot_back;//⭐︎
    static Graphics2D g;//⭐︎




    ///////////////////////////////
    //     Data
    ///////////////////////////////
    static class MappData{
        static int amount=50;//⭐︎
        static String massege;
        static Player clickplayer;//⭐︎

        static int hit;//0=未当選 1=当選　/
        static int slot_mode;// 0=回されていない 1=回されている 10~回された後
        static int slot_seconds;//︎ スロットのロールが回った時間計測
        static int hit_number;//︎ 当たり判定が出た時に揃う絵柄

        //設定に使われる項目
        static String slot_name;//︎
        static int hit_chance;//︎
        static int use_coin;//︎
        static int hit_coin;//︎
        static String picture[] = new String[3];//
        static int rool_time[] = new int[3];//
        static int slot_picture_amount;//
        static String slot_images[] = new String[30];//
        static String slot_back;//
        static Graphics2D g;
        static int in_amount;
        //   Add your data here / マップごとに保存するデータはここに追加
    }
    static HashMap<Integer,MappData> hashMap = new HashMap<>();

    //      ユーザーデーター保存
    static MappData  loadData(int mapId) {
        MappData data = hashMap.get(mapId);
        if(data == null){
            data = new MappData();
        }
        return data;
    }
    //      ユーザーデータ読み込み
    static MappData saveData(int mapId, MappData data){
        return hashMap.put(mapId,data);
    }

    static public void register() {
        /////////////////////////////////////////////////
        //       Button (nearby map) clicked event
        //      ボタン押された時の処理
        DynamicMapRenderer.registerButtonEvent(appName, (String key, int mapId, Player player) -> {
            MappData m = loadData(mapId);
            switch (slot_mode){
                case 1://スロットはもう回されている
                    player.sendMessage("§b§l[Bounen'§f§lsSlot]§cこのスロットは回されています!");
                    break;

                case 0://スロットを回す処理
                new slots_data().slots_datas(0);//どのスロットを回すか?
                    saveData(mapId,m);
                    m.slot_seconds=0;m.slot_mode = 1;m.clickplayer = player;m.amount = m.amount - m.use_coin;
                player.sendMessage("§b§l[Bounen'§f§lsSlot]§aスロットを回しました!");

                //確率 当たり判定
                if(new Random().nextInt(10000)<=m.hit_chance){
                    hit=1;
                    hit_number = new Random().nextInt(m.slot_picture_amount);
                }
            }
            saveData(mapId,m);
            return true;
        });
        //////////////////////////////////////////////////
        //      DisplayTouch Event
        DynamicMapRenderer.registerDisplayTouchEvent(appName, (String key, int mapId, Player player, int x, int y) -> {
            MappData m = loadData(mapId);

            //////////////////////////////////////////////
            //  Get Graphics context for drawing
            //  描画用コンテキスト取得
            Graphics2D gr = DynamicMapRenderer.getGraphics(mapId);
            if (gr == null) {
                return false;
            }

            gr.setColor(Color.RED);
            gr.drawLine(x, y, x, y);
            return true;
        });

        /////////////////////////////////////////////////
        //      rendering logic 描画ロジックをここに書く
        DynamicMapRenderer.register(appName, 3, (String key, int mapId, Graphics2D g) -> {
            MappData m = loadData(mapId);
            /*
             スロット ボタンを押した後の処理
             */
            switch (m.slot_mode) {
                case 1:
                    m.slot_seconds = m.slot_seconds + 1;
                    //////////
                    //終了 処理
                    if (m.slot_seconds >= m.rool_time[2] + 1) {
                        m.slot_mode = 11;
                        m.hit = 0;
                        //当たり
                        if (m.picture[0].equalsIgnoreCase(m.picture[1]) && m.picture[1].equalsIgnoreCase(m.picture[2])) {
                            m.clickplayer.sendMessage("§b§l[Bounen'§f§lsSlot]§e§lおめでとうございます! コイン" + m.hit_coin + "枚獲得!");
                            m.amount = m.amount + m.hit_coin;//あたり追加
                            DynamicMapRenderer.drawImage(m.g, m.picture[2] + "", 82, 32, 32, 32);

                            World w = m.clickplayer.getWorld();//sounds
                            w.playSound(m.clickplayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
                            return true;
                        }
                        else {//ハズレ
                            m.clickplayer.sendMessage("§b§l[Bounen'§f§lsSlot]§b§lハズレ");
                        }
                        break;
                    }
               /*
               スロット 回転中処理
                */
                    //////
                    //描画 計算
                    if (m.slot_seconds <= m.rool_time[0]) { Bounens_Slots.slot_picture(0); }
                    if (m.slot_seconds <= m.rool_time[1]) { Bounens_Slots.slot_picture(1); }
                    if (m.slot_seconds <= m.rool_time[2]) { Bounens_Slots.slot_picture(2); }
                    ///////
                    //Sounds
                    World w = m.clickplayer.getWorld();
                    w.playSound(m.clickplayer.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 2);
                    break;
                case 0:
                }
            saveData(mapId, m);
            /////////////////////////////
            //        背景描画
            if(m.slot_mode==1 || m.slot_mode>=10) {
                if (m.slot_mode >= 10) {
                    switch (m.slot_mode) {
                        case 11:
                            m.slot_mode=10;break;
                        case 10:
                            m.slot_mode = 0;
                        Bukkit.broadcastMessage("" + m.amount);
                    }
                }
                    //描画
                    DynamicMapRenderer.drawImage(g, m.slot_back + "", 0, 0, 128, 128);

                    g.setColor(Color.BLACK);
                    g.setFont(new Font("SansSerif", Font.BOLD, 10));
                    g.drawString("[" + m.slot_name + "]", 0, 85);
                    g.drawString("1 play: " + m.use_coin + "枚", 0, 98);
                    g.drawString("当たり: " + m.hit_coin + "枚", 0, 111);

                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(13, 24, 103, 49);//52
                    g.setColor(Color.GRAY);
                    g.fillRect(13, 26, 103, 45);//50
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(13, 29, 103, 39);//48
                    g.setColor(Color.WHITE);
                    g.fillRect(13, 32, 103, 33);//46

                    g.setFont(new Font("SansSerif", Font.BOLD, 16));
                    g.setColor(Color.YELLOW);
                    g.drawString("所持コイン: " + m.amount, 0, 124);

                    DynamicMapRenderer.drawImage(g, m.picture[0] + "", 14, 32, 32, 32);
                    DynamicMapRenderer.drawImage(g, m.picture[1] + "", 48, 32, 32, 32);
                    DynamicMapRenderer.drawImage(g, m.picture[2] + "", 82, 32, 32, 32);
                    return true;
                }else{
                    return false;
                }
        });
    }
    /*
    スロット 絵柄
     */
    static public void slot_picture(int in_amount){
        MappData m = loadData(mapId);
        int randomNumber = new Random().nextInt(m.slot_picture_amount);
        picture[m.in_amount]=m.slot_images[randomNumber];

        if(hit==1 && (m.slot_seconds==m.rool_time[0] || m.slot_seconds==m.rool_time[1] || m.slot_seconds>=m.rool_time[2])){
                if (m.slot_seconds == m.rool_time[0]) { m.picture[0] = new String("" + m.slot_images[m.hit_number]).toString(); }
                if (m.slot_seconds == m.rool_time[1]) { m.picture[1] = new String("" + m.slot_images[m.hit_number]).toString(); }
                if (m.slot_seconds >= m.rool_time[2]) { m.picture[2] = new String("" + m.slot_images[m.hit_number]).toString(); }
            }
        }
    }
class slots_data{
    public void slots_datas(int slots_number){
        Bounens_Slots.MappData m = Bounens_Slots.loadData(Bounens_Slots.mapId);
        switch (slots_number){
            case 0:
                Bounens_Slots.slot_name="βスロット";
                Bounens_Slots.slot_back="bounenslot_fairy";
                Bounens_Slots.hit_chance=5000;// <hit_chans>/10000の確率で当たる

                Bounens_Slots.slot_picture_amount=4;//使う絵柄の枚数
                Bounens_Slots.slot_images[0]="bounenslot_diamond_hoe";//0から1としてカウントしてね
                Bounens_Slots.slot_images[1]="bounenslot_bread";//サイズ 16*16 or 32*32
                Bounens_Slots.slot_images[2]="bounenslot_wheat";
                Bounens_Slots.slot_images[3]="bounenslot_seeds_wheat";

                Bounens_Slots.use_coin=1;//回す時に使う枚数
                Bounens_Slots.hit_coin=2;//当たりの枚数

                Bounens_Slots.rool_time[0]=30;//ロールが回る時間 左
                Bounens_Slots.rool_time[1]=60;//ロールが回る時間 中
                Bounens_Slots.rool_time[2]=90;//ロールが回る時間 右
        }

    }
}


