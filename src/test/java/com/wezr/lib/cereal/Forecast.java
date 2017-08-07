package com.wezr.lib.cereal;

public class Forecast implements Cerealizable {
    /**
     * The latitude (north is positive, south is negative) of this forecast's
     * grid location
     */
    protected float lat;

    /**
     * The longitude (west is positive, east is negative) of this forecast's
     * grid location
     */
    protected float lng;

    /**
     * The starting time point of this Forecast
     */
    protected long timestamp;
    /**
     * The data source for this data (WRF, AROME, etc)
     */
    protected int dataSource;

    /**
     * The domain configuration for this dataSource
     */
    protected int domainConfiguration;
    /**
     * The reference timestamp for this data source. For example, for a Forecast
     * of WRF dataSource this would be the GFS reference timestamp.
     */
    protected long computeChainReferenceTimestamp;
    /**
     * The number of seconds of duration this Forecast describes
     */
    protected short timeInterval;
    /**
     * Average wind speed in knots
     */
    protected Float windSpeed = null;
    /**
     * Max wind speed in knots
     */
    protected Float windGusts = null;
    /**
     * angle of the dominant wind in degrees (0 is North, 90 is East)
     */
    protected Float windDirection = null;
    /**
     * temperature at 2 meters from the ground in Kelvin
     */
    protected Float temperature = null;
    /**
     * percentage of cloud cover
     */
    protected Float cloudCover = null;
    /**
     * millimeters of rain per square meter falling during this timeInterval
     */
    protected Float rainfall = null;
    /**
     * millimeters of hail per square meter falling during this timeInterval
     */
    protected Float hailfall = null;
    /**
     * millimeters of snow per square meter falling during this timeInterval
     */
    protected Float snowfall = null;
    /**
     * millimeters of groupel per square meter falling during this timeInterval
     */
    protected Float graupelfall = null;
    /**
     * air humidity percentage
     */
    protected Float humidity = null;

    /**
     * QV at 2M Height -- something to do with cloud composition / humidity...
     * in kg kg-1
     */
    protected Float Q2 = null;
    /**
     * Temperature at 2m height (Kelvin)
     */
    protected Float T2 = null;
    /**
     * Potential Temperature at 2m height (Kelvin)
     */
    protected Float TH2 = null;

    /**
     * Surface Pressure in Pa
     */
    protected Float PSFC = null;
    /**
     * Wind Speed along U Vector in m/s
     */
    protected Float U10 = null;
    /**
     * Wind Speed along V Vector in m/s
     */
    protected Float V10 = null;
    /**
     * Snow Water Equivalent kg m-2
     */
    protected Float SNOW = null;
    /**
     * Physical Snow Depth in m
     */
    protected Float SNOWH = null;
    /**
     * cosine of solar zenith angle in ?
     */
    protected Float COSZEN = null;
    /**
     * Terrain height in m
     */
    protected Float HGT = null;
    /**
     * Surface Skin Temperature in K
     */
    protected Float TSK = null;
    /**
     * Accumulated total cumulus precipitation in mm
     */
    protected Float RAINC = null;
    /**
     * Accumulated shallow cumulus precipitation in mm
     */
    protected Float RAINSH = null;
    /**
     * Accumulated total grid scale precipitation in mm
     */
    protected Float RAINNC = null;
    /**
     * Accumulated total grid scale snow and ice in mm
     */
    protected Float SNOWNC = null;
    /**
     * Accumulated total grid scale graupel in mm
     */

    protected Float GRAUPELNC = null;
    /**
     * Accumulated total grid scale hail in mm
     */
    protected Float HAILNC = null;
    /**
     * DOWNWARD SHORT WAVE FLUX AT GROUND SURFACE ?? W m-2
     */
    protected Float SWDOWN = null;
    /**
     * DOWNWARD LONG WAVE FLUX AT GROUND SURFACE ?? W m-2
     */
    protected Float GLW = null;
    /**
     * NORMAL SHORT WAVE FLUX AT GROUND SURFACE (SLOPE-DEPENDENT) ?? in W m-2
     */
    protected Float SWNORM = null;

