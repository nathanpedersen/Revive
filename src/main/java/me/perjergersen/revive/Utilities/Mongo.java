package me.perjergersen.revive.Utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Mongo {
    public static final MongoClientURI uri = new MongoClientURI(
            "mongodb://admin:admin@cluster0-shard-00-00.kjpz8.mongodb.net:27017,cluster0-shard-00-01.kjpz8.mongodb.net:27017,cluster0-shard-00-02.kjpz8.mongodb.net:27017/Minecraft_Revive?ssl=true&replicaSet=atlas-1zet3d-shard-0&authSource=admin&retryWrites=true&w=majority");

    public static final MongoClient mongoClient = new MongoClient(uri);
}
