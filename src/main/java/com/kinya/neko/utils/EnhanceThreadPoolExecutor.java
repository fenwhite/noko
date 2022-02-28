package com.kinya.neko.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ：white
 * @description：执行器增强
 * @date ：2022/2/23
 */

public class EnhanceThreadPoolExecutor extends ThreadPoolExecutor {
    protected static final Logger logger = LoggerFactory.getLogger(EnhanceThreadPoolExecutor.class);
    // 是否使用自定义context
    private boolean isCustom = false;
    private Map<String,String> custContext;

    public EnhanceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public EnhanceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public EnhanceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public EnhanceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public void setCustContext(Map<String, String> custContext) {
        this.custContext = custContext;
    }

    public Map<String, String> getCustContext() {
        return custContext;
    }

    /**
     * @author: white
     * @description:
     * 清空MDC信息以及打印执行中异常信息
     * @date: 2022/2/23
     **/
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        // 对线程中出现的遗产进行记录
        if (t == null && r instanceof Future<?>) {
            try {
                Object res = ((Future<?>)r).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                t = e;
            }
        }
        if(t!=null){
            logger.error(t.toString());
        }
        // 清除本次traceId，防止traceId遗留，对下一次操作造成错误日志记录
        MDC.remove(Constant.TRACE_ID);
    }

    private Map<String,String> getContextMap(){
        return isCustom? custContext:MDC.getCopyOfContextMap();
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, getContextMap()));
    }

    /**
     * @author: white
     * @description:
     * 实现主线程context信息到子线程的自动复制与清空
     * @date: 2022/2/23
     **/
    protected static Runnable wrap(Runnable r, final Map<String,String> contextMap){
        return new Runnable() {
            @Override
            public void run() {
                MDC.setContextMap(contextMap);
                try{
                    r.run();
                }catch (Exception e){
                    System.out.println(e.toString());
                }finally {
                    MDC.clear();
                }
            }
        };
    }
}
