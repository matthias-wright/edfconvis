package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.EDFFile;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mwright on 1/11/17.
 */
public class EDFWriter extends AbstractFileWriter {
    private EDFFile edfFile;

    public EDFWriter(EDFFile edfFile) {
        this.edfFile = edfFile;
    }

    public List<Byte> writeHeader() {
        List<Byte> bytes = new ArrayList<>();

        //8 ascii : version of this data format (0)
        for (byte b : stringToByte(edfFile.getVersionOfThisDataFormat())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfFile.getVersionOfThisDataFormat()).length; i++) {
            bytes.add((byte) 32);
        }

        //80 ascii : local patient identification
        String localPatientId = edfFile.getLocalPatientIdentification();
        for (byte b : stringToByte(localPatientId)) {
            bytes.add(b);
        }

        for (int i = 0; i < 80 - stringToByte(localPatientId).length; i++) {
            bytes.add((byte) 32);
        }

        //80 ascii : local recording identification.
        String localRecId = edfFile.getLocalRecordingIdentification();
        for (byte b : stringToByte(localRecId)) {
            bytes.add(b);
        }

        for (int i = 0; i < 80 - stringToByte(localRecId).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : startdate of recording (dd.mm.yy)
        for (byte b : stringToByte(edfFile.getStartdateOfRecording())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfFile.getStartdateOfRecording()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : starttime of recording (hh.mm.ss)
        for (byte b : stringToByte(edfFile.getStarttimeOfRecordng())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfFile.getStarttimeOfRecordng()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : number of bytes in header record
        for (byte b : intToByte(edfFile.getNumberOfBytesInHeaderRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfFile.getNumberOfBytesInHeaderRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //44 ascii : reserved
        for (byte b : stringToByte(edfFile.getReserved1())) {
            bytes.add(b);
        }

        for (int i = 0; i < 44 - stringToByte(edfFile.getReserved1()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : number of data records (-1 if unknown)
        for (byte b : intToByte(edfFile.getNumberOfDataRecords())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfFile.getNumberOfDataRecords()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : duration of a data record, in seconds
        for (byte b : intToByte(edfFile.getDurationOfADataRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfFile.getDurationOfADataRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //4 ascii : number of signals (ns) in data record
        for (byte b : intToByte(edfFile.getNumberOfSignalsInDataRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 4 - intToByte(edfFile.getNumberOfSignalsInDataRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //ns * 16 ascii : ns * label
        for (int i = 0; i < edfFile.getLabels().length; i++) {
            byte[] array = stringToByte(edfFile.getLabels()[i]);
            for (int j = 0; j < 16; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 80 ascii : ns * transducer type (e.g. AgAgCl electrode)
        for (int i = 0; i < edfFile.getTransducerType().length-1; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getTransducerType()[i])) {
                array = stringToByte(edfFile.getTransducerType()[i]);
                for (int j = 0; j < 80; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //transducer type holds always 1 element less then physical minimum, physical maximum, digital minimum, digital maximum. If the space for the missing element isn't added there will be an error while parsing
        for (int i = 0; i < 80; i++) {
            bytes.add((byte) 32);
        }

        //ns * 8 ascii : ns * physical dimension (e.g. uV)
        for (int i = 0; i < edfFile.getPhysicalDimension().length; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getPhysicalDimension()[i])) {
                array = stringToByte(edfFile.getPhysicalDimension()[i]);
                for (int j = 0; j < 8; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //physical dimension holds always 1 element less then physical minimum, physical maximum, digital minimum, digital maximum. If the space for the missing element isn't added there will be an error while parsing
        for (int i = 0; i < 8; i++) {
            bytes.add((byte) 32);
        }

        //ns * 8 ascii : ns * physical minimum (e.g. -500 or 34)
        for (int i = 0; i < edfFile.getPhysicalMinimum().length; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getPhysicalMinimum()[i])) {
                array = stringToByte(edfFile.getPhysicalMinimum()[i]);
                for (int j = 0; j < 8; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //ns * 8 ascii : ns * physical maximum (e.g. 500 or 40)
        for (int i = 0; i < edfFile.getPhysicalMaximum().length; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getPhysicalMaximum()[i])) {
                array = stringToByte(edfFile.getPhysicalMaximum()[i]);
                for (int j = 0; j < 8; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //ns * 8 ascii : ns * digital minimum (e.g. -2048)
        for (int i = 0; i < edfFile.getDigitalMinimum().length; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getDigitalMinimum()[i])) {
                array = stringToByte(edfFile.getDigitalMinimum()[i]);
                for (int j = 0; j < 8; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //ns * 8 ascii : ns * digital maximum (e.g. 2047)
        for (int i = 0; i < edfFile.getDigitalMaximum().length; i++) {
            byte[] array = null;
            if(!"".equals(edfFile.getDigitalMaximum()[i])) {
                array = stringToByte(edfFile.getDigitalMaximum()[i]);
                for (int j = 0; j < 8; j++) {
                    if (j >= array.length) {
                        bytes.add((byte) 32);
                    } else {
                        bytes.add(array[j]);
                    }
                }
            }
        }

        //ns * 80 ascii : ns * prefiltering (e.g. HP:0.1Hz LP:75Hz)
        for (byte b : stringToByte(edfFile.getPrefiltering())) {
            bytes.add(b);
        }

        for (int i = 0; i < (80 * edfFile.getNumberOfSignalsInDataRecord()) - stringToByte(edfFile.getPrefiltering()).length; i++) {
            bytes.add((byte) 32);
        }

        bytes.add((byte) 32);

        //ns * 8 ascii : ns * nr of samples in each data record
        for (int i = 0; i < edfFile.getNrOfSamplesPerSignalInEachRecord().length; i++) {
            byte[] array = null;
            array = intToByte(edfFile.getNrOfSamplesPerSignalInEachRecord()[i]);
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 32 ascii : ns * reserved
        for (byte b : stringToByte(edfFile.getReserved2())) {
            bytes.add(b);
        }

        for (int i = 0; i < (32 * edfFile.getNumberOfSignalsInDataRecord()) - stringToByte(edfFile.getReserved2()).length; i++) {
            bytes.add((byte) 32);
        }
        return bytes;
    }

}
