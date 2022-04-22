import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileManager {

    public static Person getDatabase(String ID) throws Exception {
        Person user = getUserProfile(ID);

        for (int i = 0; i < user.accounts.length; i++) {
            Account acc = getAccount(user.accounts[i]);
            if (acc != null) {
                //Get History
                ArrayList<History> history = getAllTransaction(user.accounts[i]);
                //Get Latest
                if (history.size() > 100) {
                    for (int ii = 100; ii > 0; ii--) {
                        acc.add(history.get(history.size() - ii - 1));
                    }
                } else {
                    for (int ii = history.size(); ii > 0; ii--) {
                        acc.add(history.get(history.size() - ii - 1));
                    }
                }
                user.add(acc);
            }
        }
        return user;
    }

    public static Login getLoginData(String username) throws FileNotFoundException {
        File loginFile = new File(System.getProperty("user.dir") + "\\database\\login.csv");
        Scanner sc = new Scanner(loginFile);
        boolean found = false;
        ArrayList<String> data = new ArrayList<>();
        Login user = null;
        while (sc.hasNext()) {
            // Get data by line into array
            data = csvSplit(sc.nextLine());
            String[] accArr = data.get(0).split(";");
            for (String account : accArr) {
                if (account.equals(username)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (found)
            user = new Login(data.get(0), data.get(1), Boolean.parseBoolean(data.get(2)), Boolean.parseBoolean(data.get(3)), data.get(4), data.get(5));

        sc.close();
        return user;
    }

    public static void updateLoginCSV(String id, int option, String[] newData) throws Exception {
        // Option 1 Edit Login details
        // Option 2 Change password
        // Option 3 Change username
        // Option 4 Lock/Unlock Account
        // Option 5 New Login User

        String filepath = "\\database\\login.csv";
        File loginFile = new File(System.getProperty("user.dir") + filepath);
        boolean match = false;
        Scanner sc = new Scanner(loginFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(5).equalsIgnoreCase(id)) {
                match = true;
                String line_replace;
                if (option == 1) {
                    line_replace = newData[0] + ',' + newData[1] + ',' + newData[2] + ',' + newData[3] + ',' + newData[4] + ',' + newData[5];
                } else if (option == 2) {
                    line_replace = data.get(0) + ',' + newData[0] + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5);
                } else if (option == 3) {
                    line_replace = newData[0] + ',' + data.get(1) + ',' + data.get(2) + ',' + "FALSE" + ',' + data.get(4) + ',' + data.get(5);
                } else if (option == 4) {
                    line_replace = data.get(0) + ',' + data.get(1) + ',' + newData[0] + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5);
                } else {
                    line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5);
                }
                csvWriter.write(line_replace);
            } else {
                csvWriter.write(data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5));
            }
            csvWriter.write("\n");
        }
        if (option == 5 && !match) {
            csvWriter.write(newData[0] + ',' + newData[1] + ',' + "false" + ',' + "true" + ',' + "User" + ',' + id);
        } else if (option == 5) {
            System.out.println("Account exist.");
        }

        csvWriter.flush();
        csvWriter.close();
        sc.close();
        if(loginFile.delete()) {
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\login.csv"));
        }
    }

    public static  Person getUserProfile(String ID) throws FileNotFoundException, ParseException {
        File accountFile = new File(System.getProperty("user.dir") + "\\database\\user.csv");
        Scanner sc = new Scanner(accountFile);
        boolean found = false;
        ArrayList<String> data = new ArrayList<>();
        while (sc.hasNext()) {
            // Get data by line into array
            data = csvSplit(sc.nextLine());
            String[] accArr = data.get(0).split(";");
            for (String account : accArr) {
                if (account.equals(ID)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        String[] accountsArr = data.get(7).split(";");
        Person user = new Person(data.get(0), data.get(1), data.get(2), data.get(3), stringToDate(data.get(4)), data.get(5), data.get(6), accountsArr);
        sc.close();
        return user;
    }

    public static void addUserCSV(String id, String[] newData) throws Exception {
        String filepath = "\\database\\user.csv";
        File userFile = new File(System.getProperty("user.dir") + filepath);

        boolean match = false;
        Scanner sc = new Scanner(userFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equalsIgnoreCase(id)) {
                match = true;
            }
            csvWriter.write(data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6) + ',' + data.get(7));
            csvWriter.write("\n");
        }
        if (!match) {
            csvWriter.write(newData[0] + ',' + newData[1] + ',' + newData[2] + ',' + newData[3] + ',' + newData[4] + ',' + newData[5] + ',' + newData[6] + ',' + newData[7]);
        }
        csvWriter.flush();
        csvWriter.close();
        sc.close();
        if(userFile.delete()) {
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\user.csv"));
        }
    }

    public static void updateUserCSV(String id, String newData) throws Exception {
        String filepath = "\\database\\user.csv";
        File userFile = new File(System.getProperty("user.dir") + filepath);

        Scanner sc = new Scanner(userFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equals(id)) {
                String line_replace;
                line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + newData + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6) + ',' + data.get(7);
                csvWriter.write(line_replace);
            } else {
                csvWriter.write(data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6) + ',' + data.get(7));
            }
            csvWriter.write("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        sc.close();
        if(userFile.delete()) {
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\user.csv"));
        }
    }

    public static History getFinalTransaction(String accountNo) throws FileNotFoundException, ParseException {
        File accountFile = new File(System.getProperty("user.dir") + "\\database\\transaction.csv");
        Scanner sc = new Scanner(accountFile);
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> data;
        boolean found = false;
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equals(accountNo)) {
                temp = data;
                found = true;
            }
        }
        History his = new History(stringToDate(temp.get(1)), temp.get(2), stringToDouble(temp.get(5)), stringToDouble(temp.get(6)), stringToDouble(temp.get(7)));
        sc.close();
        return his;
    }

    public static ArrayList<History> getAllTransaction(String accountNo) throws FileNotFoundException, ParseException {
        File accountFile = new File(System.getProperty("user.dir") + "\\database\\transaction.csv");
        Scanner sc = new Scanner(accountFile);
        String[] temp = {};
        ArrayList<String> data = null;
        ArrayList<History> fullHistory = new ArrayList<>();

        boolean found = false;
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equals(accountNo)) {
                fullHistory.add(new History(stringToDate(data.get(1)), data.get(2), stringToDouble(data.get(5)), stringToDouble(data.get(6)), stringToDouble(data.get(7))));
                found = true;
            }
        }
        sc.close();
        return fullHistory;
    }

    public static Account getAccount(String accountNo) throws FileNotFoundException {
        String filepath = "\\database\\account.csv";
        File accountFile = new File(System.getProperty("user.dir") + filepath);
        Scanner sc = new Scanner(accountFile);
        ArrayList<String> data;
        Account acc = null;
        boolean found = false;
            while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equals(accountNo)) {
                acc = new Account(data.get(0), stringToDouble(data.get(1)), stringToDouble(data.get(2)), stringToDouble(data.get(3)), stringToDouble(data.get(4)), stringToDouble(data.get(5)), Boolean.parseBoolean(data.get(6)));
                found = true;
            } else {
                if (found) {
                    break;
                }
            }
        }
        sc.close();
        return acc;
    }

    public static void addAccountCSV(String accountNo) throws Exception {
        String filepath = "\\database\\account.csv";
        File accountFile = new File(System.getProperty("user.dir") + filepath);

        boolean match = false;
        Scanner sc = new Scanner(accountFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equalsIgnoreCase(accountNo)) {
                match = true;
            }
            csvWriter.write(data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6));
            csvWriter.write("\n");
        }
        if (!match) {
            csvWriter.write(accountNo + ',' + "0" + ',' + "0" + ',' + "1000" + ',' + "1000" + ',' + "500" + ',' + "FALSE");
        }
        csvWriter.flush();
        csvWriter.close();
        sc.close();
        if(accountFile.delete()) {
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\account.csv"));
        }
    }

    public static void updateAccountCSV(String accountNo, int indexOfColumn, String newData) throws Exception {
        String filepath = "\\database\\account.csv";
        File accountFile = new File(System.getProperty("user.dir") + filepath);

        Scanner sc = new Scanner(accountFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            data = csvSplit(sc.nextLine());
            if (data.get(0).equals(accountNo)) {
                String line_replace;
                switch (indexOfColumn) {
                    case 1 : {
                        line_replace = data.get(0) + ',' + newData + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6);
                        csvWriter.write(line_replace);
                        break;
                    }
                    case 2 : {
                        line_replace = data.get(0) + ',' + data.get(1) + ',' + newData + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6);
                        csvWriter.write(line_replace);
                        break;
                    }
                    case 3 : {
                        line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + newData + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6);
                        csvWriter.write(line_replace);
                        break;
                    }
                    case 4 : {
                        line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + newData + ',' + data.get(5) + ',' + data.get(6);
                        csvWriter.write(line_replace);
                        break;
                    }
                    case 5 : {
                        line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + newData + ',' + data.get(6);
                        csvWriter.write(line_replace);
                        break;
                    }
                    case 6 : {
                        line_replace = data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + newData;
                        csvWriter.write(line_replace);
                        break;
                    }
                }
            } else {
                csvWriter.write(data.get(0) + ',' + data.get(1) + ',' + data.get(2) + ',' + data.get(3) + ',' + data.get(4) + ',' + data.get(5) + ',' + data.get(6));
            }
            csvWriter.write("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        sc.close();
        if (accountFile.delete()) {
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\account.csv"));
        }
    }

    public static void addHistoryLog(Account acc, String accountNo, History newHistoryData) throws Exception {
        String filepath = "\\database\\transaction.csv";
        File accountFile = new File(System.getProperty("user.dir") + filepath);
        Scanner sc = new Scanner(accountFile);
        ArrayList<String> data;
        FileWriter csvWriter = new FileWriter(createTempCsv());
        while (sc.hasNext()) {
            csvWriter.write(sc.nextLine());
            csvWriter.write("\n");
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String strDate = formatter.format(newHistoryData.getDate());
            csvWriter.write(accountNo + "," + strDate + "," + newHistoryData.getTransactionDetails() + "," + "," + "," + newHistoryData.getWithdrawalAmt() + "," + newHistoryData.getDepositAmt() + "," + newHistoryData.getBalanceAmt());
            csvWriter.write("\n");
            csvWriter.flush();
            csvWriter.close();
            sc.close();

            if (!accountFile.delete()) {
                String message = accountFile.exists() ? "is in use by another app" : "does not exist";
                throw new IOException("Cannot delete file, because file " + message + ".");
            }
            File tempFile = new File(System.getProperty("user.dir") + "\\database\\temp.csv");

            tempFile.renameTo(new File(System.getProperty("user.dir") + "\\database\\transaction.csv"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static File createTempCsv() throws Exception {
        File tempCsv = new File(System.getProperty("user.dir") + "\\database\\temp.csv");
        tempCsv.createNewFile();
        return tempCsv;
    }

    public static Date stringToDate(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }

    public static double stringToDouble(String num) {
        if (num.equals("")) {
            //For empty data
            return 0;
        } else {
            return Double.parseDouble(num);
        }
    }

    public static ArrayList<String> csvSplit(String data) {
        boolean inString = false;
        ArrayList<String> dataArr = new ArrayList<>();
        String line = "";
        for (int i = 0, n = data.length(); i < n; i++) {
            char c = data.charAt(i);
            if (c == '"') {
                // Enter here if still in string last "
                inString = !inString;
            } else if (!inString && c == ',') {
                dataArr.add(line);
                line = "";
            } else {
                line += c;
            }
        }
        dataArr.add(line);
        return dataArr;
    }
}
