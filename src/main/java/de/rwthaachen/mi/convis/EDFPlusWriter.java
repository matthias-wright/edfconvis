package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.EDFPlusFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwright on 6/15/16.
 *
 * Writes the content of an EDFPlusFile object to a local file specified by its path
 *
 */
public class EDFPlusWriter extends AbstractFileWriter {
    private EDFPlusFile edfplusfile;

    public EDFPlusWriter(EDFPlusFile edfplusfile) {
        this.edfplusfile = edfplusfile;
    }

    public List<Byte> writeHeader() {
        List<Byte> bytes = new ArrayList<>();

        //8 ascii : version of this data format (0)
        for (byte b : stringToByte(edfplusfile.getVersionOfThisDataFormat())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfplusfile.getVersionOfThisDataFormat()).length; i++) {
            bytes.add((byte) 32);
        }

        //80 ascii : local patient identification
        String localPatientId = edfplusfile.getHospitalPatientId() + " " + edfplusfile.getSex() + " " + edfplusfile.getBirthdate() + " " + edfplusfile.getName();
        for (byte b : stringToByte(localPatientId)) {
            bytes.add(b);
        }

        for (int i = 0; i < 80 - stringToByte(localPatientId).length; i++) {
            bytes.add((byte) 32);
        }

        //80 ascii : local recording identification.
        String localRecId = edfplusfile.getStartdateString() + " " + edfplusfile.getStartdateRecId() + " " + edfplusfile.getHospitalAdministrationCode() + " " + edfplusfile.getResponsibleInvestigator() + " " + edfplusfile.getUsedEquipment();
        for (byte b : stringToByte(localRecId)) {
            bytes.add(b);
        }

        for (int i = 0; i < 80 - stringToByte(localRecId).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : startdate of recording (dd.mm.yy)
        for (byte b : stringToByte(edfplusfile.getStartdateOfRecording())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfplusfile.getStartdateOfRecording()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : starttime of recording (hh.mm.ss)
        for (byte b : stringToByte(edfplusfile.getStarttimeOfRecordng())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - stringToByte(edfplusfile.getStarttimeOfRecordng()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : number of bytes in header record
        for (byte b : intToByte(edfplusfile.getNumberOfBytesInHeaderRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfplusfile.getNumberOfBytesInHeaderRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //44 ascii : reserved
        for (byte b : stringToByte(edfplusfile.getReserved1())) {
            bytes.add(b);
        }

        for (int i = 0; i < 44 - stringToByte(edfplusfile.getReserved1()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : number of data records (-1 if unknown)
        for (byte b : intToByte(edfplusfile.getNumberOfDataRecords())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfplusfile.getNumberOfDataRecords()).length; i++) {
            bytes.add((byte) 32);
        }

        //8 ascii : duration of a data record, in seconds
        for (byte b : intToByte(edfplusfile.getDurationOfADataRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 8 - intToByte(edfplusfile.getDurationOfADataRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //4 ascii : number of signals (ns) in data record
        for (byte b : intToByte(edfplusfile.getNumberOfSignalsInDataRecord())) {
            bytes.add(b);
        }

        for (int i = 0; i < 4 - intToByte(edfplusfile.getNumberOfSignalsInDataRecord()).length; i++) {
            bytes.add((byte) 32);
        }

        //ns * 16 ascii : ns * label
        for (int i = 0; i < edfplusfile.getLabels().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getLabels()[i]).length != 0){
                array = stringToByte(edfplusfile.getLabels()[i]);
            }
            for (int j = 0; j < 16; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 80 ascii : ns * transducer type (e.g. AgAgCl electrode)
        for (int i = 0; i < edfplusfile.getTransducerType().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getTransducerType()[i]).length != 0) {
                array = stringToByte(edfplusfile.getTransducerType()[i]);
            }
            for (int j = 0; j < 80; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //transducer type holds always 1 element less then physical minimum, physical maximum, digital minimum, digital maximum. If the space for the missing element isn't added there will be an error while parsing
        for (int i = 0; i < 80; i++) {
            bytes.add((byte) 32);
        }

        //ns * 8 ascii : ns * physical dimension (e.g. uV)
        for (int i = 0; i < edfplusfile.getPhysicalDimension().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getPhysicalDimension()[i]).length != 0) {
                array = stringToByte(edfplusfile.getPhysicalDimension()[i]);
            }
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //physical dimension holds always 1 element less then physical minimum, physical maximum, digital minimum, digital maximum. If the space for the missing element isn't added there will be an error while parsing
        for (int i = 0; i < 8; i++) {
            bytes.add((byte) 32);
        }

        //ns * 8 ascii : ns * physical minimum (e.g. -500 or 34)
        for (int i = 0; i < edfplusfile.getPhysicalMinimum().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getPhysicalMinimum()[i]).length != 0) {
                array = stringToByte(edfplusfile.getPhysicalMinimum()[i]);
            }
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 8 ascii : ns * physical maximum (e.g. 500 or 40)
        for (int i = 0; i < edfplusfile.getPhysicalMaximum().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getPhysicalMaximum()[i]).length != 0) {
                array = stringToByte(edfplusfile.getPhysicalMaximum()[i]);
            }
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 8 ascii : ns * digital minimum (e.g. -2048)
        for (int i = 0; i < edfplusfile.getDigitalMinimum().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getDigitalMinimum()[i]).length != 0) {
                array = stringToByte(edfplusfile.getDigitalMinimum()[i]);
            }
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 8 ascii : ns * digital maximum (e.g. 2047)
        for (int i = 0; i < edfplusfile.getDigitalMaximum().length; i++) {
            byte[] array = null;
            if(stringToByte(edfplusfile.getDigitalMaximum()[i]).length != 0) {
                array = stringToByte(edfplusfile.getDigitalMaximum()[i]);
            }
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 80 ascii : ns * prefiltering (e.g. HP:0.1Hz LP:75Hz)
        for (byte b : stringToByte(edfplusfile.getPrefiltering())) {
            bytes.add(b);
        }

        for (int i = 0; i < (80 * edfplusfile.getNumberOfSignalsInDataRecord()) - stringToByte(edfplusfile.getPrefiltering()).length; i++) {
            bytes.add((byte) 32);
        }

        bytes.add((byte) 32);

        //ns * 8 ascii : ns * nr of samples in each data record
        for (int i = 0; i < edfplusfile.getNrOfSamplesPerSignalInEachRecord().length; i++) {
            byte[] array = intToByte(edfplusfile.getNrOfSamplesPerSignalInEachRecord()[i]);
            for (int j = 0; j < 8; j++) {
                if (j >= array.length) {
                    bytes.add((byte) 32);
                } else {
                    bytes.add(array[j]);
                }
            }
        }

        //ns * 32 ascii : ns * reserved
        for (byte b : stringToByte(edfplusfile.getReserved2())) {
            bytes.add(b);
        }

        for (int i = 0; i < (32 * edfplusfile.getNumberOfSignalsInDataRecord()) - stringToByte(edfplusfile.getReserved2()).length; i++) {
            bytes.add((byte) 32);
        }
        return bytes;
    }

}
