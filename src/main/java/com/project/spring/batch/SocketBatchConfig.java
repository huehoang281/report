package com.project.spring.batch;
import com.project.spring.entity.ItemContract;
import com.project.spring.repository.ItemContractRepository;
import com.project.spring.service.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.web3j.protocol.core.methods.response.Log;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SocketBatchConfig {

    private final ContractService contractService;
    private final ItemContractRepository itemContractRepository;

    @Bean
    @StepScope
    public ItemReader<Log> readerLog()
            throws IOException, InterruptedException {
        List<Log> logs = contractService.Socket();
        logs.forEach(log -> System.out.println("Event: " + log));
        return new ListItemReader<>(logs);
    }


    @Bean
    public ItemProcessor<Log, ItemContract> processorLog() {
        return log -> {
            String eventSignature = log.getTopics().get(0);
            if (eventSignature.equals(contractService.PRODUCTBOUGHTSOLD_EVENT)) {
                contractService.handleBuy(log, log.getBlockNumber());
            } else if (eventSignature.equals(contractService.PRODUCTADDED_EVENT)) {
                contractService.handleAdd(log, log.getBlockNumber());
            } else if (eventSignature.equals(contractService.PRODUCTDELETED_EVENT)) {
                contractService.handleDelete(log, log.getBlockNumber());
            } else if (eventSignature.equals(contractService.PRODUCTUPDATED_EVENT)) {
                contractService.handleUpdate(log, log.getBlockNumber());
            } else {
                // Unknown event
                System.out.println("Unknown Event - Signature: " + eventSignature);
            }
            return null; // or handle as needed
        };
    }

    @Bean
    public ItemWriter<ItemContract> writerLog() {
        RepositoryItemWriter<ItemContract> writer = new RepositoryItemWriter<>();
        writer.setRepository(itemContractRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job saveItemContract(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException, InterruptedException {
        return new JobBuilder("saveItemContract", jobRepository)
                .start(step3(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException, InterruptedException {
        return new StepBuilder("step3", jobRepository)
                .<Log, ItemContract>chunk(10,transactionManager )// Chỉnh kiểu dữ liệu ở đây
                .reader(readerLog())
                .processor(processorLog())
                .writer(writerLog())
                .build();
    }

}