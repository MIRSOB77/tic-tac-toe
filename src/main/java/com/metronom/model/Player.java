package com.metronom.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class Player {
    private final long id;
    private final Character symbol;

    protected Player(long id, Character symbol){
        this.id = id;
        this.symbol = symbol;
    }

    public Turn playTurn(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Player " + id + " enter field(x,y):");
        try {
            String x_y = br.readLine();

            if(x_y != null && x_y.matches("[0-9]{1,2},[0-9]{1,2}")) {
                return Turn.parseCmdLineInput(x_y, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Player create(long id, Character symbol){
        Validate.isTrue(id > 0);
        Validate.notNull(symbol);
        Validate.isTrue(!Character.isSpaceChar(symbol));
        Validate.isTrue(symbol != CPUPlayer.CPU_SYMBOL);

        Player newP = new Player(id, symbol);

        return  newP;
    }
}
