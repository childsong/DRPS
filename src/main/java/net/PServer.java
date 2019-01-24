package net;


import Util.MemoryUtil;
import Util.MessageDataTransUtil;
import com.google.common.collect.Maps;

import com.google.common.util.concurrent.AtomicDoubleArray;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yahoo.sketches.quantiles.DoublesSketch;
import com.yahoo.sketches.quantiles.UpdateDoublesSketch;
import context.Context;
import context.ServerContext;
import context.WorkerContext;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import lombok.Synchronized;
import org.iq80.leveldb.DB;
import org.jblas.FloatMatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.KVStore;

import java.io.IOException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;


/**
 * @program: simplePsForModelPartition
 * @description: 参数服务器server端
 * @author: SongZhen
 * @create: 2018-12-02 17:59
 */

@Data
public class PServer implements net.PSGrpc.PS {
    private Server server;
    private Executor updateThread= Executors.newSingleThreadExecutor();
    private Map<String,String> updateKeys= Maps.newConcurrentMap();
    private KVStore store=new KVStore();
    private Map<String,FloatMatrix> floatMatrixMap=new ConcurrentHashMap<String, FloatMatrix>();

    private AtomicInteger globalStep=new AtomicInteger(0);
    private AtomicInteger workerStep=new AtomicInteger(0);
    static Logger logger=LoggerFactory.getLogger((PServer.class));
    AtomicBoolean finished=new AtomicBoolean(false);
    private AtomicBoolean workerStepInited=new AtomicBoolean(false);
    private float[] maxFeature=new float[Context.featureSize];
    private float[] minFeature=new float[Context.featureSize];

    private static AtomicBoolean isExecuteFlag=new AtomicBoolean(false);
    private static AtomicInteger workerStepForBarrier =new AtomicInteger(0);

    private ConcurrentSet<Float> numSet_otherWorkerAccessVi=new ConcurrentSet<Float>();
    private static volatile Float floatSum=0f;
    private static AtomicBoolean isFinished=new AtomicBoolean(false);
    private static AtomicBoolean isStart=new AtomicBoolean(false);


    private static ConcurrentMap<Long,Integer> vAccessNumMap=new ConcurrentHashMap<Long, Integer>();
    private static ConcurrentSet<Long> prunedVSet=new ConcurrentSet<Long>();
    private static AtomicInteger waitThread=new AtomicInteger(0);


    //太乱了，下面是barrier专用变量
    private static AtomicInteger barrier_waitThread=new AtomicInteger(0);
    private static AtomicBoolean barrier_isExecuteFlag=new AtomicBoolean(false);

    private static AtomicBoolean isExecuteFlag_otherLocal=new AtomicBoolean(false);
    private static AtomicBoolean isFinished_otherLocal=new AtomicBoolean(false);
    private static AtomicBoolean isWait_otherLocal=new AtomicBoolean(false);
    private static AtomicInteger workerStepForBarrier_otherLocal=new AtomicInteger(0);

    private static ConcurrentSet<Long> localVSet=new ConcurrentSet<Long>();


    public PServer(int port){
        this.server = NettyServerBuilder.forPort(port).maxMessageSize(Context.maxMessageSize).addService(net.PSGrpc.bindService(this)).build();
    }

