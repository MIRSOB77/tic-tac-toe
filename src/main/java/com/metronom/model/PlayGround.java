package com.metronom.model;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;

import javax.swing.plaf.OptionPaneUI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PlayGround {
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 10;


    private static PlayGround _instance = null;
    private final long dimension;

    private Set<PlayGroundField> fields = Sets.newHashSet();

    public PlayGround(final long mapSize) {
        Validate.inclusiveBetween(MIN_SIZE, MAX_SIZE, mapSize);

        this.dimension = mapSize;

        // create all fields beginning at 1,1  mapSize,mapSize
        LongStream.range(1, dimension+1).forEach(y -> {
            LongStream.range(1, dimension+1).forEach(x -> fields.add(new PlayGroundField(new Position(x, y))));
        });

        _instance = this;
    }

    public boolean executeTurn(final Turn turn) {
        Optional<PlayGroundField> field = findFieldByPosition(turn.position());

        boolean nextTurn = true;

        if (!field.isPresent()) {
            System.out.println("feld ist nicht gültig");
            nextTurn = false;
        } else if (!field.get().capture(turn.player())) {
            System.out.println("feld ist bereits belegt");
            nextTurn = false;
        }

        return nextTurn;
    }

    public boolean checkWinningCase(final Player player) {
        List<PlayGroundField> playerFields = fields.stream().filter(e -> e.owner() == player).collect(Collectors.toList());

        return playerFields.stream().anyMatch(e -> {
            return checkRowLine(e.position().y(), player,  playerFields) || checkColumnLine(e.position().x(), player, playerFields) || checkDiagonalLine(e, playerFields);
        });
    }


    /**
     * print all display objects to sysout
     */
    public void draw() {
        LongStream.range(1, dimension+1).forEach(y -> {
            System.out.println();
            LongStream.range(1, dimension+1).forEach(x -> {

                findFieldByXY(x, y).ifPresent(f -> f.draw());

            });
        });
        System.out.println();
    }

    public Set<PlayGroundField> getFields() {
        return Collections.unmodifiableSet(_instance.fields);
    }

    public static List<PlayGroundField> getNotTakenFields() {
        return Collections.unmodifiableList(_instance.fields.stream().filter(e -> e.owner() == null).collect(Collectors.toList()));
    }

    private boolean checkRowLine(long y, Player player, List<PlayGroundField> otherPlayerFields){
        long totalRowCount = otherPlayerFields.stream().filter(e -> e.position().y() == y && e.owner() == player).count();

        return totalRowCount == dimension;

    }

    private boolean checkColumnLine(long x, Player player, List<PlayGroundField> otherPlayerFields){
        long totalColCount = otherPlayerFields.stream().filter(e -> e.position().x() == x && e.owner() == player).count();

        return totalColCount == dimension;
    }

    private boolean checkDiagonalLine(PlayGroundField origin, List<PlayGroundField> otherPlayerFields){
        boolean diagonalConditionFullfilled = false;

        // top left corner
        Optional<PlayGroundField> topLeft = otherPlayerFields.stream().filter(f -> f.position().x() == 1 && f.position().y() == 1).findFirst();

        if(topLeft.isPresent() && topLeft.get().owner() == origin.owner()) {
            diagonalConditionFullfilled = hasPlayerAllDiagonalFields(topLeft.get(), 1, 1, otherPlayerFields);
        }

        // top right corner
        Optional<PlayGroundField> topRight = otherPlayerFields.stream().filter(f -> f.position().x() == dimension && f.position().y() == 1).findFirst();

        if(topRight.isPresent() && topRight.get().owner() == origin.owner()) {
            diagonalConditionFullfilled = hasPlayerAllDiagonalFields(topRight.get(), -1, 1, otherPlayerFields);
        }

        // bottom left corner
        Optional<PlayGroundField> bottomLeft = otherPlayerFields.stream().filter(f -> f.position().x() == 1 && f.position().y() == dimension).findFirst();

        if(bottomLeft.isPresent() && bottomLeft.get().owner() == origin.owner()) {
            diagonalConditionFullfilled = hasPlayerAllDiagonalFields(bottomLeft.get(), 1, -1, otherPlayerFields);
        }


        // bottom right corner
        Optional<PlayGroundField> bottomRight = otherPlayerFields.stream().filter(f -> f.position().x() == dimension && f.position().y() == dimension).findFirst();

        if(bottomRight.isPresent() && bottomRight.get().owner() == origin.owner()) {
            diagonalConditionFullfilled = hasPlayerAllDiagonalFields(bottomRight.get(), -1, -1, otherPlayerFields);
        }


        return diagonalConditionFullfilled;
    }

    private boolean hasPlayerAllDiagonalFields(PlayGroundField origin, int offset_x, int offset_y, List<PlayGroundField> otherPlayerFields){

        for(long from_x = origin.position().x(),from_y = origin.position().y(); from_x >= 1 && from_x <= dimension; from_x += offset_x, from_y += offset_y) {
                PlayGroundField field = findFieldByXY(from_x, from_y).orElseThrow(() -> new IllegalStateException("out of bounds"));
                if (!otherPlayerFields.contains(field)) {
                    return false;
                }
        }

        return true;
    }

    private Optional<PlayGroundField> findFieldByXY(long x, long y){
        return fields.stream().filter(f -> f.position().equals(new Position(x, y))).findFirst();
    }

    private Optional<PlayGroundField> findFieldByPosition(Position position){
        return fields.stream().filter(f -> f.position().equals(position)).findFirst();
    }

}
