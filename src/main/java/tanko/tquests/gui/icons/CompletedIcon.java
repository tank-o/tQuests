package tanko.tquests.gui.icons;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import tanko.tquests.system.Quest;

import java.util.Arrays;

public class CompletedIcon extends BaseItem {
    Quest quest;

    public CompletedIcon(Quest quest) {
        this.quest = quest;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK);
        builder.setDisplayName(ChatColor.YELLOW + quest.getName());
        builder.addLoreLines(ChatColor.GRAY + quest.getDescription());
        builder.addLoreLines("");
        builder.addLoreLines(ChatColor.YELLOW + "" + ChatColor.BOLD + "COMPLETED");
        builder.setItemFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS));
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

    }
}
