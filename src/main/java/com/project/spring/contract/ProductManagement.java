package com.project.spring.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.3.
 */
@SuppressWarnings("rawtypes")
public class ProductManagement extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b50610aad8061001d5f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c80630a4ad56f14610064578063125b0b34146100795780632d5617871461008c578063572d73211461009f5780637acc0b20146100ca578063ed90c7b7146100dd575b5f80fd5b610077610072366004610622565b6100f0565b005b6100776100873660046106df565b6101de565b61007761009a366004610729565b610292565b6100b26100ad36600461077c565b610333565b6040516100c193929190610793565b60405180910390f35b6100b26100d836600461077c565b610420565b6100776100eb36600461077c565b6104da565b5f5482106101195760405162461bcd60e51b8152600401610110906107ec565b60405180910390fd5b5f80838154811061012c5761012c610813565b905f5260205f2090600302019050818160020154101561018e5760405162461bcd60e51b815260206004820152601a60248201527f4e6f7420656e6f756768207175616e7469747920746f206275790000000000006044820152606401610110565b81816002015f8282546101a19190610827565b90915550506040518281527fb7fa58beacc2469a34a89d52ee9e600c9458a728ed012f218d7fddd5705afd4b9060200160405180910390a1505050565b60408051606081018252848152602081018490529081018290525f8054600181018255908052815182916003027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5630190819061023a90826108d2565b50602082015181600101556040820151816002015550507faf15189af1861da85d7d959da20a528553cf2d64fc75f6fc7f6894097e9e9cf184848460405161028493929190610793565b60405180910390a150505050565b5f5484106102b25760405162461bcd60e51b8152600401610110906107ec565b5f8085815481106102c5576102c5610813565b5f91825260209091206003909102019050806102e185826108d2565b5060018101839055600281018290556040517fe84cd72a7a2a5f0c69a042b54a03a751bedfe4e78b47534ba127f79a5b5bc8dc9061032490869086908690610793565b60405180910390a15050505050565b60605f805f80549050841061035a5760405162461bcd60e51b8152600401610110906107ec565b5f80858154811061036d5761036d610813565b905f5260205f2090600302019050805f01816001015482600201548280546103949061084c565b80601f01602080910402602001604051908101604052809291908181526020018280546103c09061084c565b801561040b5780601f106103e25761010080835404028352916020019161040b565b820191905f5260205f20905b8154815290600101906020018083116103ee57829003601f168201915b50505050509250935093509350509193909250565b5f818154811061042e575f80fd5b905f5260205f2090600302015f91509050805f01805461044d9061084c565b80601f01602080910402602001604051908101604052809291908181526020018280546104799061084c565b80156104c45780601f1061049b576101008083540402835291602001916104c4565b820191905f5260205f20905b8154815290600101906020018083116104a757829003601f168201915b5050505050908060010154908060020154905083565b5f5481106104fa5760405162461bcd60e51b8152600401610110906107ec565b5f805461050990600190610827565b8154811061051957610519610813565b905f5260205f2090600302015f828154811061053757610537610813565b5f918252602090912060039091020180610551838261098e565b50600182810154908201556002918201549101555f80548061057557610575610a63565b5f828152602081205f199092019160038302019061059382826105d0565b505f60018201819055600290910181905591556040517f65d38ca776d3dfc55f453f15810d3ea9c410fe2a1435e51f44af1824adbc51319190a150565b5080546105dc9061084c565b5f825580601f106105eb575050565b601f0160209004905f5260205f2090810190610607919061060a565b50565b5b8082111561061e575f815560010161060b565b5090565b5f8060408385031215610633575f80fd5b50508035926020909101359150565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610665575f80fd5b813567ffffffffffffffff8082111561068057610680610642565b604051601f8301601f19908116603f011681019082821181831017156106a8576106a8610642565b816040528381528660208588010111156106c0575f80fd5b836020870160208301375f602085830101528094505050505092915050565b5f805f606084860312156106f1575f80fd5b833567ffffffffffffffff811115610707575f80fd5b61071386828701610656565b9660208601359650604090950135949350505050565b5f805f806080858703121561073c575f80fd5b84359350602085013567ffffffffffffffff811115610759575f80fd5b61076587828801610656565b949794965050505060408301359260600135919050565b5f6020828403121561078c575f80fd5b5035919050565b606081525f84518060608401525f5b818110156107bf57602081880181015160808684010152016107a2565b505f608082850101526080601f19601f830116840101915050836020830152826040830152949350505050565b6020808252600d908201526c092dcecc2d8d2c840d2dcc8caf609b1b604082015260600190565b634e487b7160e01b5f52603260045260245ffd5b8181038181111561084657634e487b7160e01b5f52601160045260245ffd5b92915050565b600181811c9082168061086057607f821691505b60208210810361087e57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f8211156108cd575f81815260208120601f850160051c810160208610156108aa5750805b601f850160051c820191505b818110156108c9578281556001016108b6565b5050505b505050565b815167ffffffffffffffff8111156108ec576108ec610642565b610900816108fa845461084c565b84610884565b602080601f831160018114610933575f841561091c5750858301515b5f19600386901b1c1916600185901b1785556108c9565b5f85815260208120601f198616915b8281101561096157888601518255948401946001909101908401610942565b508582101561097e57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b818103610999575050565b6109a3825461084c565b67ffffffffffffffff8111156109bb576109bb610642565b6109c9816108fa845461084c565b5f601f8211600181146109fa575f83156109e35750848201545b5f19600385901b1c1916600184901b178455610a5c565b5f85815260209020601f198416905f86815260209020845b83811015610a325782860154825560019586019590910190602001610a12565b5085831015610a4f57818501545f19600388901b60f8161c191681555b50505060018360011b0184555b5050505050565b634e487b7160e01b5f52603160045260245ffdfea264697066735822122061336749b6929049b40bdb8b7237398eb8abd0dea1fd79f43b7eae8a8067cd1764736f6c63430008140033";

    public static final String FUNC_ADDPRODUCT = "addProduct";

    public static final String FUNC_BUYSELLPRODUCT = "buySellProduct";

    public static final String FUNC_DELETEPRODUCT = "deleteProduct";

    public static final String FUNC_DISPLAYPRODUCT = "displayProduct";

    public static final String FUNC_PRODUCTS = "products";

    public static final String FUNC_UPDATEPRODUCT = "updateProduct";

    public static final Event PRODUCTADDED_EVENT = new Event("ProductAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PRODUCTBOUGHTSOLD_EVENT = new Event("ProductBoughtSold",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event PRODUCTDELETED_EVENT = new Event("ProductDeleted",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event PRODUCTUPDATED_EVENT = new Event("ProductUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected ProductManagement(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProductManagement(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ProductManagement(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ProductManagement(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ProductAddedEventResponse> getProductAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRODUCTADDED_EVENT, transactionReceipt);
        ArrayList<ProductAddedEventResponse> responses = new ArrayList<ProductAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProductAddedEventResponse typedResponse = new ProductAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.quantity = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProductAddedEventResponse getProductAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRODUCTADDED_EVENT, log);
        ProductAddedEventResponse typedResponse = new ProductAddedEventResponse();
        typedResponse.log = log;
        typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.quantity = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ProductAddedEventResponse> productAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProductAddedEventFromLog(log));
    }

    public Flowable<ProductAddedEventResponse> productAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRODUCTADDED_EVENT));
        return productAddedEventFlowable(filter);
    }

    public static List<ProductBoughtSoldEventResponse> getProductBoughtSoldEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRODUCTBOUGHTSOLD_EVENT, transactionReceipt);
        ArrayList<ProductBoughtSoldEventResponse> responses = new ArrayList<ProductBoughtSoldEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProductBoughtSoldEventResponse typedResponse = new ProductBoughtSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.quantity = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProductBoughtSoldEventResponse getProductBoughtSoldEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRODUCTBOUGHTSOLD_EVENT, log);
        ProductBoughtSoldEventResponse typedResponse = new ProductBoughtSoldEventResponse();
        typedResponse.log = log;
        typedResponse.quantity = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ProductBoughtSoldEventResponse> productBoughtSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProductBoughtSoldEventFromLog(log));
    }

    public Flowable<ProductBoughtSoldEventResponse> productBoughtSoldEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRODUCTBOUGHTSOLD_EVENT));
        return productBoughtSoldEventFlowable(filter);
    }

    public static List<ProductDeletedEventResponse> getProductDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRODUCTDELETED_EVENT, transactionReceipt);
        ArrayList<ProductDeletedEventResponse> responses = new ArrayList<ProductDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProductDeletedEventResponse typedResponse = new ProductDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProductDeletedEventResponse getProductDeletedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRODUCTDELETED_EVENT, log);
        ProductDeletedEventResponse typedResponse = new ProductDeletedEventResponse();
        typedResponse.log = log;
        return typedResponse;
    }

    public Flowable<ProductDeletedEventResponse> productDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProductDeletedEventFromLog(log));
    }

    public Flowable<ProductDeletedEventResponse> productDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRODUCTDELETED_EVENT));
        return productDeletedEventFlowable(filter);
    }

    public static List<ProductUpdatedEventResponse> getProductUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRODUCTUPDATED_EVENT, transactionReceipt);
        ArrayList<ProductUpdatedEventResponse> responses = new ArrayList<ProductUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProductUpdatedEventResponse typedResponse = new ProductUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newPrice = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newQuantity = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProductUpdatedEventResponse getProductUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRODUCTUPDATED_EVENT, log);
        ProductUpdatedEventResponse typedResponse = new ProductUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.newName = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newPrice = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.newQuantity = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ProductUpdatedEventResponse> productUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProductUpdatedEventFromLog(log));
    }

    public Flowable<ProductUpdatedEventResponse> productUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRODUCTUPDATED_EVENT));
        return productUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addProduct(String _name, BigInteger _price, BigInteger _quantity) {
        final Function function = new Function(
                FUNC_ADDPRODUCT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name),
                new org.web3j.abi.datatypes.generated.Uint256(_price),
                new org.web3j.abi.datatypes.generated.Uint256(_quantity)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> buySellProduct(BigInteger _index, BigInteger _quantity) {
        final Function function = new Function(
                FUNC_BUYSELLPRODUCT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_index),
                new org.web3j.abi.datatypes.generated.Uint256(_quantity)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteProduct(BigInteger _index) {
        final Function function = new Function(
                FUNC_DELETEPRODUCT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_index)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> displayProduct(BigInteger _index) {
        final Function function = new Function(FUNC_DISPLAYPRODUCT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> products(BigInteger param0) {
        final Function function = new Function(FUNC_PRODUCTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> updateProduct(BigInteger _index, String _newName, BigInteger _newPrice, BigInteger _newQuantity) {
        final Function function = new Function(
                FUNC_UPDATEPRODUCT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_index),
                new org.web3j.abi.datatypes.Utf8String(_newName),
                new org.web3j.abi.datatypes.generated.Uint256(_newPrice),
                new org.web3j.abi.datatypes.generated.Uint256(_newQuantity)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ProductManagement load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProductManagement(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ProductManagement load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProductManagement(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ProductManagement load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProductManagement(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ProductManagement load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProductManagement(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ProductManagement> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProductManagement.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProductManagement> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProductManagement.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ProductManagement> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProductManagement.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProductManagement> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProductManagement.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ProductAddedEventResponse extends BaseEventResponse {
        public String name;

        public BigInteger price;

        public BigInteger quantity;
    }

    public static class ProductBoughtSoldEventResponse extends BaseEventResponse {
        public BigInteger quantity;
    }

    public static class ProductDeletedEventResponse extends BaseEventResponse {
    }

    public static class ProductUpdatedEventResponse extends BaseEventResponse {
        public String newName;

        public BigInteger newPrice;

        public BigInteger newQuantity;
    }
}
