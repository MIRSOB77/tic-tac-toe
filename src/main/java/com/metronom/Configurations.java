package com.metronom;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.metronom.model.CPUPlayer;
import com.metronom.model.PlayGround;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.*;

@Data
public class Configurations {
    public static final String PROPERTIES_FILE = "game.properties";

    private final int mapSize;
    private final Set<Character> playerSymbols;

    private Configurations(Integer size, Set<Character> characterSymbols){
        mapSize = size;
        playerSymbols = characterSymbols;
        validate();
    }

    public static Configurations loadDefaultProps() throws IOException {
        return loadGameProperties("game.properties");
    }

    public static Configurations loadGameProperties(String path) throws IOException {
        Properties gameProps = new Properties();
        gameProps.load(Resources.getResource(path).openStream());

        String mapSizeProp = gameProps.getProperty("playground.size");

        if(StringUtils.isBlank(mapSizeProp)){
            throw new IllegalArgumentException("playground.size property missing");
        }

        String playerSymbols = gameProps.getProperty("player.symbols");

        if(StringUtils.isBlank(playerSymbols)){
            throw new IllegalArgumentException("player.symbols property missing");
        }

        Set<Character> symbolsFromFile = Sets.newHashSet();
        Splitter.on(',').split(playerSymbols).forEach(e -> {
            symbolsFromFile.add(e.charAt(0));
        });

        return new Configurations(Integer.parseInt(mapSizeProp), symbolsFromFile);

    }

    public void validate(){

        Validate.inclusiveBetween(PlayGround.MIN_SIZE, PlayGround.MAX_SIZE, mapSize);

        Validate.inclusiveBetween(1,2, playerSymbols.size());

        if(playerSymbols.contains(CPUPlayer.CPU_SYMBOL)){
            throw new IllegalArgumentException("symbol " + CPUPlayer.CPU_SYMBOL + " is reserved for CPU player!");
        }

    }



}
