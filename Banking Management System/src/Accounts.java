import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String sql = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES (?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter your name");
            String fullName = scanner.nextLine();
            System.out.println("Enter your Ammount");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter your security pin");
            String security = scanner.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,fullName);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security);

                int rowAffacted = preparedStatement.executeUpdate();

                if(rowAffacted > 0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account creation failed");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        throw new RuntimeException("Account already exist1");
    }
    public long getAccount(String email){
        String sql = "SELECT account_number from accounts WHERE email = '?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return rs.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        throw new RuntimeException("Account Number Does not exist!");
    }
    public long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");

            if(rs.next()){
                long last_account_number = rs.getLong("account_number");
                return last_account_number;
            }else{
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }
    public boolean account_exist(String email){
        String sql = "SELECT * FROM user where emil = '?'";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
