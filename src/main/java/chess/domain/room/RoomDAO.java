package chess.domain.room;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    private final Gson gson = new Gson();

    public Connection getConnection() {
        Connection con = null;
        String server = "localhost:13306"; // MySQL 서버 주소
        String database = "test"; // MySQL DATABASE 이름
        String option = "?useSSL=false&serverTimezone=UTC";
        String userName = "root"; //  MySQL 서버 아이디
        String password = "root"; // MySQL 서버 비밀번호

        // 드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! JDBC Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 드라이버 연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + option, userName, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return con;
    }

    // 드라이버 연결해제
    public void closeConnection(Connection con) {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            System.err.println("con 오류:" + e.getMessage());
        }
    }

    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO room (room_id, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";
        PreparedStatement pstmt = getConnection().prepareStatement(query);
        pstmt.setString(1, room.getRoomId());
        pstmt.setString(2, room.getTurn());
        pstmt.setString(3, room.getState().toString());
        pstmt.executeUpdate();
    }

    public Room findByRoomId(String roomId) throws SQLException {
        String query = "SELECT * FROM room WHERE room_id = ?";
        PreparedStatement pstmt = getConnection().prepareStatement(query);
        pstmt.setString(1, roomId);
        ResultSet rs = pstmt.executeQuery();

        return Optional.ofNullable(getRoom(rs))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void validateRoomExistence(String roomId) throws SQLException {
        String query = "SELECT * FROM room WHERE room_id = ?";
        PreparedStatement pstmt = getConnection().prepareStatement(query);
        pstmt.setString(1, roomId);
        ResultSet rs = pstmt.executeQuery();

        Optional.ofNullable(getRoom(rs))
                .ifPresent(room -> {
                    throw new IllegalArgumentException();
                });
    }

    private Room getRoom(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Room(
                rs.getString("room_id"),
                rs.getString("turn"),
                gson.fromJson(rs.getString("state"), JsonObject.class));
    }

    public List<Room> getAllRoom() throws SQLException {
        String query = "SELECT room_id FROM room";
        PreparedStatement pstmt = getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Room> rooms = new ArrayList<>();
        while (rs.next()) {
            rooms.add(new Room(rs.getString("room_id"), null, null));
        }
        return rooms;
    }
}
