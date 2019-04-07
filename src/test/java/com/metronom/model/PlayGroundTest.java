package com.metronom.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

public class PlayGroundTest {

    private PlayGround playground;


    @Before
    public void setup(){
        playground = new PlayGround(5);
    }

    @Test
    public void test_executeTurn_OuterBounds() {
        Assert.assertFalse(playground.executeTurn(Turn.parseCmdLineInput("10,3", new Player(4l, 'x'))));
    }

    @Test
    public void test_executeTurn_FieldTaken() {
        playground.executeTurn(Turn.parseCmdLineInput("1,3", new Player(1l, 'x')));
        Assert.assertFalse(playground.executeTurn(Turn.parseCmdLineInput("1,3", new Player(4l, 'o'))));
    }


}