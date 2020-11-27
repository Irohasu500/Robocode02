//2020_11_19作成開始
//2020_11_23加筆
//2020_11_26 デバッグ終了
// onDeath,onWin,onScannedRobotがまだ

//Set_default.pyは
// 1:ファイルの作成。
// a_max, s_maxの分、0でファイルを埋める。


//robocodeで使うとき
// //%を外す

//%package Agent;
//%import robocode.*;
//%import java.awt.Color;

import java.lang.Math;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.lang.StringBuilder;

/*

    このプログラムの使用方法について。
    1　DLする。
    2  Agentというパッケージをrobocodeの "robocode/robot"に Agentパッケージを作成する。
    3  このプログラムをパッケージに入れる。
    4  Set_default.pyを実行して、このプログラムのコンパイルとQ値を格納するファイルの保存を行う。
    5  Learning.pyを実行して学習を進める。
    **注意** 
        Set_default.pyを実行すると、Q値を格納したファイルが初期化されてしまう。
        新しく学習を進めたい場合は、Set_defailt.pyの 設定部分->ファイル名 を変更すること。
    
    環境
        ・robocodeファイルはroot(Windowsでは、"C:"のこと)にあることを仮定している。
        Usersで実行したい場合、
        *********** 要編集　 ************
        ・このファイルの  行目の　　　
        ・Set_default.pyの  部分

        *********** 要編集　 ************

        を書き直せばよい。



*/


// 体力
//　自身の場所
//　敵の場所
// 



//Agentがrobocodeのrobotクラスを拡張しないといけないのでは？
//以下に書き換える。
//%public class Agent extends Robot
public class Agent{

    /*==================設定項目=============================*/

    String file_name = "q.txt"; //= "/robocode/robots/Agent/q_vql.txt";



    //======================================================

    //フィールド変数
    int s;
    int a;
    String a_str;
    String s_str;
    int a_max; //= 299; //種類 インデックスは 0-298
    int s_max;// = 400 + (19 + 19 * 20)+(19 + 19 * 20)*400 + 1; //種類 400 * 401
    int x;
    int y;
    double hp;
    double q[][];       //q値。q[状態][行動]
    /*
        学習率：現在の値、未来の値についてどの程度
        値を考慮して配分するかを決める。
        aplha >0.5なら、未来重視
        else            現在重視(Q値が減りにくいようにする)
    */
    double alpha = 0.2;  //パラメータ
    /*

        割引率gammma:将来の価値をどの程度割り引いて反映させるかの値
        gammaが小さければ小さいほど、将来の値は小さく反映。

    */
    double gamma = 0.5;
    int caldid_flag = 1;
    int s_next;
    

    //コントラクタ
    public Agent(int s_max, int a_max){
        this.s_max = s_max;
        this.a_max = a_max;
        //q値を用意。
        //javaでは配列は初期化される。
        q = new double[s_max][a_max];
    }

    /****************  メイン ***************/
    public static void main(String args[]){
        //Agent agent = new Agent(160400,299);
        Agent agent = new Agent(10,10);

        agent.ReadFile();
        for(int i=0;i<10;i++){
            for(int j =0;j<10;j++){
                System.out.println(agent.q[i][j]);
            }
        }

        agent.q[0][5] = 1000;
        agent.q[2][0] = 222;

        agent.save_Qval();
 


    }
    //=====================================================================================

