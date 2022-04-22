import java.util.Date;

public class History implements Printing {
    private Date date;
    private String transactionDetails;
    private double withdrawalAmt;
    private double depositAmt;
    private double balanceAmt;
    private boolean set = false;

    public History(Date date, String transactionDetails, double withdrawalAmt, double depositAmt, double balanceAmt) {
        this.date = date;
        this.transactionDetails = transactionDetails;
        this.withdrawalAmt = withdrawalAmt;
        this.depositAmt = depositAmt;
        this.balanceAmt = balanceAmt;
        this.set = true;
    }

    public History() {
    }

    public void setHistory(Date date, String transactionDetails, double withdrawalAmt, double depositAmt, double balanceAmt) throws NoSuchMethodException {
        if (!set) {
            this.date = date;
            this.transactionDetails = transactionDetails;
            this.withdrawalAmt = withdrawalAmt;
            this.depositAmt = depositAmt;
            this.balanceAmt = balanceAmt;
            this.set = true;
        } else {
            throw new NoSuchMethodException("Can only set once , please do not attempt to set user detail more then once.");
        }
    }

    public Date getDate() {
        return date;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public double getDepositAmt() {
        return depositAmt;
    }

    public double getWithdrawalAmt() {
        return withdrawalAmt;
    }

    public double getBalanceAmt() {
        return balanceAmt;
    }

    @Override
    public void printDetail() {
        System.out.println("Transaction Details : " + this.transactionDetails);
        System.out.println("Withdrawal : " + this.withdrawalAmt);
        System.out.println("Deposit  : " + this.depositAmt);
        System.out.println("Total Balance : " + this.balanceAmt);
    }
}
