package gte.com.itextmosimayor.modules;

public class StringFormat {
    public String convertSpace(String string) {
        string = string.replace(" ", "%20");
        return string;
    }
}