    //未

//     /*
//     10桁のint型の整数aabbccdde
//     aa = 自戦車のx座標
//     bb = 自戦車のy座標
//     cc = 敵戦車のx座標
//     dd = 敵戦車のy座標
//     e  = 敵戦車をスキャンしたかどうか(スキャンしたときは0できなかったときは1)
//     */

//     public String correspondence_a(int s){
//         String now_action;

//         /*ここから*/

//         now_action = "asda";

//         String s1 = "scan:";
//         int r = 0;

//         r = s % 10;
//         s = s / 10;

//         if(r == 0){                 //敵をスキャンしたかどうかの場合分け
//             s1 = s1 + "on ";
//         }else if(r == 1){           //敵をスキャンしたかどうかの場合分け
//             s1 = s1 + "off ";
//         }else{
//             s1 = s1 + "error ";
//         }

//         s1 = s1 + "enemy:y=";       //敵のy座標
//         r = s % 100;
//         s = s / 100;
//         s1 = s1 + r;
        
//         s1 = s1 + " x=";            //敵のx座標
//         r = s % 100;
//         s = s / 100;
//         s1 = s1 + r;

//         s1 = s1 + " me:y=";         //自機のy座標
//         r = s % 100;
//         s = s / 100;
//         s1 = s1 + r;

//         s1 = s1 + " x=";            //自機のx座標
//         r = s % 100;
//         s = s / 100;
//         s1 = s1 + r;

//         return s1;
//         /*
//         出力例)　scan:on enemy:y=12 x=8 me:y=10 x=16
//         */
        
//         /*ここまで*/
        
//         return now_action;
//     }

//     /*
//         int型の状態を表すsを格納したとき、
//         文字列に直して返す関数。
//         * 文字列は,(カンマ)で区切る

//     */
//     /*
//     10桁のint型の整数aabbccddef
//     aa = 自戦車のx座標
//     bb = 自戦車のy座標
//     cc = 敵戦車のx座標
//     dd = 敵戦車のy座標
//     e  = 自戦車の体力(低 = 0 中 = 1 高 = 2)
//     f  = 敵戦車をスキャンしたかどうか(スキャンしたときは0できなかったときは1)
//     */
//     public String correspondence_b(int s){
//         String now_state;

//         /*ここから*/

//         now_state = "asda";

//         String s2 = "scan:";
//         int r = 0;

//         r = s % 10;
//         s = s / 10;
//         if(r == 0){                 //敵をスキャンしたかどうかの場合分け
//             s2 = s2 + "on ";
//         }else if(r == 1){           //敵をスキャンしたかどうかの場合分け
//             s2 = s2 + "off ";
//         }else{
//             s2 = s2 + "error ";
//         }

//         s2 = s2 + "hp:";
//         r = s % 10;
//         s = s / 10;
//         if(r == 0){                 //自機のhpが低い
//             s2 = s2 + "row ";
//         }else if(r == 1){           //自機のhpが中
//             s2 = s2 + "middle ";
//         }else if(r== 2){            //自機のhpが高い
//             s2 = s2 + "high ";
//         }else{
//             s2 = s2 + "error ";
//         }

//         s2 = s2 + "enemy:y=";       //敵のy座標
//         r = s % 100;
//         s = s / 100;
//         s2 = s2 + r;
        
//         s2 = s2 + " x=";            //敵のx座標
//         r = s % 100;
//         s = s / 100;
//         s2 = s2 + r;

//         s2 = s2 + " me:y=";         //自機のy座標
//         r = s % 100;
//         s = s / 100;
//         s2 = s2 + r;

//         s2 = s2 + " x=";            //自機のx座標
//         r = s % 100;
//         s = s / 100;
//         s2 = s2 + r;

//         return s2;
//         /*
//         出力例)　scan:on hp:high enemy:y=12 x=8 me:y=10 x=16
//         */
        
//         /*ここまで*/
        
//         return now_state;
//     }


//     /*
//         int型の状態を表すsを格納したとき、
//         文字列に直して返す関数。
//         * 文字列は,(カンマ)で区切る

//     */

//     /*
//     10桁のint型の整数aabbccddef
//     aa = 自戦車のx座標
//     bb = 自戦車のy座標
//     cc = 敵戦車のx座標
//     dd = 敵戦車のy座標
//     e  = 自機の速度(遅 = 0 中 = 1 速 = 2)
//     f  = 奇数(f=1,3)ならスキャン失敗,偶数(f=2,6)なら成功
//         それぞれで小さいほう(f=1,2)なら敵の弾がヒット,大きい方(f=3,6)ならヒットしなかった
//     */

//     public String correspondence_c(int s){
//         String now_action;

//         /*ここから*/

//         now_action = "asda";

//         String s3 = "scan:";
//         int r = 0;

//         r = s % 10;
//         s = s / 10;
//         if(r % 2 == 0){             //敵をスキャンしたかどうかの場合分け(偶数はスキャン成功)
//             s3 = s3 + "on ";
//             r = r / 2;
//         }else if(r % 2 == 1){       //敵をスキャンしたかどうかの場合分け(奇数はスキャン失敗)
//             s3 = s3 + "off ";
//         }else{                      //それ以外ならエラー
//             s3 = s3 + "error ";
//         }

//         s3 = s3 + "hit:";
//         if(r == 1){                 //敵の弾が当たった
//             s3 = s3 + "on ";
//         }else if(r == 3){           //敵の弾が当たらなかった
//             s3 = s3 + "off ";
//         }else{                      //それ以外ならエラー
//             s3 = s3 + "error ";
//         }

//         s3 = s3 + "speed:";
//         r = s % 10;
//         s = s / 10;
//         if(r == 0){
//             s3 = s3 + "slow ";      //自機の速度が遅い
//         }else if(r == 1){
//             s3 = s3 + "middle ";    //自機の速度が中
//         }else if(r ==2){
//             s3 = s3 + "fast ";      //時機の速度が速い
//         }else{
//             s3 = s3 + "error ";
//         }

//         s3 = s3 + "enemy:y=";       //敵のy座標
//         r = s % 100;
//         s = s / 100;
//         s3 = s3 + r;
        
//         s3 = s3 + " x=";            //敵のx座標
//         r = s % 100;
//         s = s / 100;
//         s3 = s3 + r;

//         s3 = s3 + " me:y=";         //自機のy座標
//         r = s % 100;
//         s = s / 100;
//         s3 = s3 + r;

//         s3 = s3 + " x=";            //自機のx座標
//         r = s % 100;
//         s = s / 100;
//         s3 = s3 + r;

//         return s3;
//         /*
//         出力例)　scan:on hit:on speed:middle enemy:y=12 x=8 me:y=10 x=16
//         */
        
//         /*ここまで*/
        
//         return now_action;
//     }

