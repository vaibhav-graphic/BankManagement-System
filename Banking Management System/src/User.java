import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;
    private Scanner scanner;
    public User(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("Enter your Full Name ");
        String fullName = scanner.nextLine();
        System.out.println("Enter your email");
        String email = scanner.nextLine();
        System.out.println("Enter your password");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User Already exist for this email Address!");
            return;
        }

        String sql = "INSERT INTO user(full_name,email,password) VALUES (?,?,?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int rowAffacted = preparedStatement.executeUpdate();

            if(rowAffacted > 0){
                System.out.println("Registration is Succesfull");
            }else{
                System.out.println("Registration failed!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //return type String because now we can use this email throught out the programe
    //no need to get email again and again
    public String login(){
        scanner.nextLine();
        System.out.println("Enter yput email");
        String email = scanner.nextLine();
        System.out.println("Enter yout password");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM user WHERE email = '?' AND password = '?'";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return email;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email){

        String sql = "SELECT * FROM user WHERE email = '?'";

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
