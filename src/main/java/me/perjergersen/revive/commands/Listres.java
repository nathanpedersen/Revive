package me.perjergersen.revive.commands;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

import static me.perjergersen.revive.Utilities.Mongo.mongoClient;

public class Listres implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        MongoDatabase database = mongoClient.getDatabase("Minecraft_Revive");
        MongoCollection collection = database.getCollection("Revive_Data");

        Block<Document> printBlock = document -> sender.sendMessage(document.toJson());

        collection.aggregate(Arrays.asList()).forEach(printBlock);

        return true;
    }
}
