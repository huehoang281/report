package com.project.spring.batch;

import com.project.spring.entity.UserInfo;
import com.project.spring.repository.UserRepository;
import com.project.spring.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;


@Configuration
@AllArgsConstructor
public class VerifyBatchConfig {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Bean
    @StepScope
    public ItemReader<UserInfo> readerUser() {
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
        List<UserInfo> unverifiedUsers = userRepository.findByIsVerifiedFalseAndExpirationTimeBefore(thirtySecondsAgo);
        unverifiedUsers.forEach(user ->
                System.out.println("User " + user.getUserId() + ": " + user.getEmail())
        );
        return new ListItemReader<>(unverifiedUsers);
    }

    @Bean
    public ItemProcessor<UserInfo, UserInfo> processorUser() {
        return user -> {
            System.out.println("Processing Inactive User: " + user.getUsername());
            boolean verified = user.isVerified();
            if (verified == false) {
                // Gửi thông báo email
                emailService.sendDeleteAccountNotification(user.getUsername(), user.getEmail(), "The authentication time has expired and your account will be deleted!");
                userRepository.delete(user);
            }
            return null;
        };
    }

    @Bean
    public ItemWriter<UserInfo> writerUser() {
        RepositoryItemWriter<UserInfo> writer = new RepositoryItemWriter<>();
        writer.setRepository(userRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    @Primary
    public Job sendEmail(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("sendEmail", jobRepository)
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<UserInfo, UserInfo>chunk(10, transactionManager)
                .reader(readerUser())
                .processor(processorUser())
                .writer(writerUser())
                .build();
    }
}