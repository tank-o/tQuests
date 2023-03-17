package tanko.tquests.tInteractions;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tinteractions.system.Interaction;
import tanko.tquests.QuestRegistry;
import tanko.tquests.TQuests;
import tanko.tquests.system.Quest;

public class QuestInteraction extends Interaction {
    Quest quest;

    public QuestInteraction(String ID) {
        super(ID);
    }

    @Override
    public void action(Player player, NPC npc) {
        QuestRegistry questRegistry = TQuests.getQuestRegistry();
        if (questRegistry.playerHasQuest(quest,player)) {
            if (quest.playerFinishedSteps(player)) {
                quest.completeQuest(player);
            }
        } else {
            quest.accept(player);
        }
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String var = args[0];
        switch (var){
            case "quest":
                String questID = args[1];
                quest = TQuests.getQuestRegistry().getQuest(questID);
                if (quest == null){
                    player.sendMessage("Quest not found - check spelling and capitalization");
                }
                break;
        }
    }

    @Override
    public boolean completeChecks(Player player, NPC npc) {
        return TQuests.getQuestRegistry().playerHasCompletedQuest(quest,player);
    }

    @Override
    public void readConfig(ConfigurationSection section) {
        String questID = section.getString("quest");
        quest = TQuests.getQuestRegistry().getQuest(questID);
    }

    @Override
    public void writeConfig(ConfigurationSection section) {
        section.set("quest", quest.getID());
    }
}
