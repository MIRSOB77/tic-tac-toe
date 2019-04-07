package com.metronom.model;

import com.google.common.base.Splitter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Turn {
    private final Player player;
    private Position position;

    public static Turn parseCmdLineInput(String position, Player player){
        Validate.notNull(player);

        Turn turn = new Turn(player);

        List<Long> turnPos = Splitter.on(",").splitToList(position).stream().map(Long::new).collect(Collectors.toList());
        if(turnPos.size() != 2) {
            return null;
        }
        turn.position = new Position(turnPos.get(0), turnPos.get(1));

        return turn;
    }
}
