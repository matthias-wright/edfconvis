package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.Signal;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by mwright on 11/11/16.
 */
public class Tools {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mm.ss");
    private static final Logger LOGGER = Logger.getLogger(Tools.class);

    /**
     * Prevent instantiation
     */
    private Tools() {

    }

    public static int[] getAverageDistanceToXAxis(Signal[] signals) {
        int[] averages = new int[signals.length];
        for(int i = 0; i < signals.length; i++) {
            int average = 0;
            for(int j = 0; j < signals[i].getValues().length; j++) {
                average = average + signals[i].getValues()[j];
            }
            average = average/signals[i].getValues().length;
            averages[i] = average;
        }
        return averages;
    }

    public static int getLengthOfLongestSignal(Signal[] signals) {
        int maxLength = 0;
        for (int i = 0; i < signals.length; i++) {
            if (signals[i].getValues().length > maxLength) {
                maxLength = signals[i].getValues().length;
            }
        }
        return maxLength;
    }

    public static int[] getAxisScale(int maxLength) {
        int[] x = new int[maxLength];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        return x;
    }

    /**
     * Returns the minimum value of all of the signals
     * @param signals
     * @return minValue
     */
    public static int getMinOfSignals(Signal[] signals) {
        int minValue = signals[0].getValues()[0];
        for (int i = 0; i < signals.length; i++) {
            for (int j = 0; j < signals[i].getValues().length; j++) {
                if(signals[i].getValues()[j] < minValue) {
                    minValue = signals[i].getValues()[j];
                }
            }
        }
        return minValue;
    }

    /**
     * Returns the maximum value of all of the signals
     * @param signals
     * @return maxValue
     */
    public static int getMaxOfSignals(Signal[] signals) {
        int maxValue = signals[0].getValues()[0];
        for (int i = 0; i < signals.length; i++) {
            for (int j = 0; j < signals[i].getValues().length; j++) {
                if(signals[i].getValues()[j] > maxValue) {
                    maxValue = signals[i].getValues()[j];
                }
            }
        }
        return maxValue;
    }


    /**
     * Reads a file into a byte array
     * @param filename
     *      Path
     * @return
     *      byte array
     * @throws IOException
     */
    public static byte[] retrieveByteArrayDataFromFile(String filename) throws IOException {
        java.io.File file = new java.io.File(filename);
        BufferedInputStream fStream = null;
        long size = file.length();
        byte[] data = new byte[(int)size];
        int by;
        int i = 0;

        try {
            fStream = new BufferedInputStream(new FileInputStream(file));
            while ( (by = fStream.read()) != -1) {
                data[i] = (byte)by;
                i++;
            }
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            if (fStream != null) {
                fStream.close();
            }
        }
        return data;
    }

    /**
     * Parses time string of pattern HH.mm.ss to LocalTime object and adds a certain amount of seconds
     * @param startTime
     * @param seconds
     * @return LocalTime object
     */
    public static LocalTime getEndTime(String startTime, int seconds) {
        LocalTime time = LocalTime.from(TIME_FORMATTER.parse(startTime));
        return time.plusSeconds(seconds);
    }
}
