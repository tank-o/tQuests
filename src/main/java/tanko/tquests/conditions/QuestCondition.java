package tanko.tquests.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.TQuests;
import tanko.tquests.system.Condition;
import tanko.tquests.system.Quest;

public class QuestCondition extends Condition {
    private Quest quest;

    public QuestCondition(String ID) {
        super(ID);
    }

    @Override
    public boolean satisfied(Player player) {
        return TQuests.getQuestRegistry().playerHasCompletedQuest(quest,player);
    }

    @Override
    public boolean action(Player player) {
        return true;
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
    public void load(ConfigurationSection section) {
        section.set("quest",quest.getID());
    }

    @Override
    public void save(ConfigurationSection section) {
        String questID = section.getString("quest");
        quest = TQuests.getQuestRegistry().getQuest(questID);
    }
}
