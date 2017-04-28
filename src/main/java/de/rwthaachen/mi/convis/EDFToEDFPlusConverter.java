package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.EDFFile;
import de.rwthaachen.mi.convis.data.EDFPlusFile;
import de.rwthaachen.mi.convis.data.Signal;

/**
 * Created by mwright on 9/28/16.
 *
 * Converts an EDF file to an EDF+ file
 *
 * Helpfulness score: **. Consider naming the class a SomethingToSomethingConverter class.
 */
public class EDFToEDFPlusConverter {

    /**
     * Fills the EDF+ file with the information from the EDF file
     *
     * Helpfulness score: ** . Kinda stating the obvious.
     * @return
     */
    public EDFPlusFile convert(EDFFile edfFile) {
        EDFPlusFile edfPlusFile = new EDFPlusFile();

        edfPlusFile.setVersionOfThisDataFormat(edfFile.getVersionOfThisDataFormat());
        String[] localPatientId = edfFile.getLocalPatientIdentification().split(" ");
        if(localPatientId.length > 1) {
            edfPlusFile.setHospitalPatientId(localPatientId[0]);
            edfPlusFile.setSex(localPatientId[1]);
            edfPlusFile.setBirthdate(localPatientId[2]);
            edfPlusFile.setName(localPatientId[3]);
        } else {
            edfPlusFile.setHospitalPatientId("X");
            edfPlusFile.setSex("X");
            edfPlusFile.setBirthdate("X");
            edfPlusFile.setName(localPatientId[0]);
        }
        String[] localRecordingId = edfFile.getLocalRecordingIdentification().split(" ");
        if(localRecordingId.length>1) {
            edfPlusFile.setStartdateRecId(localRecordingId[1]);
            edfPlusFile.setHospitalAdministrationCode(localRecordingId[2]);
            edfPlusFile.setResponsibleInvestigator(localRecordingId[3]);
            //"                                                     "//
            edfPlusFile.setUsedEquipment(localRecordingId[4]);
        } else {
            edfPlusFile.setStartdateRecId("X");
            edfPlusFile.setHospitalAdministrationCode("X");
            edfPlusFile.setResponsibleInvestigator("X");
            edfPlusFile.setUsedEquipment("X");
        }

        edfPlusFile.setStartdateOfRecording(edfFile.getStartdateOfRecording());
        edfPlusFile.setStarttimeOfRecordng(edfFile.getStarttimeOfRecordng());
        edfPlusFile.setNumberOfBytesInHeaderRecord(edfFile.getNumberOfBytesInHeaderRecord());
        edfPlusFile.setReserved1("EDF+C");
        edfPlusFile.setNumberOfDataRecords(edfFile.getNumberOfDataRecords());
        edfPlusFile.setDurationOfADataRecord(edfFile.getDurationOfADataRecord());
        edfPlusFile.setNumberOfSignalsInDataRecord(edfFile.getNumberOfSignalsInDataRecord());

        edfPlusFile.setLabels(new String[edfFile.getLabels().length]);
        for (int i = 0; i < edfFile.getLabels().length; i++) {
            edfPlusFile.getLabels()[i] = edfFile.getLabels()[i];
        }

        edfPlusFile.setTransducerType(edfFile.getTransducerType());
        edfPlusFile.setPhysicalDimension(edfFile.getPhysicalDimension());
        edfPlusFile.setPhysicalMinimum(edfFile.getPhysicalMinimum());
        edfPlusFile.setPhysicalMaximum(edfFile.getPhysicalMaximum());
        edfPlusFile.setDigitalMinimum(edfFile.getDigitalMinimum());
        edfPlusFile.setDigitalMaximum(edfFile.getDigitalMaximum());
        edfPlusFile.setPrefiltering(edfFile.getPrefiltering());

        edfPlusFile.setNrOfSamplesPerSignalInEachRecord(new int[edfFile.getNrOfSamplesPerSignalInEachRecord().length]);
        for (int i = 0; i < edfFile.getNrOfSamplesPerSignalInEachRecord().length; i++) {
            edfPlusFile.getNrOfSamplesPerSignalInEachRecord()[i] = edfFile.getNrOfSamplesPerSignalInEachRecord()[i];
        }

        edfPlusFile.setReserved2(edfFile.getReserved2());

        edfPlusFile.setSignals(new Signal[edfFile.getSignals().length]);
        for (int i = 0; i < edfFile.getSignals().length; i++) {
            edfPlusFile.getSignals()[i] = new Signal(edfFile.getSignals()[i].getValues().length);
            edfPlusFile.getSignals()[i].setValues(edfFile.getSignals()[i].getValues());
        }

        return edfPlusFile;
    }

}
