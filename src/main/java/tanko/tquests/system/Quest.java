package tanko.tquests.system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import tanko.tquests.TQuests;

import java.util.*;

public class Quest {
    protected String ID;
    protected String name;
    protected String description;
    protected List<Step> steps = new ArrayList<>();
    protected List<Reward> rewards = new ArrayList<>();
    protected List<Condition> conditions = new ArrayList<>();
    protected Map<UUID,Integer> stepProgress = new HashMap<>();

    // This is the constructor that is used when creating a new quest
    public Quest(String ID){
        this.ID = ID;
    }

    // This is the constructor that is used when loading a quest from file
    public Quest(String ID,
                 String name,
                 String description,
                 List<Reward> rewards,
                 Map<UUID,Integer> stepProgress) {

        this.ID = ID;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.stepProgress = stepProgress;
    }

    // Getters and Setters

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Map<UUID, Integer> getStepProgress() {
        return stepProgress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Quest management

    public void accept(Player player){
        // Add to local quest data
        stepProgress.put(player.getUniqueId(), 0);
        Step step = getCurrentStep(player);
        step.addPlayer(player);
    }

    public void completeQuest(Player player){
        giveRewards(player);
        stepProgress.remove(player.getUniqueId());
        // Let the registry know that the quest is completed
        TQuests.getQuestRegistry().completeQuest(this, player.getUniqueId());
    }

    public void resetQuest(Player player){
        if (!stepProgress.containsKey(player.getUniqueId())) return;
        stepProgress.remove(player.getUniqueId());
    }

    // Step management

    public Step getCurrentStep(Player player){
        int step = stepProgress.getOrDefault(player.getUniqueId(), 0);
        return steps.get(step);
    }

    public int getCurrentStepNumber(Player player){
        return stepProgress.getOrDefault(player.getUniqueId(), 0);
    }

    public void nextStep(Player player){
        int step = stepProgress.getOrDefault(player.getUniqueId(), 0);
        Step currentStep = steps.get(step);
        currentStep.removePlayer(player);
        if (step == steps.size() - 1){
            completeQuest(player);
        } else {
            stepProgress.put(player.getUniqueId(), step + 1);
            Step nextStep = steps.get(step + 1);
            nextStep.addPlayer(player);
        }
    }

    public Step stepByID(String ID){
        return steps.stream().filter(step -> step.getID().equals(ID)).findFirst().orElse(null);
    }

    public boolean addStep(String ID, String type){
        Class<? extends Step> stepClass = TQuests.getComponentManager().getStep(type);
        if (stepClass == null) return false;
        try {
            Step step = stepClass.getConstructor(String.class,Quest.class).newInstance(ID,this);
            steps.add(step);
            Bukkit.getPluginManager().registerEvents(step, TQuests.getInstance());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addStep(Step step){
        steps.add(step);
        Bukkit.getPluginManager().registerEvents(step, TQuests.getInstance());
    }

    public boolean removeStep(String ID){
        try {
            Step step = steps.stream().filter(s -> s.getID().equals(ID)).findFirst().orElse(null);
            if (step == null) return false;
            steps.remove(step);
            // Get the handler list for the step and unregister it
            HandlerList.unregisterAll(step);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean playerFinishedSteps(Player player){
        return getCurrentStepNumber(player) == steps.size() - 1;
    }

    // Reward management

    public void giveRewards(Player player){
        for (Reward reward : rewards) {
            reward.give(player);
        }
    }

    public boolean addReward(String ID, String type){
        Class<? extends Reward> rewardClass = TQuests.getComponentManager().getReward(type);
        if (rewardClass == null) return false;
        try {
            Reward reward = rewardClass.getConstructor(String.class).newInstance(ID);
            rewards.add(reward);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeReward(String ID){
        try {
            Reward reward = rewards.stream().filter(r -> r.getID().equals(ID)).findFirst().orElse(null);
            if (reward == null) return false;
            rewards.remove(reward);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Reward rewardByID(String ID){
        return rewards.stream().filter(reward -> reward.getID().equals(ID)).findFirst().orElse(null);
    }

    // Condition management

    public boolean addCondition(String ID, String type){
        Class<? extends Condition> conditionClass = TQuests.getComponentManager().getCondition(type);
        if (conditionClass == null) return false;
        try {
            Condition condition = conditionClass.getConstructor(String.class).newInstance(ID);
            conditions.add(condition);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCondition(String ID){
        try {
            Condition condition = conditions.stream().filter(c -> c.getID().equals(ID)).findFirst().orElse(null);
            if (condition == null) return false;
            conditions.remove(condition);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Condition conditionByID(String ID){
        return conditions.stream().filter(condition -> condition.getID().equals(ID)).findFirst().orElse(null);
    }

    public boolean checkConditions(Player player){
        for (Condition condition : conditions) {
            if (!condition.satisfied(player)) return false;
        }
        return true;
    }
}
