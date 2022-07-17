import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;

public class SQLite {
    static Connection connection = null;
    /**
     * Conexion a la base de datos
     * @return retorna connection
     */
    public static java.sql.Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:DailyDevotional.db");
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveRegister(Connection connection, String idUser, String userName, String state, String languages){
        String query = "INSERT INTO users ( idUser,  userName, state, languages) VALUES"
                + "('"+idUser+"', '"+userName+"', '"+state+"', '"+languages+"');";

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  static ResultSet getUsers(Connection connection){
        String query = "SELECT idUser, languages FROM users WHERE state ='enable'";
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public  static ResultSet getVerse(Connection connection, String languages){
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());
        int verse = secureRandom.nextInt((31102-1)+1)+1;
        String query = "SELECT text FROM "+languages+" WHERE id ="+verse;
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public  static boolean getCheck(Connection connection, String idUser){
        String query = "SELECT idUser FROM users WHERE idUser = '"+idUser+"'";
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if(resultSet.isClosed()){
                System.out.println("FALSO");
                return false;

            }else {
                System.out.println("VERDADERO");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateLanguages(Connection connection, String language, String idUser){
        String query = "UPDATE users SET languages = '"+language+"' WHERE idUser = '"+idUser+"'";

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  static ResultSet getLanguage(Connection connection, String idUser){
        String query = "SELECT languages FROM users WHERE idUser = '"+idUser+"'";
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public static void updateState(Connection connection,  String idUser){
        String query = "UPDATE users SET state = 'enable' WHERE idUser = '"+idUser+"'";

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}