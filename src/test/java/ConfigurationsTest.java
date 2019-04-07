import com.metronom.Configurations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ConfigurationsTest {


    @Test(expected = IllegalArgumentException.class)
    public void testMapSizeMissing() throws IOException {
        Configurations.loadGameProperties("configuration/missingMapSize.properties");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCMissing() throws IOException {
        Configurations.loadGameProperties("configuration/missingPlayerSymbols.properties");
    }

    @Test
    public void testLoadDefaultProps() throws IOException {
        Configurations.loadDefaultProps();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMapSize() throws IOException {
        Configurations.loadGameProperties("configuration/invalidMapSize.properties");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterSymbols() throws IOException {
        Configurations.loadGameProperties("configuration/invalidPlayerSymbols.properties");
    }

    @Test
    public void testSuccessful() throws IOException {
        Configurations configurations = Configurations.loadGameProperties("game.properties");

        assertNotNull(configurations);
    }



}