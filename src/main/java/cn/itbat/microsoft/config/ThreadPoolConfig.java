package cn.itbat.microsoft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 * <p>
 * 使用方法  @Async("asyncPoolTaskExecutor")
 *
 * @author mjj  (;￢＿￢) 
 * @version 1.0
 * @date 2019/6/11 19:27
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 配置线程池
     *
     * @return 线程池
     */
    @Bean(name = "asyncPoolTaskExecutor")
    public ThreadPoolTaskExecutor getAsyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setMaxPoolSize(200);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setThreadNamePrefix("microsoft-async-");
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应⽤最后能够被关闭，⽽不是阻塞住。
        taskExecutor.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
