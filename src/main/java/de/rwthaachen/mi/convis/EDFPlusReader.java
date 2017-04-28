package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.AnnotationSignal;
import de.rwthaachen.mi.convis.data.EDFPlusFile;
import de.rwthaachen.mi.convis.data.Signal;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by mwright on 9/28/16.
 *
 * Parses an EDF+ file and stores it into EDFPlusFile object
 */
public class EDFPlusReader extends AbstractFileReader {
    private static final Logger LOGGER = Logger.getLogger(EDFPlusReader.class);
    private static final String UTF_8 = "UTF-8";
    private static final int ASCII_NUMBER_BOTTOM = 48;
    private static final int ASCII_NUMBER_TOP = 57;

    /**
     * Parses the byte array into variables for the header
     * @param data
     * @return
     */
    @Override
    public EDFPlusFile parseHeaderData(byte[] data) {
        //Parsing of the data in the header record
        //the byte allocation can be found on http://edfplus.info/specs/edfplus.html
        int b = 0;
        int c = 8;
        String versionOfThisDataFormat = byteToString(splitBytes(data, b, c));
        b = c;
        c = c+80;
        String localPatientIdentification = byteToString(splitBytes(data, b, c));
        b = c;
        c = c+80;
        String localRecordingIdentification = byteToString(splitBytes(data, b, c));
        b = c;
        c = c+8;
        String startdateOfRecording = byteToString(splitBytes(data, b, c));// dd.mm.yy
        b = c;
        c = c+8;
        String starttimeOfRecordng = byteToString(splitBytes(data, b, c)); //hh.mm.ss
        b = c;
        c = c+8;
        int numberOfBytesInHeaderRecord = Integer.parseInt(byteToString(splitBytes(data, b, c)).trim());
        b = c;
        c = c+44;
        String reserved1 = byteToString(splitBytes(data, b, c));
        b = c;
        c = c+8;
        int numberOfDataRecords = Integer.parseInt(byteToString(splitBytes(data, b, c)).trim()); //-1 if unknown
        b = c;
        c = c+8;
        int durationOfADataRecord = (int)Double.parseDouble(byteToString(splitBytes(data, b, c)).trim()); //in seconds
        b = c;
        c = c+4;
        int numberOfSignalsInDataRecord = Integer.parseInt(byteToString(splitBytes(data, b, c)).trim()); //ns

        int ns = numberOfSignalsInDataRecord;

        //Labels of the signals
        b = c;
        c = c+16*ns;
        byte[] arrayLabels = splitBytes(data, b ,c);

        //each label represents the name of a signal
        //i stored them in an array so you can find the fitting name right away
        String[] labels = new String[(int)(arrayLabels.length/16.)];
        int startLab = 0;
        int endLab = 15;
        for (int i = 0; i < labels.length; i++) {
            labels[i] = byteToString(splitBytes(arrayLabels, startLab, endLab)).trim();
            startLab = startLab + 16;
            endLab = endLab + 16;
        }

        b = c;
        c = c+ns*80;
        byte[] transducer = splitBytes(data, b, c);
        String[] transducerType = new String[(int)(transducer.length/80.)];
        int startTrans = 0;
        int endTrans = 79;
        for(int i = 0; i < transducerType.length; i++) {
            transducerType[i] = byteToString(splitBytes(transducer, startTrans, endTrans)).trim();
            startTrans = startTrans + 80;
            endTrans = endTrans + 80;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalDim = splitBytes(data, b, c);
        String[] physicalDimension = new String[(int)(physicalDim.length/8.)];
        int startDim = 0;
        int endDim = 7;
        for (int i = 0; i < physicalDimension.length; i++) {
            physicalDimension[i] = byteToString(splitBytes(physicalDim, startDim, endDim)).trim();
            startDim = startDim + 8;
            endDim = endDim + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalMin = splitBytes(data, b, c);
        String[] physicalMinimum = new String[(int)(physicalMin.length/8.)];
        int startMin = 0;
        int endMin = 7;
        for (int i = 0; i < physicalMinimum.length; i++) {
            physicalMinimum[i] = byteToString(splitBytes(physicalMin, startMin, endMin)).trim();
            startMin = startMin + 8;
            endMin = endMin + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalMax = splitBytes(data, b, c);
        String[] physicalMaximum = new String[(int)(physicalMax.length/8.)];
        int startMax = 0;
        int endMax = 7;
        for(int i = 0; i < physicalMaximum.length; i++) {
            physicalMaximum[i] = byteToString(splitBytes(physicalMax, startMax, endMax)).trim();
            startMax = startMax + 8;
            endMax = endMax + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] digitalMin = splitBytes(data, b, c);
        String[] digitalMinimum = new String[(int)(digitalMin.length/8.)];
        int startDigitalMin = 0;
        int endDigitalMin = 7;
        for(int i = 0; i < digitalMinimum.length; i++) {
            digitalMinimum[i] = byteToString(splitBytes(digitalMin, startDigitalMin, endDigitalMin)).trim();
            startDigitalMin = startDigitalMin + 8;
            endDigitalMin = endDigitalMin + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] digitalMax = splitBytes(data, b, c);
        String[] digitalMaximum = new String[(int)(digitalMax.length/8.)];
        int startDigitalMax = 0;
        int endDigitalMax = 7;
        for(int i = 0; i < digitalMaximum.length; i++) {
            digitalMaximum[i] = byteToString(splitBytes(digitalMax, startDigitalMax, endDigitalMax)).trim();
            startDigitalMax = startDigitalMax + 8;
            endDigitalMax = endDigitalMax + 8;
        }

        b = c;
        c = c+ns*80;
        String prefiltering = byteToString(splitBytes(data, b, c));
        int c1 = c;
        c = c+ns*8;
        String nrOfSamplesPerSignalInEachRecord = byteToString(splitBytes(data, c1, c));
        byte[] arrayOfSamplesInEachDataRecord = splitBytes(data, c1, c);
        int[] nrOfSamplesInEachDataRecords = new int[(int)(arrayOfSamplesInEachDataRecord.length/8.)];
        int begin = 0;
        int ending = 7;
        for (int i = 0; i < nrOfSamplesInEachDataRecords.length; i++) {
            nrOfSamplesInEachDataRecords[i] = Integer.parseInt(byteToString(splitBytes(arrayOfSamplesInEachDataRecord, begin, ending)).trim());
            begin = begin + 8;
            ending = ending + 8;
        }

        b = c;
        c = c+ns*32;
        String reserved2 = byteToString(splitBytes(data, b, c));

        //Storing of the header information in the EDFPlusFile object
        EDFPlusFile edfPlusFile = new EDFPlusFile();
        edfPlusFile.setVersionOfThisDataFormat(versionOfThisDataFormat);
        //localPatientIdentification
        String [] localPatId = localPatientIdentification.split(" ");
        edfPlusFile.setHospitalPatientId(localPatId[0].trim());
        edfPlusFile.setSex(localPatId[1].trim());
        edfPlusFile.setBirthdate(localPatId[2].trim());
        edfPlusFile.setName(localPatId[3].trim());
        //localRecordingIdentification
        String[] localRecId = localRecordingIdentification.split(" ");
        edfPlusFile.setStartdateRecId(localRecId[1].trim());
        edfPlusFile.setHospitalAdministrationCode(localRecId[2].trim());
        edfPlusFile.setResponsibleInvestigator(localRecId[3].trim());
        edfPlusFile.setUsedEquipment(localRecId[4].trim());
        //
        edfPlusFile.setStartdateOfRecording(startdateOfRecording.trim());
        edfPlusFile.setStarttimeOfRecordng(starttimeOfRecordng.trim());
        edfPlusFile.setNumberOfBytesInHeaderRecord(numberOfBytesInHeaderRecord);
        edfPlusFile.setReserved1(reserved1.trim());
        edfPlusFile.setNumberOfDataRecords(numberOfDataRecords);
        edfPlusFile.setDurationOfADataRecord(durationOfADataRecord);
        edfPlusFile.setNumberOfSignalsInDataRecord(numberOfSignalsInDataRecord);
        edfPlusFile.setLabels(labels);
        edfPlusFile.setTransducerType(transducerType);
        edfPlusFile.setPhysicalDimension(physicalDimension);
        edfPlusFile.setPhysicalMinimum(physicalMinimum);
        edfPlusFile.setPhysicalMaximum(physicalMaximum);
        edfPlusFile.setDigitalMinimum(digitalMinimum);
        edfPlusFile.setDigitalMaximum(digitalMaximum);
        edfPlusFile.setPrefiltering(prefiltering);
        edfPlusFile.setNrOfSamplesPerSignalInEachRecord(nrOfSamplesInEachDataRecords);
        edfPlusFile.setReserved2(reserved2);

        byte[] records = splitBytes(data, numberOfBytesInHeaderRecord, data.length);
        Signal[] signals = storeSignals(records, numberOfSignalsInDataRecord, numberOfDataRecords, nrOfSamplesInEachDataRecords);
        edfPlusFile.setSignals(signals);

        //Checking if annotations signal is existent
        int annotationIndex = findAnnotationSignal(labels);
        edfPlusFile.setAnnotationsIndex(annotationIndex);
        if(annotationIndex != -1) {
            int[] annotationsInt = edfPlusFile.getSignals()[annotationIndex].getValues();
            //edfPlusFile.signals = extractAnnotationSignal(edfPlusFile.signals, annotationIndex);
            byte[] annotationsByte = convertToByte(annotationsInt);
            ArrayList<AnnotationSignal> annotationsAList = parseAnnotationsSignal(annotationsByte, new ArrayList<>());
            edfPlusFile.setAnnotations(handleAnnotations(annotationsAList));
        }

        return edfPlusFile;
    }

    private AnnotationSignal[] handleAnnotations(ArrayList<AnnotationSignal> annotationsAList) {
        AnnotationSignal[] annotations = new AnnotationSignal[annotationsAList.size()];
        for(int i = 0; i < annotationsAList.size(); i++) {
            annotations[i] = annotationsAList.get(i);
        }
        return annotations;
    }

    /**
     * Parses the EDF Annotations signal and stores the sign, onset, duration and the annotation text into a object
     * @param annotationsByte
     *      EDF Annotations signal in bytes
     * @param bucket
     *      every recursive function call adds a Time-stamped Annotations List (TAL) to this ArrayList
     * @return
     *      ArrayList of all the Time-stamped Annotations Lists (TALs) contained in the EDF Annotations signal
     */
    private ArrayList<AnnotationSignal> parseAnnotationsSignal(byte[] annotationsByte, ArrayList<AnnotationSignal> bucket) {
        AnnotationSignal annotationSignal = new AnnotationSignal();
        //either '+' or '-'
        annotationSignal.setSign((char)annotationsByte[0]);

        //Parse the onset and store it
        int endOfOnset = 0;
        String onsetStr = "";
        for(int i = 1; i < annotationsByte.length; i++) {
            if(annotationsByte[i] == 20) {
                endOfOnset = i;
                try {
                    onsetStr = new String(splitBytes(annotationsByte, 1, i), UTF_8);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e);
                }
                char[] onsetChar = onsetStr.toCharArray();
                for(int j = 0; j < onsetChar.length; j++) {
                    char tempChar = onsetChar[j];
                    if(tempChar < ASCII_NUMBER_BOTTOM || tempChar > ASCII_NUMBER_TOP) {
                        onsetChar[j] = '.';
                    }
                }
                onsetStr = new String(onsetChar);
                double onsetDbl = Double.parseDouble(onsetStr);
                annotationSignal.setOnset(onsetDbl);
                break;
            }
        }

        //if 'duration' exists, parse and store it
        int endOfDuration = 0;
        String durationStr = "";
        if(annotationsByte[endOfOnset] == 21) {
            for(int i = endOfOnset; i < annotationsByte.length; i++) {
                if(annotationsByte[i] == 20) {
                    endOfDuration = i;
                    try {
                        durationStr = new String(splitBytes(annotationsByte, endOfOnset+1, i), UTF_8);
                    } catch (UnsupportedEncodingException e) {
                        LOGGER.error(e);
                    }
                    char[] durationChar = durationStr.toCharArray();
                    for(int j = 0; j < durationChar.length; j++) {
                        char tempChar = durationChar[j];
                        if(tempChar < ASCII_NUMBER_BOTTOM || tempChar > ASCII_NUMBER_TOP) {
                            durationChar[j] = '.';
                        }
                    }
                    durationStr = new String(durationChar);
                    double durationDbl = Double.parseDouble(durationStr);
                    annotationSignal.setDuration(durationDbl);
                    break;
                }
            }
        } else {
            annotationSignal.setDuration(0);
            endOfDuration = endOfOnset;
        }

        //parse the actual annotations
        int temp = endOfDuration+1;
        for(int i = endOfDuration+1; i < annotationsByte.length; i++) {
            if(annotationsByte[i] == 0 && annotationsByte[i+1] == 0 && annotationsByte[i+2] == 0){
                return bucket;
            }

            if(annotationsByte[i] == 20) {
                String annotationTxt = "";
                try {
                    annotationTxt = new String(splitBytes(annotationsByte, temp, i), UTF_8);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e);
                }
                annotationSignal.addAnnotation(annotationTxt);
                temp = i+1;
            }

            if(annotationsByte[i] == 20 && annotationsByte[i+1] == 0) {
                bucket.add(annotationSignal);
                return parseAnnotationsSignal(splitBytes(annotationsByte, i+2, annotationsByte.length), bucket);
            }

        }
        return bucket;
    }

    private byte[] convertToByte(int[] values) {
        ArrayList<Byte> temp = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            int tempInt = values[i];
            temp.add((byte)(tempInt & 0xFF));
            temp.add((byte)((tempInt >> 8) & 0xFF));
        }
        byte[] ret = new byte[temp.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = temp.get(i);
        }
        return ret;
    }

    private Signal[] extractAnnotationSignal(Signal[] signals, int annotationIndex) {
        Signal[] ret = new Signal[signals.length-1];
        for(int i = 0; i < signals.length; i++) {
            if(i != annotationIndex) {
                ret[i] = signals[i];
            }
        }
        return ret;
    }

    /**
     * Iterates through the label names and returns the index of the 'EDFAnnotaions' signal.
     * Returns -1 if the signal doesn't exist
     *
     * @param labels
     *      string array - signal labels
     * @return
     *      index of EDFAnnotations signal
     */
    private int findAnnotationSignal(String[] labels) {
        for(int i = 0; i < labels.length; i++) {
            if("EDF Annotations".equals(labels[i])) {
                return i;
            }
        }
        return -1;
    }

}
