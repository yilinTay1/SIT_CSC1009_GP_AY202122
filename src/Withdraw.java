import java.text.ParseException;
import java.util.Scanner;

public class Withdraw extends Transaction {

    Withdraw(String accountNumber, Account userAccount) throws ParseException {
        super(accountNumber, userAccount);
        this.transactionType = "Withdraw"; //Setting transaction type to Withdraw
    }

    @Override
    boolean executeTransaction() {
        printBalance();                                     //Displaying current balance
        transactionLimit = userAccount.getWithdrawLimit();  //Getting limit for transaction
        checkDeductAmt(transactionLimit);                   //Checking for any amount errors
        transactionMade = updateAccountFile(exitOption);    //Assigning true false for transaction made
        return transactionMade;
    }

    @Override
    void updateTransactionFile() throws Exception {
        //Updating user account
        History fromAccountHistory = new History(date, transactionType, transactionAmt, 0, totalBalance);
        FileManager.addHistoryLog(userAccount, userAccountNumber, fromAccountHistory);
    }

    @Override
    boolean updateAccountFile(int exitOption) {
        switch (exitOption) {
            case 1:
                try {
                    //Deducting from user account
                    userAccount.subFromBalance(transactionAmt);
                    //Updating details
                    FileManager.updateAccountCSV(userAccountNumber, 1, String.format("%.2f", userAccount.getAvailableBalance() - transactionAmt));
                    FileManager.updateAccountCSV(userAccountNumber, 2, String.format("%.2f", userAccount.getTotalBalance()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transaction: Withdraw Un-successful!");
                }
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
        if (transactionMade) {
            Scanner enter = new Scanner(System.in);
            System.out.println("\nYou have withdrawn $" + transactionAmt);
            System.out.println("\nYour Remaining Balance is: $" + userAccount.getTotalBalance());
            System.out.println("\nPress enter to continue ...");
            enter.nextLine();
        }
    }
}
