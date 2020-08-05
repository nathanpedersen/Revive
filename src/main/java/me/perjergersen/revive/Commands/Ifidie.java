package me.perjergersen.revive.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static me.perjergersen.revive.Utilities.HelperFunctions.calcDiamondCost;
import static me.perjergersen.revive.Utilities.HelperFunctions.getHoursPlayed;

public class Ifidie implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int diamonds = calcDiamondCost(player);
            double hoursPlayed = getHoursPlayed(player);
            DecimalFormat df = new DecimalFormat("#.##");
            player.sendMessage("If you die right now you will cost " + Integer.toString(diamonds) + " diamonds to resurrect! (Playtime: " + df.format(hoursPlayed) + " hrs)");
        }
        return false;
    }
}
