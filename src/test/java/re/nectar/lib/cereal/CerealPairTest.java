package re.nectar.lib.cereal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CerealPairTest {

    @Test
    public void testPairContstructor() {
        CerealPair<CerealString, CerealLong> pair = new CerealPair<>(new CerealString("a"), new CerealLong(1L));
        assertInstanceOf(CerealString.class, pair.getKey());
        assertInstanceOf(CerealLong.class, pair.getValue());
        assertEquals("a", pair.getKey().getValue());
        assertEquals(1L, pair.getValue().getValue());
    }

    @Test
    public void testPairSetters() {
        CerealPair<CerealString, CerealLong> pair = new CerealPair<>(new CerealString("a"), new CerealLong(1L));
        pair.setKey(new CerealString("b"));
        pair.setValue(new CerealLong(2L));
        assertInstanceOf(CerealString.class, pair.getKey());
        assertInstanceOf(CerealLong.class, pair.getValue());
        assertEquals("b", pair.getKey().getValue());
        assertEquals(2L, pair.getValue().getValue());
    }





}
