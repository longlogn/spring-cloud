package com.cxlsky.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cxlsky.bean.AliyunOssUploadClient;
import com.cxlsky.properties.AliyunOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author CXL
 */
@EnableConfigurationProperties(AliyunOssProperties.class)
@Slf4j
@EnableAsync
public class AliyunOssAutoConfiguration {


    @Bean
    public OSS ossClient(AliyunOssProperties aliyunOssProperties) {
        Assert.notNull(aliyunOssProperties.getId(), "[access key id] not set");
        Assert.notNull(aliyunOssProperties.getSecret(), "[access key secret] not set");
        Assert.notNull(aliyunOssProperties.getEndpoint(), "[access key endpoint] not set");
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getId(), aliyunOssProperties.getSecret());
    }

    /**
     * @return
     */
    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(30);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("oss-task-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.afterPropertiesSet();
        return executor;
    }


    @Bean
    public AliyunOssUploadClient aliyunOssUploadClient(AliyunOssProperties aliyunOssProperties, OSS ossClient) {
        Assert.notNull(aliyunOssProperties.getBucket(), "默认上传的bucketName未设置");
        return new AliyunOssUploadClient(ossClient, aliyunOssProperties, taskExecutor());
    }
}
