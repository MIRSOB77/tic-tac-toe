package com.metronom.model;

import com.metronom.Configurations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    PlayGround playGround;

    Controller controller;

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private PlayGround playGroundSpy;

    ExecutorService executorService = null;

    @Before
    public void setUpOutput() throws IOException {
        Configurations configurations = Configurations.loadGameProperties("configuration/game.properties");

        playGround = new PlayGround(configurations.getMapSize());
        playGroundSpy = Mockito.spy(playGround);

        controller = new Controller(configurations, playGroundSpy, false);
        MockitoAnnotations.initMocks(this);

        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }


    @Test
    public void testCreate() throws IOException {
        Configurations configurations = Configurations.loadDefaultProps();
        controller = new Controller(configurations, playGround, false);
        assertEquals(3, controller.getInGamePlayers().size());
        assertEquals(25, controller.getPlayGround().getFields().size());
    }

    @Test
    public void testControllerStart() throws Exception {

        provideInput("1,2");

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(controller);

        Thread.sleep(1500);
        controller.setGameFinished(true);

        Mockito.verify(playGroundSpy, Mockito.atLeastOnce()).executeTurn(Mockito.any(Turn.class));
        assertNotEquals(25, PlayGround.getNotTakenFields().size());
    }

    @Test
    public void testControllerStart_OutOfBounds() throws Exception {

        provideInput("6,5");


        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(controller);

        Thread.sleep(1500);
        controller.setGameFinished(true);

        Mockito.verify(playGroundSpy, atLeastOnce()).executeTurn(Mockito.any(Turn.class));
        assertEquals(25, PlayGround.getNotTakenFields().size());
    }

    @Test
    public void testControllerStart_WithWinner_ByVerticalLine() throws Exception {
        Configurations configurations = Configurations.loadGameProperties("configuration/game_with_winner.properties");

        playGround = new PlayGround(configurations.getMapSize());
        playGroundSpy = Mockito.spy(playGround);

        controller = new Controller(configurations, playGroundSpy, true);
        provideInput("1,1");

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(controller);

        Thread.sleep(1000);
        provideInput("2,2");
        Thread.sleep(500);
        provideInput("1,2");
        Thread.sleep(1000);
        provideInput("3,3");
        Thread.sleep( 500);
        provideInput("1,3");
        Thread.sleep(500);



        Mockito.verify(playGroundSpy, times(5)).executeTurn(Mockito.any(Turn.class));
        int freeCount = PlayGround.getNotTakenFields().size();

        assertTrue(freeCount <= 4);
        assertNotNull(controller.getWinner());
    }

    @Test
    public void testControllerStart_WithWinner_ByHorizontalLine() throws Exception {
        Configurations configurations = Configurations.loadGameProperties("configuration/game_with_winner.properties");

        playGround = new PlayGround(configurations.getMapSize());
        playGroundSpy = Mockito.spy(playGround);

        controller = new Controller(configurations, playGroundSpy, true);
        provideInput("1,1");

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(controller);

        Thread.sleep(1000);
        provideInput("2,2");
        Thread.sleep(500);
        provideInput("2,1");
        Thread.sleep(1000);
        provideInput("3,3");
        Thread.sleep(500);
        provideInput("3,1");
        Thread.sleep(500);



        Mockito.verify(playGroundSpy, atLeast(5)).executeTurn(Mockito.any(Turn.class));
        int freeCount = PlayGround.getNotTakenFields().size();

        assertTrue(freeCount <= 4);
        assertNotNull(controller.getWinner());
    }

    @Test
    public void testControllerStart_WithWinner_ByDiagonalLine() throws Exception {
        Configurations configurations = Configurations.loadGameProperties("configuration/game_with_winner.properties");

        playGround = new PlayGround(configurations.getMapSize());
        playGroundSpy = Mockito.spy(playGround);

        controller = new Controller(configurations, playGroundSpy, true);
        provideInput("1,1");

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(controller);

        Thread.sleep(1000);
        provideInput("2,1");
        Thread.sleep(500);
        provideInput("2,2");
        Thread.sleep(1000);
        provideInput("3,1");
        Thread.sleep(500);
        provideInput("3,3");
        Thread.sleep(500);



        Mockito.verify(playGroundSpy, times(5)).executeTurn(Mockito.any(Turn.class));
        int freeCount = PlayGround.getNotTakenFields().size();

        assertTrue(freeCount == 4);
        assertNotNull(controller.getWinner());
    }



    @After
    public void reset(){
        if(executorService != null)
            executorService.shutdown();
    }
}