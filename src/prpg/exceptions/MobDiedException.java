package prpg.exceptions;

import prpg.gameLogic.entities.mobs.Mob;

public class MobDiedException extends Exception {

    public MobDiedException(Mob mob) {
        super(mob.getDisplayName() + " is dead");
    }

}
