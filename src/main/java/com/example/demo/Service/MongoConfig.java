package com.example.demo.Service;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "MySpringProject";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://ganeshkailash8344_db_user:k8524839876@myspringproject.f15ulmo.mongodb.net/MySpringProject?retryWrites=true&w=majority");
    }
}