package asmuo;

import java.util.Map;

public class Vartotojas {

    private String vardas;
    private String pavarde;
    private String ePastas;

    private int asmensKodas;

    private String mikrorajonas;
    private String miestas;

    private int nekilnojamoTurtoKiekis;
    private Map<Integer, String> priklausantisNekilnojamoTurtoSarasas;

    public Vartotojas(String vardas, String pavarde, String ePastas, int asmensKodas, String mikrorajonas, String miestas, int nekilnojamoTurtoKiekis, Map<Integer, String> priklausantisNekilnojamoTurtoSarasas) {
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.ePastas = ePastas;
        this.asmensKodas = asmensKodas;
        this.mikrorajonas = mikrorajonas;
        this.miestas = miestas;
        this.nekilnojamoTurtoKiekis = nekilnojamoTurtoKiekis;
        this.priklausantisNekilnojamoTurtoSarasas = priklausantisNekilnojamoTurtoSarasas;
    }

    public String getVardas() {
        return vardas;
    }

    public String getPavarde() {
        return pavarde;
    }

    public String getePastas() {
        return ePastas;
    }

    public int getAsmensKodas() {
        return asmensKodas;
    }

    public String getMikrorajonas() {
        return mikrorajonas;
    }

    public String getMiestas() {
        return miestas;
    }

    public int getNekilnojamoTurtoKiekis() {
        return nekilnojamoTurtoKiekis;
    }

    public Map<Integer, String> getPriklausantisNekilnojamoTurtoSarasas() {
        return priklausantisNekilnojamoTurtoSarasas;
    }

    @Override
    public String toString() {
        return "Vartotojas{" +
                "vardas='" + vardas + '\'' +
                ", pavarde='" + pavarde + '\'' +
                ", ePastas='" + ePastas + '\'' +
                ", asmensKodas=" + asmensKodas +
                ", mikrorajonas='" + mikrorajonas + '\'' +
                ", miestas='" + miestas + '\'' +
                ", nekilnojamoTurtoKiekis=" + nekilnojamoTurtoKiekis +
                ", priklausantisNekilnojamoTurtoSarasas=" + priklausantisNekilnojamoTurtoSarasas +
                '}';
    }
}



