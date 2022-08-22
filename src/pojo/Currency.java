package pojo;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Currency {

    private String charCode;
    private String name;
    private double value;


    public String getCharCode() {
        return charCode;
    }

    @JsonSetter("CharCode")
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("Name")
    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    @JsonSetter("Value")
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "1 " + name + " (" + charCode + ") = " + value + " rub.";
    }
}
