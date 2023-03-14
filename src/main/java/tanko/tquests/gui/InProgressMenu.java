package tanko.tquests.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.citizensnpcs.trait.shop.NPCShopAction;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.gui.icons.Border;
import tanko.tquests.gui.icons.QuestIcon;
import tanko.tquests.system.Quest;
import java.util.List;

public class InProgressMenu {
    protected GUI menu;
    protected String title = "GUI";
    protected Player player;

    public InProgressMenu(Player player){
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

        List<String> questList = TQuests.getQuestRegistry().getInProgressQuests(player);
        for (String questID : questList){
            Quest quest = TQuests.getQuestRegistry().getQuest(questID);
            menu.addItems(new QuestIcon(player,quest));
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
