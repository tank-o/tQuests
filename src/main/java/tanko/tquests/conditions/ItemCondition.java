package tanko.tquests.conditions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tanko.tquests.system.Condition;

public class ItemCondition extends Condition {
    private ItemStack item;
    private int amount;
    private boolean take;

    public ItemCondition(String ID) {
        super(ID);
    }

    @Override
    public boolean satisfied(Player player) {
        if (player.getInventory().containsAtLeast(item,amount)){
            if (take){
                for (int i = 0; i < amount; i++) {
                    player.getInventory().removeItem(item);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String var = args[0];
        switch (var){
            case "item":
                if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
                    player.sendMessage("You must be holding an item");
                    return;
                }
                this.item = player.getInventory().getItemInMainHand();
                this.item.setAmount(1);
                player.sendMessage("Item set to " + item.getType());
                break;
            case "amount":
                try {
                    amount = Integer.parseInt(args[1]);
                    if (amount < 1){
                        player.sendMessage("Amount must be greater than 0");
                    }
                } catch (NumberFormatException e){
                    player.sendMessage("Invalid number");
                }
                break;
            case "take":
                String takeString = args[1];
                if (takeString.equalsIgnoreCase("true")){
                    take = true;
                } else if (takeString.equalsIgnoreCase("false")){
                    take = false;
                } else {
                    player.sendMessage("Invalid boolean - expected true or false");
                }
                break;
            default:
                player.sendMessage("Invalid variable - expected item, amount or take");
                break;
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        item = section.getItemStack("item");
        amount = section.getInt("amount");
        take = section.getBoolean("take");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("item",item);
        section.set("amount",amount);
        section.set("take",take);
    }
}