    //=====================================================================================

    //済

    /*
        ε-Greedy法に基づいて、次の行動を決定する
        引数：double epsilon
        戻り値：int 
            1：ランダム
            0:Q値最大を選択する
    */

    public int epsilon_greedy(double epsilon){
        //0.0以上～1.0未満の乱数を生成
        double k = Math.random();
        //epsilonより小さい　→　ランダム
        if (k <epsilon){
            return 1;
        }else{
            return 0;
        }
    }

    //=====================================================================================
    //済
    /*
        Q値が最大の行動を選択する
        引数：状態sを表すint型変数
        戻り値：次の行動を表すint型変数
        か、-1:ランダム



    */
    public int action_max_Q(int s){
        int index_max=-1;
        double max=0;
        //Q値が最大の行動を求める
        for(int i=0; i<a_max; i++){
            if(q[s][i] > max){
                index_max = i;
            }
        }
        //更新されなければ、-1を
        //更新されたら、q値が最大の行動を返す。
        return index_max;
    }
    //=====================================================================================
    //済

    /*
        状態を固定してランダムに行動する。
        引数：int型変数状態s
        戻り値：次の行動を表すint型変数a

    */
    public int random_action(int s){
        Random rand = new Random();
        //0以上～a_max-1以下の整数乱数を生成
        int k = rand.nextInt(a_max);
        return k;
    }

