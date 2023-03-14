package tanko.tquests.gui.icons;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import tanko.tquests.TQuests;
import tanko.tquests.gui.InProgressMenu;

public class QuestProgressMenuIcon extends BaseItem {
    Player player;

    public QuestProgressMenuIcon(Player player) {
        this.player = player;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
        int questsInProgress = TQuests.getQuestRegistry().getInProgressQuests(player).size();
        builder.setDisplayName(ChatColor.YELLOW + "Quests In Progress");
        builder.addLoreLines("");
        if (questsInProgress == 0) {
            builder.addLoreLines(ChatColor.RED + "No quests in progress");
        } else {
            builder.addLoreLines(ChatColor.GRAY + "Amount: " + ChatColor.WHITE + questsInProgress);
        }
        builder.addLoreLines("");
        builder.addLoreLines(ChatColor.YELLOW + "Click to view quests in progress!");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        new InProgressMenu(player).open();
    }
}
