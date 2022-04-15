package org.bitbucket._newage.commandhook;

import org.junit.jupiter.api.Test;

import static org.bitbucket._newage.commandhook.CommandBlockListener.getSelectorWithArguments;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectorParserTest {

    @Test
    public void testSelectorNoArguments() {
        final String selector = "give @p stone 1";
        final String result = getSelectorWithArguments(selector);

        assertEquals("@p", result);
    }

    @Test
    public void testSelectorNoArgumentsAtTheEnd() {
        final String selector = "broadcast @a";
        final String result = getSelectorWithArguments(selector);

        assertEquals("@a", result);
    }

    @Test
    public void testSelectorWithArguments() {
        final String selector = "/kick @e[type=player,distance=..2] Too close to me";
        final String result = getSelectorWithArguments(selector);

        assertEquals("@e[type=player,distance=..2]", result);
    }

    @Test
    public void testSelectorWithArgumentsAtTheEnd() {
        final String selector = "/kick @e[type=player,distance=..2]";
        final String result = getSelectorWithArguments(selector);

        assertEquals("@e[type=player,distance=..2]", result);
    }

    @Test
    public void testSelectorWithArgumentsNbt() {

        final String expected = "@p[nbt={SelectedItem:{id:\"minecraft:stone_sword\",tag:{display:{Name:'[{\"text\":\"Blade of the Outsider\",\"italic\":false,\"color\":\"dark_gray\",\"bold\":true}]'}}}}]";
        final String selector = "give " + expected + " stone 1";
        final String result = getSelectorWithArguments(selector);

        assertEquals(expected, result);
    }
}
