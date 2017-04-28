package de.rwthaachen.mi.convis.data;

/**
 * Created by mwright on 1/11/17.
 */
abstract public class AbstractFile {
    //8 bytes
    private String versionOfThisDataFormat;
    //8 bytes
    private String startdateOfRecording; //dd.mm.yy
    //8 bytes
    private String starttimeOfRecordng; //hh.mm.ss
    //8 bytes
    private int numberOfBytesInHeaderRecord;
    //44 bytes
    private String reserved1;
    //8 bytes
    private int numberOfDataRecords; // -1 if unknown
    //8 bytes
    private int durationOfADataRecord; // in seconds
    //4 bytes
    private int numberOfSignalsInDataRecord; // ns
    //ns*16 bytes
    private String[] labels;
    //ns*80 bytes
    private String[] transducerType;
    //ns*8 bytes
    private String[] physicalDimension;
    //ns*8 bytes
    private String[] physicalMinimum;
    //ns*8 bytes
    private String[] physicalMaximum;
    //ns*8 bytes
    private String[] digitalMinimum;
    //ns*8 bytes
    private String[] digitalMaximum;
    //ns*80 bytes
    private String prefiltering;
    //ns*8 bytes
    private int[] nrOfSamplesPerSignalInEachRecord;
    //ns*32 bytes
    private String reserved2;

    //data records
    private Signal[] signals;

    public String getVersionOfThisDataFormat() {
        return versionOfThisDataFormat;
    }

    public void setVersionOfThisDataFormat(String versionOfThisDataFormat) {
        this.versionOfThisDataFormat = versionOfThisDataFormat;
    }

    public String getStartdateOfRecording() {
        return startdateOfRecording;
    }

    public void setStartdateOfRecording(String startdateOfRecording) {
        this.startdateOfRecording = startdateOfRecording;
    }

    public String getStarttimeOfRecordng() {
        return starttimeOfRecordng;
    }

    public void setStarttimeOfRecordng(String starttimeOfRecordng) {
        this.starttimeOfRecordng = starttimeOfRecordng;
    }

    public int getNumberOfBytesInHeaderRecord() {
        return numberOfBytesInHeaderRecord;
    }

    public void setNumberOfBytesInHeaderRecord(int numberOfBytesInHeaderRecord) {
        this.numberOfBytesInHeaderRecord = numberOfBytesInHeaderRecord;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public int getNumberOfDataRecords() {
        return numberOfDataRecords;
    }

    public void setNumberOfDataRecords(int numberOfDataRecords) {
        this.numberOfDataRecords = numberOfDataRecords;
    }

    public int getDurationOfADataRecord() {
        return durationOfADataRecord;
    }

    public void setDurationOfADataRecord(int durationOfADataRecord) {
        this.durationOfADataRecord = durationOfADataRecord;
    }

    public int getNumberOfSignalsInDataRecord() {
        return numberOfSignalsInDataRecord;
    }

    public void setNumberOfSignalsInDataRecord(int numberOfSignalsInDataRecord) {
        this.numberOfSignalsInDataRecord = numberOfSignalsInDataRecord;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public String[] getTransducerType() {
        return transducerType;
    }

    public void setTransducerType(String[] transducerType) {
        this.transducerType = transducerType;
    }

    public String[] getPhysicalDimension() {
        return physicalDimension;
    }

    public void setPhysicalDimension(String[] physicalDimension) {
        this.physicalDimension = physicalDimension;
    }

    public String[] getPhysicalMinimum() {
        return physicalMinimum;
    }

    public void setPhysicalMinimum(String[] physicalMinimum) {
        this.physicalMinimum = physicalMinimum;
    }

    public String[] getPhysicalMaximum() {
        return physicalMaximum;
    }

    public void setPhysicalMaximum(String[] physicalMaximum) {
        this.physicalMaximum = physicalMaximum;
    }

    public String[] getDigitalMinimum() {
        return digitalMinimum;
    }

    public void setDigitalMinimum(String[] digitalMinimum) {
        this.digitalMinimum = digitalMinimum;
    }

    public String[] getDigitalMaximum() {
        return digitalMaximum;
    }

    public void setDigitalMaximum(String[] digitalMaximum) {
        this.digitalMaximum = digitalMaximum;
    }

    public String getPrefiltering() {
        return prefiltering;
    }

    public void setPrefiltering(String prefiltering) {
        this.prefiltering = prefiltering;
    }

    public int[] getNrOfSamplesPerSignalInEachRecord() {
        return nrOfSamplesPerSignalInEachRecord;
    }

    public void setNrOfSamplesPerSignalInEachRecord(int[] nrOfSamplesPerSignalInEachRecord) {
        this.nrOfSamplesPerSignalInEachRecord = nrOfSamplesPerSignalInEachRecord;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public Signal[] getSignals() {
        return this.signals;
    }

    public void setSignals(Signal[] signals) {
        this.signals = signals;
    }

}
