package clit;

import java.lang.reflect.*;

import org.testng.*;
import org.testng.annotations.Test;

public class CLITamerTest {

    @Test
    public void parseTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final CLITamer<Example> tamer = new CLITamer<Example>(Example.class);
        final Parameters<Example> parsed = tamer.parse(new String[] {"-t", "foo", "--other", "bar"});
        Assert.assertEquals(parsed.size(), 2);
        Assert.assertEquals(parsed.get(Example.TEST), "foo");
        Assert.assertEquals(parsed.get(Example.OTHER), "bar");
        final Parameters<Example> parsed2 = tamer.parse(new String[] {"--test", "baz", "-o"});
        Assert.assertEquals(parsed2.size(), 2);
        Assert.assertEquals(parsed2.get(Example.TEST), "baz");
        Assert.assertEquals(parsed2.get(Example.OTHER), "true");
        final Parameters<Example> parsed3 = tamer.parse(new String[] {"-t"});
        Assert.assertEquals(parsed3.size(), 1);
        Assert.assertEquals(parsed3.get(Example.TEST), "true");
    }

}
