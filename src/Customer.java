import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Customer {

    static Scanner sc = new Scanner(System.in);

    public static void newUser() throws Exception {
        System.out.println("============================================ User Information ========================================");

		System.out.print("Enter NRIC: ");
        String nric = sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        System.out.print("Enter Contact: ");
        String contact = sc.nextLine();

        System.out.print("Enter Date of Birth (dd/mm/yyyy): ");
        Date birthday = new SimpleDateFormat("dd/MM/yyyy").parse(sc.next());

        System.out.print("Enter Gender (1 = Male , 2 = Female): ");
        int gender = sc.nextInt();
        sc.nextLine();
        String genderString = null;
        if (gender == 1)
            genderString = "Male";
        if (gender == 2)
            genderString = "Female";
        System.out.print("Enter Nationality: ");
        String nationality = sc.nextLine();
        System.out.println("\n");
        String account = "400" + contact + "'";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strBirthday = dateFormat.format(birthday);
        String[] newUserData = {nric, name, address, contact, strBirthday, genderString, nationality, account};
        String[] newLoginData = {nric, Encryption.encryption(nric, nric, 'E')};
        FileManager.updateLoginCSV(nric, 5, newLoginData);
        FileManager.addUserCSV(nric, newUserData);
        FileManager.addAccountCSV(account);
    }

    public static void unlockAcc() throws Exception {
        System.out.println("=====================Unlock User Account==================");
        System.out.print("Enter NRIC: ");
        String nric = sc.next();
        String[] newdata = {"false"};
        FileManager.updateLoginCSV(nric, 4, newdata);
        System.out.println("Account " + nric + " had been unlocked.");
    }
}