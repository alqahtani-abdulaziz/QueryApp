import com.microsoft.windowsazure.Configuration;
// Include the following imports to use table APIs
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class QueryApp {

    private static final String CAMERA_TABLE_NAME = "cameras";
    private static final String VEHICLE_TABLE_NAME = "vehicles";

    // Define the connection-string with your values.
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=nosql;" +
                    "AccountKey=2d7s25YGjHn5GxcW0qW1DYsyMwcp0cxT9/z0jGpvoDZJTa9Q2R0pP7hizwEN9jSzoAil5KzrRzH8RTAQEP4rpw==";
    static Configuration config;
    CloudStorageAccount storageAccount;
    CloudTableClient tableClient;
    public QueryApp() throws URISyntaxException, InvalidKeyException {

        // Retrieve storage account from connection-string.
        storageAccount =
                CloudStorageAccount.parse(storageConnectionString);

        // Create the table client.
        tableClient = storageAccount.createCloudTableClient();

    }

    public ArrayList<CameraEntity> getAllCameras() throws URISyntaxException, StorageException {
        ArrayList<CameraEntity> cameras = new ArrayList<CameraEntity>();
        CloudTable cloudTable = tableClient.getTableReference(CAMERA_TABLE_NAME);
        TableQuery<CameraEntity> query = TableQuery.from(CameraEntity.class);
        for(CameraEntity entity : cloudTable.execute(query)){
            cameras.add(entity);
        }
        return cameras;
    }

    public ArrayList<VehicleEntity> getOffenderVehicles() throws URISyntaxException, StorageException {
        final String PARTITION_KEY = "PartitionKey";
        ArrayList<VehicleEntity> offenders = new ArrayList<VehicleEntity>();
        CloudTable cloudTable = tableClient.getTableReference(VEHICLE_TABLE_NAME);
        String partitionFilter = TableQuery.generateFilterCondition(
                PARTITION_KEY,
                TableQuery.QueryComparisons.EQUAL,
                "true");
        TableQuery<VehicleEntity> query = TableQuery.from(VehicleEntity.class);
        for(VehicleEntity entity : cloudTable.execute(query)){
            offenders.add(entity);
        }
        return offenders;

    }


}
