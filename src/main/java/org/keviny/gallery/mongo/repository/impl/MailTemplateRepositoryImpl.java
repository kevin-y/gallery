package org.keviny.gallery.mongo.repository.impl;

import org.bson.types.ObjectId;
import org.keviny.gallery.common.Collection;
import org.keviny.gallery.common.mongo.MongoFactory;
import org.keviny.gallery.mongo.model.MailTemplate;
import org.keviny.gallery.mongo.repository.MailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 6/6/15.
 */
@Repository
public class MailTemplateRepositoryImpl implements MailTemplateRepository {

    private static final String DB_NAME = "MAIL_db";
    private static final String COLLECTION = Collection.MAIL_TEMPLATE;

    @Autowired
    private MongoFactory mongoFactory;


    public MailTemplate get(ObjectId id) {
        MongoTemplate mongoTemplate = mongoFactory.getMongoTemplate(DB_NAME);
        mongoTemplate.findAll(Map.class);
        return null;
    }

    public List<MailTemplate> list() {
        return null;
    }

}
