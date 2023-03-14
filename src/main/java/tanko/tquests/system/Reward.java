package tanko.tquests.system;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Reward {
    protected String ID;
    protected String name;

    public Reward(String ID){
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void give(Player player);

    public abstract void load(ConfigurationSection section);

    public abstract void save(ConfigurationSection section);

    public abstract void handleCommand(Player player, String[] args);

    public abstract void viewInfo(Player player);
}