    public void start() throws  IOException{
        this.server.start();
        logger.info("PServer Start");
        init();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                PServer.this.stop();
            }
        });
    }


    public void init(){
        // 初始化feature的min和max数组
        for(int i=0;i<maxFeature.length;i++){
            maxFeature[i]=Float.MIN_VALUE;
            minFeature[i]=Float.MAX_VALUE;

        }
    }

    public void stop(){
        if(this.server!=null){
            server.shutdown();
        }
    }

    public void blockUntilShutdown()throws InterruptedException{
        if(server!=null){
            server.awaitTermination();
        }
    }

    @Override
    public void pushAFMatrix(MatrixMessage req,StreamObserver<MatrixMessage> responseObject){
        store.getL().set(0);
        FloatMatrix afMatrix=MessageDataTransUtil.MatrixMessage_2_FloatMatrix(req);


        floatMatrixMap.put(req.getKey(),afMatrix);

        store.sumAFMatrix(afMatrix);
        while(store.getL().get()<Context.workerNum){
            try{
                Thread.sleep(10);
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        responseObject.onNext(MessageDataTransUtil.FloatMatrix_2_MatrixMessage(store.getSum().get("freq")));
        responseObject.onCompleted();

    }

    @Override
    public void aFMatrixDimPartition(KeyValueListMessage req,StreamObserver<PartitionListMessage> responseObject){
//        Map<Long,Integer> map=MessageDataTransUtil.KeyValueListMessage_2_Map(req);
//        store.mergeDim(map);
//        store.getL().incrementAndGet();
//
//        while(store.getL().get()< Context.workerNum){
//            try{
//                Thread.sleep(10);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        responseObject.onNext(null);
//        responseObject.onCompleted();
        return;

    }

    @Override
    public void getIndexOfSparseDim(SListMessage req,StreamObserver<SLKVListMessage> responsedObject){
        synchronized (ServerContext.kvStoreForLevelDB.getCurIndexOfSparseDim()){
            try{
                Map<String,Long> map=ServerContext.kvStoreForLevelDB.getIndex(req);
                map.put("CurIndexNum",ServerContext.kvStoreForLevelDB.getCurIndexOfSparseDim().longValue());
//            logger.info("curIndex:"+ServerContext.kvStoreForLevelDB.getCurIndexOfSparseDim().longValue());

                SLKVListMessage slkvListMessage=MessageDataTransUtil.Map_2_SLKVListMessage(map);
                logger.info(ServerContext.kvStoreForLevelDB.getCurIndexOfSparseDim().toString());

                responsedObject.onNext(slkvListMessage);
                responsedObject.onCompleted();
            }catch (IOException e){
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getSparseDimSize(RequestMetaMessage req,StreamObserver<LMessage> reponseObject){
        LMessage.Builder sparseDimSize=LMessage.newBuilder();

        logger.info("host:"+req.getHost());
        workerStep.incrementAndGet();

        while(workerStep.longValue()<Context.workerNum){
            try {
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }


        sparseDimSize.setL(ServerContext.kvStoreForLevelDB.getCurIndexOfSparseDim().longValue());
        reponseObject.onNext(sparseDimSize.build());
        reponseObject.onCompleted();
    }

    @Override
    public void sentSparseDimSizeAndInitParams(InitVMessage req,StreamObserver<BMessage> responseObject){
        Context.sparseDimSize=req.getL();
        // 开始利用sparseDimSize，采用取余的方式进行数据分配
        // 把LList转换成Set
        Set[] vSet=new Set[Context.serverNum];
        for(int i=0;i<vSet.length;i++){
            vSet[i]=new HashSet<Long>();
        }

        // 把list转换成Set[]
        for(int i=0;i<vSet.length;i++){
            for(long l:req.getList(i).getLlistList()){
                System.out.println("key:"+req.getList(i).getKey()+",i:"+i);
                vSet[i].add(l);
            }
        }


        try{
            ServerContext.kvStoreForLevelDB.initParams(req.getL(),vSet);
            BMessage.Builder booleanMessage=BMessage.newBuilder();
            booleanMessage.setB(true);
            responseObject.onNext(booleanMessage.build());
            responseObject.onCompleted();


        }catch (IOException e){
            e.printStackTrace();
        }

    }


    @Override
    public void barrier(RequestMetaMessage req,StreamObserver<BMessage> resp){
        waitBarrier();

        BMessage.Builder boolMessage=BMessage.newBuilder();
        boolMessage.setB(true);
//        logger.info(""+workerStep.longValue());
        resp.onNext(boolMessage.build());
        resp.onCompleted();

    }

    @Override
    public void getMaxAndMinValueOfEachFeature(MaxAndMinArrayMessage req,StreamObserver<MaxAndMinArrayMessage> resp){
        float[] reqMax=new float[req.getMaxCount()];
        float[] reqMin=new float[req.getMinCount()];

        for(int i=0;i<reqMax.length;i++){
            reqMax[i]=req.getMax(i);
            reqMin[i]=req.getMin(i);
        }


        synchronized (this){
            for(int i=0;i<Context.featureSize;i++){
                if(reqMax[i]>maxFeature[i]){
                    maxFeature[i]=reqMax[i];
                }
                if(reqMin[i]<minFeature[i]){
                    minFeature[i]=reqMin[i];
                }
            }
        }


        waitBarrier();

        MaxAndMinArrayMessage.Builder respMaxAndMin=MaxAndMinArrayMessage.newBuilder();
        for(int i=0;i<Context.featureSize;i++){
            respMaxAndMin.addMax(maxFeature[i]);
            respMaxAndMin.addMin(minFeature[i]);
        }

        resp.onNext(respMaxAndMin.build());
        resp.onCompleted();



    }


    public void waitBarrier() {
        try {
            if (!barrier_isExecuteFlag.getAndSet(true)) {
//                System.out.println("chuxian2");
                while(barrier_waitThread.get()<(Context.workerNum-1)){
                    Thread.sleep(10);
                }
//                System.out.println("chuxian3");
                synchronized (barrier_waitThread) {


                    barrier_waitThread.set(0);
                    barrier_waitThread.notifyAll();
                }

            }else {
                synchronized (barrier_waitThread){
//                    System.out.println("chuxian4");
                    barrier_waitThread.incrementAndGet();
                    barrier_waitThread.wait();
                }

            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }

        barrier_isExecuteFlag.set(false);
//        System.out.println("num2:"+num_waitOthers);
//        System.out.println(workerStepForBarrier.intValue());




    }



    @Override
    public void getNeededParams(SListMessage req,StreamObserver<SFKVListMessage> resp){
        // 获取需要访问的参数的key
        Set<String> neededParamIndices=MessageDataTransUtil.SListMessage_2_Set(req);
        try {
            SFKVListMessage sfkvListMessage=ServerContext.kvStoreForLevelDB.getNeededParams(neededParamIndices);
            resp.onNext(sfkvListMessage);
            resp.onCompleted();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void sendSFMap(SFKVListMessage req,StreamObserver<SMessage> resp){
        Map<String,Float> map=MessageDataTransUtil.SFKVListMessage_2_Map(req);
        SMessage.Builder smessage=SMessage.newBuilder();

        ServerContext.kvStoreForLevelDB.updateParams(map);
        smessage.setStr("success");
        waitBarrier();

        resp.onNext(smessage.build());
        resp.onCompleted();
    }

    @Override
    @Synchronized
    public void sentCurIndexNum(LMessage req,StreamObserver<SMessage> resp){
        ServerContext.kvStoreForLevelDB.setCurIndexOfSparseDim(new AtomicLong(req.getL()));
        SMessage.Builder sMessage=SMessage.newBuilder();
        sMessage.setStr("success");
        resp.onNext(sMessage.build());
        resp.onCompleted();
    }

    @Override
    public void sentInitedT(IFMessage req,StreamObserver<IMessage> resp){
        IMessage.Builder intMessage=IMessage.newBuilder();

        logger.info("req:"+req.getI());
        ServerContext.kvStoreForLevelDB.getTimeCostMap().put(req.getI(),req.getF());
//        logger.info("TimeCostMapSize:"+ServerContext.kvStoreForLevelDB.getTimeCostMap().size());
        try{
            if(ServerContext.kvStoreForLevelDB.getTimeCostMap().size()==Context.workerNum){
                synchronized (ServerContext.kvStoreForLevelDB.getTimeCostMap()){
                    ServerContext.kvStoreForLevelDB.getTimeCostMap().notifyAll();
                }

            }else {
                synchronized (ServerContext.kvStoreForLevelDB.getTimeCostMap()){
                    ServerContext.kvStoreForLevelDB.getTimeCostMap().wait();
                }

            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }



        System.out.println("size:"+ServerContext.kvStoreForLevelDB.getTimeCostMap().size());

        isFinished.set(false);
        waitThread.set(0);
        isExecuteFlag.set(false);
        waitBarrier();
//        logger.info("isFinish1:"+isFinished+",:waitThread:"+waitThread);
        if (!isExecuteFlag.getAndSet(true)) {
            ServerContext.kvStoreForLevelDB.getMinTimeCostI().set(getKeyOfMinValue());
            while(waitThread.get()<(Context.workerNum-1)){
                try{
                    System.out.println("ka1");
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
            isFinished.set(true);
//            logger.info("test1");

        }
//        logger.info("test2");
        logger.info("isFinish2:"+isFinished+",:waitThread:"+waitThread);
        waitFinishedWithWaitThread();

        logger.info("test3");

        logger.info("I:"+req.getI()+",F:"+req.getF()+",minI:"+ServerContext.kvStoreForLevelDB.getMinTimeCostI().get());



        intMessage.setI(ServerContext.kvStoreForLevelDB.getMinTimeCostI().get());

        waitBarrier();
        ServerContext.kvStoreForLevelDB.getMinTimeCostI().set(0);
        ServerContext.kvStoreForLevelDB.getTimeCostMap().clear();

        resp.onNext(intMessage.build());
        resp.onCompleted();

    }

    private void waitFinishedWithWaitThread() {
        try {
            if(isFinished.get()){

                synchronized (isFinished){
                    isFinished.notifyAll();

                }
            }else {
                synchronized (isFinished){
                    waitThread.incrementAndGet();
                    isFinished.wait();
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void waitFinished() {
        try {
            if(isFinished.get()){
                synchronized (isFinished){
                    isFinished.notifyAll();
                }
            }else {
                synchronized (isFinished){

                    isFinished.wait();
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        isExecuteFlag.set(false);
        isFinished.set(false);
        waitThread.set(0);
    }


    public int getKeyOfMinValue(){
        int keyOfMaxValue=-1;
        Map<Integer,Float> map=ServerContext.kvStoreForLevelDB.getTimeCostMap();
        float minValue=Float.MAX_VALUE;
        for(int i:map.keySet()){
//            System.out.println("i:"+i);
            if(keyOfMaxValue==-1){
                keyOfMaxValue=i;
                minValue=map.get(i);
            }
            else {
                if(map.get(i)<minValue){
                    keyOfMaxValue=i;
                    minValue=map.get(i);
                }
            }
        }
        return keyOfMaxValue;
    }

    @Override
    public void pushLocalViAccessNum(FMessage req,StreamObserver<BMessage> resp){

        numSet_otherWorkerAccessVi.add(req.getF());
        System.out.println("haha1");
        waitBarrier2(Context.workerNum - 1);
        System.out.println("haha2");
        // 开始计算numSet_otherWorkerAccessVi的总和
        synchronized (floatSum) {
//            logger.info("isExecuteFlag:"+isExecuteFlag_otherLocal.get());
            if (!isExecuteFlag_otherLocal.getAndSet(true)) {
                for (float f : numSet_otherWorkerAccessVi) {
//                    System.out.println("hahaadd");
                    floatSum += f;
                    isFinished_otherLocal.set(true);
                }

//                logger.info("iswait:"+isWait_otherLocal.get());
                System.out.println("haha3");
                while(!isWait_otherLocal.get()){
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("haha4");
                synchronized (isFinished_otherLocal) {
                    isWait_otherLocal.set(false);
                    isFinished_otherLocal.notify();
                }


            }
        }

        System.out.println("haha5");
        // 同步
        try {
            workerStepForBarrier_otherLocal.incrementAndGet();
            if (workerStepForBarrier_otherLocal.get() == Context.workerNum-1) {
                synchronized (workerStepForBarrier_otherLocal) {
                    workerStepForBarrier_otherLocal.notifyAll();
                }

            }else {
                synchronized (workerStepForBarrier_otherLocal){
                    workerStepForBarrier_otherLocal.wait();
                }

            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }

//        System.out.println("num2:"+num_waitOthers);
//        System.out.println(workerStepForBarrier.intValue());
        workerStepForBarrier_otherLocal.set(0);
        System.out.println("haha6");

//        System.out.println("haha2");
        BMessage.Builder executeStatus=BMessage.newBuilder();
        executeStatus.setB(true);
        isExecuteFlag_otherLocal.set(false);
        resp.onNext(executeStatus.build());
        resp.onCompleted();



    }

    @Override
    public void pullOtherWorkerAccessForVi(RequestMetaMessage req,StreamObserver<FMessage> resp){
//        System.out.println("haha3");

        synchronized (isFinished_otherLocal){
            try{
                isWait_otherLocal.set(true);
                // 这里是只有是多台机器的时候才wait，单机跑不wait
                if(Context.workerNum>1){
                    isFinished_otherLocal.wait();
                }


            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }


        isFinished_otherLocal.set(false);
//        System.out.println("haha4");
        logger.info("pullOtherWorkerAccessForVi from:"+req.getHost());
        FMessage.Builder floatMessage=FMessage.newBuilder();
        floatMessage.setF(floatSum);
        floatSum=0f;
        resp.onNext(floatMessage.build());
        resp.onCompleted();

    }

    @Override
    public void pushVANumAndGetCatPrunedRecord(LIListMessage req,StreamObserver<LListMessage> resp){
        Map<Long,Integer> map=MessageDataTransUtil.LIListMessage_2_Map(req);



        // 下面开始计算，且只计算一次各个map的和
        synchronized (vAccessNumMap){
            for(long l:map.keySet()){
                if(vAccessNumMap.containsKey(l)){
                    int num=vAccessNumMap.get(l)+map.get(l);

                    vAccessNumMap.put(l,num);
                }else {
                    vAccessNumMap.put(l,map.get(l));
                }
            }
        }

        waitBarrier();

        // 取频率高于freqThreshold,统计到
        if(!isExecuteFlag.getAndSet(true)){
            for(Long l:vAccessNumMap.keySet()){
                if(vAccessNumMap.get(l)>Context.freqThreshold){
                    prunedVSet.add(l);
                }
            }
//            System.out.println("isFinish02:"+isFinished.get());
            isFinished.set(true);
//            System.out.println("isFinish03:"+isFinished.get());
        }

//        System.out.println("isFinish04:"+isFinished.get());

        waitFinished();

        isFinished.set(false);
        isExecuteFlag.set(false);



//        System.out.println("isFinish05:");
        LListMessage.Builder respMessage=LListMessage.newBuilder();
        for(long l:prunedVSet){
            respMessage.addL(l);
        }
//        System.out.println("isFinish06:");
//        logger.info("prunedVSet"+prunedVSet.size());
        resp.onNext(respMessage.build());
        resp.onCompleted();


    }

    public void waitBarrier2(int num_waitOthers) {
        try {
            workerStepForBarrier.incrementAndGet();
            if (workerStepForBarrier.get() == num_waitOthers) {
                synchronized (workerStepForBarrier) {
                    workerStepForBarrier.notifyAll();
                }

            }else {
                synchronized (workerStepForBarrier){
                    workerStepForBarrier.wait();
                }

            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }

//        System.out.println("num2:"+num_waitOthers);
//        System.out.println(workerStepForBarrier.intValue());
        workerStepForBarrier.set(0);



    }

    @Override
    public void pullPartitionedVset(RequestMetaMessage req,StreamObserver<ListSetMessage> resp){

    }

}