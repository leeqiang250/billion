package com.billion.service.aptos;

import com.billion.model.factory.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

/**
 * @author liqiang
 * 若不为周期任务配置线程池，只使用@EnableScheduling和@Scheduled注解的话，则所有周期任务共用一个子线程，若出现下一个周期开始上一个周期任务还没结束的情况，则线程阻塞，直到前一个任务完成。
 */
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledTaskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService scheduledTaskScheduler() {
        return new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("Scheduled", true));
    }

}