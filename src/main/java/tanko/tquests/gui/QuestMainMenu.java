package tanko.tquests.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.gui.icons.Border;
import tanko.tquests.gui.icons.CompletedIcon;
import tanko.tquests.gui.icons.QuestCompletedMenuIcon;
import tanko.tquests.gui.icons.QuestProgressMenuIcon;
import tanko.tquests.system.Quest;

import java.util.List;

public class QuestMainMenu {
    protected GUI menu;
    protected String title = "Quest Menu";
    protected Player player;

    public QuestMainMenu(Player player){
        this.player = player;
        menu = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . p . c . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #")
                .addIngredient('#', Border::new)
                .addIngredient('p', new QuestProgressMenuIcon(player))
                .addIngredient('c', new QuestCompletedMenuIcon(player))
                .build();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void open() {
        if (menu == null){
            player.sendMessage("Menu not initialized, Contact an admin");
            return;
        }
        new SimpleWindow(player, title, menu).show();
    }
}
