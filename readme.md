
# ***Disclaimer***
***JXCH-RPC-Library is an unofficial third party library and has no relation to, or endorsement by Chia Network, Inc***

# **About**
***Currently implements Chia Blockchain RPC API up to 1.7.1, 1.8.2 coming soon. Datalayer support not currently implemented***

<br>

This library aims to provide a modular and robust RPC library for interacting with the Chia blockchain and directly interfacing with node/wallets it via Java.

The Goals of the library are as follows:

- Provide a modern simple to use library for interfacing with the Chia blockchain via RPC requests to nodes and wallets.
- Provide a modular/granular library implementation that allows for various levels of use.
- Provide a means to load self signed certificates via their .crt & .key files into a keystore.
- Provide a RPCClient wrapper around Apache Closeable HTTP Client for making direct requests to predefined endpoints using defined configs.
- Provide API classes that provide a higher level implementation of methods for making RPC request.
- Provide the option to manually handle response json.
- Provide immutable java records that correspond to the schemas of the response json.
- Ensure immutability of all response data.
- Provide the ability to easily communicate to various nodes, with the ability to pool the responses and differentiate between requests.

<br>

Using the above outlined features the library can be used simply to load self signed certificate/key pairs (which can be a challenge with java), be used simply as a RPCClient to make a process requests/response json manually or be used as a full abstraction for making RPC calls with requests returned as predefined immutable java records that map 1:1 with response data. Due to the nature of blockchains being immutable, and immutability being generally preferable whenever possible, all data relating to responses is contained in immutable records, and all collections that returned are also wrapped immutably. As such this library can provide a solid backbone for interfacing via RPC in a simply, safely and allows for thread safe high performance processing of RPC requests across multiple nodes/wallets.

<br>


## **Current Project State**
<br>

Currently, the library provides implementations for all endpoints ***except for the Data Layer*** for the chia client as of 1.7. The library is still in a beta state, all implemented endpoints except for the ones introduced in version 1.7 of the client (Did not have a node on 1.7 when creating, but the new endpoints are implemented, just not tested) have been tested and should function as expected, but due to the nature of software development and the complexity of some of the endpoints and the associated return data, some latent bugs are to be expected. THe library will be updated and maintained as I use it in future projects, and I will try to keep it up to date as possible, but I cannot guarantee up to date full coverage of all endpoints. ***If any bugs are found please create an issue and I will investigate and fix them when time permits.*** I wll try my best to keep the every thing updated and fix any issues proactively, but I am only one person. I tried to architect the library to be fairly simple, and easy to maintainable/contribute to. ***Any contributions are welcome, even if it is just something little.***

<br>

## **Future Plans**

<br>

