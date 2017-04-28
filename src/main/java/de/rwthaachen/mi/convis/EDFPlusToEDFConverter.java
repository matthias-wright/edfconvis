package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.EDFFile;
import de.rwthaachen.mi.convis.data.EDFPlusFile;


/**
 * Created by mwright on 1/5/17.
 *
 * Converts an EDF+ file to an EDF file
 *
 */
public class EDFPlusToEDFConverter {

    public EDFFile convert(EDFPlusFile edfPlusFile) {
        EDFFile edfFile = new EDFFile();

        edfFile.setVersionOfThisDataFormat(edfPlusFile.getVersionOfThisDataFormat());
        edfFile.setLocalPatientIdentification(edfPlusFile.getHospitalPatientId() + " " + edfPlusFile.getSex() + " " + edfPlusFile.getBirthdate() + " " + edfPlusFile.getName());
        edfFile.setLocalRecordingIdentification(edfPlusFile.getStartdateString() + " " + edfPlusFile.getStartdateRecId() + " " + edfPlusFile.getHospitalAdministrationCode() + " " + edfPlusFile.getResponsibleInvestigator() + " " + edfPlusFile.getUsedEquipment());
        edfFile.setStartdateOfRecording(edfPlusFile.getStartdateOfRecording());
        edfFile.setStarttimeOfRecordng(edfPlusFile.getStarttimeOfRecordng());
        edfFile.setNumberOfBytesInHeaderRecord(edfPlusFile.getNumberOfBytesInHeaderRecord());
        edfFile.setReserved1(edfPlusFile.getReserved1());
        edfFile.setNumberOfDataRecords(edfPlusFile.getNumberOfDataRecords());
        edfFile.setDurationOfADataRecord(edfPlusFile.getDurationOfADataRecord());
        edfFile.setNumberOfSignalsInDataRecord(edfPlusFile.getNumberOfSignalsInDataRecord());
        edfFile.setDurationOfADataRecord(edfPlusFile.getDurationOfADataRecord());
        edfFile.setLabels(edfPlusFile.getLabels());
        String[] labelsTemp = edfPlusFile.getLabels();
        for(int i = 0; i < labelsTemp.length; i++) {
            if(labelsTemp[i].trim().matches("EDF.Annotation.")) {
                labelsTemp[i] = "";
            }
        }
        edfFile.setLabels(labelsTemp);
        edfFile.setTransducerType(edfPlusFile.getTransducerType());
        edfFile.setPhysicalDimension(edfPlusFile.getPhysicalDimension());
        edfFile.setPhysicalMinimum(edfPlusFile.getPhysicalMinimum());
        edfFile.setPhysicalMaximum(edfPlusFile.getPhysicalMaximum());
        edfFile.setDigitalMinimum(edfPlusFile.getDigitalMinimum());
        edfFile.setDigitalMaximum(edfPlusFile.getDigitalMaximum());
        edfFile.setPrefiltering(edfPlusFile.getPrefiltering());
        edfFile.setNrOfSamplesPerSignalInEachRecord(edfPlusFile.getNrOfSamplesPerSignalInEachRecord());
        edfFile.setReserved2(edfPlusFile.getReserved2());

        //Check the index of the Annotations Signal
        int annotationsIndex = -1;
        for(int i = 0; i < edfPlusFile.getLabels().length; i++) {
            if("EDF Annotations".equals(edfPlusFile.getLabels()[i])) {
                annotationsIndex = i;
            }
        }

        for(int i = 0; i < edfPlusFile.getLabels().length; i++) {
            edfFile.getLabels()[i] = edfPlusFile.getLabels()[i];
        }

        edfFile.setSignals(edfPlusFile.getSignals());
        if(annotationsIndex != -1) {
            for(int i = 0; i < edfFile.getSignals()[annotationsIndex].getValues().length; i++) {
                edfFile.getSignals()[annotationsIndex].getValues()[i] = 0;
            }
        }
        return edfFile;
    }
}
