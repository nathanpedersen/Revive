package me.perjergersen.revive.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HelperFunctions {

    // Teleports player to their bed or spawn if bed does not exist. Will teleport from other dimensions.
    // EDIT: This will teleport from the nether to the overworld but for some reason wont teleport from the end to the overworld
    // keeping here to figure out why some day.
    public static void TeleportPlayer(Player player) {
        if (player.getBedSpawnLocation() != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in minecraft:overworld run teleport "
                    + player.getName() + " "
                    + player.getBedSpawnLocation().getX() + " "
                    + player.getBedSpawnLocation().getY() + " "
                    + player.getBedSpawnLocation().getZ());
        }
        else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in minecraft:overworld run teleport " + player.getName() + " "
                    + Bukkit.getWorld("HardcoreServer").getSpawnLocation().getX() + " "
                    + Bukkit.getWorld("HardcoreServer").getSpawnLocation().getY() + " "
                    + Bukkit.getWorld("HardcoreServer").getSpawnLocation().getZ());
        }
    }

    public static void giveBook(Player p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + p.getName() + " written_book{pages:['[\"\",{\"text\":\"Revive Plugin Info\",\"bold\":true},{\"text\":\"\\\\n\\\\nCommands:\\\\n\\\\n/listres - lists all dead players and their resurrection cost.\\\\n\\\\n/resurrect <player> - resurrects given player given you have the diamonds (They need to be online).\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"/ifidie - shows how much your resurrection cost will be if you die right now + total playtime\\\\n\\\\n\"},{\"text\":\"Other Information\",\"bold\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Death mechanics\",\"italic\":true},{\"text\":\":\\\\n\",\"color\":\"reset\"},{\"text\":\"first death\",\"bold\":true},{\"text\":\" will spawn you back at spawn or your bed and halve your maximum hp.\",\"color\":\"reset\"}]','{\"text\":\"you can restore 1 maximum hp by consuming a golden apple (half a heart).\\\\n\\\\nyou can restore maximum hp back to 20 with an enchanted golden apple.\\\\n\\\\n \"}','[\"\",{\"text\":\"Second death \",\"bold\":true},{\"text\":\"will put you in spectator mode, even if you restored your maximum hp.\\\\nYou will need to be revived in order to play again.\\\\n\\\\nthis cycle will repeat.\\\\n\\\\nOnce revived you will be back at max maximum hp.\\\\n\\\\n \",\"color\":\"reset\"}]','{\"text\":\"Keep in mind every time you die you will be put into the death screen and given the option to go into spectator mode. No matter what death you\\'re on you can click spectator mode and it will work correctly. I can\\'t change this.\"}','{\"text\":\"Resurrection cost is calculated from this equation:\\\\n\\\\ncost = (3h+350) * 0.1\\\\n\\\\nwhere h is hours played on the server rounded down.\"}'],title:\"Revive Manual\",author:pouch1,display:{Lore:[\"Server game mode information. Please read.\"]}}");
    }

    public static int calcDiamondCost(Player player) {
        return (int) (((3 * getHoursPlayed(player)) + 350) * 0.1);
    }
    // PLAY_ONE_MINUTE returns ticks played. 20 tics/second
    public static double getHoursPlayed(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000.0;
    }
}
