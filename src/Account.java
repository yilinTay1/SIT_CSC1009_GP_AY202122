import java.math.BigDecimal;
import java.util.ArrayList;
import java.lang.Math;

public class Account extends ArrayList<History> implements Printing{
    private String accountNumber;
    private double availableBalance;
    private double totalBalance;
    private double transferLimit;
    private double withdrawLimit;
    private double overseasTransferLimit;
    private boolean locked;
    private boolean set = false;

    public Account(){}

    public Account(
            String theAccountNumber,
            double theAvailableBalance,
            double theTotalBalance,
            double transferLimit,
            double withdrawLimit,
            double overseasTransferLimit,
            boolean locked
        ) {
        this.accountNumber = theAccountNumber;
        this.availableBalance = theAvailableBalance;
        this.transferLimit = transferLimit;
        this.withdrawLimit = withdrawLimit;
        this.overseasTransferLimit = overseasTransferLimit;
        this.totalBalance = theTotalBalance;
        this.locked = locked;
        this.set = true;
    }



    public void setAccount(
            String theAccountNumber,
            double theAvailableBalance,
            double theTotalBalance,
            double transferLimit,
            double withdrawLimit,
            double overseasTransferLimit,
            boolean locked
    ) throws NoSuchMethodException {
        // Only set once security feature.
        if(!set){
            this.accountNumber = theAccountNumber;
            this.availableBalance = theAvailableBalance;
            this.totalBalance = theTotalBalance;
            this.transferLimit = transferLimit;
            this.withdrawLimit = withdrawLimit;
            this.overseasTransferLimit = overseasTransferLimit;
            this.locked = locked;
            this.set = true;
        }
        else{
            throw new NoSuchMethodException(" Can only set once , please do not attempt to set user detail more then once.");
        }
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    // totalBalance (Credit)
    public boolean addToBalance(double amount) {
        if(amount < 0 ){
            return false;
        }else {
            totalBalance += amount;
            return true;
        }

    }

    // availableBalance & totalBalance (Debit)
    public boolean subFromBalance(double amount) {
        if(amount < 0 ){
            return false;
        }else{
            totalBalance -= amount;
            return true;
        }
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean getLocked(){
        return this.locked;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    public double getOverseasTransferLimit() {
        return overseasTransferLimit;
    }

    public void updateTransferLimit(double limit) {
        transferLimit = limit;
    }

    public void updateWithdrawLimit(double limit) {
        withdrawLimit = limit;
    }

    public void updateOverseasTransferLimit(double limit) {
        overseasTransferLimit = limit;
    }

    public void printBalanceAnalytic() {
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        double totalWithdraw = 0;
        double totalDeposit = 0;
        // For each history
        int totalSize = super.size();
        for(int i = 0 ; i<totalSize ; i++){
            totalWithdraw += super.get(i).getWithdrawalAmt();
            totalDeposit += super.get(i).getDepositAmt();
            if(super.get(i).getBalanceAmt() > max){
                max = super.get(i).getBalanceAmt();
            }
            if(super.get(i).getBalanceAmt() < min){
                min = super.get(i).getBalanceAmt();
            }
        }
        double diff = max - min;
        // We will only do range of 5 lines if not a little too big to display
        String firstRange = "|";
        String secRange = "|";
        String thirdRange = "|";
        String forthRange = "|";
        String fifthRange = "|";


        for(int i = totalSize-1 ; i>=0 ; i--){
            double range = diff/5;
            // Quantization
            if(super.get(i).getBalanceAmt()<min+(range*1)){
                firstRange += "*";
                // Add padding to the rest
                secRange += " ";
                thirdRange+= " ";
                forthRange+= " ";
                fifthRange+= " ";
            }
            else if(super.get(i).getBalanceAmt()<min+(range*2)){
                firstRange += " ";
                secRange += "*";
                thirdRange+= " ";
                forthRange+= " ";
                fifthRange+= " ";
            }
            else if(super.get(i).getBalanceAmt()<min+(range*3)){
                firstRange += " ";
                secRange += " ";
                thirdRange+= "*";
                forthRange+= " ";
                fifthRange+= " ";
            }
            else if(super.get(i).getBalanceAmt()<min+(range*4)){
                firstRange += " ";
                secRange += " ";
                thirdRange+= " ";
                forthRange+= "*";
                fifthRange+= " ";
            }
            else if(super.get(i).getBalanceAmt()<min+(range*5)){
                firstRange += " ";
                secRange += " ";
                thirdRange+= " ";
                forthRange+= " ";
                fifthRange+= "*";
            }
        }
        System.out.println("max: " +max);
        String maxAsText = BigDecimal.valueOf(max).toPlainString();
        String minAsText = BigDecimal.valueOf(min).toPlainString();

        double tabMaxRequired = Math.floor((float) (maxAsText.length()+1) /4)+1;
        double tabMinRequired = Math.floor((float) (minAsText.length()+1) /4)+1;
        String firstLine = "$"+maxAsText+"\t";
        String gap = "";
        String fifthLine = "$"+minAsText+"\t";

        if( tabMinRequired - tabMaxRequired <0){
            //Max need lesser tab , add first like one tab to organize everything
            for(int x = 0 ; x < tabMaxRequired ;x++){
                gap += "\t";
            }
            for(int x = 0 ; x < tabMaxRequired - tabMinRequired ;x++){
                fifthLine += "\t";
            }
        }else if(tabMaxRequired - tabMinRequired<0){
            //Min need lesser tab
            for(int x = 0 ; x < tabMinRequired ;x++){
                gap += "\t";
            }
            for(int x = 0 ; x < tabMinRequired - tabMaxRequired ;x++){
                firstLine += "\t";
            }
        }
        System.out.println("============================================ Display balance plotted chart ============================================ " );
        System.out.println(firstLine+firstRange);
        System.out.println(gap+secRange );
        System.out.println(gap+thirdRange );
        System.out.println(gap+forthRange );
        System.out.println(gap+fifthRange );
        System.out.println(fifthLine+"|____________________________________________________________________________________________________" );
        System.out.println(gap+"0                        Latest 100 Transaction count (100 being the latest)                        100");
    }

    @Override
    public void printDetail() {
        System.out.println("====================================================================================================");
        System.out.println("Account Number : "+ this.accountNumber);
        System.out.println("Total balance : $"+ this.totalBalance);
        System.out.println("Transfer Limit : $"+ this.transferLimit);
        System.out.println("Withdrawal Limit : $"+ this.withdrawLimit);
        System.out.println("Overseas Transfer Limit: $"+ this.overseasTransferLimit);
        System.out.println("====================================================================================================");
    }
}