    //=====================================================================================
    //未
    /*
        報酬を得るメソッド
        ＊報酬はscoreとする。　無理。
            報酬は死んだら-100
            生き残れば、100
        ＊robocodeのイベント処理を使わないとできない。
            robotが死んだとき　or 試合が終了したとき
        に実行されるようにする。

        (イベント処理を使うので、robotクラスに実装されるかも)

        アルゴリズム
            robotが死んだ　or 試合が終了したとき
            Q値を更新する。
            ファイルにQ値を保存する。

        ヒント：
        onDeath(DeathEvent event)　や
        onWin(WinEvent event)で実行して、
        結果をフィールド変数に保存するなどする
        
    public void onDeath(DeathEvent event){
        int r = -100;   //報酬
        //ここで次の状態(this.s_next)を格納
        //Q値の更新
        cal_Q_value(this.s, this.a,this.s_next , r);

        //ファイルに書き込み
        save_Qval();
    }

    public void onWin(WinEvent event){
        int r = 100;   //報酬
        //ここで次の状態(this.s_next)を格納
        //Q値の更新
        cal_Q_value(this.s, this.a,this.s_next , r);

        //ファイルに書き込み
        save_Qval();

    }



    */


    //=====================================================================================

    //未
    /*
        ロボットの現在位置を返すメソッド
        ＊robocodeのrobotクラスのメソッドを使う。
        x: getX();
        y: getY();
        で可能。
        結果はフィールド変数に保存するなどする。
        引数：なし。
        戻り値：マス目(状態.txt参照)

        ＊注意getX, getYはdoubleなので
        ディジタル化したものに直す。
        (状態.txtを参照)

        0-399
    */

    /*
    public int get_position(){
        double robot_x = getX();
        double robot_y = getY();
        //前処理
        if (robot_x == 800){
            robot_x = robot_x - 0.01;
        }
        if (robot_y == 800){
            robot_y = robot_x - 0.01;
        }
        //マスに戻す。
        this.x = (int) robot_x / 40;
        this.y = (int) robot_y /40;
        return this.x + this.y * 20;
    }
    */
    

    //=====================================================================================

    //未

    /*
        ロボット自身の体力を返すメソッド
        引数：なし。
        戻り値：体力の段階を表す状態int型変数。
        0: 0以上～30未満, 
        1:30以上～60未満, 
        2:60以上～100以下

        double型変数hpに自分の体力を格納する。
    */
    /*
    public int get_hp(){
        this.hp = getEnegy();
        if(this.hp <30){
            return 0;
        }else if(this.hp<60){
            return 1;
        }else{
            return 2;
        }
    }
    */
    

    //=====================================================================================
    
    /*
        実際にrun()で使う。
        １：状態を把握し、
        1.5; 方策を決定し、
        2:行動し、
        3:次の状態を把握し、
        4:Q値を更新し、
        (報酬は得ないが今後のことを考えて一応組み込む)
        
        5:1-4を繰り返す。
        引数：なし。
        戻り値：報酬を得た1, 得ない0を返す。

    */
    public void action(){
        //1
        //ここで状態を把握してthis.s, this.aを更新。
        //デバッグ
        this.s =100;// get_position();

        //2
        if (epsilon_greedy(0.05) == 1){
            //ランダム
            this.a = random_action(this.s);
            //a, sがきまった。
        }else{
            //Q値最大を選択。
            this.a = action_max_Q(this.s);
            //全て0 →　ランダム
            if(this.a == -1){
                this.a = random_action(this.s);
            }
        }
        caldid_flag = 0;

        
        //実際に行動
        set_action(this.a);

        

        //3:次の状態の把握
        //this.s_next = get_position();
        //ここで次の状態(this.s_next)を格納

        //報酬を設定。
        //int r=0;

        //4:Q値の更新
        //cal_Q_value(this.s, this.a,this.s_next , r);
        caldid_flag = 1;

        //終了
    }

    //=====================================================================================