    /**
     * U* IN SIMILARITY THEORY ?? m s-1
     */
    protected Float UST = null;
    /**
     * PBL HEIGHT ?? m
     */
    protected Float PBLH = null;
    /**
     * FLAG INDICATING SNOW COVERAGE (1 FOR SNOW COVER)
     */
    protected Boolean SNOWC = null;
    /**
     * Wind Speed Maximum at 10m height in m s-1
     */
    protected Float WSPD10MAX = null;
    /**
     * MAX Z-WIND UPDRAFT
     */
    protected Float W_UP_MAX = null;
    /**
     * MAX Z-WIND DOWNDRAFT in m s-1
     */
    protected Float W_DN_MAX = null;
    /**
     * MAX UPDRAFT HELICITY m2/s2
     */
    protected Float UP_HELI_MAX = null;
    /**
     * HOURLY MEAN Z-WIND m/s
     */
    protected Float W_MEAN = null;
    /**
     * MAX COL INT GRAUPEL ?? in kg/m2
     */
    protected Float GRPL_MAX = null;
    /**
     * AFWA Diagnostic: Cloud cover fraction
     */
    protected Float AFWA_CLOUD = null;
    /**
     * Surface Sea Temperature in K
     */
    protected Float SST = null;

    @Override
    public void cerealizeTo(ByteArray ba) {
        // 41 the number of variables that can be null
        BitMap nullBitMap = new BitMap(41);
        int t = 0;
        nullBitMap.set(t++, windSpeed == null);
        nullBitMap.set(t++, windGusts == null);
        nullBitMap.set(t++, windDirection == null);
        nullBitMap.set(t++, temperature == null);
        nullBitMap.set(t++, cloudCover == null);
        nullBitMap.set(t++, rainfall == null);
        nullBitMap.set(t++, hailfall == null);
        nullBitMap.set(t++, snowfall == null);
        nullBitMap.set(t++, graupelfall == null);
        nullBitMap.set(t++, humidity == null);
        nullBitMap.set(t++, Q2 == null);
        nullBitMap.set(t++, T2 == null);
        nullBitMap.set(t++, TH2 == null);
        nullBitMap.set(t++, PSFC == null);
        nullBitMap.set(t++, U10 == null);
        nullBitMap.set(t++, V10 == null);
        nullBitMap.set(t++, SNOW == null);
        nullBitMap.set(t++, SNOWH == null);
        nullBitMap.set(t++, COSZEN == null);
        nullBitMap.set(t++, HGT == null);
        nullBitMap.set(t++, TSK == null);
        nullBitMap.set(t++, RAINC == null);
        nullBitMap.set(t++, RAINSH == null);
        nullBitMap.set(t++, RAINNC == null);
        nullBitMap.set(t++, SNOWNC == null);
        nullBitMap.set(t++, GRAUPELNC == null);
        nullBitMap.set(t++, HAILNC == null);
        nullBitMap.set(t++, SWDOWN == null);
        nullBitMap.set(t++, GLW == null);
        nullBitMap.set(t++, SWNORM == null);
        nullBitMap.set(t++, UST == null);
        nullBitMap.set(t++, PBLH == null);
        nullBitMap.set(t++, SNOWC == null);
        nullBitMap.set(t++, WSPD10MAX == null);
        nullBitMap.set(t++, W_UP_MAX == null);
        nullBitMap.set(t++, W_DN_MAX == null);
        nullBitMap.set(t++, UP_HELI_MAX == null);
        nullBitMap.set(t++, W_MEAN == null);
        nullBitMap.set(t++, GRPL_MAX == null);
        nullBitMap.set(t++, AFWA_CLOUD == null);
        nullBitMap.set(t++, SST == null);
        nullBitMap.cerealizeTo(ba);

        ba.add(lat);
        ba.add(lng);
        ba.add(timestamp);
        ba.add(dataSource);
        ba.add(domainConfiguration);
        ba.add(computeChainReferenceTimestamp);
        ba.add(timeInterval);
        ba.addIfNotNull(windSpeed);
        ba.addIfNotNull(windGusts);
        ba.addIfNotNull(windDirection);
        ba.addIfNotNull(temperature);
        ba.addIfNotNull(cloudCover);
        ba.addIfNotNull(rainfall);
        ba.addIfNotNull(hailfall);
        ba.addIfNotNull(snowfall);
        ba.addIfNotNull(graupelfall);
        ba.addIfNotNull(humidity);
        ba.addIfNotNull(Q2);
        ba.addIfNotNull(T2);
        ba.addIfNotNull(TH2);
        ba.addIfNotNull(PSFC);
        ba.addIfNotNull(U10);
        ba.addIfNotNull(V10);
        ba.addIfNotNull(SNOW);
        ba.addIfNotNull(SNOWH);
        ba.addIfNotNull(COSZEN);
        ba.addIfNotNull(HGT);
        ba.addIfNotNull(TSK);
        ba.addIfNotNull(RAINC);
        ba.addIfNotNull(RAINSH);
        ba.addIfNotNull(RAINNC);
        ba.addIfNotNull(SNOWNC);
        ba.addIfNotNull(GRAUPELNC);
        ba.addIfNotNull(HAILNC);
        ba.addIfNotNull(SWDOWN);
        ba.addIfNotNull(GLW);
        ba.addIfNotNull(SWNORM);
        ba.addIfNotNull(UST);
        ba.addIfNotNull(PBLH);
        ba.addIfNotNull(SNOWC);
        ba.addIfNotNull(WSPD10MAX);
        ba.addIfNotNull(W_UP_MAX);
        ba.addIfNotNull(W_DN_MAX);
        ba.addIfNotNull(UP_HELI_MAX);
        ba.addIfNotNull(W_MEAN);
        ba.addIfNotNull(GRPL_MAX);
        ba.addIfNotNull(AFWA_CLOUD);
        ba.addIfNotNull(SST);

    }

