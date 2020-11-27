package config;


import lombok.Getter;

@Getter
public enum FilenameConstant {

    BILL_DESTINATION_PATH("src/main/resources/reportsJson/");

    public final String billDestinationPath;

    FilenameConstant(String billDestinationPath) {
        this.billDestinationPath = billDestinationPath;
    }

}
