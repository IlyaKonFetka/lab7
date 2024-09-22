package org.example.database;

import org.example.exceptions.UserAlreadyExistException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WrongUserPasswordException;
import org.example.interfaces.Console;
import org.example.user.User;

import java.sql.*;

public class UsersTableHelper {
    private DBHelper dbHelper;
    private String tableName;
    private Console console;

    public UsersTableHelper(DBHelper dbHelper, String tableName, Console console) {
        this.dbHelper = dbHelper;
        this.tableName = tableName;
        this.console = console;
    }

    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "userName VARCHAR(255) UNIQUE NOT NULL, " +
                "passwordHash VARCHAR(255) NOT NULL" +
                ")";
        try (Statement stmt = dbHelper.getConnection().createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            console.printError("UsersTableHelper.createTable");
            console.printError(e);
        }
    }

    public long insertUser(String userName, String passwordHash) throws UserAlreadyExistException, SQLException {
        if (userExists(userName)) {
            throw new UserAlreadyExistException("Пользователь с именем " + userName + " уже существует");
        }

        String sql = "INSERT INTO " + tableName + " (userName, passwordHash) VALUES (?, ?) RETURNING id";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, passwordHash);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Не удалось получить id нового пользователя");
                }
            }
        } catch (SQLException e) {
            console.printError("UsersTableHelper.insertUser");
            console.printError(e);
            throw e;
        }
    }

    private boolean userExists(String userName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE userName = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public long checkUserPassword(User user) throws UserNotFoundException, WrongUserPasswordException, SQLException {
        String sql = "SELECT id, passwordHash FROM " + tableName + " WHERE userName = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getLogin());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String storedPasswordHash = rs.getString("passwordHash");
                    if (storedPasswordHash.equals(user.getHashPassword())) {
                        return userId;
                    } else {
                        throw new WrongUserPasswordException("Неверный пароль для пользователя: " + user.getLogin());
                    }
                } else {
                    throw new UserNotFoundException("Пользователь не найден: " + user.getLogin());
                }
            }
        } catch (SQLException e) {
            console.printError("UsersTableHelper.checkUserPassword");
            console.printError(e);
            throw e;
        }
    }


    public String login(String userName, String inputPasswordHash) {
        String sql = "SELECT passwordHash FROM " + tableName + " WHERE userName = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            pstmt.setString(1, userName);
            if (!rs.next()) {
                return "Пользователь с таким именем отсутствует";
            }
            String storedPasswordHash = rs.getString("passwordHash");
            if (!storedPasswordHash.equals(inputPasswordHash)) {
                return "Неверный пароль";
            }
            return "Вход выполнен успешно";
        } catch (SQLException e) {
            console.printError("UsersTableHelper.login");
            console.printError(e);
            return "Ошибка при входе";
        }
    }

    public String getTableContents() throws SQLException {
        StringBuilder tableBuilder = new StringBuilder();
        String sql = "SELECT * FROM " + tableName;
        Statement stmt = dbHelper.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sql);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        int[] maxColumnLengths = new int[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            maxColumnLengths[i - 1] = metaData.getColumnName(i).length();
        }
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                int length = value == null ? 0 : value.length();
                maxColumnLengths[i - 1] = Math.max(maxColumnLengths[i - 1], length);
            }
        }
        rs.beforeFirst();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            tableBuilder.append(String.format("%-" + (maxColumnLengths[i - 1] + 2) + "s", columnName));
        }
        tableBuilder.append("\n");
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                tableBuilder.append(String.format("%-" + (maxColumnLengths[i - 1] + 2) + "s", value == null ? "" : value));
            }
            tableBuilder.append("\n");
        }
        return tableBuilder.toString();
    }
}