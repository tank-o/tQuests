package tanko.tquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.gui.InProgressMenu;
import tanko.tquests.gui.QuestMainMenu;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Reward;
import tanko.tquests.system.Step;

import java.util.Arrays;
import java.util.List;

public class QuestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Usage: /quest <command> [args]");
            return true;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "create" -> {
                createQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "select" -> {
                selectQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "delete" -> {
                deleteQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "set" -> {
                setQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "accept" -> {
                acceptQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "view" -> {
                viewQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "reset" -> {
                resetPlayerQuest(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            case "menu" -> new QuestMainMenu(player).open();
        }
        return true;
    }

    private void createQuest(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /quest create <quest name>");
            return;
        }
        String questName = args[0];
        // Make sure that the quest name is all lowercase and has no spaces
        questName = questName.toLowerCase().replace(" ", "_");
        Quest quest = new Quest(questName);
        TQuests.getQuestRegistry().registerQuest(quest);
        player.sendMessage("Quest " + questName + " created");
        player.sendMessage("Quest " + questName + " selected");
        TQuests.setSelectedQuest(player, quest);
    }

    private void selectQuest(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /quest select <quest name>");
            return;
        }
        String questID = args[0];
        // Make sure that the quest name is all lowercase and has no spaces
        questID = questID.toLowerCase().replace(" ", "_");
        Quest quest = TQuests.getQuestRegistry().getQuest(questID);
        if (quest == null) {
            player.sendMessage("Quest " + questID + " does not exist");
            return;
        }
        TQuests.setSelectedQuest(player, quest);
        player.sendMessage("Quest " + questID + " selected");
    }

    private void deleteQuest(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /quest delete <quest name>");
            return;
        }
        String questID = args[0];
        // Make sure that the quest name is all lowercase and has no spaces
        questID = questID.toLowerCase().replace(" ", "_");
        Quest quest = TQuests.getQuestRegistry().getQuest(questID);
        if (quest == null) {
            player.sendMessage("Quest " + questID + " does not exist");
            return;
        }
        TQuests.getQuestRegistry().deregisterQuest(questID);
        player.sendMessage("Quest " + questID + " deleted, along with all of its data");
    }

    private void acceptQuest(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /quest accept <quest ID>");
            return;
        }
        String questID = args[0];
        // Make sure that the quest name is all lowercase and has no spaces
        questID = questID.toLowerCase().replace(" ", "_");
        Quest quest = TQuests.getQuestRegistry().getQuest(questID);
        if (quest == null) {
            player.sendMessage("Quest " + questID + " does not exist");
            return;
        }
        if (!TQuests.getQuestRegistry().startQuest(quest,player)){
            player.sendMessage("Quest could not be started");
            return;
        }
        player.sendMessage("Quest " + questID + " accepted");
    }

    private void viewQuest(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /quest view <quest ID>");
            return;
        }
        String questID = args[0];
        // Make sure that the quest name is all lowercase and has no spaces
        questID = questID.toLowerCase().replace(" ", "_");
        Quest quest = TQuests.getQuestRegistry().getQuest(questID);
        if (quest == null) {
            player.sendMessage("Quest " + questID + " does not exist");
            return;
        }
        player.sendMessage("Quest " + questID + " info:");
        player.sendMessage("Name: " + quest.getName());
        player.sendMessage("Description: " + quest.getDescription());
        List<Step> steps = quest.getSteps();
        if (steps.size() == 0) {
            player.sendMessage("Steps: None");
        } else {
            player.sendMessage("Steps:");
            for (Step step : steps) {
                player.sendMessage(" -> " + step.getID() + " : " + step.getClass().getSimpleName());
            }
        }
        List<Reward> rewards = quest.getRewards();
        if (rewards.size() == 0) {
            player.sendMessage("Rewards: None");
        } else {
            player.sendMessage("Rewards:");
            for (Reward reward : rewards) {
                player.sendMessage(" -> " + reward.getID() + " : " + reward.getClass().getSimpleName());
            }
        }
    }

    private void setQuest(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /quest set <variable> <value>");
            return;
        }
        Quest quest = TQuests.getSelectedQuest(player);
        if (quest == null) {
            player.sendMessage("No quest selected");
            return;
        }
        String variable = args[0];
        switch (variable){
            case "name" ->  {
                // Join the rest of the arguments into a single string
                String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                quest.setName(name);
                player.sendMessage("Quest name set to: " + name);
            }
            case "description" -> {
                // Join the rest of the arguments into a single string
                String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                quest.setDescription(description);
                player.sendMessage("Quest description set to: " + description);
            }
        }
    }

    private void resetPlayerQuest(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /quest reset <quest ID> <player name>");
            return;
        }
        String questID = args[0];
        String playerName = args[1];
        // Make sure that the quest name is all lowercase and has no spaces
        questID = questID.toLowerCase().replace(" ", "_");
        Quest quest = TQuests.getQuestRegistry().getQuest(questID);
        if (quest == null) {
            player.sendMessage("Quest " + questID + " does not exist");
            return;
        }
        // Make sure that the player exists
        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            player.sendMessage("Player does not exist");
            return;
        }
        TQuests.getQuestRegistry().resetQuest(quest, player);
        player.sendMessage("Quest " + questID + " reset for player " + playerName);
    }
}
