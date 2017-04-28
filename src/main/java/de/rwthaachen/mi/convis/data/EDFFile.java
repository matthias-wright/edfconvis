package de.rwthaachen.mi.convis.data;

/**
 * Created by mwright on 6/15/16.
 *
 * Represents an EDF-file
 *
 * Data records are stored in the Signal array
 *
 */
public class EDFFile extends AbstractFile {

    // header information starts here
    private String localPatientIdentification;
    private String localRecordingIdentification;
    // Header data variables end here

    public String getLocalPatientIdentification() {
        return localPatientIdentification;
    }

    public void setLocalPatientIdentification(String localPatientIdentification) {
        this.localPatientIdentification = localPatientIdentification;
    }

    public String getLocalRecordingIdentification() {
        return localRecordingIdentification;
    }

    public void setLocalRecordingIdentification(String localRecordingIdentification) {
        this.localRecordingIdentification = localRecordingIdentification;
    }

}
