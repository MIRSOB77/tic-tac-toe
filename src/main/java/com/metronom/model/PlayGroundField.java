package com.metronom.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class PlayGroundField {
    private final Position position;
    private Player owner;

    public boolean capture(Player player){
        if(owner != null){
            return false;
        }

        owner = player;

        return true;
    }

    public void draw(){
        System.out.print(owner != null ? owner.getSymbol() + " | " : "  | ");
    }
}
