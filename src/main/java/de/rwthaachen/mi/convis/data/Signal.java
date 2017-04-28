package de.rwthaachen.mi.convis.data;

/**
 * Created by mwright on 02.06.16.
 *
 * Represents a signal
 *
 * Recorded signal data in EDF files are divided into data records. Each data record contains a certain amount of samples of each recorded signals
 */
public class Signal {
    //Attributes
    private int lengthOfValues;
    private int[] values;

    public Signal(int lengthOfValues) {
        values = new int[lengthOfValues];
    }

    public void addValue(int value, int index) {
        values[index] = value;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int getLengthOfValues() {
        return lengthOfValues;
    }

    public void setLengthOfValues(int lengthOfValues) {
        this.lengthOfValues = lengthOfValues;
    }

    public int getSignalLength() {
        return values.length;
    }

}
