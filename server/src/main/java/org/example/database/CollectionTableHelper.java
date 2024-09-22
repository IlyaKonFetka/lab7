package org.example.database;

import org.example.Main;
import org.example.interfaces.Console;
import org.example.model.Color;
import org.example.model.Coordinates;
import org.example.model.Location;
import org.example.model.Person;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CollectionTableHelper {
    private DBHelper dbHelper;
    private Console console;
    private String tableName;



    public CollectionTableHelper(DBHelper dbHelper, Console console, String tableName) {
        this.dbHelper = dbHelper;
        this.console = console;
        this.tableName = tableName;
    }

    public List<Integer> deleteAllForUser(long userId) throws SQLException {
        List<Integer> removedIds = new ArrayList<>();
        String selectSql = "SELECT id FROM " + tableName + " WHERE owner_id = ?";
        String deleteSql = "DELETE FROM " + tableName + " WHERE owner_id = ?";

        Connection conn = dbHelper.getConnection();
        try {
            conn.setAutoCommit(false);


            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setLong(1, userId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        removedIds.add(rs.getInt("id"));
                    }
                }
            }


            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, userId);
                deleteStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

        return removedIds;
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "coordinates_x INT, " +
                "coordinates_y FLOAT NOT NULL, " +
                "creation_date TIMESTAMP NOT NULL, " +
                "height BIGINT NOT NULL, " +
                "weight BIGINT NOT NULL, " +
                "passport_id VARCHAR(255) NOT NULL, " +
                "hair_color VARCHAR(50), " +
                "location_x FLOAT, " +
                "location_y INT, " +
                "location_name VARCHAR(255), " +
                "owner_id INT NOT NULL" +
                ")";

        try (Statement stmt = dbHelper.getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }

    public TreeSet<Person> readCollection() {
        TreeSet<Person> collection = new TreeSet<>();
        String sql = "SELECT * FROM " + tableName;

        try (Statement stmt = dbHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));

                Coordinates coordinates = new Coordinates();
                coordinates.setX(rs.getInt("coordinates_x"));
                coordinates.setY(rs.getFloat("coordinates_y"));
                person.setCoordinates(coordinates);

                person.setCreationDate(rs.getObject("creation_date", LocalDateTime.class));
                person.setHeight(rs.getLong("height"));
                person.setWeight(rs.getLong("weight"));
                person.setPassportID(rs.getString("passport_id"));

                String colorStr = rs.getString("hair_color");
                if (colorStr != null) {
                    person.setHairColor(Color.valueOf(colorStr));
                }

                if (rs.getString("location_name") != null) {
                    Location location = new Location();
                    location.setX(rs.getFloat("location_x"));
                    location.setY(rs.getInt("location_y"));
                    location.setName(rs.getString("location_name"));
                    person.setLocation(location);
                }

                person.setOwnerID(rs.getInt("owner_id"));

                collection.add(person);
            }
        } catch (SQLException e) {
        }

        return collection;
    }
    public int getTableRowCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = dbHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public LocalDateTime getLastModificationTime() throws SQLException {
        String sql = "SELECT MAX(creation_date) FROM " + tableName;
        try (Statement stmt = dbHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getObject(1, LocalDateTime.class);
            }
        }
        return null;
    }
    public DBHelper getDbHelper() {
        return this.dbHelper;
    }

    public String getTableName() {
        return this.tableName;
    }
    public Integer insertAndGetId(Person person, long userId) throws SQLException {
        String sql = "INSERT INTO " + tableName +
                " (name, coordinates_x, coordinates_y, creation_date, height, weight, passport_id, hair_color, location_x, location_y, location_name, owner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, person.getName());
            pstmt.setInt(2, person.getCoordinates().getX());
            pstmt.setFloat(3, person.getCoordinates().getY());
            pstmt.setTimestamp(4, Timestamp.valueOf(person.getCreationDate()));
            pstmt.setLong(5, person.getHeight());
            pstmt.setLong(6, person.getWeight());
            pstmt.setString(7, person.getPassportID());
            pstmt.setString(8, person.getHairColor() != null ? person.getHairColor().name() : null);

            if (person.getLocation() != null) {
                pstmt.setFloat(9, person.getLocation().getX());
                pstmt.setInt(10, person.getLocation().getY());
                pstmt.setString(11, person.getLocation().getName());
            } else {
                pstmt.setNull(9, java.sql.Types.FLOAT);
                pstmt.setNull(10, java.sql.Types.INTEGER);
                pstmt.setNull(11, java.sql.Types.VARCHAR);
            }

            pstmt.setLong(12, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public boolean update(Person person, long userId) {
        String updateSql = "UPDATE " + tableName + " SET name = ?, coordinates_x = ?, coordinates_y = ?, " +
                "height = ?, weight = ?, passport_id = ?, hair_color = ?, " +
                "location_x = ?, location_y = ?, location_name = ? WHERE id = ? AND owner_id = ?";

        try (PreparedStatement updateStmt = dbHelper.getConnection().prepareStatement(updateSql)) {
            updateStmt.setString(1, person.getName());
            updateStmt.setInt(2, person.getCoordinates().getX());
            updateStmt.setFloat(3, person.getCoordinates().getY());
            updateStmt.setLong(4, person.getHeight());
            updateStmt.setLong(5, person.getWeight());
            updateStmt.setString(6, person.getPassportID());
            updateStmt.setString(7, person.getHairColor() != null ? person.getHairColor().name() : null);

            if (person.getLocation() != null) {
                updateStmt.setFloat(8, person.getLocation().getX());
                updateStmt.setInt(9, person.getLocation().getY());
                updateStmt.setString(10, person.getLocation().getName());
            } else {
                updateStmt.setNull(8, Types.FLOAT);
                updateStmt.setNull(9, Types.INTEGER);
                updateStmt.setNull(10, Types.VARCHAR);
            }

            updateStmt.setInt(11, person.getID());
            updateStmt.setLong(12, userId);

            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            Main.logger.error(e);
            return false;
        }
    }

    public boolean delete(int id, long userId) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ? AND owner_id = ?";

        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setLong(2, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteAll(long userId) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE owner_id = ?";

        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}

