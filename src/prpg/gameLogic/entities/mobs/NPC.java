package prpg.gameLogic.entities.mobs;

import prpg.gameLogic.quests.Quest;

public class NPC extends Mob {

    private Quest activeQuest;

    public NPC(String name, int level) {
        super(name, name + " is standing here", 1, level, 0, 0, 0);
        this.activeQuest = null;
    }

    @Override
    public String getDisplayName() {
        return "[N] " + this.name;
    }

    @Override
    public String getActionName() {
        return "Talk";
    }

    public Quest getActiveQuest() {
        return this.activeQuest;
    }

    public void endQuest() {
        this.activeQuest = null;
        this.level++;
    }

    public void startQuest(Quest quest) {
        this.activeQuest = quest;
    }
}