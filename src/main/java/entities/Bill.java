package entities;

import java.util.Date;
import java.util.Map;

public class Bill {

    private String billNumber;
    private String userPersonalCode;
    private String utilityType;

    private Map<String, Float> totalPerUtility;
    private float billTotal;

    private String billsMonth;
    private Date billsDate;

    public Bill(String billNumber, String userPersonalCode, String utilityType, Map<String, Float> totalPerUtility,
                float billTotal, String billsMonth, Date billsDate) {
        this.billNumber = billNumber;
        this.userPersonalCode = userPersonalCode;
        this.utilityType = utilityType;
        this.totalPerUtility = totalPerUtility;
        this.billTotal = billTotal;
        this.billsMonth = billsMonth;
        this.billsDate = billsDate;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getUserPersonalCode() {
        return userPersonalCode;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public Map<String, Float> getTotalPerUtility() {
        return totalPerUtility;
    }

    public float getBillTotal() {
        return billTotal;
    }

    public String getBillsMonth() {
        return billsMonth;
    }

    public Date getBillsDate() { return billsDate;}

    @Override
    public String toString() {
        return "Bill{" +
                "billNumber='" + billNumber + '\'' +
                ", userPersonalCode='" + userPersonalCode + '\'' +
                ", utilityType='" + utilityType + '\'' +
                ", totalPerUtility=" + totalPerUtility +
                ", billTotal=" + billTotal +
                ", billsMonth=" + billsMonth +
                ", billsDate=" + billsDate.toString() +
                '}';
    }
}
