package context;

import java.io.IOException;


/**
 * @program: simplePsForModelPartition
 * @description: 全局常亮存储在这个类中
 * @author: SongZhen
 * @create: 2018-12-02 18:19
 */
public class ServerContext {

    /** 当前是第几台server*/
    public static int serverId;

    /** 判断是否已经初始化过了*/
    private static boolean inited=false;

    /** 判断是否异步*/
    public static boolean isAsy;


    public static void init()  {
        if (inited){
            return;
        }

        serverId=1;
        isAsy=false;



        inited=true;






    }
}