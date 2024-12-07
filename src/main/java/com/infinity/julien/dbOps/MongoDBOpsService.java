package com.infinity.julien.dbOps;

import com.infinity.julien.dbOps.dtos.CollectionInfo;
import com.infinity.julien.dbOps.dtos.MoveRequest;
import com.infinity.julien.environment.Environment;
import com.infinity.julien.environment.EnvironmentService;
import com.infinity.julien.exception.exceptions.FailedToRestoreException;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoDBOpsService {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBOpsService.class);
    private final EnvironmentService environmentService;

    private static String getRestoreCommand(Environment destinationEnv, String collection, boolean dropExistingCollection) {
        return dropExistingCollection ? String.format(
                "mongorestore --host %s --username %s --password %s --authenticationDatabase %s --nsInclude %s.%s " +
                        "--archive --drop",
                destinationEnv.getDbHost(), destinationEnv.getDbUser(), destinationEnv.getDbPassword(),
                destinationEnv.getDbName(), destinationEnv.getDbName(), collection
        ) : String.format(
                "mongorestore --host %s --username %s --password %s --authenticationDatabase %s --nsInclude %s.%s --archive",
                destinationEnv.getDbHost(), destinationEnv.getDbUser(), destinationEnv.getDbPassword(),
                destinationEnv.getDbName(), destinationEnv.getDbName(), collection
        );
    }

    public List<CollectionInfo> collections(@NonNull String environmentId) throws NotFoundException {
        Environment environment = environmentService.findById(environmentId);
        return getCollectionsInfo(
                environment.getDbHost(),
                environment.getDbUser(),
                environment.getDbPassword(),
                environment.getDbName()
        );
    }

    public List<CollectionInfo> getCollectionsInfo(
            String dbHost, String dbUser, String dbPassword, String dbName
    ) {
        MongoCredential credential = MongoCredential.createCredential(dbUser, dbName, dbPassword.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress(dbHost))))
                .credential(credential)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            List<CollectionInfo> result = new ArrayList<>();
            MongoDatabase db = mongoClient.getDatabase(dbName);
            MongoIterable<String> collections = db.listCollectionNames();
            logger.info("MongoDB collections: {}", collections);

            for (String collection : collections) {
                Document stats = db.runCommand(new Document("collStats", collection));
                long size = stats.getInteger("size");
                long count = stats.getInteger("count");
                var collectionInfo = new CollectionInfo(collection, size, count);
                result.add(collectionInfo);
            }
            result.sort(Comparator.comparing(CollectionInfo::getName));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean move(MoveRequest request) throws Exception {
        var source = environmentService.findById(request.sourceEnvId());
        var destination = environmentService.findById(request.destinationEnvId());
        return moveCollection(source, destination,
                request.collectionName(), request.clearExistingData());
    }

    public boolean moveCollection(
            Environment sourceEnv,
            Environment destinationEnv,
            String collection,
            boolean dropExistingCollection
    ) {
        logger.info("Moving collection: {}", collection);
        String dumpCommand = String.format(
                "mongodump --host %s --db %s --collection %s --username %s --password %s --archive",
                sourceEnv.getDbHost(), sourceEnv.getDbName(), collection, sourceEnv.getDbUser(), sourceEnv.getDbPassword()
        );

        String restoreCommand = getRestoreCommand(destinationEnv, collection, dropExistingCollection);

        try {
            Process dumpProcess = new ProcessBuilder(dumpCommand.split(" ")).start();

            Process restoreProcess = new ProcessBuilder(restoreCommand.split(" ")).start();

            Thread dumpToRestoreThread = new Thread(() -> {
                try (InputStream inputStream = dumpProcess.getInputStream();
                     OutputStream outputStream = restoreProcess.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            dumpToRestoreThread.start();

            int dumpExitCode = dumpProcess.waitFor();
            int restoreExitCode = restoreProcess.waitFor();

            if (dumpExitCode == 0 && restoreExitCode == 0) {
                logger.info("Successfully restored collection: {}", collection);
                return true;
            } else {
                throw new FailedToRestoreException("Failed to restore collection: " + collection);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new FailedToRestoreException("Failed to move collection: " + collection);
        }
    }

}