    @Override
    public void uncerealizeFrom(ByteArray ba) {
        // TODO Auto-generated method stub
        BitMap nullBitMap = new BitMap();
        nullBitMap.uncerealizeFrom(ba);
        lat = ba.getFloat();
        lng = ba.getFloat();
        timestamp = ba.getLong();
        dataSource = ba.getInt();
        domainConfiguration = ba.getInt();
        computeChainReferenceTimestamp = ba.getLong();
        timeInterval = ba.getShort();
        int t = 0;
        windSpeed = (nullBitMap.is(t++) ? null : ba.getFloat());
        windGusts = (nullBitMap.is(t++) ? null : ba.getFloat());
        windDirection = (nullBitMap.is(t++) ? null : ba.getFloat());
        temperature = (nullBitMap.is(t++) ? null : ba.getFloat());
        cloudCover = (nullBitMap.is(t++) ? null : ba.getFloat());
        rainfall = (nullBitMap.is(t++) ? null : ba.getFloat());
        hailfall = (nullBitMap.is(t++) ? null : ba.getFloat());
        snowfall = (nullBitMap.is(t++) ? null : ba.getFloat());
        graupelfall = (nullBitMap.is(t++) ? null : ba.getFloat());
        humidity = (nullBitMap.is(t++) ? null : ba.getFloat());
        Q2 = (nullBitMap.is(t++) ? null : ba.getFloat());
        T2 = (nullBitMap.is(t++) ? null : ba.getFloat());
        TH2 = (nullBitMap.is(t++) ? null : ba.getFloat());
        PSFC = (nullBitMap.is(t++) ? null : ba.getFloat());
        U10 = (nullBitMap.is(t++) ? null : ba.getFloat());
        V10 = (nullBitMap.is(t++) ? null : ba.getFloat());
        SNOW = (nullBitMap.is(t++) ? null : ba.getFloat());
        SNOWH = (nullBitMap.is(t++) ? null : ba.getFloat());
        COSZEN = (nullBitMap.is(t++) ? null : ba.getFloat());
        HGT = (nullBitMap.is(t++) ? null : ba.getFloat());
        TSK = (nullBitMap.is(t++) ? null : ba.getFloat());
        RAINC = (nullBitMap.is(t++) ? null : ba.getFloat());
        RAINSH = (nullBitMap.is(t++) ? null : ba.getFloat());
        RAINNC = (nullBitMap.is(t++) ? null : ba.getFloat());
        SNOWNC = (nullBitMap.is(t++) ? null : ba.getFloat());
        GRAUPELNC = (nullBitMap.is(t++) ? null : ba.getFloat());
        HAILNC = (nullBitMap.is(t++) ? null : ba.getFloat());
        SWDOWN = (nullBitMap.is(t++) ? null : ba.getFloat());
        GLW = (nullBitMap.is(t++) ? null : ba.getFloat());
        SWNORM = (nullBitMap.is(t++) ? null : ba.getFloat());
        UST = (nullBitMap.is(t++) ? null : ba.getFloat());
        PBLH = (nullBitMap.is(t++) ? null : ba.getFloat());
        SNOWC = (nullBitMap.is(t++) ? null : ba.getBoolean());
        WSPD10MAX = (nullBitMap.is(t++) ? null : ba.getFloat());
        W_UP_MAX = (nullBitMap.is(t++) ? null : ba.getFloat());
        W_DN_MAX = (nullBitMap.is(t++) ? null : ba.getFloat());
        UP_HELI_MAX = (nullBitMap.is(t++) ? null : ba.getFloat());
        W_MEAN = (nullBitMap.is(t++) ? null : ba.getFloat());
        GRPL_MAX = (nullBitMap.is(t++) ? null : ba.getFloat());
        AFWA_CLOUD = (nullBitMap.is(t++) ? null : ba.getFloat());
        SST = (nullBitMap.is(t++) ? null : ba.getFloat());
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public int getDomainConfiguration() {
        return domainConfiguration;
    }

    public void setDomainConfiguration(int domainConfiguration) {
        this.domainConfiguration = domainConfiguration;
    }

    public long getComputeChainReferenceTimestamp() {
        return computeChainReferenceTimestamp;
    }

    public void setComputeChainReferenceTimestamp(long computeChainReferenceTimestamp) {
        this.computeChainReferenceTimestamp = computeChainReferenceTimestamp;
    }

    public short getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(short timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindGusts() {
        return windGusts;
    }

    public void setWindGusts(Float windGusts) {
        this.windGusts = windGusts;
    }

    public Float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Float windDirection) {
        this.windDirection = windDirection;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(Float cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Float getRainfall() {
        return rainfall;
    }

    public void setRainfall(Float rainfall) {
        this.rainfall = rainfall;
    }

    public Float getHailfall() {
        return hailfall;
    }

    public void setHailfall(Float hailfall) {
        this.hailfall = hailfall;
    }

    public Float getSnowfall() {
        return snowfall;
    }

    public void setSnowfall(Float snowfall) {
        this.snowfall = snowfall;
    }

    public Float getGraupelfall() {
        return graupelfall;
    }

    public void setGraupelfall(Float graupelfall) {
        this.graupelfall = graupelfall;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getQ2() {
        return Q2;
    }

    public void setQ2(Float q2) {
        Q2 = q2;
    }

    public Float getT2() {
        return T2;
    }

    public void setT2(Float t2) {
        T2 = t2;
    }

    public Float getTH2() {
        return TH2;
    }

    public void setTH2(Float tH2) {
        TH2 = tH2;
    }

    public Float getPSFC() {
        return PSFC;
    }

    public void setPSFC(Float pSFC) {
        PSFC = pSFC;
    }

    public Float getU10() {
        return U10;
    }

    public void setU10(Float u10) {
        U10 = u10;
    }

    public Float getV10() {
        return V10;
    }

    public void setV10(Float v10) {
        V10 = v10;
    }

    public Float getSNOW() {
        return SNOW;
    }

    public void setSNOW(Float sNOW) {
        SNOW = sNOW;
    }

    public Float getSNOWH() {
        return SNOWH;
    }

    public void setSNOWH(Float sNOWH) {
        SNOWH = sNOWH;
    }

    public Float getCOSZEN() {
        return COSZEN;
    }

    public void setCOSZEN(Float cOSZEN) {
        COSZEN = cOSZEN;
    }

    public Float getHGT() {
        return HGT;
    }

    public void setHGT(Float hGT) {
        HGT = hGT;
    }

    public Float getTSK() {
        return TSK;
    }

    public void setTSK(Float tSK) {
        TSK = tSK;
    }

    public Float getRAINC() {
        return RAINC;
    }

    public void setRAINC(Float rAINC) {
        RAINC = rAINC;
    }

    public Float getRAINSH() {
        return RAINSH;
    }

    public void setRAINSH(Float rAINSH) {
        RAINSH = rAINSH;
    }

    public Float getRAINNC() {
        return RAINNC;
    }

    public void setRAINNC(Float rAINNC) {
        RAINNC = rAINNC;
    }

    public Float getSNOWNC() {
        return SNOWNC;
    }

    public void setSNOWNC(Float sNOWNC) {
        SNOWNC = sNOWNC;
    }

    public Float getGRAUPELNC() {
        return GRAUPELNC;
    }

    public void setGRAUPELNC(Float gRAUPELNC) {
        GRAUPELNC = gRAUPELNC;
    }

    public Float getHAILNC() {
        return HAILNC;
    }

    public void setHAILNC(Float hAILNC) {
        HAILNC = hAILNC;
    }

    public Float getSWDOWN() {
        return SWDOWN;
    }

    public void setSWDOWN(Float sWDOWN) {
        SWDOWN = sWDOWN;
    }

    public Float getGLW() {
        return GLW;
    }

    public void setGLW(Float gLW) {
        GLW = gLW;
    }

    public Float getSWNORM() {
        return SWNORM;
    }

    public void setSWNORM(Float sWNORM) {
        SWNORM = sWNORM;
    }

    public Float getUST() {
        return UST;
    }

    public void setUST(Float uST) {
        UST = uST;
    }

    public Float getPBLH() {
        return PBLH;
    }

    public void setPBLH(Float pBLH) {
        PBLH = pBLH;
    }

    public Boolean getSNOWC() {
        return SNOWC;
    }

    public void setSNOWC(Boolean sNOWC) {
        SNOWC = sNOWC;
    }

    public Float getWSPD10MAX() {
        return WSPD10MAX;
    }

    public void setWSPD10MAX(Float wSPD10MAX) {
        WSPD10MAX = wSPD10MAX;
    }

    public Float getW_UP_MAX() {
        return W_UP_MAX;
    }

    public void setW_UP_MAX(Float w_UP_MAX) {
        W_UP_MAX = w_UP_MAX;
    }

    public Float getW_DN_MAX() {
        return W_DN_MAX;
    }

    public void setW_DN_MAX(Float w_DN_MAX) {
        W_DN_MAX = w_DN_MAX;
    }

    public Float getUP_HELI_MAX() {
        return UP_HELI_MAX;
    }

    public void setUP_HELI_MAX(Float uP_HELI_MAX) {
        UP_HELI_MAX = uP_HELI_MAX;
    }

    public Float getW_MEAN() {
        return W_MEAN;
    }

    public void setW_MEAN(Float w_MEAN) {
        W_MEAN = w_MEAN;
    }

    public Float getGRPL_MAX() {
        return GRPL_MAX;
    }

    public void setGRPL_MAX(Float gRPL_MAX) {
        GRPL_MAX = gRPL_MAX;
    }

    public Float getAFWA_CLOUD() {
        return AFWA_CLOUD;
    }

    public void setAFWA_CLOUD(Float aFWA_CLOUD) {
        AFWA_CLOUD = aFWA_CLOUD;
    }

    public Float getSST() {
        return SST;
    }

    public void setSST(Float sST) {
        SST = sST;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((AFWA_CLOUD == null) ? 0 : AFWA_CLOUD.hashCode());
        result = prime * result + ((COSZEN == null) ? 0 : COSZEN.hashCode());
        result = prime * result + ((GLW == null) ? 0 : GLW.hashCode());
        result = prime * result + ((GRAUPELNC == null) ? 0 : GRAUPELNC.hashCode());
        result = prime * result + ((GRPL_MAX == null) ? 0 : GRPL_MAX.hashCode());
        result = prime * result + ((HAILNC == null) ? 0 : HAILNC.hashCode());
        result = prime * result + ((HGT == null) ? 0 : HGT.hashCode());
        result = prime * result + ((PBLH == null) ? 0 : PBLH.hashCode());
        result = prime * result + ((PSFC == null) ? 0 : PSFC.hashCode());
        result = prime * result + ((Q2 == null) ? 0 : Q2.hashCode());
        result = prime * result + ((RAINC == null) ? 0 : RAINC.hashCode());
        result = prime * result + ((RAINNC == null) ? 0 : RAINNC.hashCode());
        result = prime * result + ((RAINSH == null) ? 0 : RAINSH.hashCode());
        result = prime * result + ((SNOW == null) ? 0 : SNOW.hashCode());
        result = prime * result + ((SNOWC == null) ? 0 : SNOWC.hashCode());
        result = prime * result + ((SNOWH == null) ? 0 : SNOWH.hashCode());
        result = prime * result + ((SNOWNC == null) ? 0 : SNOWNC.hashCode());
        result = prime * result + ((SST == null) ? 0 : SST.hashCode());
        result = prime * result + ((SWDOWN == null) ? 0 : SWDOWN.hashCode());
        result = prime * result + ((SWNORM == null) ? 0 : SWNORM.hashCode());
        result = prime * result + ((T2 == null) ? 0 : T2.hashCode());
        result = prime * result + ((TH2 == null) ? 0 : TH2.hashCode());
        result = prime * result + ((TSK == null) ? 0 : TSK.hashCode());
        result = prime * result + ((U10 == null) ? 0 : U10.hashCode());
        result = prime * result + ((UP_HELI_MAX == null) ? 0 : UP_HELI_MAX.hashCode());
        result = prime * result + ((UST == null) ? 0 : UST.hashCode());
        result = prime * result + ((V10 == null) ? 0 : V10.hashCode());
        result = prime * result + ((WSPD10MAX == null) ? 0 : WSPD10MAX.hashCode());
        result = prime * result + ((W_DN_MAX == null) ? 0 : W_DN_MAX.hashCode());
        result = prime * result + ((W_MEAN == null) ? 0 : W_MEAN.hashCode());
        result = prime * result + ((W_UP_MAX == null) ? 0 : W_UP_MAX.hashCode());
        result = prime * result + ((cloudCover == null) ? 0 : cloudCover.hashCode());
        result = prime * result + (int) (computeChainReferenceTimestamp ^ (computeChainReferenceTimestamp >>> 32));
        result = prime * result + dataSource;
        result = prime * result + domainConfiguration;
        result = prime * result + ((graupelfall == null) ? 0 : graupelfall.hashCode());
        result = prime * result + ((hailfall == null) ? 0 : hailfall.hashCode());
        result = prime * result + ((humidity == null) ? 0 : humidity.hashCode());
        result = prime * result + Float.floatToIntBits(lat);
        result = prime * result + Float.floatToIntBits(lng);
        result = prime * result + ((rainfall == null) ? 0 : rainfall.hashCode());
        result = prime * result + ((snowfall == null) ? 0 : snowfall.hashCode());
        result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
        result = prime * result + timeInterval;
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + ((windDirection == null) ? 0 : windDirection.hashCode());
        result = prime * result + ((windGusts == null) ? 0 : windGusts.hashCode());
        result = prime * result + ((windSpeed == null) ? 0 : windSpeed.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Forecast other = (Forecast) obj;
        if (AFWA_CLOUD == null) {
            if (other.AFWA_CLOUD != null) {
                return false;
            }
        } else if (!AFWA_CLOUD.equals(other.AFWA_CLOUD)) {
            return false;
        }
        if (COSZEN == null) {
            if (other.COSZEN != null) {
                return false;
            }
        } else if (!COSZEN.equals(other.COSZEN)) {
            return false;
        }
        if (GLW == null) {
            if (other.GLW != null) {
                return false;
            }
        } else if (!GLW.equals(other.GLW)) {
            return false;
        }
        if (GRAUPELNC == null) {
            if (other.GRAUPELNC != null) {
                return false;
            }
        } else if (!GRAUPELNC.equals(other.GRAUPELNC)) {
            return false;
        }
        if (GRPL_MAX == null) {
            if (other.GRPL_MAX != null) {
                return false;
            }
        } else if (!GRPL_MAX.equals(other.GRPL_MAX)) {
            return false;
        }
        if (HAILNC == null) {
            if (other.HAILNC != null) {
                return false;
            }
        } else if (!HAILNC.equals(other.HAILNC)) {
            return false;
        }
        if (HGT == null) {
            if (other.HGT != null) {
                return false;
            }
        } else if (!HGT.equals(other.HGT)) {
            return false;
        }
        if (PBLH == null) {
            if (other.PBLH != null) {
                return false;
            }
        } else if (!PBLH.equals(other.PBLH)) {
            return false;
        }
        if (PSFC == null) {
            if (other.PSFC != null) {
                return false;
            }
        } else if (!PSFC.equals(other.PSFC)) {
            return false;
        }
        if (Q2 == null) {
            if (other.Q2 != null) {
                return false;
            }
        } else if (!Q2.equals(other.Q2)) {
            return false;
        }
        if (RAINC == null) {
            if (other.RAINC != null) {
                return false;
            }
        } else if (!RAINC.equals(other.RAINC)) {
            return false;
        }
        if (RAINNC == null) {
            if (other.RAINNC != null) {
                return false;
            }
        } else if (!RAINNC.equals(other.RAINNC)) {
            return false;
        }
        if (RAINSH == null) {
            if (other.RAINSH != null) {
                return false;
            }
        } else if (!RAINSH.equals(other.RAINSH)) {
            return false;
        }
        if (SNOW == null) {
            if (other.SNOW != null) {
                return false;
            }
        } else if (!SNOW.equals(other.SNOW)) {
            return false;
        }
        if (SNOWC == null) {
            if (other.SNOWC != null) {
                return false;
            }
        } else if (!SNOWC.equals(other.SNOWC)) {
            return false;
        }
        if (SNOWH == null) {
            if (other.SNOWH != null) {
                return false;
            }
        } else if (!SNOWH.equals(other.SNOWH)) {
            return false;
        }
        if (SNOWNC == null) {
            if (other.SNOWNC != null) {
                return false;
            }
        } else if (!SNOWNC.equals(other.SNOWNC)) {
            return false;
        }
        if (SST == null) {
            if (other.SST != null) {
                return false;
            }
        } else if (!SST.equals(other.SST)) {
            return false;
        }
        if (SWDOWN == null) {
            if (other.SWDOWN != null) {
                return false;
            }
        } else if (!SWDOWN.equals(other.SWDOWN)) {
            return false;
        }
        if (SWNORM == null) {
            if (other.SWNORM != null) {
                return false;
            }
        } else if (!SWNORM.equals(other.SWNORM)) {
            return false;
        }
        if (T2 == null) {
            if (other.T2 != null) {
                return false;
            }
        } else if (!T2.equals(other.T2)) {
            return false;
        }
        if (TH2 == null) {
            if (other.TH2 != null) {
                return false;
            }
        } else if (!TH2.equals(other.TH2)) {
            return false;
        }
        if (TSK == null) {
            if (other.TSK != null) {
                return false;
            }
        } else if (!TSK.equals(other.TSK)) {
            return false;
        }
        if (U10 == null) {
            if (other.U10 != null) {
                return false;
            }
        } else if (!U10.equals(other.U10)) {
            return false;
        }
        if (UP_HELI_MAX == null) {
            if (other.UP_HELI_MAX != null) {
                return false;
            }
        } else if (!UP_HELI_MAX.equals(other.UP_HELI_MAX)) {
            return false;
        }
        if (UST == null) {
            if (other.UST != null) {
                return false;
            }
        } else if (!UST.equals(other.UST)) {
            return false;
        }
        if (V10 == null) {
            if (other.V10 != null) {
                return false;
            }
        } else if (!V10.equals(other.V10)) {
            return false;
        }
        if (WSPD10MAX == null) {
            if (other.WSPD10MAX != null) {
                return false;
            }
        } else if (!WSPD10MAX.equals(other.WSPD10MAX)) {
            return false;
        }
        if (W_DN_MAX == null) {
            if (other.W_DN_MAX != null) {
                return false;
            }
        } else if (!W_DN_MAX.equals(other.W_DN_MAX)) {
            return false;
        }
        if (W_MEAN == null) {
            if (other.W_MEAN != null) {
                return false;
            }
        } else if (!W_MEAN.equals(other.W_MEAN)) {
            return false;
        }
        if (W_UP_MAX == null) {
            if (other.W_UP_MAX != null) {
                return false;
            }
        } else if (!W_UP_MAX.equals(other.W_UP_MAX)) {
            return false;
        }
        if (cloudCover == null) {
            if (other.cloudCover != null) {
                return false;
            }
        } else if (!cloudCover.equals(other.cloudCover)) {
            return false;
        }
        if (computeChainReferenceTimestamp != other.computeChainReferenceTimestamp) {
            return false;
        }
        if (dataSource != other.dataSource) {
            return false;
        }
        if (domainConfiguration != other.domainConfiguration) {
            return false;
        }
        if (graupelfall == null) {
            if (other.graupelfall != null) {
                return false;
            }
        } else if (!graupelfall.equals(other.graupelfall)) {
            return false;
        }
        if (hailfall == null) {
            if (other.hailfall != null) {
                return false;
            }
        } else if (!hailfall.equals(other.hailfall)) {
            return false;
        }
        if (humidity == null) {
            if (other.humidity != null) {
                return false;
            }
        } else if (!humidity.equals(other.humidity)) {
            return false;
        }
        if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat)) {
            return false;
        }
        if (Float.floatToIntBits(lng) != Float.floatToIntBits(other.lng)) {
            return false;
        }
        if (rainfall == null) {
            if (other.rainfall != null) {
                return false;
            }
        } else if (!rainfall.equals(other.rainfall)) {
            return false;
        }
        if (snowfall == null) {
            if (other.snowfall != null) {
                return false;
            }
        } else if (!snowfall.equals(other.snowfall)) {
            return false;
        }
        if (temperature == null) {
            if (other.temperature != null) {
                return false;
            }
        } else if (!temperature.equals(other.temperature)) {
            return false;
        }
        if (timeInterval != other.timeInterval) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }
        if (windDirection == null) {
            if (other.windDirection != null) {
                return false;
            }
        } else if (!windDirection.equals(other.windDirection)) {
            return false;
        }
        if (windGusts == null) {
            if (other.windGusts != null) {
                return false;
            }
        } else if (!windGusts.equals(other.windGusts)) {
            return false;
        }
        if (windSpeed == null) {
            if (other.windSpeed != null) {
                return false;
            }
        } else if (!windSpeed.equals(other.windSpeed)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Forecast [lat=" + lat + ", lng=" + lng + ", timestamp=" + timestamp + ", dataSource=" + dataSource
                + ", domainConfiguration=" + domainConfiguration + ", computeChainReferenceTimestamp="
                + computeChainReferenceTimestamp + ", timeInterval=" + timeInterval + ", windSpeed=" + windSpeed
                + ", windGusts=" + windGusts + ", windDirection=" + windDirection + ", temperature=" + temperature
                + ", cloudCover=" + cloudCover + ", rainfall=" + rainfall + ", hailfall=" + hailfall + ", snowfall="
                + snowfall + ", graupelfall=" + graupelfall + ", humidity=" + humidity + ", Q2=" + Q2 + ", T2=" + T2
                + ", TH2=" + TH2 + ", PSFC=" + PSFC + ", U10=" + U10 + ", V10=" + V10 + ", SNOW=" + SNOW + ", SNOWH="
                + SNOWH + ", COSZEN=" + COSZEN + ", HGT=" + HGT + ", TSK=" + TSK + ", RAINC=" + RAINC + ", RAINSH="
                + RAINSH + ", RAINNC=" + RAINNC + ", SNOWNC=" + SNOWNC + ", GRAUPELNC=" + GRAUPELNC + ", HAILNC="
                + HAILNC + ", SWDOWN=" + SWDOWN + ", GLW=" + GLW + ", SWNORM=" + SWNORM + ", UST=" + UST + ", PBLH="
                + PBLH + ", SNOWC=" + SNOWC + ", WSPD10MAX=" + WSPD10MAX + ", W_UP_MAX=" + W_UP_MAX + ", W_DN_MAX="
                + W_DN_MAX + ", UP_HELI_MAX=" + UP_HELI_MAX + ", W_MEAN=" + W_MEAN + ", GRPL_MAX=" + GRPL_MAX
                + ", AFWA_CLOUD=" + AFWA_CLOUD + ", SST=" + SST + "]";
    }

}
