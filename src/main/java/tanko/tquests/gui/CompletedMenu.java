package tanko.tquests.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.gui.icons.Border;
import tanko.tquests.gui.icons.CompletedIcon;
import tanko.tquests.gui.icons.QuestIcon;
import tanko.tquests.system.Quest;

import java.util.List;

public class CompletedMenu {
    protected GUI menu;
    protected String title = "Completed Quests";
    protected Player player;

    public CompletedMenu(Player player){
        this.player = player;
        menu = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #")
                .addIngredient('#', Border::new)
                .build();

        List<String> questList = TQuests.getQuestRegistry().getCompletedQuests(player);
        for (String questID : questList){
            Quest quest = TQuests.getQuestRegistry().getQuest(questID);
            menu.addItems(new CompletedIcon(quest));
        }
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
