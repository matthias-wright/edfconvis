package de.rwthaachen.mi.convis.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwright on 11/18/16.
 *
 * Represents an EDF Annotation signal
 */
public class AnnotationSignal {
    private char sign;
    private double onset; //seconds
    private double duration; //seconds
    private List<String> annotations;

    public AnnotationSignal() {
        annotations = new ArrayList<>();
    }

    public void addAnnotation(String s) {
        annotations.add(s);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public double getOnset() {
        return onset;
    }

    public void setOnset(double onset) {
        this.onset = onset;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

}
