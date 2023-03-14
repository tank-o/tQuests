package tanko.tquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Reward;

import java.util.Arrays;

public class RewardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Usage: /reward <command> [args]");
            return true;
        }
        Quest quest = TQuests.getSelectedQuest(player);
        if (quest == null) {
            player.sendMessage("You must select a quest first before using a reward command");
            return true;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "add" -> addReward(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "remove" -> removeReward(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "set" -> setReward(player, Arrays.copyOfRange(args, 1, args.length));
            case "view" -> viewReward(player);
            case "select" -> selectReward(player, Arrays.copyOfRange(args, 1, args.length),quest);
            default -> {
                player.sendMessage("Not known subcommand");
                return true;
            }
        }
        return true;
    }

    private void addReward(Player player, String[] args,Quest quest) {
        if (args.length < 2) {
            player.sendMessage("Usage: /reward add <reward name> <reward type>");
            return;
        }
        String rewardID = args[0];
        String rewardType = args[1];
        if (quest.addReward(rewardID, rewardType)) {
            player.sendMessage("Reward " + rewardID + " added to quest " + quest.getName());
        } else {
            player.sendMessage("Problem adding reward " + rewardID + " to quest " + quest.getName());
        }
    }

    private void setReward(Player player,String[] args){
        Reward reward = TQuests.getSelectedReward(player);
        if (reward == null) {
            player.sendMessage("You must select a reward first");
            return;
        }
        reward.handleCommand(player, args);
    }

    private void removeReward(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /reward remove <reward ID>");
            return;
        }
        String rewardID = args[0];
        if (quest.removeReward(rewardID)) {
            player.sendMessage("Reward " + rewardID + " removed from quest " + quest.getName());
        } else {
            player.sendMessage("Problem removing reward " + rewardID + " from quest " + quest.getName());
        }
    }

    private void viewReward(Player player) {
        Reward reward = TQuests.getSelectedReward(player);
        if (reward == null) {
            player.sendMessage("You must select a reward first");
            return;
        }
        reward.viewInfo(player);
    }

    private void selectReward(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /reward select <reward ID>");
            return;
        }
        String rewardID = args[0];
        Reward reward = quest.rewardByID(rewardID);
        if (reward == null) {
            player.sendMessage("Reward " + rewardID + " not found in quest " + quest.getName());
            return;
        }
        TQuests.setSelectedReward(player, reward);
        player.sendMessage("Reward " + rewardID + " selected");
    }
}
