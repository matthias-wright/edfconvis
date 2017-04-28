package de.rwthaachen.mi.convis;

import de.rwthaachen.mi.convis.data.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);

    @RequestMapping("/")
    public String showHome() {
        return "main";
    }


    /**
     * Adds the variables to the model the it as a string
     * @param model
     * @param request
     * @return
     */
    public String plot(Model model, HttpServletRequest request) {
        AbstractFile abstractFile = (AbstractFile) request.getSession().getAttribute("edfFile");
        Signal[] signals = abstractFile.getSignals();
        //x-axis scale
        int[] x = Tools.getAxisScale(Tools.getLengthOfLongestSignal(signals));

        //copy integer arrays from signals into new array
        int[][] signalArray = new int[signals.length][];
        for (int i = 0; i < signals.length; i++ ) {
            signalArray[i] = signals[i].getValues();
        }

        int[] averages = Tools.getAverageDistanceToXAxis(signals);
        //get the min value of all signals
        int minValue = Tools.getMinOfSignals(signals);
        //get the max value of all signals
        int maxValue = Tools.getMaxOfSignals(signals);
        int fileType = (int)request.getSession().getAttribute("fileType");

        model.addAttribute("maxValue", maxValue);
        model.addAttribute("minValue", minValue);
        model.addAttribute("averages", averages);
        model.addAttribute("durationRecord", abstractFile.getDurationOfADataRecord());
        model.addAttribute("signalArray", signalArray);
        model.addAttribute("scale", x);
        model.addAttribute("labels", abstractFile.getLabels());
        model.addAttribute("numberOfSamples", abstractFile.getNrOfSamplesPerSignalInEachRecord()[0]);
        model.addAttribute("numberOfRecords", abstractFile.getNumberOfDataRecords());
        model.addAttribute("maxSeconds", abstractFile.getNumberOfDataRecords()*abstractFile.getDurationOfADataRecord());

        //calculate end time
        int duration = abstractFile.getNumberOfDataRecords()*abstractFile.getDurationOfADataRecord();
        LocalTime endTime = Tools.getEndTime(abstractFile.getStarttimeOfRecordng(), duration);
        model.addAttribute("endtime", endTime.toString());

        if(fileType == 1) { // if file is edf
            EDFFile edfFile = (EDFFile)request.getSession().getAttribute("edfFile");

            model.addAttribute("edfFormat", 1);
            model.addAttribute("numberOfSignals", signals.length);
            //meta data
            String[] localPatientId = edfFile.getLocalPatientIdentification().split(" ");
            if(localPatientId.length < 4) {
                model.addAttribute("patientName", edfFile.getLocalPatientIdentification());
            } else {
                model.addAttribute("patientName", localPatientId[3]);
                model.addAttribute("sex", localPatientId[1]);
                model.addAttribute("birthdate", localPatientId[2]);
                model.addAttribute("hospitalId", localPatientId[0]);
            }

        } else { // file is edf+
            EDFPlusFile edfFile = (EDFPlusFile)request.getSession().getAttribute("edfFile");

            model.addAttribute("edfFormat", 2);
            //-1 b/c of the annotations signal
            model.addAttribute("numberOfSignals", signals.length-1);
            //edf plus
            model.addAttribute("annoSignal", edfFile.getAnnotations());
            model.addAttribute("annoIndex", edfFile.getAnnotationsIndex());
            //meta data
            model.addAttribute("patientName", edfFile.getName());
            model.addAttribute("sex", edfFile.getSex());
            model.addAttribute("birthdate", edfFile.getBirthdate());
            model.addAttribute("hospitalId", edfFile.getHospitalPatientId());

            model.addAttribute("startdate", edfFile.getStartdateRecId());
            model.addAttribute("starttime", edfFile.getStarttimeOfRecordng().replace(".", ":"));
            model.addAttribute("hospitalAdmin", edfFile.getHospitalAdministrationCode());
            model.addAttribute("usedEquipment", edfFile.getUsedEquipment());
            model.addAttribute("resInvest", edfFile.getResponsibleInvestigator());
        }
        return "plot";
    }

    /**
     * Copies the elements from indexes 'start' to 'end' of an array into a new array and returns it
     *
     * @param data
     *      source array
     * @param start
     *      start index
     * @param end
     *      end index
     * @return
     *      array containing the elements from indexes 'start' to 'end'
     */
    private byte[] splitBytes(byte[] data, int start, int end) {
        byte[] dataSplit = new byte[Math.abs(start-end)];
        for (int i = 0; i < Math.abs(start-end); i++) {
            dataSplit[i] = data[i+start];
        }
        return dataSplit;
    }

    private String byteToString(byte[] data) {
        String ret = null;
        try {
            ret = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
            return "";
        }
        return ret;
    }

    /**
     * Returns 1 if the uploaded file is an EDf file and 2 if it an EDF+ file
     * @param data
     * @return
     */
    private int checkEDFOrEDFPlus(byte[] data) {
        int ns = Integer.parseInt(byteToString(splitBytes(data, 252, 256)).trim());
        byte[] arrayLabels = splitBytes(data, 256 ,256+16*ns);

        String[] labels = new String[(int)(arrayLabels.length/16.)];
        int startLab = 0;
        int endLab = 15;
        for (int i = 0; i < labels.length; i++) {
            labels[i] = byteToString(splitBytes(arrayLabels, startLab, endLab)).trim();
            startLab = startLab + 16;
            endLab = endLab + 16;
        }

        for(int i = 0; i < labels.length; i++) {
            if(labels[i].trim().matches("EDF.Annotation.")) {
                return 2;
            }
        }
        return 1;
    }

    @RequestMapping(value = "/plot", method = RequestMethod.POST)
    public String handleFormUploadUpdate(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) throws IOException {
        if (!file.isEmpty()) {
            byte[] data = file.getBytes();

            if(checkEDFOrEDFPlus(data) == 1) {
                //EDF file
                AbstractFileReader fileReader = new EDFReader();
                EDFFile edfFile = (EDFFile)fileReader.parseHeaderData(data);
                request.getSession().setAttribute("edfFile", edfFile);
                request.getSession().setAttribute("fileName", file.getOriginalFilename());
                request.getSession().setAttribute("fileType", 1);
            } else {
                //EDF+ file
                AbstractFileReader edfFileReader = new EDFPlusReader();

                EDFPlusFile edfPlusFile = (EDFPlusFile) edfFileReader.parseHeaderData(data);

                request.getSession().setAttribute("edfFile", edfPlusFile);
                request.getSession().setAttribute("fileName", file.getOriginalFilename());
                request.getSession().setAttribute("fileType", 2);
            }

        }
        plot(model, request);
        return "plot";
    }

    @RequestMapping(value="/download", method=RequestMethod.GET)
    public ResponseEntity<byte[]> downloadEDFFile(HttpServletRequest request) throws IOException {

            String filename = (String)request.getSession().getAttribute("fileName");

            int fileType = (int)request.getSession().getAttribute("fileType");
            if(fileType == 1) { //EDF file
                EDFFile edfFile = (EDFFile)request.getSession().getAttribute("edfFile");
                if(edfFile == null) {
                    //download fail
                } else {
                    EDFToEDFPlusConverter to = new EDFToEDFPlusConverter();
                    EDFPlusFile edfPlusFile = to.convert(edfFile);
                    EDFPlusWriter writer = new EDFPlusWriter(edfPlusFile);
                    List<Byte> header = writer.writeHeader();
                    List<Byte> bytes = writer.writeData(header, edfPlusFile);
                    byte[] file = new byte[bytes.size()];
                    for(int i = 0; i < file.length; i++) {
                        file[i] = bytes.get(i);
                    }
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/edf"));
                    headers.add("content-disposition", "inline;filename="+"edf+-"+filename);
                    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
                    return response;
                }
            } else if(fileType == 2) { //EDF+ file
                EDFPlusFile edfFile = (EDFPlusFile)request.getSession().getAttribute("edfFile");
                if(edfFile == null) {
                    //download fail
                } else {
                    EDFPlusToEDFConverter to = new EDFPlusToEDFConverter();
                    EDFFile toEdfFile = to.convert(edfFile);
                    EDFWriter writer = new EDFWriter(toEdfFile);
                    List<Byte> header = writer.writeHeader();
                    List<Byte> bytes = writer.writeData(header, toEdfFile);
                    byte[] file = new byte[bytes.size()];
                    for(int i = 0; i < file.length; i++) {
                        file[i] = bytes.get(i);
                    }
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/edf"));
                    headers.add("content-disposition", "inline;filename="+"edf-"+filename);
                    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
                    return response;
                }
            }
            return null;
    }

}
