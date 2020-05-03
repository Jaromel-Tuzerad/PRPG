package prpg.gameLogic.quests;

import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.mobs.NPC;

public class KillQuest extends Quest {

    private int killProgress;
    private int requiredKills;
    private Enemy enemyType;

    public KillQuest(String title, NPC questgiver, int requiredKills, Enemy enemyType) {
        super(QuestType.KILL, title, questgiver, enemyType.getYieldedXP()*requiredKills/2, (enemyType.getLevel()+1)*requiredKills/2, enemyType.getInventory());
        this.killProgress = 0;
        this.requiredKills = requiredKills;
        this.enemyType = enemyType;
    }

    @Override
    public boolean isFinished() {
        return this.killProgress - this.requiredKills >= 0;
    }

    @Override
    public String getDescription() {
        if(!isFinished()) {
            return "You have killed " + this.killProgress + "/" + this.requiredKills + " of " + this.enemyType.getDisplayName();
        } else {
            return "You have killed all " + this.killProgress + " of " + this.enemyType.getDisplayName() + "!" +
                    "\nGo get your reward from " + this.questgiver.getName() + "!";
        }
    }

    @Override
    public String getNPCText() {
        return "Hi, I would like you to do something for me." +
                "\nPlease, kill " + this.requiredKills + " " + this.enemyType.getName() +
                "\nand then come back to me for the reward. Thx!";
    }

    public void incrementKillProgress() {
        this.killProgress++;
    }

    public int getKillProgress() {
        return this.killProgress;
    }

    public int getRequiredKills() {
        return this.requiredKills;
    }

    public Enemy getEnemyType() {
        return this.enemyType;
    }

}
