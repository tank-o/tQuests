package tanko.tquests.gui.icons;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import de.studiocode.invui.item.impl.SimpleItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import tanko.tquests.system.Quest;

import java.util.Arrays;

public class QuestIcon extends BaseItem {
    Player player;
    Quest quest;

    public QuestIcon(Player player, Quest quest) {
        this.player = player;
        this.quest = quest;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
        builder.setDisplayName(ChatColor.YELLOW + quest.getName());
        builder.addLoreLines(ChatColor.GRAY + quest.getDescription());
        builder.addLoreLines("");
        int stepsCompleted = quest.getCurrentStepNumber(player);
        builder.addLoreLines(ChatColor.BLUE + "Steps: " + ChatColor.WHITE + stepsCompleted + "/" + quest.getSteps().size());
        builder.addLoreLines("");
        builder.addLoreLines(ChatColor.YELLOW + "Click to view quest details");
        builder.setItemFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS));
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

    }
}
