package re.nectar.lib.cereal;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    Float nextFloatOrNull(Random rand, boolean withNulls) {
        if (!withNulls || rand.nextBoolean()) {
            return rand.nextFloat();
        }
        return null;
    }

    Forecast getRandomForecast(boolean withNulls) {
        Random rand = new Random();
        Forecast forecast = new Forecast();
        forecast.setLat(rand.nextFloat());
        forecast.setLng(rand.nextFloat());
        forecast.setTimestamp(rand.nextLong());
        forecast.setDataSource(rand.nextInt(3) + 1);
        forecast.setDomainConfiguration(rand.nextInt(3) + 1);
        forecast.setComputeChainReferenceTimestamp(rand.nextLong());
        forecast.setTimeInterval((short) rand.nextInt());
        forecast.setWindSpeed(nextFloatOrNull(rand, withNulls));
        forecast.setWindGusts(nextFloatOrNull(rand, withNulls));
        forecast.setWindDirection(nextFloatOrNull(rand, withNulls));
        forecast.setTemperature(nextFloatOrNull(rand, withNulls));
        forecast.setCloudCover(nextFloatOrNull(rand, withNulls));
        forecast.setRainfall(nextFloatOrNull(rand, withNulls));
        forecast.setHailfall(nextFloatOrNull(rand, withNulls));
        forecast.setSnowfall(nextFloatOrNull(rand, withNulls));
        forecast.setGraupelfall(nextFloatOrNull(rand, withNulls));
        forecast.setHumidity(nextFloatOrNull(rand, withNulls));
        forecast.setQ2(nextFloatOrNull(rand, withNulls));
        forecast.setT2(nextFloatOrNull(rand, withNulls));
        forecast.setTH2(nextFloatOrNull(rand, withNulls));
        forecast.setPSFC(nextFloatOrNull(rand, withNulls));
        forecast.setU10(nextFloatOrNull(rand, withNulls));
        forecast.setV10(nextFloatOrNull(rand, withNulls));
        forecast.setSNOW(nextFloatOrNull(rand, withNulls));
        forecast.setSNOWH(nextFloatOrNull(rand, withNulls));
        forecast.setCOSZEN(nextFloatOrNull(rand, withNulls));
        forecast.setHGT(nextFloatOrNull(rand, withNulls));
        forecast.setTSK(nextFloatOrNull(rand, withNulls));
        forecast.setRAINC(nextFloatOrNull(rand, withNulls));
        forecast.setRAINSH(nextFloatOrNull(rand, withNulls));
        forecast.setRAINNC(nextFloatOrNull(rand, withNulls));
        forecast.setSNOWNC(nextFloatOrNull(rand, withNulls));
        forecast.setGRAUPELNC(nextFloatOrNull(rand, withNulls));
        forecast.setHAILNC(nextFloatOrNull(rand, withNulls));
        forecast.setSWDOWN(nextFloatOrNull(rand, withNulls));
        forecast.setGLW(nextFloatOrNull(rand, withNulls));
        forecast.setSWNORM(nextFloatOrNull(rand, withNulls));
        forecast.setUST(nextFloatOrNull(rand, withNulls));
        forecast.setPBLH(nextFloatOrNull(rand, withNulls));
        forecast.setWSPD10MAX(nextFloatOrNull(rand, withNulls));
        forecast.setW_UP_MAX(nextFloatOrNull(rand, withNulls));
        forecast.setW_DN_MAX(nextFloatOrNull(rand, withNulls));
        forecast.setUP_HELI_MAX(nextFloatOrNull(rand, withNulls));
        forecast.setW_MEAN(nextFloatOrNull(rand, withNulls));
        forecast.setGRPL_MAX(nextFloatOrNull(rand, withNulls));
        forecast.setAFWA_CLOUD(nextFloatOrNull(rand, withNulls));
        forecast.setSST(nextFloatOrNull(rand, withNulls));
        forecast.setSNOWC((!withNulls || rand.nextBoolean() ? rand.nextBoolean() : null));
        return forecast;
    }

    @Test
    public void cerealizeTest() {
        Forecast randForecast = getRandomForecast(true);
        ByteArray ba = new ByteArray();
        randForecast.cerealizeTo(ba);

        Forecast uncerealizedForecast = new Forecast();
        uncerealizedForecast.uncerealizeFrom(ba);

        assertEquals(randForecast, uncerealizedForecast);
    }

}
