package exceptions;

import gameLogic.entities.mobs.Mob;

public class MobDiedException extends Exception {

    private Mob deadMob;

    public MobDiedException(Mob mob) {
        super(mob.getDisplayName() + " is dead");
        this.deadMob = mob;
    }

}
