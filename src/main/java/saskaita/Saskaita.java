package saskaita;

import java.util.Date;

public class Saskaita {

    private String saskaitosNumeris;
    private String moketojoNumeris;
    private String paslaugosTipas;

    private float isVisoUzPaslauga;
    private float isViso;

    private Date moketiUzMenesi;

    public Saskaita(String saskaitosNumeris, String moketojoNumeris, String paslaugosTipas, float isVisoUzPaslauga,
                    float isViso, Date moketiUzMenesi) {
        this.saskaitosNumeris = saskaitosNumeris;
        this.moketojoNumeris = moketojoNumeris;
        this.paslaugosTipas = paslaugosTipas;
        this.isVisoUzPaslauga = isVisoUzPaslauga;
        this.isViso = isViso;
        this.moketiUzMenesi = moketiUzMenesi;
    }

    public String getSaskaitosNumeris() {
        return saskaitosNumeris;
    }

    public String getMoketojoNumeris() {
        return moketojoNumeris;
    }

    public String getPaslaugosTipas() {
        return paslaugosTipas;
    }

    public float getIsVisoUzPaslauga() {
        return isVisoUzPaslauga;
    }

    public float getIsViso() {
        return isViso;
    }

    public Date getMoketiUzMenesi() {
        return moketiUzMenesi;
    }

    @Override
    public String toString() {
        return "Saskaita{" +
                "saskaitosNumeris='" + saskaitosNumeris + '\'' +
                ", moketojoNumeris='" + moketojoNumeris + '\'' +
                ", paslaugosTipas='" + paslaugosTipas + '\'' +
                ", isVisoUzPaslauga=" + isVisoUzPaslauga +
                ", isViso=" + isViso +
                ", moketiUzMenesi=" + moketiUzMenesi +
                '}';
    }
}
