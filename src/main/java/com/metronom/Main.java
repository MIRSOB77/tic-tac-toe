package com.metronom;

import com.metronom.Configurations;
import com.metronom.model.Controller;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Controller controller = new Controller(Configurations.loadDefaultProps());
        new Thread(controller).start();
    }
}
