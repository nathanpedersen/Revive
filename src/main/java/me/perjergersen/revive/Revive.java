package me.perjergersen.revive;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.perjergersen.revive.Commands.Ifidie;
import me.perjergersen.revive.Commands.Resurrect;
import me.perjergersen.revive.Commands.Book;
import me.perjergersen.revive.Commands.Listres;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import static me.perjergersen.revive.Utilities.HelperFunctions.*;
import static me.perjergersen.revive.Utilities.Mongo.mongoClient;

public final class Revive extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        /* Plugin startup logic */
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("resurrect").setExecutor(new Resurrect());
        getCommand("ifidie").setExecutor(new Ifidie());
        getCommand("listres").setExecutor(new Listres());
        getCommand("Book").setExecutor(new Book());
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
    }

    @Override
    public void onDisable() {
        /* Plugin shutdown logic */
        mongoClient.close();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Use /book to get server game mode information.");

        if (!player.hasPlayedBefore()) {
            giveBook(player);
        }
    }

    @EventHandler
    public void onEatGoldenApple(PlayerItemConsumeEvent e) {
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
    public void onBedSleepInteraction(PlayerBedEnterEvent e) {
        e.getPlayer().setBedSpawnLocation(e.getBed().getLocation());
    }

    /* DEATH MECHANIC HANDLER
       I originally had this in a PlayerDeathEvent handler but had some problems with teleporting the player out of other dimensions.
       Putting it here allows me to utilize the the normal respawn mechanic in the game so I don't have to manually
       teleport the player */
    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        MongoDatabase database = mongoClient.getDatabase("Minecraft_Revive");
        MongoCollection collection = database.getCollection("Revive_Data");

        Document databaseEntry = new Document();

        Player player = e.getPlayer();
        int diamonds = calcDiamondCost(player);

        /* player enters spectator mode when they die in hardcore mode */
        if (e.getNewGameMode() == GameMode.SPECTATOR) {
            if (player.getStatistic(Statistic.DEATHS) % 2 == 0) {
                player.resetMaxHealth();
                databaseEntry.append("name", player.getName());
                databaseEntry.append("cost", diamonds);
                collection.insertOne(databaseEntry);
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
