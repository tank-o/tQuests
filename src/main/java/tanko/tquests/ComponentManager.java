package tanko.tquests;

import tanko.tquests.conditions.vault.MoneyCondition;
import tanko.tquests.rewards.vault.MoneyReward;
import tanko.tquests.steps.citizens.GiveItemStep;
import tanko.tquests.steps.citizens.NPCTalkStep;
import tanko.tquests.conditions.ItemCondition;
import tanko.tquests.conditions.QuestCondition;
import tanko.tquests.rewards.ExperienceReward;
import tanko.tquests.rewards.ItemReward;
import tanko.tquests.steps.BreakBlockStep;
import tanko.tquests.steps.ObtainItemStep;
import tanko.tquests.system.Condition;
import tanko.tquests.system.Reward;
import tanko.tquests.system.Step;

import java.util.HashMap;
import java.util.Map;

public final class ComponentManager {
    private final Map<String,Class<? extends Step>> stepRegistry = new HashMap<>();
    private final Map<String,Class<? extends Reward>> rewardRegistry = new HashMap<>();
    private final Map<String,Class<? extends Condition>> conditionRegistry = new HashMap<>();

    public ComponentManager(){
        TQuests.getInstance().getLogger().info("Registering Default Components");
        rewardRegistry.put("item", ItemReward.class);
        rewardRegistry.put("xp", ExperienceReward.class);

        stepRegistry.put("block", BreakBlockStep.class);
        stepRegistry.put("obtainItem", ObtainItemStep.class);

        conditionRegistry.put("item", ItemCondition.class);
        conditionRegistry.put("quest", QuestCondition.class);
    }

    public void registerCitizensSteps(){
        TQuests.getInstance().getLogger().info("Registering Citizens Components");
        stepRegistry.put("giveItem", GiveItemStep.class);
        stepRegistry.put("talk", NPCTalkStep.class);
    }

    public void registerVaultSteps(){
        TQuests.getInstance().getLogger().info("Registering Vault Components");
        conditionRegistry.put("money", MoneyCondition.class);
        rewardRegistry.put("money", MoneyReward.class);
    }

    public void registerMythicSteps() {
        TQuests.getInstance().getLogger().info("Registering Mythic Components");
    }

    public void registerCondition(String ID, Class<? extends Condition> condition){
        conditionRegistry.put(ID,condition);
    }

    public void registerStep(String ID, Class<? extends Step> step){
        stepRegistry.put(ID,step);
    }

    public void registerReward(String ID, Class<? extends Reward> reward){
        rewardRegistry.put(ID,reward);
    }

    public Class<? extends Step> getStep(String ID){
        return stepRegistry.get(ID);
    }

    public Class<? extends Reward> getReward(String ID){
        return rewardRegistry.get(ID);
    }

    public Class<? extends Condition> getCondition(String ID){
        return conditionRegistry.get(ID);
    }

    public  String getStepID(Step step){
        for (Map.Entry<String, Class<? extends Step>> entry : stepRegistry.entrySet()) {
            if (entry.getValue().equals(step.getClass())){
                return entry.getKey();
            }
        }
        return null;
    }

    public String getRewardID(Reward reward){
        for (Map.Entry<String, Class<? extends Reward>> entry : rewardRegistry.entrySet()) {
            if (entry.getValue().equals(reward.getClass())){
                return entry.getKey();
            }
        }
        return null;
    }

    public String getConditionID(Condition condition){
        for (Map.Entry<String, Class<? extends Condition>> entry : conditionRegistry.entrySet()) {
            if (entry.getValue().equals(condition.getClass())){
                return entry.getKey();
            }
        }
        return null;
    }
}
