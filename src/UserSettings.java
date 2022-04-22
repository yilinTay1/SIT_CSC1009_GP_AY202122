import java.util.Scanner;

public class UserSettings implements Settings {
    /* option 1 and 2 of setting menu */
    /* option 1 - change password */
    /* option 2 - update contact number */

    static Scanner myObj = new Scanner(System.in);
    private final Login loginInformation;

    /* Constructor */
    public UserSettings(Login loginInformation) {
        this.loginInformation = loginInformation;
    }

    /* Settings Option 1 - user able to change password for login */
    public void changePassword() throws Exception {
        String currentPassword = loginInformation.getPassword();
        String id = loginInformation.getID();
        System.out.println("\n====================================== Change User's Password ======================================");
        boolean check = false;
        /* Loop until user key in correct password */
        while (!check) {
            System.out.print("Enter current password: ");
            String password = myObj.next();
            if (password.equals(Encryption.encryption(currentPassword, id, 'D'))) {
                check = true;
                loginInformation.resetPassword(); /* Reset password using function in Login class */
            } else {
                System.out.println("Password entered does not match current password. Please try again.");
            }
        }
    }

    /* Setting option 2 - User able to update contact number */
    public void updateContact() throws Exception {
        String id = loginInformation.getID();
        System.out.println("\n=================================== Update User's Contact Number ===================================");
        boolean check = false;
        while (!check) {
            System.out.println("Enter new contact number: ");
            String newPhoneNum = myObj.next();
            String phoneNumFormat = "^[8-9]{1}[0-9]{7}$"; /* Format: digits only with length of 8, only 1st number begin with 8  or 9 */
            if (newPhoneNum.matches(phoneNumFormat)) {
                loginInformation.user.updatePhoneNum(newPhoneNum);
                FileManager.updateUserCSV(id, newPhoneNum);
                System.out.println("Your contact number have been updated successfully");
                check = true;
            } else if (newPhoneNum.matches("^[0-9]{8}$")) { /* Check if 1st digit begins with 0-7 */
                System.out.println("Your phone number should begin with digit 8 or 9");
            } else {
                System.out.println("Please try again in the format of 8 digits");
            }
        }
    }

    @Override
    public void executeSettings(int option) throws Exception {
        switch (option) {
            case 1:
                changePassword();
                break;
            case 2:
                updateContact();
                break;
        }
    }

    @Override
    public void enterToContinue() {
        Scanner enter = new Scanner(System.in);
        System.out.println("\nPress enter to continue");
        enter.nextLine();
    }
}
