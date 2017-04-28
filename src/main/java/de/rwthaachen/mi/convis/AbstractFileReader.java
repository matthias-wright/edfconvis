package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.AbstractFile;
import de.rwthaachen.mi.convis.data.Signal;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;


/**
 * Created by mwright on 3/20/17.
 */
public abstract class AbstractFileReader {
    private static final Logger LOGGER = Logger.getLogger(AbstractFileReader.class);

    abstract AbstractFile parseHeaderData(byte[] data);

    /**
     * Reads in all the records and puts the signals together sequentially
     *
     * @param data
     *      byte array containing the data records
     * @return signals
     *      Signal array
     */
    protected Signal[] storeSignals(byte[] data, int signalCount, int recordsCount, int[] nrOfSamplesPerSignalInEachRecordGlobal) {
        Signal[] signals = new Signal[signalCount];
        for (int i = 0; i < signals.length; i++) {
            signals[i] = new Signal(nrOfSamplesPerSignalInEachRecordGlobal[i]*recordsCount);
        }
        //iterates through the records
        for (int i = 0; i < recordsCount; i++) {
            //iterates through the signals
            for (int j = 0; j < signalCount; j++) {
                int offsetBytes = 0;
                for(int l = 0; l < j; l++) {
                    offsetBytes = offsetBytes + nrOfSamplesPerSignalInEachRecordGlobal[l];
                }
                for (int m = 0; m < i; m++) {
                    for (int n = 0; n < signalCount; n++) {
                        offsetBytes = offsetBytes + nrOfSamplesPerSignalInEachRecordGlobal[n];
                    }
                }
                int offsetSignalIndex = i*nrOfSamplesPerSignalInEachRecordGlobal[j];
                //iterates through the samples
                for (int k = 0; k < nrOfSamplesPerSignalInEachRecordGlobal[j]-1; k++) {
                    signals[j].addValue((short)(data[2 * (offsetBytes + k)] & 0xff) | (short) (data[2 * (offsetBytes + k) + 1] << 8), offsetSignalIndex + k);
                }
            }
        }
        return signals;
    }

    /**
     * Copies the elements from indexes 'start' to 'end' of an array into a new array and returns the resulting array
     *
     * @param data
     *      source array
     * @param start
     *      start index
     * @param end
     *      end index
     * @return
     *      new array containing copies of elements from the input array between 'start' and 'end' index
     */
    protected byte[] splitBytes(byte[] data, int start, int end) {
        byte[] dataSplit = new byte[Math.abs(start-end)];
        for (int i = 0; i < Math.abs(start-end); i++) {
            dataSplit[i] = data[i+start];
        }
        return dataSplit;
    }

    protected String byteToString(byte[] data) {
        String ret = null;
        try {
            ret = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
            return "";
        }
        return ret;
    }
}
