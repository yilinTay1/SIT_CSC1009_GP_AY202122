import java.text.ParseException;
import java.util.Scanner;

public class Deposit extends Transaction {

    Deposit(String accountNumber, Account userAccount) throws ParseException {
        super(accountNumber, userAccount);
        this.transactionType="Deposit";         //Setting transaction type
    }

    @Override
    boolean executeTransaction() throws Exception {
        printBalance();                                     //Displaying current balance
        checkAddAmt();                                      //Checking for any amount errors
        transactionMade = updateAccountFile(exitOption);    //Assigning true false for transaction made
        return transactionMade;
    }

    @Override
    void updateTransactionFile() throws Exception {
            //Updating user account
            History fromAccountHistory = new History(date, transactionType, 0, transactionAmt, totalBalance);
            FileManager.addHistoryLog(userAccount,userAccountNumber, fromAccountHistory);
    }

    @Override
    boolean updateAccountFile(int exitOption) throws Exception {
        switch (exitOption) {
            case 1:
                this.userAccount.addToBalance(transactionAmt);
                //Updating target account
                FileManager.updateAccountCSV(userAccountNumber, 1, String.format("%.2f", this.userAccount.getAvailableBalance() + transactionAmt));
                FileManager.updateAccountCSV(userAccountNumber, 2, String.format("%.2f", this.userAccount.getTotalBalance()));
                transactionMade = true;
                break;
            case 2:
                transactionMade = false;
                break;
            default:
                System.out.print("\nYou did not enter a valid selection. Try again.");
                System.out.println("\nPlease Select Option 1 or 2:");
                break;
        }
        return transactionMade;
    }

    @Override
    void printTransactionDetails(boolean transactionMade) {
        //if transaction was made, print transaction details
        if(transactionMade) {
            Scanner enter = new Scanner(System.in);
            System.out.println("\nYou have deposited $" + transactionAmt);
            System.out.println("\nYour Remaining Balance is: $" + userAccount.getTotalBalance());
            System.out.println("\nPress enter to continue ...");
            enter.nextLine();
        }
    }
}
