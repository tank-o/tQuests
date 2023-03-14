package tanko.tquests.rewards;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tanko.tquests.system.Reward;

public class ItemReward extends Reward {
    ItemStack item = null;
    int amount = 0;

    public ItemReward(String ID) {
        super(ID);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void give(Player player) {
        if (item == null || amount == 0) return;
        // Loop the number of times in case its an item that can't stack
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        item = section.getItemStack("item");
        amount = section.getInt("amount");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("item", item);
        section.set("amount", amount);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /reward set <var> <value>");
            return;
        }
        String var = args[0];
        switch (var) {
            case "item" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType().equals(Material.AIR)) {
                    player.sendMessage("You must be holding a block in your main hand");
                    return;
                }
                item.setAmount(1);
                setItem(item);
                player.sendMessage("Item set to " + item.getType().toString());
            }
            case "amount" -> {
                if (args.length < 2) {
                    player.sendMessage("Usage: /step set amount <value>");
                    return;
                }
                try {
                    int amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        player.sendMessage("§cYou can't set the amount to be less than 1");
                        return;
                    }
                    setAmount(amount);
                    player.sendMessage("Amount set to " + amount);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cError setting amount, most likely not a number");
                }
            }
        }
    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("Item: " + item.getType().toString());
        player.sendMessage("Amount: " + amount);
    }
}
