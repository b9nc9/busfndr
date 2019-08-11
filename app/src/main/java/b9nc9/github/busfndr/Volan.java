package b9nc9.github.busfndr;

import java.io.Serializable;

/**
 * Created by b9nc9 on 2/25/18.
 */

public class Volan implements Serializable {
    public String indul;
    public String cel;
    public String indulIdo;
    public String erkezIdo;
    public String kozlekedik;

    public Volan(String sor) {
        String[] feloszt = sor.split(";");
        indul = feloszt[0] + ", " + feloszt[1];
        cel = feloszt[2] + ", " + feloszt[3];
        indulIdo = feloszt[4];
        erkezIdo = feloszt[5];
        kozlekedik = feloszt[6];
    }

}
