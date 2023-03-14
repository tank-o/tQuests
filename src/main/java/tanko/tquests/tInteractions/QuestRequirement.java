package tanko.tquests.tInteractions;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tinteractions.system.Requirement;
import tanko.tquests.TQuests;
import tanko.tquests.system.Quest;

public class QuestRequirement extends Requirement {
    Quest quest;

    public QuestRequirement(String ID) {
        super(ID);
    }

    @Override
    public void writeConfig(ConfigurationSection section) {
        section.set("quest", quest.getID());
    }

    @Override
    public void readConfig(ConfigurationSection section) {
        String questID = section.getString("quest");
        quest = TQuests.getQuestRegistry().getQuest(questID);
    }

    @Override
    public boolean checkSatisfied(Player player, NPC npc) {
        return TQuests.getQuestRegistry().playerHasCompletedQuest(quest,player);
    }

    @Override
    public void requirementSatisfiedAction(Player player, NPC npc) {

    }

    @Override
    public void handleCommand(Player player, String[] args) {

    }
}
