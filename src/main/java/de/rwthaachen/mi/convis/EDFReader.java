package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.AbstractFile;
import de.rwthaachen.mi.convis.data.EDFFile;
import de.rwthaachen.mi.convis.data.Signal;

/**
 * Created by mwright on 02.06.16.
 *
 * Parses an EDF file and stores it into EDFFile object
 */
public class EDFReader extends AbstractFileReader {

    /**
     * Parses the byte array into variables for the header
     *
     * @param data
     * @return
     */
    @Override
    public AbstractFile parseHeaderData(byte[] data) {
        EDFReader edfreader = new EDFReader();

        //Parsing of the data in the header record
        //the byte allocation can be found on http://edfplus.info/specs/edf.html
        int b = 0;
        int c = 8;
        String versionOfThisDataFormat = edfreader.byteToString(edfreader.splitBytes(data, b, c));
        b = c;
        c = c+80;
        String localPatientIdentification = edfreader.byteToString(edfreader.splitBytes(data, b, c));
        b = c;
        c = c+80;
        String localRecordingIdentification = edfreader.byteToString(edfreader.splitBytes(data, b, c));
        b = c;
        c = c+8;
        String startdateOfRecording = edfreader.byteToString(edfreader.splitBytes(data, b, c));// dd.mm.yy
        b = c;
        c = c+8;
        String starttimeOfRecordng = edfreader.byteToString(edfreader.splitBytes(data, b, c)); //hh.mm.ss
        b = c;
        c = c+8;
        int numberOfBytesInHeaderRecord = Integer.parseInt(edfreader.byteToString(edfreader.splitBytes(data, b, c)).trim());
        b = c;
        c = c+44;
        String reserved1 = edfreader.byteToString(edfreader.splitBytes(data, b, c));
        b = c;
        c = c+8;
        int numberOfDataRecords = Integer.parseInt(edfreader.byteToString(edfreader.splitBytes(data, b, c)).trim()); //-1 if unknown
        b = c;
        c = c+8;
        int durationOfADataRecord = (int)Double.parseDouble(edfreader.byteToString(edfreader.splitBytes(data, b, c)).trim()); //in seconds
        b = c;
        c = c+4;
        int numberOfSignalsInDataRecord = Integer.parseInt(edfreader.byteToString(edfreader.splitBytes(data, b, c)).trim()); //ns

        int ns = numberOfSignalsInDataRecord;
        b = c;
        c = c+16*ns;
        byte[] arraylabels = edfreader.splitBytes(data, b ,c);

        //each label represents the name of a signal
        //i stored them in an array so you can find the fitting name right away
        String[] labels = new String[(int)(arraylabels.length/16.)];
        int startLab = 0;
        int endLab = 15;
        for (int i = 0; i < labels.length; i++) {
            labels[i] = edfreader.byteToString(edfreader.splitBytes(arraylabels, startLab, endLab)).trim();
            startLab = startLab + 16;
            endLab = endLab + 16;
        }

        b = c;
        c = c+ns*80;
        byte[] transducer = edfreader.splitBytes(data, b, c);
        String[] transducerType = new String[(int)(transducer.length/80.)-1];
        int startTrans = 0;
        int endTrans = 79;
        for(int i = 0; i < transducerType.length; i++) {
            transducerType[i] = edfreader.byteToString(edfreader.splitBytes(transducer, startTrans, endTrans)).trim();
            startTrans = startTrans + 80;
            endTrans = endTrans + 80;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalDim = edfreader.splitBytes(data, b, c);
        String[] physicalDimension = new String[(int)(physicalDim.length/8.)-1];
        int startDim = 0;
        int endDim = 7;
        for (int i = 0; i < physicalDimension.length; i++) {
            physicalDimension[i] = edfreader.byteToString(edfreader.splitBytes(physicalDim, startDim, endDim)).trim();
            startDim = startDim + 8;
            endDim = endDim + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalMin = edfreader.splitBytes(data, b, c);
        String[] physicalMinimum = new String[(int)(physicalMin.length/8.)];
        int startMin = 0;
        int endMin = 7;
        for (int i = 0; i < physicalMinimum.length; i++) {
            physicalMinimum[i] = edfreader.byteToString(edfreader.splitBytes(physicalMin, startMin, endMin)).trim();
            startMin = startMin + 8;
            endMin = endMin + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] physicalMax = edfreader.splitBytes(data, b, c);
        String[] physicalMaximum = new String[(int)(physicalMax.length/8.)];
        int startMax = 0;
        int endMax = 7;
        for(int i = 0; i < physicalMaximum.length; i++) {
            physicalMaximum[i] = edfreader.byteToString(edfreader.splitBytes(physicalMax, startMax, endMax)).trim();
            startMax = startMax + 8;
            endMax = endMax + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] digitalMin = edfreader.splitBytes(data, b, c);
        String[] digitalMinimum = new String[(int)(digitalMin.length/8.)];
        int startDigitalMin = 0;
        int endDigitalMin = 7;
        for(int i = 0; i < digitalMinimum.length; i++) {
            digitalMinimum[i] = edfreader.byteToString(edfreader.splitBytes(digitalMin, startDigitalMin, endDigitalMin)).trim();
            startDigitalMin = startDigitalMin + 8;
            endDigitalMin = endDigitalMin + 8;
        }

        b = c;
        c = c+ns*8;
        byte[] digitalMax = edfreader.splitBytes(data, b, c);
        String[] digitalMaximum = new String[(int)(digitalMax.length/8.)];
        int startDigitalMax = 0;
        int endDigitalMax = 7;
        for(int i = 0; i < digitalMaximum.length; i++) {
            digitalMaximum[i] = edfreader.byteToString(edfreader.splitBytes(digitalMax, startDigitalMax, endDigitalMax)).trim();
            startDigitalMax = startDigitalMax + 8;
            endDigitalMax = endDigitalMax + 8;
        }

        b = c;
        c = c+ns*80;
        String prefiltering = edfreader.byteToString(edfreader.splitBytes(data, b, c));
        int c1 = c;
        c = c+ns*8;
        String nrOfSamplesPerSignalInEachRecord = edfreader.byteToString(edfreader.splitBytes(data, c1, c));

        byte[] arrayrOfSamplesInEachDataRecord = edfreader.splitBytes(data, c1, c);

        int[] nrOfSamplesInEachDataRecords = new int[(int)(arrayrOfSamplesInEachDataRecord.length/8.)];
        int begin = 0;
        int ending = 7;
        for (int i = 0; i < nrOfSamplesInEachDataRecords.length; i++) {
            nrOfSamplesInEachDataRecords[i] = Integer.parseInt(edfreader.byteToString(edfreader.splitBytes(arrayrOfSamplesInEachDataRecord, begin, ending)).trim());
            begin = begin + 8;
            ending = ending + 8;
        }

        b = c;
        c = c+ns*32;
        String reserved2 = edfreader.byteToString(edfreader.splitBytes(data, b, c));

        //Storing of the header information in the EDFFile object
        EDFFile edffile = new EDFFile();
        edffile.setVersionOfThisDataFormat(versionOfThisDataFormat);
        edffile.setLocalPatientIdentification(localPatientIdentification);
        edffile.setLocalRecordingIdentification(localRecordingIdentification);
        edffile.setStartdateOfRecording(startdateOfRecording);
        edffile.setStarttimeOfRecordng(starttimeOfRecordng);
        edffile.setNumberOfBytesInHeaderRecord(numberOfBytesInHeaderRecord);
        edffile.setReserved1(reserved1);
        edffile.setNumberOfDataRecords(numberOfDataRecords);
        edffile.setDurationOfADataRecord(durationOfADataRecord);
        edffile.setNumberOfSignalsInDataRecord(numberOfSignalsInDataRecord);
        edffile.setLabels(labels);
        edffile.setTransducerType(transducerType);
        edffile.setPhysicalDimension(physicalDimension);
        edffile.setPhysicalMinimum(physicalMinimum);
        edffile.setPhysicalMaximum(physicalMaximum);
        edffile.setDigitalMinimum(digitalMinimum);
        edffile.setDigitalMaximum(digitalMaximum);
        edffile.setPrefiltering(prefiltering);
        edffile.setNrOfSamplesPerSignalInEachRecord(nrOfSamplesInEachDataRecords);
        edffile.setReserved2(reserved2);

        //the data record is parsed and stored in a Signal array
        byte[] records = splitBytes(data, numberOfBytesInHeaderRecord, data.length);
        Signal[] signals = storeSignals(records, numberOfSignalsInDataRecord, numberOfDataRecords, nrOfSamplesInEachDataRecords);
        edffile.setSignals(signals);

        return edffile;
    }

}
