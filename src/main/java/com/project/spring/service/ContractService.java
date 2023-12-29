package com.project.spring.service;
import com.project.spring.contract.ProductManagement;
import com.project.spring.entity.ItemContract;
import com.project.spring.repository.ItemContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import java.math.BigInteger;

@Service
public class ContractService {

    @Autowired
    private ProductManagement contract;


    @Value("${com.project.spring.address_contract}")
    private String address_contract;

    @Value("${com.project.spring.enpoint}")
    private String enpoint;

    public final String PRODUCTADDED_EVENT = EventEncoder.encode(contract.PRODUCTADDED_EVENT);
    public final String PRODUCTBOUGHTSOLD_EVENT = EventEncoder.encode(contract.PRODUCTBOUGHTSOLD_EVENT);
    public final String PRODUCTDELETED_EVENT = EventEncoder.encode(contract.PRODUCTDELETED_EVENT);
    public final String PRODUCTUPDATED_EVENT = EventEncoder.encode(contract.PRODUCTUPDATED_EVENT);

    public List<Log> Socket() throws ConnectException, InterruptedException {
        WebSocketService webSocketService = new WebSocketService(enpoint, true);
        webSocketService.connect();
        Web3j web3j = Web3j.build(webSocketService);

        List<Log> logs = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(100);

        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Arrays.asList(address_contract));

        web3j.ethLogFlowable(filter).subscribe(txHash -> {
            System.out.println("New block mined: " + txHash.getBlockNumber());
            web3j.ethGetTransactionReceipt(txHash.getTransactionHash()).sendAsync().thenAccept(receipt -> {
                if (receipt.getTransactionReceipt().isPresent()) {
                    for (Log log : receipt.getTransactionReceipt().get().getLogs()) {
                        String eventSignature = log.getTopics().get(0);
                        logs.add(log);
                        if (eventSignature.equals(PRODUCTADDED_EVENT)) {
                            handleAdd(log, log.getBlockNumber());
                        } else if (eventSignature.equals(PRODUCTBOUGHTSOLD_EVENT)) {
                            handleBuy(log, log.getBlockNumber());
                        } else if (eventSignature.equals(PRODUCTDELETED_EVENT)) {
                            handleDelete(log, log.getBlockNumber());
                        } else if (eventSignature.equals(PRODUCTUPDATED_EVENT)) {
                            handleUpdate(log, log.getBlockNumber());
                        }else {
                            System.out.println("Unknown Event - Signature: " + eventSignature);
                        }
                    }
                }
                latch.countDown(); // Signal that logs are collected
            });
        });
        webSocketService.close();
        latch.await(30, TimeUnit.SECONDS);
        return logs;
    }

    public void handleUpdate(Log log, BigInteger blockNumber) {
        String name = contract.getProductAddedEventFromLog(log).name;
        BigInteger price = contract.getProductAddedEventFromLog(log).price;
        BigInteger quantity = contract.getProductAddedEventFromLog(log).quantity;
        saveItemContract(blockNumber, "UPDATE" , name, price, quantity );
    }

    public void handleDelete(Log log, BigInteger blockNumber) {
        String name = contract.getProductDeletedEventFromLog(log).name;
        saveItemContract(blockNumber, "DELETE", name, null, null);
    }

    public void handleBuy(Log log, BigInteger blockNumber) {
        BigInteger quantity = contract.getProductBoughtSoldEventFromLog(log).quantity;
        saveItemContract(blockNumber, "BUY", null, null, quantity);
    }

    public void handleAdd(Log log, BigInteger blockNumber) {
        String name = contract.getProductAddedEventFromLog(log).name;
        BigInteger price = contract.getProductAddedEventFromLog(log).price;
        BigInteger quantity = contract.getProductAddedEventFromLog(log).quantity;
        saveItemContract(blockNumber, "ADD", name, price, quantity);
    }


    private void saveItemContract(BigInteger block, String type, String name, BigInteger price, BigInteger quantity) {
        ItemContract itemContract = new ItemContract();
        itemContract.setId(block);
        itemContract.setType(type);
        itemContract.setName(name);
        itemContract.setPrice(price);
        itemContract.setQuantity(quantity);
        try {
            ItemContractRepository.save(itemContract);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Log> scanBlockRange(BigInteger startBlock, BigInteger endBlock) throws IOException, InterruptedException {
        WebSocketService webSocketService = new WebSocketService(enpoint, true);
        webSocketService.connect();
        Web3j web3j = Web3j.build(webSocketService);

        EthFilter filter = new EthFilter(
                new DefaultBlockParameterNumber(startBlock),
                new DefaultBlockParameterNumber(endBlock),
                Arrays.asList(address_contract));

        List<Log> logs = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(100);

        web3j.ethLogFlowable(filter).subscribe(txHash -> {
            System.out.println("New block mined: " + txHash.getTopics().get(0));
            web3j.ethGetTransactionReceipt(txHash.getTransactionHash()).sendAsync().thenAccept(receipt -> {
                if (receipt.getTransactionReceipt().isPresent()) {
                    for (Log log : receipt.getTransactionReceipt().get().getLogs()) {
                        System.out.println("Event - Signature: " + log);
                        logs.add(log);
                    }
                }
                latch.countDown(); // Signal that logs are collected
            });
        });

        // Wait for the logs to be collected or timeout after 5 seconds
        webSocketService.close();
        latch.await(30, TimeUnit.SECONDS);

        return logs;
    }

    public void addProduct(String name, BigInteger price, BigInteger quantity) throws Exception {
        contract.addProduct(name, price, quantity).send();
    }

    public String displayProduct(BigInteger index) throws Exception {
        var result = contract.displayProduct(index).send();
        return "Name: " + result.component1() + ", Price: " + result.component2() + ", Quantity: " + result.component3();
    }

    public void updateProduct(BigInteger index, String newName, BigInteger newPrice, BigInteger newQuantity) throws Exception {
        contract.updateProduct(index, newName, newPrice, newQuantity).send();
    }

    public void deleteProduct(BigInteger index) throws Exception {
        contract.deleteProduct(index).send();
    }

    public void buyProduct(BigInteger index, BigInteger quantity) throws Exception {
        contract.buySellProduct(index, quantity).send();
    }
}
