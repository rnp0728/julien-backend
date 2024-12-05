package com.infinity.julien.dbOps;

import com.infinity.julien.dbOps.dtos.CollectionInfo;
import com.infinity.julien.environment.Environment;
import com.infinity.julien.environment.EnvironmentService;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoDBOpsService {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBOpsService.class);
    private final EnvironmentService environmentService;

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
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
