package com.metronom.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void playTurn() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_create_wrongSymbol() {
        Player.create(1l, ' ');
    }


    public void create_success() {
        Player.create(1l, 'A');
    }

    public void test_create_failed_id_required(){
        Player.create(-1l, 'p');
    }
}