    /*
        現在の状態を把握して
        状態sを変更するメソッド

        今回はrobotAについて作成

        ・自身の位置 20*20分割
        400通り 0-399
        敵のスキャン 400
        ・敵の位置(スキャンした場合のみ)
        
        ・敵の位置20*20=400通り
        401-800

        ＊スキャンしてない
        [400] * 1 
                0-399

        *スキャンした
        [400] * [400] = 160000
        index = 0-(160000-1)
                400 + index

    */
    //スキャンしてない場合
    public int get_now_state(){
        int now_state=0;
        //now_state = get_position();
        return now_state;
    }
    //スキャンした場合
    /*
    public void onScannedRobot(ScannedRobotEvent event){
        int position=0;
        int pre_state = this.s;

        //============================================================

        //自身の位置を習得
        double my_x= getX();
        double my_y = getY();
        //敵の位置
        double ene_angle = event.getBearing();
        double ene_d = event.getDisttance();
        double ene_x = my_x + ene_d * Math.sin(ene_angle);
        double ene_y = my_y + ene_d * Math.cos(ene_angle);

        //正規化
        if(my_x == 800){
            my_x = my_x - 0.01;
        }
        if(my_y == 800){
            my_y = my_y - 0.01;
        }
        if(ene_x == 800){
            ene_x = ene_x - 0.01;
        }
        if(ene_y == 800 ){
            ene_y = ene_y - 0.01;
        }

        //配列に直す。
        //1:状態を求める
        this.s = 400 + (my_x/40 + (my_y/40) * 20)+ 
            (ene_x/40 + (ene_y/40) * 20)*400;
        //============================================================
        
        //未計算ならQ値更新
        if(caldid_flag == 0){
            int r=0;
            cal_Q_value(pre_state, this.a,this.s,r);
        }

        //デバッグ
        //System.out.println(this.s);


        //2:行動を求める
        if (epsilon_greedy(0.05) == 1){
            //ランダム
            this.a = random_action(this.s);
            //a, sがきまった。
        }else{
            //Q値最大を選択。
            this.a = action_max_Q(this.s);
            if(this.a == -1){
                this.a = random_action(this.s);
            }
        }

        caldid_flag = 0;
        
        //実際に行動
        set_action(this.a);

        //3:次の状態の把握
        //ここで次の状態(this.s_next)を格納

        //報酬を設定。
        int r=0;

        //4:Q値の更新
        cal_Q_value(this.s, this.a,this.s_next , r);
        caldid_flag = 1;

        //終了
        

    }
    */


    //=====================================================================================

    //済
    /*
        行動を決めるメソッド
        aから行動を選択する。

        行動aの決め方は以下の通り。
        1: ahead(x); x= 10刻み 0-400    40 10, ... 400
        2: back(x); x= 10刻み 0-400     40
        3: turnRight(x); x= 10刻み　0-360   36 10, 20, ... 360
        4: turnLeft(x); x= 10刻み   36
        5: turnRadarLeft(x) x=10 36
        6:turnRadarRight(x)x=10 36
        7:turnGunLeft(x) x=10 36
        8:turnGunRight(x) x=10 36
        9:fire(x)x=1,2,3 3
        計　299通り(0-298)

        よって、a_max = 299(0インデックスではない)

    */
    //上の決め方でいく。
    //行動に成功したら1を返す

    // //%をとって使う。
    public int set_action(int a_num){
        int x=0;
        if(a_num <40){
            //ahead
            x = a_num *10 + 10;
            System.out.println(x);
            System.out.println("ahead(x)");
            //% ahead(x);
            return 1;
        }else if(a_num <80){ //40-79
            //back
            a_num = a_num -40;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("back(x)");
            //% back(x);
            return 1;
        }else if(a_num < 116){// 80-115
            //残ったのは80以上
            //turnRight
            a_num = a_num -80;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnRight(x)");
            //% turnRight(x);
            return 1;
        }else if(a_num< 152){
            //turnLeft
            a_num = a_num -116;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnLeft(x)");
            //% turnLeft(x);
            return 1;
        }else if(a_num< 188){
            //turnRadarRight
            a_num = a_num -152;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnRadarRight(x)");
            //% turnRadarRight(x);
            return 1;
        }else if(a_num <224){
            //turnRadarLeft
            a_num = a_num -188;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnRadarLeft(x)");
            //% turnRadarLeft(x);
            return 1;
        }else if(a_num< 260){
            //turnGunLeft
            a_num = a_num -224;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnGunLeft(x)");
            //% turnGunLeft(x);
            return 1;
        }else if(a_num <296){
            //turnGunRight
            a_num = a_num -260;
            x= a_num *10 + 10;
            System.out.println(x);
            System.out.println("turnGunRight(x)");
            //% turnGunRight(x);
            return 1;
        }else if(a_num < 299){
            //fire
            a_num = a_num -296;
            x= a_num  + 1;
            System.out.println(x);
            System.out.println("fire(x)");
            //% fire(x);
            return 1;
        }
        return 0;
    }

