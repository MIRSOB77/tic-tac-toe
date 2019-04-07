package com.metronom.model;

import java.util.List;
import java.util.Random;

public class CPUPlayer extends Player {
    public static final Character CPU_SYMBOL = 'C';

    public CPUPlayer(long order) {
        super(order, CPU_SYMBOL);
    }

    public Turn playTurn(){
        //TODO improve KI of cpu ;)
        List<PlayGroundField> freeFields = PlayGround.getNotTakenFields();

        int idx = new Random().nextInt(freeFields.size());

        PlayGroundField selected = freeFields.get(idx);
        Turn myTurn = new Turn(this);
        myTurn.position(selected.position());

        return myTurn;
    }
}
