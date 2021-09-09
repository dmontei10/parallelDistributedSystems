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

@WebServlet("/deliveries/*")
public class API_Deliveries extends HttpServlet {

    private int id;
    private int quantity;
    private int local_id;
    private int item_id;

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

                String sql = "SELECT id, quantity, local_id, item_id FROM delivery";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    id = rs.getInt("id");
                    quantity = rs.getInt("quantity");
                    local_id = rs.getInt("local_id");
                    item_id = rs.getInt("item_id");

                    Data data = new Data(id, quantity, local_id, item_id);

                    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                    jsonBuilder.add("id", data.getId());
                    jsonBuilder.add("quantity", data.getQuantity());
                    jsonBuilder.add("local_id", data.getLocal_id());
                    jsonBuilder.add("item_id", data.getItem_id());
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

        String deliveryId = splits[1];
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM delivery WHERE id='"+ Integer.parseInt(deliveryId) +"'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            id = rs.getInt("id");
            quantity = rs.getInt("quantity");
            local_id = rs.getInt("local_id");
            item_id = rs.getInt("item_id");

            if(!sql.contains(deliveryId)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Data data = new Data(id, quantity, local_id, item_id);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("quantity", data.getQuantity());
            jsonBuilder.add("local_id", data.getLocal_id());
            jsonBuilder.add("item_id", data.getItem_id());
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
        local_id = Integer.parseInt(req.getParameter("local_id"));
        item_id = Integer.parseInt(req.getParameter("item_id"));

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "INSERT INTO delivery (id, quantity, local_id, item_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, quantity);
            statement.setInt(3, local_id);
            statement.setInt(4, item_id);
            statement.executeUpdate();

            Data data = new Data(id, quantity, local_id, item_id);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("quantity", data.getQuantity());
            jsonBuilder.add("local_id", data.getLocal_id());
            jsonBuilder.add("item_id", data.getItem_id());
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
    //---------------------------------------------------------------------------------------------
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {

        String pathInfo = req.getPathInfo();
        String[] splits = pathInfo.split("/");

        String deliveryId = splits[1];
        
        local_id = Integer.parseInt(req.getParameter("local_id"));

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "UPDATE delivery SET local_id= ? WHERE id='"+ Integer.parseInt(deliveryId) +"'";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, local_id);
            statement.executeUpdate();

            Data data = new Data(id, quantity, local_id, item_id);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("quantity", data.getQuantity());
            jsonBuilder.add("local_id", data.getLocal_id());
            jsonBuilder.add("item_id", data.getItem_id());
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