    //=====================================================================================

    //robotA用にrobotの状態を把握するメソッド
    //用いる状態は以下
    /*　
        自分の位置
        敵の位置
        スキャンしたか

    */ 

    public void s_robotA(){
        
    }

    /*
        自分の位置
        敵をスキャンしたか
        スキャンした敵の位置
        自分のrobotの体力

    */
    public void s_robotB(){

    }

    /*
        自分の位置
        敵をスキャンしたか
        スキャンした敵の位置
        自分の速度
        敵の弾があたったか
    */
    public void s_robotC(){

    }



    //=====================================================================================

    //済

    //ファイルを開いて、Q値を取り出す。
    //Q値はthis.qに保存。
    //プログラム開始時に一回だけ実行
    public void ReadFile(){
        int i=0;
        int j=0;
        try{
            File file  =  new File(this.file_name);
            FileReader filereader = new FileReader(file);
            String val = "";
            int s_int;
            String s;

            //一文字読み込み
            while((s_int = filereader.read()) != -1){
                s = String.valueOf((char)s_int);
                val = val + s;
                //System.out.println(val);
                if(! s.equals(",")){
                    continue;
                }
                //カンマ取り除き (末尾削除)
                val = val.substring(0, val.length()-1);
                //暗黙的型変換 int →　double
                q[i][j] = Integer.parseInt(val);  //q値。q[状態][行動]
                //System.out.println(val);
                j++;
                if(j == a_max){
                    j=0;
                    i++;
                }
                val = "";
            }
            filereader.close();
            /* デバッグ
            if((i==s_max) && (j==a_max)){
                System.out.println("seccess!");
            }*/
        } catch(FileNotFoundException e) {
            System.out.println(e);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    //=====================================================================================

    //要実験
    //仮済

    //ファイルを開いて、Q値を保存。
    public void save_Qval(){
        int i=0;
        int j=0;
        try{
            File file  =  new File(this.file_name);
            FileWriter filewriter = new FileWriter(file);
            int val;
            for(i=0;i<s_max;i++){
                for(j=0;j<a_max;j++){
                    filewriter.write(q[i][j] + ",");     //\rミスかも
                }
            }


            filewriter.close();
            
        }  catch(IOException e) {
            System.out.println(e);
        }
    }
    
    //=====================================================================================

    //済

    //Q値の計算

    public void cal_Q_value(int i, int j, int i_next, double r){
        int max_a = action_max_Q(i_next);
        
        //デバッグ
        System.out.println(max_a);

        //q[状態][行動]
        q[i][j] = q[i][j] + this.alpha * 
            (r + this.gamma*q[i_next][max_a] - q[i][j]);
    }
    //=====================================================================================




    //=========今後すること============-

    //run関数での動作
    //実際にrobocodeに埋め込み

    //=================================

    //onScannedRobot問題を解決する。
    // onScannedRobotが再帰的に実行された場合、
    // 状態がすでに変わっており、Q値の更新ができない問題
    //解決策１：この状態設定をやめる
    //解決策2:　onScannedRobotを実行させないように、
    // flagをたてて、onScanedRobotの先頭で例外処理
    //そこで最初に状態を把握してこれを次の状態として
    //　Q値を更新し、
    //次の処理に移る。
    // 3つぐらい変数いる
    // event_flag
    // pre_a;  //いらないかも
    // pre_s;   //いらないかも

    //解決策3: レーダーを無効化させる。


    //9.10 print_Q_value

    //run関数中での動作
    //actionとの兼ね合い
  


}
