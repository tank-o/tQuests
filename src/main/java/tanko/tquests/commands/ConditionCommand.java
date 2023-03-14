package tanko.tquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tanko.tquests.TQuests;
import tanko.tquests.system.Condition;
import tanko.tquests.system.Quest;

import java.util.Arrays;

public class ConditionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Usage: /conndition <command> [args]");
            return true;
        }
        Quest quest = TQuests.getSelectedQuest(player);
        if (quest == null) {
            player.sendMessage("You must select a quest first before using a condition command");
            return true;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "add" -> addCondition(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "remove" -> removeCondition(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "set" -> setCondition(player, Arrays.copyOfRange(args, 1, args.length));
            case "select" -> selectCondition(player, Arrays.copyOfRange(args, 1, args.length),quest);
            default -> {
                player.sendMessage("Not known subcommand");
                return true;
            }
        }
        return true;
    }

    private void addCondition(Player player, String[] args,Quest quest) {
        if (args.length < 2) {
            player.sendMessage("Usage: /condition add <condition name> <condition type>");
            return;
        }
        String conditionID = args[0];
        String conditionType = args[1];
        if (quest.addCondition(conditionID, conditionType)) {
            player.sendMessage("Condition " + conditionID + " added to quest " + quest.getName());
        } else {
            player.sendMessage("Problem adding condition " + conditionID + " to quest " + quest.getName());
        }
    }

    private void removeCondition(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /condition remove <condition name>");
            return;
        }
        String conditionID = args[0];
        if (quest.removeCondition(conditionID)) {
            player.sendMessage("Condition " + conditionID + " removed from quest " + quest.getName());
        } else {
            player.sendMessage("Problem removing condition " + conditionID + " from quest " + quest.getName());
        }
    }

    private void selectCondition(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /condition select <condition name>");
            return;
        }
        String conditionID = args[0];
        Condition condition = quest.conditionByID(conditionID);
        if (condition == null) {
            player.sendMessage("Condition " + conditionID + " not found");
            return;
        }
        TQuests.setSelectedCondition(player, condition);
        player.sendMessage("Condition " + conditionID + " selected");
    }

    private void setCondition(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /condition set <condition name> <condition type>");
            return;
        }
        Condition condition = TQuests.getSelectedCondition(player);
        if (condition == null) {
            player.sendMessage("You must select a condition first before using this condition command");
            return;
        }
        condition.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
    }
}
