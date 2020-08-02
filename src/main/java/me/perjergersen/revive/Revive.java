package me.perjergersen.revive;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.perjergersen.revive.commands.Ifidie;
import me.perjergersen.revive.commands.Resurrect;
import me.perjergersen.revive.commands.book;
import me.perjergersen.revive.commands.listres;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static me.perjergersen.revive.Utilities.HelperFunctions.*;
import static me.perjergersen.revive.Utilities.Mongo.mongoClient;

public final class Revive extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("resurrect").setExecutor(new Resurrect());
        getCommand("ifidie").setExecutor(new Ifidie());
        getCommand("listres").setExecutor(new listres());
        getCommand("Book").setExecutor(new book());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mongoClient.close();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Use /book to get server game mode information.");
        player.sendMessage(ChatColor.AQUA + "Message Nathan-#2574 if you are dead. I messed up a bit updating the plugin :( Should be fixed.");

        if (!player.hasPlayedBefore()) {
            giveBook(player);
        }
    }

    @EventHandler
    public void eatGoldenApple(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (player.getMaxHealth() < 20) {
            if (e.getItem().isSimilar(new ItemStack(Material.GOLDEN_APPLE))) {
                player.setMaxHealth(player.getMaxHealth() + 1);
            } else if (e.getItem().isSimilar(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE))) {
                player.setMaxHealth(20);
            }
            if (player.getMaxHealth() > 20) {
                player.setMaxHealth(20);
            }
        }
    }

    @EventHandler
    public void setSpawn(PlayerBedEnterEvent e) {
        e.getPlayer().setBedSpawnLocation(e.getBed().getLocation());
    }

    // DEATH MECHANIC HANDLER
    // I originally had this in a PlayerDeathEvent handler but had some problems with teleporting the player out of other dimensions.
    // Putting it here allows me to utilize the the normal respawn mechanic in the game so I don't have to manually
    // teleport the player
    @EventHandler
    public void PlayerGameModeChange(PlayerGameModeChangeEvent e) {
        MongoDatabase database = mongoClient.getDatabase("Minecraft_Revive");
        MongoCollection collection = database.getCollection("Revive_Data");

        Document document = new Document();

        Player player = e.getPlayer();
        int diamonds = calcDiamondCost(player);
        if (e.getNewGameMode() == GameMode.SPECTATOR) {
            if (player.getStatistic(Statistic.DEATHS) % 2 == 0) {
                player.resetMaxHealth();
                document.append("name", player.getName());
                document.append("cost", diamonds);
                collection.insertOne(document);
                player.setHealth(20);
                Bukkit.getServer().broadcastMessage(player.getName() + " has died! (Resurrection cost: " + diamonds + " diamonds).");
            } else {
                player.setHealth(20);
                player.setMaxHealth(10.0);
                e.setCancelled(true);
                player.setGameMode(GameMode.SURVIVAL);
                Bukkit.getServer().broadcastMessage(player.getName() + " just halved their total HP pepelaugh.");
            }
        }
    }
}