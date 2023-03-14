package tanko.tquests.system;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public abstract class Condition {
    protected String ID;

    public Condition(String ID){
        this.ID = ID;
    }

    public abstract boolean satisfied(Player player);
    public abstract void handleCommand(Player player, String[] args);
    public abstract void load(ConfigurationSection section);
    public abstract void save(ConfigurationSection section);

    public String getID() {
        return ID;
    }
}
