package org.glassfish.mojarra.util;

import static java.lang.Character.toChars;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.glassfish.mojarra.util.HtmlUtils.isAllowedXmlCharacter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

class HtmlUtilsTest {

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/4516
     * https://github.com/eclipse-ee4j/mojarra/issues/5464
     */
    @Test
    void testAllowedXmlCharacter() {
        for (char c = 0x0000; c <= 0x0008; c++) {
            assertFalse(isAllowedXmlCharacter(c));
        }

        assertTrue(isAllowedXmlCharacter((char) 0x0009));
        assertTrue(isAllowedXmlCharacter((char) 0x000A));

        assertFalse(isAllowedXmlCharacter((char) 0x000B));
        assertFalse(isAllowedXmlCharacter((char) 0x000C));

        assertTrue(isAllowedXmlCharacter((char) 0x000D));

        for (char c = 0x000E; c <= 0x0019; c++) {
            assertFalse(isAllowedXmlCharacter(c));
        }

        for (char c = 0x0020; c <= 0xD7FF; c++) {
            assertTrue(isAllowedXmlCharacter(c));
        }

        for (char c = 0xD800; c <= 0xDFFF; c++) {
            assertFalse(isAllowedXmlCharacter(c));
        }

        for (char c = 0xE000; c <= 0xFFFD; c++) {
            assertTrue(isAllowedXmlCharacter(c));
        }

        assertFalse(isAllowedXmlCharacter((char) 0xFFFE));
        assertFalse(isAllowedXmlCharacter((char) 0xFFFF));
    }

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/5562
     */
    @Test
    void testAllowedSurrogateChars() {
        for (char low1 = 0xDC00; low1 <= 0xDFFF; low1++) {
            assertEquals("", writeUnescapedTextForXML(new String(new char[] { low1 })));

            for (char high2 = 0xD800; high2 <= 0xDBFF; high2++) {
                assertEquals("", writeUnescapedTextForXML(new String(new char[] { low1, high2 })));
            }
            for (char low2 = 0xDC00; low2 <= 0xDFFF; low2++) {
                assertEquals("", writeUnescapedTextForXML(new String(new char[] { low1, low2 })));
            }
        }

        for (char high1 = 0xD800; high1 <= 0xDBFF; high1++) {
            assertEquals("", writeUnescapedTextForXML(new String(new char[] { high1 })));

            for (char high2 = 0xD800; high2 <= 0xDBFF; high2++) {
                assertEquals("", writeUnescapedTextForXML(new String(new char[] { high1, high2 })));
            }
            for (char low2 = 0xDC00; low2 <= 0xDFFF; low2++) {
                assertNotEquals("", writeUnescapedTextForXML(new String(new char[] { high1, low2 }))); // This is the only combination which is allowed.
            }
        }
    }

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/5562
     */
    @Test
    void testWriteEmojisAsUnescapedTextForXML() {
        assertEquals("𝄞", writeUnescapedTextForXML("𝄞"));
        assertEquals("𝄞", writeUnescapedTextForXML(new String(toChars(0x1D11E))));
        assertEquals("😎", writeUnescapedTextForXML("😎"));
        assertEquals("😎", writeUnescapedTextForXML(new String(toChars(0x1F60E))));
        assertEquals("🎉", writeUnescapedTextForXML("🎉"));
        assertEquals("🎉", writeUnescapedTextForXML(new String(toChars(0x1F389))));
        assertEquals("🚀", writeUnescapedTextForXML("🚀"));
        assertEquals("🚀", writeUnescapedTextForXML(new String(toChars(0x1F680))));
        assertEquals("🐻", writeUnescapedTextForXML("🐻"));
        assertEquals("🐻", writeUnescapedTextForXML(new String(toChars(0x1F43B))));
        assertEquals("🐻‍❄️", writeUnescapedTextForXML("🐻‍❄️"));
        assertEquals("🐻‍❄️", writeUnescapedTextForXML(new String(new int[] { 0x1F43B, 0x200D, 0x2744, 0xFE0F }, 0, 4)));
    }

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/5562
     */
    @Test
    void testWriteEmojisAsTextForXML() {
        assertEquals("&#55348;&#56606;", writeTextForXML("𝄞"));
        assertEquals("&#55357;&#56846;", writeTextForXML("😎"));
        assertEquals("&#55356;&#57225;", writeTextForXML("🎉"));
        assertEquals("&#55357;&#56960;", writeTextForXML("🚀"));
        assertEquals("&#55357;&#56379;", writeTextForXML("🐻"));
        assertEquals("&#55357;&#56379;&#8205;&#10052;&#65039;", writeTextForXML("🐻‍❄️"));
    }

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/5562
     */
    @Test
    void testWriteEmojisAsAttributeForXML() {
        assertEquals("&#55348;&#56606;", writeAttributeForXML("𝄞"));
        assertEquals("&#55357;&#56846;", writeAttributeForXML("😎"));
        assertEquals("&#55356;&#57225;", writeAttributeForXML("🎉"));
        assertEquals("&#55357;&#56960;", writeAttributeForXML("🚀"));
        assertEquals("&#55357;&#56379;", writeAttributeForXML("🐻"));
        assertEquals("&#55357;&#56379;&#8205;&#10052;&#65039;", writeAttributeForXML("🐻‍❄️"));
    }

    /**
     * https://github.com/eclipse-ee4j/mojarra/issues/5585
     */
    @Test
    void testWriteURLWithQuestionMarkInFragment() {
        assertEquals("https://server.com/sap?query#fragment?p=v", writeURL("https://server.com/sap?query#fragment?p=v"));
        assertEquals("https://server.com/sap?query=foo%3Fbar#fragment?p=v", writeURL("https://server.com/sap?query=foo?bar#fragment?p=v"));
    }

    private static String writeUnescapedTextForXML(String string) {
        return write(output -> HtmlUtils.writeUnescapedTextForXML(output, string));
    }

    private static String writeTextForXML(String string) {
        return write(output -> HtmlUtils.writeTextForXML(output, string, new char[1024]));
    }

    private static String writeAttributeForXML(String string) {
        return write(output -> HtmlUtils.writeAttribute(output, true, true, new char[16], string, new char[1024], false, true));
    }

    private static String writeURL(String string) {
        return write(output -> HtmlUtils.writeURL(output, string, new char[string.length()], UTF_8.name()));
    }

    private static String write(ThrowingConsumer<Writer> output) {
        StringWriter writer = new StringWriter();

        try {
            output.accept(writer);
        }
        catch (Throwable e) {
            fail(e);
        }

        return writer.toString();
    }

}
