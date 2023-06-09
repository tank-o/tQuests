package tanko.tquests.rewards;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.system.Reward;

public class ExperienceReward extends Reward {
    int amount = 0;

    public ExperienceReward(String ID) {
        super(ID);
    }

    @Override
    public void give(Player player) {
        player.giveExp(amount);
    }

    @Override
    public void load(ConfigurationSection section) {
        amount = section.getInt("amount");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("amount", amount);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String var = args[0];
        switch (var){
            case "amount":
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    player.sendMessage("Invalid number");
                }
                break;
        }
    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("Amount: " + amount);
    }
}
