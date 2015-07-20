package org.keviny.gallery.common.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kevin on 6/7/15.
 */
public class MongoFactory {

    private MongoClient mongoClient;
    private static final String ADMIN_DB_NAME = "admin";
    private ConcurrentHashMap<String, MongoTemplate> mongoTemplates = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, GridFsTemplate> gridFsTemplates = new ConcurrentHashMap<>();

    public MongoFactory(Mongo mongo) {
        this(mongo, null, null);
    }

    public MongoFactory(Mongo mongo, String username, String password) {
        if(username == null || "".equals(username.trim())) {
            this.mongoClient = new MongoClient(mongo.getAddress());
        } else {
            MongoCredential credential =  MongoCredential.createMongoCRCredential(username, ADMIN_DB_NAME, password.toCharArray());
            ArrayList<MongoCredential> credentials = new ArrayList<>();
            credentials.add(credential);
            this.mongoClient = new MongoClient(mongo.getAddress(), credentials);
        }
    }

    public MongoTemplate getMongoTemplate(String dbname) {
        MongoTemplate mongoTemplate = mongoTemplates.get(dbname);
        if(mongoTemplate != null)
            return mongoTemplate;

        SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, dbname);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(mongoDbFactory);
        mongoTemplates.putIfAbsent(dbname, mongoTemplate);
        return mongoTemplate;
    }

    public GridFsTemplate getGridFsTemplate(String dbname) {
        GridFsTemplate gridFsTemplate = gridFsTemplates.get(dbname);
        if(gridFsTemplate != null)
            return gridFsTemplate;

        SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, dbname);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter);
        gridFsTemplates.putIfAbsent(dbname, gridFsTemplate);
        return gridFsTemplate;
    }
 }
