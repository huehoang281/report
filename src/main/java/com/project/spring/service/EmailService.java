package com.project.spring.service;
import com.project.spring.batch.VerifyBatchConfig;
import com.project.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;


@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    @Lazy
    @Qualifier("sendEmail")
    private Job job;

    @Autowired
    private JobLauncher jobLauncher; // Add JobLauncher

    private Logger logger = LoggerFactory.getLogger(VerifyBatchConfig.class); // Add logger

    @Autowired
    private UserRepository userRepository;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String username, String to, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account Verification " + username);
        message.setText("Please click the following link to verify your account: " + verificationToken);
        javaMailSender.send(message);
    }

    public void sendDeleteAccountNotification(String username, String to, String notificationMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verification Expiration " + username);
        message.setText(notificationMessage);
        javaMailSender.send(message);
    }


    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters();

                JobExecution jobExecution = jobLauncher.run(job, jobParameters);

                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    logger.info("Batch Job Completed Successfully");
                } else {
                    logger.error("Batch Job Failed with Status: " + jobExecution.getStatus());
                }
            } catch (Exception e) {
                logger.error("Error running batch job", e);
            }
            logger.info("Delayed task executed!");
        }
    }

    public void schedule() {
        Timer timer = new Timer();
        long delayInMillis = 300000;
        timer.schedule(new MyTimerTask(), delayInMillis);
    }
}