package me.perjergersen.revive.commands;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.perjergersen.revive.Utilities.HelperFunctions.TeleportPlayer;
import static me.perjergersen.revive.Utilities.Mongo.mongoClient;

public class Resurrect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // checking if the person sending the command is a player. (could be the console)
        if (sender instanceof Player) {
            // player is the person sending the command
            Player player = (Player) sender;
            // checking if command was typed out correctly. Usage: /resurrect <dead player>
            if (args.length == 1) {
                // making sure player is alive in order to resurrect someone else.
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    // redundant check? making sure target is a player ie. not the console. Target is already a player. Makes me feel more comfortable :)
                    if (target instanceof Player) {
                        // checking if target is dead
                        if (target.getGameMode() == GameMode.SPECTATOR) {
                            // connect to mangodb
                            MongoDatabase database = mongoClient.getDatabase("Minecraft_Revive");
                            MongoCollection collection = database.getCollection("Revive_Data");

                            // find entry in mangodb with the targets name
                            Document found = (Document) collection.find(new Document("name", target.getName())).first();

                            // if found is null then targets name was not in database
                            if (found != null) {
                                // retrieve targets resurrection cost from db
                                int targetCost = (int) found.get("cost");
                                // check if player has enough diamonds to resurrect player
                                if (player.getInventory().contains(Material.DIAMOND, targetCost)) {
                                    // remove diamonds from player
                                    ItemStack iStack = new ItemStack(Material.DIAMOND);
                                    iStack.setAmount(targetCost);
                                    player.getInventory().removeItem(iStack);

                                    // resurrect target
                                    target.setGameMode(GameMode.SURVIVAL);
                                    TeleportPlayer(target);
                                    Bukkit.getServer().broadcastMessage(target.getName() + " has been resurrected!");

                                    // remove target entry from db
                                    collection.deleteOne(found);
                                } else {
                                    player.sendMessage("You need " + targetCost + " diamonds to resurrect " + target.getName() + ".");
                                }
                            }
                        } else {
                            player.sendMessage("Player is already alive!");
                        }
                    } else {
                        player.sendMessage("Not a valid player.");
                    }
                } else {
                    player.sendMessage("You need to be in Survival Mode to resurrect players!");
                }
            } else {
                player.sendMessage("Usage: /resurrect <currently dead player (case sensitive)>");
            }
        } else
            sender.sendMessage("You're a console! You can't do that!");
        return true;
    }
}
