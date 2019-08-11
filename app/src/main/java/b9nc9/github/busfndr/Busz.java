package b9nc9.github.busfndr;

/**
 * Created by b9nc9 on 2/28/18.
 */

public class Busz {
    public String indulIdo;
    public String erkezIdo;
    public String kozlekedik;
    public String indul;
    public String cel;

    public Busz(String i, String c, String ii, String ei, String k) {
        indul = i;
        cel = c;
        indulIdo = ii;
        erkezIdo = ei;
        kozlekedik = k;
    }
}
