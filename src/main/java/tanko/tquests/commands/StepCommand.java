package tanko.tquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

import java.util.Arrays;

public class StepCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Usage: /step <command> [args]");
            return true;
        }
        Quest quest = TQuests.getSelectedQuest(player);
        if (quest == null) {
            player.sendMessage("You must select a quest first before using a step command");
            return true;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "add" -> addStep(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "remove" -> removeStep(player, Arrays.copyOfRange(args, 1, args.length),quest);
            case "set" -> setStep(player, Arrays.copyOfRange(args, 1, args.length));
            case "view" -> viewStep(player);
            case "select" -> selectStep(player, Arrays.copyOfRange(args, 1, args.length),quest);
            default -> {
                player.sendMessage("Not known subcommand");
                return true;
            }
        }
        return true;
    }

    private void addStep(Player player, String[] args,Quest quest) {
        if (args.length < 2) {
            player.sendMessage("Usage: /step add <step name> <step type>");
            return;
        }
        String stepID = args[0];
        String stepType = args[1];
        if (quest.addStep(stepID, stepType)) {
            player.sendMessage("Step " + stepID + " added to quest " + quest.getName());
        } else {
            player.sendMessage("Problem adding step " + stepID + " to quest " + quest.getName());
        }
    }

    private void selectStep(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /step select <step name>");
            return;
        }
        String stepID = args[0];
        Step step = quest.stepByID(stepID);
        if (step == null) {
            player.sendMessage("Step " + stepID + " does not exist");
            return;
        }
        TQuests.setSelectedStep(player, step);
        player.sendMessage("Step " + stepID + " selected");
    }

    private void setStep(Player player, String[] args) {
        Step step = TQuests.getSelectedStep(player);
        if (step == null) {
            player.sendMessage("You must select a step first");
            return;
        }
        step.handleCommand(player, args);
    }

    private void viewStep(Player player) {
        Step step = TQuests.getSelectedStep(player);
        if (step == null) {
            player.sendMessage("You must select a step first");
            return;
        }
        step.viewInfo(player);
    }

    private void removeStep(Player player, String[] args,Quest quest) {
        if (args.length == 0) {
            player.sendMessage("Usage: /step remove <step name>");
            return;
        }
        String stepID = args[0];
        if (quest.stepByID(stepID) == null) {
            player.sendMessage("Step " + stepID + " does not exist in this quest");
            return;
        }
        if (quest.removeStep(stepID)) {
            player.sendMessage("Step " + stepID + " removed from quest " + quest.getName());
        } else {
            player.sendMessage("Problem removing step " + stepID + " from quest " + quest.getName());
        }
    }
}
