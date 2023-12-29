package com.project.spring.controller;

import com.project.spring.service.ContractService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.math.BigInteger;

@RestController
@RequestMapping("/api/products")
public class ContractController {

    @Autowired
    private ContractService contractService;

    private final JobLauncher jobLauncher;

    @Autowired
    @Lazy
    @Qualifier("saveItem")
    private final Job saveItem;

    @Autowired
    @Lazy
    @Qualifier("saveItemContract")
    private final Job saveItemContract;

    public ContractController(JobLauncher jobLauncher, Job saveItem, Job saveItemContract) {
        this.jobLauncher = jobLauncher;
        this.saveItem = saveItem;
        this.saveItemContract = saveItemContract;
    }

    @PostMapping
    public void addProduct(@RequestParam String name, @RequestParam BigInteger price, @RequestParam BigInteger quantity) throws Exception {
        contractService.addProduct(name, price, quantity);
    }

    @GetMapping("/{index}")
    public String displayProduct(@PathVariable BigInteger index) throws Exception {
        return contractService.displayProduct(index);
    }

    @PutMapping("/{index}")
    public void updateProduct(@PathVariable BigInteger index, @RequestParam String newName, @RequestParam BigInteger newPrice, @RequestParam BigInteger newQuantity) throws Exception {
        contractService.updateProduct(index, newName, newPrice, newQuantity);
    }

    @DeleteMapping("/{index}")
    public void deleteProduct(@PathVariable BigInteger index) throws Exception {
        contractService.deleteProduct(index);
    }

    @PostMapping("/{index}/buy")
    public void buyProduct(@PathVariable BigInteger index, @RequestParam BigInteger quantity) throws Exception {
        contractService.buyProduct(index, quantity);
    }

    @GetMapping("/scanblock/{startBlock}/{endBlock}")
    public ResponseEntity<String> runBatchScan(@PathVariable BigInteger startBlock, @PathVariable BigInteger endBlock) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startBlock", startBlock.longValue())
                    .addLong("endBlock", endBlock.longValue())
                    .toJobParameters();

            jobLauncher.run(saveItem, jobParameters);

            return ResponseEntity.ok("Batch job started successfully.");
        } catch (JobExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting batch job.");
        }
    }

    @GetMapping("/scanblock/socket")
    public ResponseEntity<String> runBatchScan() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .toJobParameters();

            jobLauncher.run(saveItemContract, jobParameters);

            return ResponseEntity.ok("Batch job started successfully.");
        } catch (JobExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting batch job.");
        }
    }
}
