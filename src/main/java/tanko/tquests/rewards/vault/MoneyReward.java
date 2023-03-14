package tanko.tquests.rewards.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.system.Reward;

public class MoneyReward extends Reward {
    private double amount;

    public MoneyReward(String ID) {
        super(ID);
    }

    @Override
    public void give(Player player) {
        try {
            Economy economy = TQuests.getEconomy();
            economy.depositPlayer(player, amount);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        amount = section.getDouble("amount");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("amount",amount);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String var = args[0];
        switch (var){
            case "amount":
                try {
                    amount = Double.parseDouble(args[1]);
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
