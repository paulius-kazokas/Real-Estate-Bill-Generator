package utilities;

import java.util.Date;

public class PublicUtilities {

    private String imonesPavadinimas;
    private String imonesAdresas;
    private String imonesBarkodas;
    private String paslaugosPavadinimas;
    private String paslaugosTipas;
    private String matavimoVienetai;

    private int saskaitosNumeris;
    private int moketojoNumeris;
    private int pvm;
    private int rodikliaiMenesioPries;
    private int rodikliaiEinamoMenesio;

    private float kainaUzVieneta;
    private float kainaSuPvm;

    private Date moketiUzMenesi;

    public PublicUtilities(String imonesPavadinimas, String imonesAdresas, String imonesBarkodas, String paslaugosPavadinimas,
                           String paslaugosTipas, String matavimoVienetai, int saskaitosNumeris, int moketojoNumeris, int pvm,
                           int rodikliaiMenesioPries, int rodikliaiEinamoMenesio, float kainaUzVieneta, float kainaSuPvm,
                           Date moketiUzMenesi) {
        this.imonesPavadinimas = imonesPavadinimas;
        this.imonesAdresas = imonesAdresas;
        this.imonesBarkodas = imonesBarkodas;
        this.paslaugosPavadinimas = paslaugosPavadinimas;
        this.paslaugosTipas = paslaugosTipas;
        this.matavimoVienetai = matavimoVienetai;
        this.saskaitosNumeris = saskaitosNumeris;
        this.moketojoNumeris = moketojoNumeris;
        this.pvm = pvm;
        this.rodikliaiMenesioPries = rodikliaiMenesioPries;
        this.rodikliaiEinamoMenesio = rodikliaiEinamoMenesio;
        this.kainaUzVieneta = kainaUzVieneta;
        this.kainaSuPvm = kainaSuPvm;
        this.moketiUzMenesi = moketiUzMenesi;
    }

    public String getImonesPavadinimas() {
        return imonesPavadinimas;
    }

    public String getImonesAdresas() {
        return imonesAdresas;
    }

    public String getImonesBarkodas() {
        return imonesBarkodas;
    }

    public String getPaslaugosPavadinimas() {
        return paslaugosPavadinimas;
    }

    public String getPaslaugosTipas() { return paslaugosTipas; }

    public String getMatavimoVienetai() {
        return matavimoVienetai;
    }

    public int getSaskaitosNumeris() {
        return saskaitosNumeris;
    }

    public int getMoketojoNumeris() {
        return moketojoNumeris;
    }

    public int getPvm() {
        return pvm;
    }

    public float getKainaUzVieneta() { return kainaUzVieneta; }

    public float getKainaSuPvm() {
        return kainaSuPvm;
    }

    public Date getMoketiUzMenesi() {
        return moketiUzMenesi;
    }

    public int getRodikliaiMenesioPries() { return rodikliaiMenesioPries; }

    public int getRodikliaiEinamoMenesio() { return rodikliaiEinamoMenesio; }

    @Override
    public String toString() {
        return "KomunalinesPaslaugos{" +
                "imonesPavadinimas='" + imonesPavadinimas + '\'' +
                ", imonesAdresas='" + imonesAdresas + '\'' +
                ", imonesBarkodas='" + imonesBarkodas + '\'' +
                ", paslaugosPavadinimas='" + paslaugosPavadinimas + '\'' +
                ", matavimoVienetai='" + matavimoVienetai + '\'' +
                ", saskaitosNumeris=" + saskaitosNumeris +
                ", moketojoNumeris=" + moketojoNumeris +
                ", pvm=" + pvm +
                ", rodikliaiMenesioPries=" + rodikliaiMenesioPries +
                ", rodikliaiEinamoMenesio=" + rodikliaiEinamoMenesio +
                ", kainaUzVieneta=" + kainaUzVieneta +
                ", kainaSuPvm=" + kainaSuPvm +
                ", moketiUzMenesi=" + moketiUzMenesi +
                '}';
    }
}