- Add DataLayer Endpoints
- Add JavaDocs mirroring RPC documentation for all the Api member methods
- Add Unit Tests
- Implement BLS functionalities,  (https://github.com/chiachat/kbls is a good existing one wtih aggregation if needed)

<br>

When writing the library I didn't have any experience with using the data layer and didn't implement any of those endpoints, this is something I plan to do in the future, or if anyone has experience with the datalayer and runs one would like to contribute to these that would be great. I also plan on adding JavaDocs to better document where builder methods are used and to mirror the details from the official documentation for the api related methods so there is no need to reference it and it can be read in the IDE when calling the associated methods.

Unit tests are not the easiest to implement due to the complexity of some of the api parameters and their response, but it still would be a great benefits for regression testing, proper validation, and will be indispensable for endpoint changes. There was a lot of ground to cover when writing all of the endpoint methods, so I didn't implement any unit tests while writing. I did test all the endpoints while implementing and manually validated them, but this not a valid or maintainable metric to go by.

Things like fully crafting spendbundles would benefit from BLS functionality, mainly aggregation, and it would also be nice to provide the ability to do puzzlehash <-> address conversion. KBLS(https://github.com/chiachat/kbls ) is a nice java library for this if you need BLS for your project. I wanted to keep the first release simple so I didn't approach  BLS. But the goal is to have the library provide all functionality needed to interface with the blockchain, and BLS signature aggregation is needed for this.

<br>

## Donations

If wishing to support/donate donations can be made to :
>xch1z9mpf55pfl75hv8q5nh02zkcvp57gxzzxmh50xlq9p8s6h4tcjrqucsffj

<br>
<br>

# Dependencies
The library was developed using Java 17, but *should* be compatible with Java 14, though not for certain. Up to date dependencies can be found in the pom file. Depending the use case some can be avoid if not using all of the libraries features.

```xml
         <!-- Needed for CertPairStore and loading self-signed .crt/.key pairs -->
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
            <version>1.70</version>
        </dependency>

        <!-- Needed if using the internal Api classes -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.14.2</version>
        </dependency>

        <!-- Needed for loading config via .yaml -->
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
            <version>2.13.0</version>
        </dependency>

        <!-- Needed for making http request via RPCClient, and for the Api classes -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.13</version>
        </dependency>

         <!-- @Nullable annotations -->
        <dependency>
            <groupId>com.google.code.findbugs</groupId>
            <artifactId>annotations</artifactId>
            <version>3.0.1</version>
        </dependency>
```
<br>

# **Usage**

<br>

## **Initialization**

Before using the library a few initialization steps must be taken to define the address URI of your node , override ports if needed, and load your certificates for authentication of RPC requests. This config is then passed to the HTTP client that is used to make your requests.

<br>


### **Initializing Config**

<br>

The first step to ready the library for use is to initialize a config for the node you wish to make requests to, this involves loading the certificates needed for the endpoints you will be communicating with and the URI of the node and port overrides if needed. By default the config will use localhost for the node URI and the default port numbers, if using locally these will not need to be changed.


Two options are provided for initializing the config, these are loading the config directly from a yaml file, or declaring your configuration via code.


<br>

#### ***Loading From YAML***
<br>


Loading the config from a yaml file is the recommended way of initializing the config and can be done via a static method as follows:

```java
NodeConfig myNodeConfig = NodeConfig.loadConfig("/path/to/config/Java/config.yaml");
```

A default empty config file is provided in the repo, and the config schema is:

```yaml
address: "127.0.0.1"
crawlerPort: 8561
dataLayerPort: 8561
daemonPort: 55400
fullNodePort: 8555
farmerPort: 8559
harvesterPort: 8560
walletPort: 9256
crawlerCrt: "/home/<user>/.chia_certs/crawler/private_crawler.crt"
crawlerKey: "/home/<user>/.chia_certs/crawler/private_crawler.key"
dataLayerCrt: "/home/<user>/.chia_certs/data_layer/private_data_layer.crt"
dataLayerKey: "/home/<user>/.chia_certs/data_layer/private_data_layer.key"
daemonCrt: "/home/<user>/.chia_certs/daemon/private_daemon.crt"
daemonKey: "/home/<user>/.chia_certs/daemon/private_daemon.key"
fullNodeCrt: "/home/<user>/.chia_certs/full_node/private_full_node.crt"
fullNodeKey: "/home/<user>/.chia_certs/full_node/private_full_node.key"
farmerCrt: "/home/<user>/.chia_certs/farmer/private_farmer.crt"
farmerKey: "/home/<user>/.chia_certs/farmer/private_farmer.key"
harvesterCrt: "/home/<user>/.chia_certs/harvester/private_harvester.crt"
harvesterKey: "/home/<user>/.chia_certs/harvester/private_harvester.key"
walletCrt: "/home/<user>/.chia_certs/wallet/private_wallet.crt"
walletKey: "/home/<user>/.chia_certs/wallet/private_wallet.key"

```


If you chose not to load certificates for all endpoints, either comment out or remove those fields, attempting to make requests to those endpoints will then throw an IllegalStateException.

<br>

#### ***Initializing Via Code***

<br>

A builder method is provided for config initialization via code the process of doing so is as follows:

* *The library make use of enums to aid usability, the builder for NodeConfig uses the enum class ChiaService, to define what      
 services a setting is for with the fallowing enums:*

        CRAWLER,
        DATA_LAYER,
        DAEMON,
        FARMER,
        FULL_NODE,
        HARVESTER,
        WALLET;

* The following builder methods are provide for constructing a config:

        setAddress(String address)
        addServiceCert(ChiaService chiaService, String crtPath, String keyPath)
        setPortOverride(ChiaService chiaService, int port)

<br>

Example:

```java
NodeConfig myNodeConfig = new NodeConfig.Builder()
        .setAddress("127.0.0.1")
        .addServiceCert(
                ChiaService.FULL_NODE,
                "/home/<user>/.chia_certs/full_node/private_full_node.crt",
                "/home/<user>/.chia_certs/full_node/private_full_node.key")
        .addServiceCert(
                ChiaService.FARMER,
                "/home/<user>/.chia_certs/farmer/private_farmer.crt",
                "/home/<user>/.chia_certs/farmer/private_farmer.key")
        .build();
```

<br>

### **Initializing The HTTP(RPC) Client**

<br>

The second step of readying the library for use is initializing the RPCClient, rpc client is a wrapper around``` org.apache.http.impl.client.CloseableHttpClient```. This wrapper provides the ability to initialize it with your NodeConfig, and then pass it to one or more API classes for making requests, it can be shared among several API classes that use the same node address and it will process requests made via those classes to the node defined in the config with proper authentication (as long as the certs exist and are valid).

<br>

Initializing it is as simple as passing your config object during creation:
```java
RPCClient rpcClient = new RPCClient(myNodeConfig);
```


<br>

### **Initializing API Classes For Requests**

<br>

The final step of readying the library for use is the initialize the API classes for the Chia services you plan on making requests to, this is also a simple process and just requires passing the RPCClient from the prior step to your API classes when creating them.

<br>

Example:
```java
FarmerAPI farmer = new FarmerAPI(rpcClient);
FullNodeAPI node = new FullNodeAPI(rpcClient);
```


<br>
<br>

After completing the following steps you should have a few lines of initialization code that looks something like this.

```java
NodeConfig myNodeConfig = NodeConfig.loadConfig("/path/to/config/config.yaml");
RPCClient rpcClient = new RPCClient(myNodeConfig);
FarmerAPI farmer = new FarmerAPI(rpcClient);
FullNodeAPI node = new FullNodeAPI(rpcClient);
```

<br>



## **Making requests**

<br>

You can now make requests directly to the node defined in your NodeConfig directly through the API objects you created. Any endpoint provided by a service has an associated member method in the related API class, the the only difference being that the method name is in camelCase instead of snake_case.

Example:

```java
ApiResponse<BlockChainState> response =  node.getBlockChainState();

// Or if you prefer not to declare the full generic type

var response = node.getBlockChainState();
```

<br>

***All Api requests throw RPCException***

<br>


## **API Response Object**

<br>

All requests return an instance of the generic record class APIResponse, this employs a optional generic "data" field will return either an object corresponding to the schema of the endpoints response, a built-in type in the case of single return values, an immutable collection, or  ```Optional.empty``` in the case of a failed request, or if the request returns no values.

Optional was chosen here as to be able to properly handle the lack of a value on some responses in the context of the generic response class. The rationale for using a generic response class and an optional data value was to provide robust support for more functional modern java approaches and stream compatibility.

The response object is an immutable record class consisting of the follow fields:

```java
Optional<T> data, 
boolean success,
String error,
String requestURI,
Endpoint endpoint
```

- **data:** The data returned by the endpoint, this either relates to an object mirroring the return schema of the endpoint, a single built-in type(Integer, String ect.), A collection of custom or built-in types or Optional.empty for the absence of value. All data object/types returned are immutable.

- **success:** Contains the success boolean returned by the endpoint.

- **error:** Contains the error code or message return by the endpoint in the case of an unsuccessful request.

- **address:** The raw URI that the request was made to, can be used for filtering pooled responses.

- **requestURI:** The full URI that the request was made to, can be useful for logging.

- **endpoint:** An enum of the endpoint that the request was made to, can be used to filter response of the same type, that may have come from different endpoints.

<br>
ApiResponse also contains the method

<br>


> Endpoint is an interface type, which is implemented by all endpoint enums, these enums are used internally when making requests. The value of this field will be the same as the endpoint the request was made to, and the interface also contains to methods:
>
>**getService:** returns the ChiaService enum of the service the request was made to.
>
>**getPath:** returns the path to the endpoint, used internally and is of little value outside of that. Returns the endpoint enum in all lowercase with a forward slash before it.



<br>

***In some cases the data object may also contain redundant mappings of the success and error fields, but these can be ignored and were done for ease of testing where fail on unknown properties was used for debugging. These can be ignored, its best to use the top level fields of the ApiResponse class as these will always exist.***

<br>


## **Data Schemas**

<br>

Complex response data is represented by immutable record classes containing a 1:1 mapping of the response json, but with the field names in camelCase as is the naming convention with java. In the case of more complex queries there will be nested schemas. Object representations of response data was designed with modularity and reuse in mind to avoid redundant code.

In the case of single return values, the data object of ApiResponse will be just a single built-in types, im most cases this is an Integer/Long value(fees, heights etc.), or a String(block headers, puzzlehash, etc.)

In some cases where a list of types or values are returned, the returned list/map is returned in an immutable wrapper.

In the absence of value the data field will contain Optional.empty, as such before accessing the value a condition check must occur to avoid the chance of exceptions, while in most cases checking for the request success will suffice, for best practice its best to always make sure the value is not empty as can be the case for some requests that return successful but contain no return values.

In the case of responses that return no data, and just return if the request was successful or not, the data value will be a Boolean mirroring the success field of the response.

<br>

Examples:
```java

// Check for presence of value
var response = node.getBlockChainState();                  
if (response.data().isPresent()) {                         
    BlockChainState blockchainState = response.data().get()
    // act on data                                         
}

// Example of using streams on a list of data elements that may have empty values
long totalFees = recordsList.stream()                                   
    .filter(r -> r.data().isPresent())                                  
    .mapToLong(r -> r.data().get().fees()) 
    .sum();                                                             

```

<br>

### **Blockchain Primitives/Objects**

<br>

 Found in ```io.mindspice.schemas.object``` these are a subset of the schema objects that are directly related to on-chain info and direct interactions with the blockchain/nodes.

<br>


## **API Requests**

<br>

For every API endpoint two request method, the "default" method that will return a parsed data object, and secondary ``<method>AsBytes`` methods that will return the request exactly as received from the endpoint as an array of bytes. This secondary method can be used if you would prefer to parse the json with a different library and/or work directly with the json itself without having it returned as a data object.

<br>

### **Making HTTP Request Directly**

<br>

If not wanting to use the built-in API classes for requests, the RPCClient can be used to make request directly. This will return an array of bytes and allows full control of handling requests/responses and their associated json. All the API classes use a jackson singleton wrapper for requests, due to lazy initialization if these classes are not instanced and used, there is no need for the jackson dependency.

To make a request directly with RPCClient follow the initialization steps outlined above, without instancing any APIClasses. The RPCClient can then be used to make requests to the node defined in the NodeConfig.

Using the ```makeRequest(Request req)``` method of RPCClient a ```Request``` Object can be passed containing the data required for the request which will then be dispatched to the address in the NoneConfig provided during the construction of the RPCClient.

The schema for the ```Request``` object is:

```java
public class Request {
    public final ChiaService service; // Service for the request, this is automatically set via the Endpoint enum
    public final String endpoint;     // The Endpoint, this is an interface type that all endpoint enum Implement
    public final byte[] data;         // The request json as a byte array

    public Request(Endpoint endpoint, byte[] data) {
        this.service = endpoint.getService();
        this.endpoint = endpoint.getPath();
        this.data = data;
    }

    // Allows for overriding the service, this will be needed if using one of the shared endpoints
    public Request(ChiaService service, Endpoint endpoint, byte[] data) {
        this.service = service;
        this.endpoint = endpoint.getPath();
        this.data = data;
    }
}
```

The enums classes containing the enums of the endpoints are:
- Daemon
- Farmer
- FullNode
- Harvester
- Shared
- Wallet

<br>

## **Builder Methods**

<br>

Any of the API endpoints that directly take a JsonNode, have an associated builder method that can be found in ```io.mindspice.util.ChiaUtils```. These will later be documented in the JavaDocs for the project, but currently are not, which can be slightly confusing and was an oversight. JavaDocs for all the the API methods, will be added soon and should clear things up more. ***When encountering a method that takes a parameter of type JsonNode, the parameter name is the name of the builder you should use.*** These are used for endpoints that have a lot of parameters and/or optional parameters. If a method take a JsonNode (The builders actually return an ObjectNode, but these are interchangeable in this context) type parameter, then there is a related builder method. There is also a builder method for crafting spendbundles.

To identify what parameters are required for your use case, and what is optional or not, its best to consult the RPC documentation at: https://docs.chia.net/rpc

There are some small validation checks when building, but due to all the possible combinations these methods expect you to know what the endpoint is expecting for your use case.

<br>

The provided builder methods are:

- **CatSpendBuilder:** Used for crafting a CAT spend.

- **OfferBuilder:** Used for crafting an offer.

- **OfferSearchBuilder:** Used to define the parameters for a search of existing offers.

- **SelectCoinBuilder:** Used for selecting a coin via various parameters/conditions.

- **TakeOfferBuilder:** Used for taking an offer and defining the parameters/conditions around taking it.

- **CatWalletBuilder:** Used with the ```createNewWallet``` method to craft a new CAT wallet.

- **DIDWalletBuilder:** Used with the ```createNewWallet``` method to craft a new DID wallet.

- **NFTWalletBuilder:** Used with the ```createNewWallet``` method to craft a new NFT wallet.

- **SignedTransactionBuilder:** Used Tto craft a signed transactions.

- **SpendableCoinBuilder:** Used to select a spendable coin via various parameters/conditions.

- **TransactionBuilder:** Used to craft a simple transaction.

- **MultiTransactionBuilder:** Used to crafts multiple transactions in one spend.

- **SpendBundleBuilder:** Used to craft a spendbundle.

- **BulkMintBuilder:** Used to bulk mint NFTs.

- **MetaDataBuilder:** Used to craft metadata for updating an NFT's metadata.

- **SingleMintBuilder:** Used to mint a single NFT.

- **SetDidBulkBuilder:** Used to bulk update NFTs with a DID.

- **NftBulkTransferBuilder:** Used For bulk NFT transfers.

<br>

Examples:
```java
JsonNode req = new ChiaUtils.SpendableCoinBuilder()
        .setWalletId(1)
        .setExcludedCoinIds(List.of("0x4d0012503cb0b31947ed582881e59d334b667a0b4c96ac86c4f540c850055a22"))
        .setMinCoinAmount(10000000000L)
        .setMaxCoinAmount(20000000000L)
        .build();

wallet.getSpendableCoins(req);



```

<br>

## **JsonUtils**
<br>

This is a small custom wrapper around the Jackson json library that provides fluent functionality, some extra caching and is mainly used lessen the amount of code needed in the internal methods of the library. Since json performance should not be a bottleneck it is also implemented as a singleton to make it easy to reused the same ObjectMapper anywhere in a project. This class will also makes uses of reader for classes to deserialize allowing for performant, thread-safe deserialization. This will scale well into deserializing thousands of requests per second. It can also be used directly if one is choosing to not use the API methods and wants to craft the request json by hand to use directly with RPCClient.

Most of the methods are simple wrappers of existing ObjectMapper methods, and are used to easily contain them to the singleton without having to explicitly do ```mapper.x``` while also providing some caching of existing readers and type references.

There are also some methods to easily get a single field JsonNode ```newSingleNode``` and get empty nodes when need ```newEmptyNode```as well as a method to merge two json structures.

The biggest benefit it provides is the ```JsonUtils.ObjectBuilder()``` class that allow for fluent building of json structures.

example:

```java
ObjectNode = new JsonUtils.ObjectBuilder()
   .put("string", "value")
   .put("int", 42)
   .put("class", new MyClass())
   .buildNode();
```

