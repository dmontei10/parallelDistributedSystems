package app;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/deposits/*")
public class API_Deposits extends HttpServlet {

    private int id;
    private int quantity;

    Connection conn = null;
    Statement stmt = null;

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://db:5432/armazem";

    static final String USER = "postgres";
    static final String PASS = "admin";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if(pathInfo == null || pathInfo.equals("/")) {

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();

                String sql = "SELECT * FROM deposit";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    id = rs.getInt("id");
                    quantity = rs.getInt("quantity");

                    Data data = new Data(id, quantity);

                    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                    jsonBuilder.add("id", data.getId());
                    jsonBuilder.add("quantity", data.getQuantity());
                    JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
                    jsonWriter.writeObject(jsonBuilder.build());

                }

                rs.close();
                return;
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                    se2.printStackTrace();
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        String[] splits = pathInfo.split("/");

        if(splits.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String itemId = splits[1];
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM deposit WHERE id='"+ Integer.parseInt(itemId) +"'";
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();
            id = rs.getInt("id");
            quantity = rs.getInt("quantity");

            if(!sql.contains(itemId)) {

                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Data data = new Data(id, quantity);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("quantity", data.getQuantity());
            JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
            jsonWriter.writeObject(jsonBuilder.build());
            rs.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    // ---------------------------------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        id = Integer.parseInt(req.getParameter("id"));
        quantity = Integer.parseInt(req.getParameter("quantity"));

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "INSERT INTO deposit (id, quantity) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, quantity);
            statement.executeUpdate();

            Data data = new Data(id, quantity);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("quantity", data.getQuantity());
            JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
            jsonWriter.writeObject(jsonBuilder.build());

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}



