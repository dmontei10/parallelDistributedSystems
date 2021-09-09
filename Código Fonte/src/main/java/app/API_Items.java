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

@WebServlet("/items/*")
public class API_Items extends HttpServlet {

    private int id;
    private String name;
    private String description;
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

                String sql = "SELECT id, name, description, quantity FROM item LEFT JOIN deposit USING (id)";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    id = rs.getInt("id");
                    name = rs.getString("name");
                    description = rs.getString("description");
                    quantity = rs.getInt("quantity");

                    Data data = new Data(id, name, description, quantity);

                    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                    jsonBuilder.add("id", data.getId());
                    jsonBuilder.add("name", data.getName());
                    jsonBuilder.add("quantity", data.getQuantity());
                    jsonBuilder.add("description", data.getDescription());
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

            String sql = "SELECT id, name, description, quantity FROM item LEFT JOIN deposit USING (id) WHERE id='"+ Integer.parseInt(itemId) +"'";
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();
            id = rs.getInt("id");
            name = rs.getString("name");
            description = rs.getString("description");
            quantity = rs.getInt("quantity");

            if(!sql.contains(itemId)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Data data = new Data(id, name, description, quantity);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("name", data.getName());
            jsonBuilder.add("quantity", data.getQuantity());
            jsonBuilder.add("description", data.getDescription());
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
    //----------------------------------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        id = Integer.parseInt(req.getParameter("id"));
        name = req.getParameter("name");
        description = req.getParameter("description");

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "INSERT INTO item (id, name, description) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.executeUpdate();

            Data data = new Data(id, name, description);

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("id", data.getId());
            jsonBuilder.add("name", data.getName());
            jsonBuilder.add("description", data.getDescription());
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
    // -----------------------------------------------------------------------------------------------
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {

        String pathInfo = req.getPathInfo();
        String[] splits = pathInfo.split("/");

        String itemId = splits[1];

        description = req.getParameter("name");

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "UPDATE item SET description= ? WHERE id='"+ Integer.parseInt(itemId) +"'";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, description);
            statement.executeUpdate();

            Data data = new Data(Integer.parseInt(itemId));

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("Alterado o nome do item id", data.getId());
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
    // -----------------------------------------------------------------------------
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        String[] splits = pathInfo.split("/");
        String itemId = splits[1];

        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "DELETE FROM item WHERE id='"+ Integer.parseInt(itemId) +"'";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            Data data = new Data(Integer.parseInt(itemId));

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("Eliminado item com o id", data.getId());
            JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
            jsonWriter.writeObject(jsonBuilder.build());

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}



