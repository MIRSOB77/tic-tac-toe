package com.metronom.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class TurnTest {

    @Test(expected = NumberFormatException.class)
    public void test_parseCmdLineInput_WrongFormat() {
        Turn.parseCmdLineInput("1,a", Player.create(1l , 'A'));
    }

    @Test(expected = NullPointerException.class)
    public void test_parseCmdLineInput_PlayerInvalid() {
        Turn.parseCmdLineInput("1,3", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_parseCmdLineInput_NotSpace() {
        Turn.parseCmdLineInput("1,1", Player.create(2l, ' '));
    }

    @Test
    public void test_parseCmdLineInput_Ok() {
        Turn.parseCmdLineInput("1,1", Player.create(2l, 'o'));
    }


}