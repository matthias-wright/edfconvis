package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.AbstractFile;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_16LE;

/**
 * Created by mwright on 1/11/17.
 */
public class AbstractFileWriter {
    private static final Logger LOGGER = Logger.getLogger(AbstractFileWriter.class);

    /**
     * Writes all the data records back to a byte array (ArrayList), same structure as the original file
     *
     * Helpfulness score: **** . OK, but the explanation is somewhat unclear.
     *
     * @param bytes
     * @return
     */
    public List<Byte> writeData(List<Byte> bytes, AbstractFile edfFile) {
        List<Byte> ret = new ArrayList<>(bytes);
        ret.remove(ret.size()-1);
        int[] offsetStart = new int[edfFile.getSignals().length];
        Arrays.fill(offsetStart, 0);
        int[] offsetEnd = new int[edfFile.getSignals().length];

        for(int i = 0; i < edfFile.getSignals().length; i++) {
            offsetEnd[i] = edfFile.getNrOfSamplesPerSignalInEachRecord()[i];
        }

        for (int i = 0; i < edfFile.getNumberOfDataRecords(); i++) {
            for (int j = 0; j < edfFile.getSignals().length; j++) {
                ret = convertToByte(edfFile, ret, j, offsetStart[j], offsetEnd[j]);
                offsetStart[j] = offsetStart[j] + edfFile.getNrOfSamplesPerSignalInEachRecord()[j];
                offsetEnd[j] = offsetEnd[j] + edfFile.getNrOfSamplesPerSignalInEachRecord()[j];
            }
        }
        return ret;
    }

    /**
     * Converts an integer into 2 bytes in 2's complement format. The byte endianness is little endian.
     *
     * @param bytes
     *      the ArrayList where the bytes will be stored
     * @param signalIndex
     *       indicates the current signal
     * @param start
     *       the start index for the conversion
     * @param end
     *       the end index for the conversion
     * @return
     *       the ArrayList containing the new bytes
     */
    public List<Byte> convertToByte(AbstractFile edfFile, List<Byte> bytes, int signalIndex, int start, int end) {
        for (int i = start; i < end; i++) {
            int tempInt = edfFile.getSignals()[signalIndex].getValues()[i];
            bytes.add((byte)(tempInt & 0xff));
            bytes.add((byte)((tempInt >> 8) & 0xff));
        }
        return bytes;
    }

    /**
     * Converts a string to a byte array
     *
     * Helpfulness score: *
     *
     * @param s
     * @return
     */
    public byte[] stringToByte(String s) {
        if(s.length() == 0) {
            return new byte[0];
        }
        byte [] temp = s.getBytes(UTF_16LE);
        byte [] ret = new byte[(int)(temp.length/2.)];
        ret[0] = temp[0];
        int j = 1;
        for (int i = 1; i < temp.length; i++) {
            if (i%2 == 0) {
                ret[j] = temp[i];
                j++;
            }
        }
        return ret;
    }

    /**
     * Converts an integer to a byte array
     *
     * Helpfulness score: *
     *
     * @param a
     * @return
     */
    public byte[] intToByte(int a) {
        String s = ""+a;
        byte [] temp = s.getBytes(UTF_16LE);
        byte [] ret = new byte[(int)(temp.length/2.)];
        ret[0] = temp[0];
        int j = 1;
        for (int i = 1; i < temp.length; i++) {
            if (i%2 == 0) {
                ret[j] = temp[i];
                j++;
            }
        }
        return ret;
    }

    /**
     * Writes the byte array to an actual file
     *
     * Helpfulness score: ** . Also, consider naming the method writeByteArrayToFile
     *
     * @param bytes
     * @param path
     * @throws FileNotFoundException
     */
    public void writeFile(List<Byte> bytes, String path) throws FileNotFoundException {
        FileOutputStream fos = null;
        File file;
        try {
            file = new File(path);
            fos = new FileOutputStream(file);

            if(!file.exists()) {
                file.createNewFile();
            }

            byte[] byteArray = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                byteArray[i] = bytes.get(i);
            }

            fos.write(byteArray);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
