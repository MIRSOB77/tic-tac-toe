package com.metronom.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.metronom.Configurations;
import lombok.Data;

import java.util.Random;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;
import java.util.stream.LongStream;

@Data
public class Controller implements Runnable{
    private final Set<Player> inGamePlayers;

    private PlayGround playGround;

    // used for testing
    private boolean gameFinished;

    private Player winner = null;

    public Controller(Configurations configurations){
        configurations.validate();

        Iterator<Character> smybols = configurations.getPlayerSymbols().iterator();

        Set<Player> players = Sets.<Player>newHashSet();

        // create human players
        LongStream.range(1, configurations.getPlayerSymbols().size()
                +1).forEach(e -> players.add(Player.create(e, smybols.next())));

        // create cpu player
        long nextPlayer = configurations.getPlayerSymbols().size()+1;
        players.add(new CPUPlayer(nextPlayer));

        playGround = new PlayGround(configurations.getMapSize());
        inGamePlayers = Collections.unmodifiableSet(players);

    }

    public Controller(Configurations configurations, PlayGround playGround, boolean withoutCPUPlayer){
        configurations.validate();

        Iterator<Character> symbols = configurations.getPlayerSymbols().iterator();

        Set<Player> players = Sets.<Player>newHashSet();

        // create human players
        LongStream.range(1, configurations.getPlayerSymbols().size()
                +1).forEach(e -> players.add(Player.create(e, symbols.next())));

        // create cpu player

        if(!withoutCPUPlayer){
            long nextPlayer = configurations.getPlayerSymbols().size()+1;
            players.add(new CPUPlayer(nextPlayer));
        }

        this.playGround = playGround;
        inGamePlayers = Collections.unmodifiableSet(players);
    }



    private Set<Player> initPlayersOrder(){
        int beginner = new Random().nextInt(inGamePlayers.size());

        Set<Player> gameOrder = Sets.newHashSet(Lists.newArrayList(inGamePlayers).get(beginner));

        gameOrder.addAll(inGamePlayers);

        return gameOrder;
    }

    @Override
    public void run() {
        gameFinished = false;
        winner = null;

        Set<Player> playersGameOrder = initPlayersOrder();
        Iterator<Player> playerIterator = playersGameOrder.iterator();

        // current player
        Player onTurn = null;

        //indicates if turn was accepted on playground
        boolean lastTurnOk = true;

        do {
            if(!playerIterator.hasNext()){
                playerIterator = playersGameOrder.iterator();
            }

            playGround.draw();

            if(lastTurnOk){
                onTurn = playerIterator.next();
                lastTurnOk = false;
            }

            System.out.println();
            Turn turn = onTurn.playTurn();


            if(turn != null){
                lastTurnOk = playGround.executeTurn(turn);
            }

            // check if finishing conditions matches
            if(playGround.checkWinningCase(onTurn)){
                winner = onTurn;
                gameFinished = true;
            } else if(PlayGround.getNotTakenFields().size()==0){
                gameFinished=true;
            }


        } while(!gameFinished);

        playGround.draw();

        if(winner != null){
            System.out.println("Player" + winner.getId() + " wins");
        } else {
            System.out.println("Drawn!");

        }

    }
}
