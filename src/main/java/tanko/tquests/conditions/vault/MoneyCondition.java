package tanko.tquests.conditions.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.system.Condition;

public class MoneyCondition extends Condition {
    private double amount;

    public MoneyCondition(String ID) {
        super(ID);
    }

    @Override
    public boolean satisfied(Player player) {
        try {
            Economy economy = TQuests.getEconomy();
            return economy.getBalance(player) >= amount;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean action(Player player) {
        try {
            Economy economy = TQuests.getEconomy();
            economy.withdrawPlayer(player, amount);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void handleCommand(Player player, String[] args) {

    }

    @Override
    public void load(ConfigurationSection section) {

    }

    @Override
    public void save(ConfigurationSection section) {

    }
}
