package de.rwthaachen.mi.convis.data;

/**
 * Created by mwright on 6/29/16.
 *
 * Represents a edf+ file
 * Data records are stored in the Signal array
 */
public class EDFPlusFile extends AbstractFile {
    //header information
    //80 bytes - the 4 following attributes are put together (separated by spaces) to the local patient identification field
    private String hospitalPatientId; //the code by which the patient is known to the hospital
    private String sex; //F or M
    private String birthdate;
    private String name; //forename and last name separated by an underscore and in capital letters
    //80 bytes - the 5 following attributes are put together (separated by spaces) to the local recording identification field
    private static final String STARTDATE_STRING = "Startdate";
    private String startdateRecId; //dd-MMM-yyyy format using the English 3-character abbreviations of the month in capitals
    private String hospitalAdministrationCode;
    private String responsibleInvestigator;
    private String usedEquipment;
    //EDF Annotations signal
    private AnnotationSignal[] annotations;
    //Index of the EDF Annotations signal, -1 if not existing
    private int annotationsIndex;

    public String getHospitalPatientId() {
        return hospitalPatientId;
    }

    public void setHospitalPatientId(String hospitalPatientId) {
        this.hospitalPatientId = hospitalPatientId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdateString() {
        return STARTDATE_STRING;
    }

    public String getStartdateRecId() {
        return startdateRecId;
    }

    public void setStartdateRecId(String startdateRecId) {
        this.startdateRecId = startdateRecId;
    }

    public String getHospitalAdministrationCode() {
        return hospitalAdministrationCode;
    }

    public void setHospitalAdministrationCode(String hospitalAdministrationCode) {
        this.hospitalAdministrationCode = hospitalAdministrationCode;
    }

    public String getResponsibleInvestigator() {
        return responsibleInvestigator;
    }

    public void setResponsibleInvestigator(String responsibleInvestigator) {
        this.responsibleInvestigator = responsibleInvestigator;
    }

    public String getUsedEquipment() {
        return usedEquipment;
    }

    public void setUsedEquipment(String usedEquipment) {
        this.usedEquipment = usedEquipment;
    }

    public AnnotationSignal[] getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(AnnotationSignal[] annotations) {
        this.annotations = annotations;
    }

    public int getAnnotationsIndex() {
        return this.annotationsIndex;
    }

    public void setAnnotationsIndex(int annotationsIndex) {
        this.annotationsIndex = annotationsIndex;
    }


}
