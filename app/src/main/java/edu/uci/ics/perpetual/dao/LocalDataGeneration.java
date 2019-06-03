package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDataGeneration {

    static String startTime = "2017-11-20";
    static String endTime = "2018-08-17";

    public static void main(String[] args) {
        // Entering user's name or part of user's name (but has to be able to speficy only one user) are both okay.
        generateData("Yiming Lin");
    }

    public static boolean generateData(String name) {
        try (Connect connectServer = new Connect("server");
             Connect connectLocal = new Connect("local")) {
            Connection serverConn = connectServer.getConnection();
            Connection localConn = connectLocal.getConnection();

            // Create local table, table existence checked in advance.
            Statement st1 = localConn.createStatement();
            String tableName = getTableName(name);
            st1.execute(String.format("create table %s (timestamp datetime, sensor_id varchar(255),mac varchar(255))", tableName));

            // Get Mac-address list
            List<String> macs = new ArrayList<>();
            List<String> users = new ArrayList<>();
            if (name.equals("Sharad Mehrotra")) {
                macs.add("c0c9e34eb7a2d17ea4a2ebd7ee4c00293cc24996");
                users.add("Sharad Mehrotra");
            } else if (name.equals("Yiming Lin")) {
                macs.add("9867312b6133ba7e9832f2ce3c74236ed4be16fc");
                users.add("Yiming Lin");
            } else if (name.equals("Nalini Venkatasubramanian")) {
                macs.add("4e9eb411e04463833c7ebc1a1b2ecc66a44cf4ce");
                users.add("Nalini Venkatasubramanian");
            } else if (name.equals("Dhrub Ghosh")) {
                macs.add("11d58fd604e31332d0e061f9e445058afb453291");
                users.add("Dhrub Ghosh");
            } else {
                Statement st2 = serverConn.createStatement();
                ResultSet rs3 = st2.executeQuery(String.format("select SENSOR.id, USER.name from USER, SENSOR where USER.name like '%%%s%%' " +
                        "and USER.SEMANTIC_ENTITY_ID = SENSOR.USER_ID and (SENSOR.sensor_type_id = 3 or SENSOR.sensor_type_id is null)", name));
                while (rs3.next()) {
                    macs.add(rs3.getString(1));
                    users.add(rs3.getString(2));
                }
            }

            if (macs.size() == 0) {
                System.out.println(String.format("Didn't find any mac address entry for user %s in SENSOR table.", name));
                return false;
            }

            // Populate the table
            PreparedStatement ps1 = serverConn.prepareStatement("select timeStamp, sensor_id from OBSERVATION " +
                    "where payload = ? and timeStamp > ? and timeStamp < ? and observation_type_id = 1;");
            ps1.setString(2, startTime);
            ps1.setString(3, endTime);
            long queryStart = System.currentTimeMillis();
            int total_count = 0;
            for (int i = 0; i < macs.size(); ++i) {
                String mac = macs.get(i);
                if (mac.contains("-")) { // This is a beacon id.
                    continue;
                }
                String mac_short = mac.substring(0, 4);
                String clientID = String.format("{\"client_id\":\"%s\"}", mac);
                ps1.setString(1, clientID);
                ResultSet rs1 = ps1.executeQuery();
                int count = 0;
                PreparedStatement ps2 = localConn.prepareStatement(String.format("insert into %s values(?,?,?)", tableName));
                while (rs1.next()) {
                    ++count;
                    ps2.setString(1, rs1.getString(1));
                    ps2.setString(2, rs1.getString(2));
                    ps2.setString(3, mac_short);
                    ps2.executeUpdate();
                }
                long tick = System.currentTimeMillis();
                System.out.println(String.format("User name: %s, Mac-address: %s, count: %d, time: %.2f s", name, mac
                        , count, (double) (tick - queryStart) / 1000));
                total_count += count;
            }
            long queryEnd = System.currentTimeMillis();
            System.out.println(String.format("Table %s with %d lines created, queryTime %.2f s", name, total_count,
                    (double) (queryEnd - queryStart) / 1000));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTableName(String name) {
        if (name.equals("EunJeong Joyce Shin"))
            name = "joyce";
        else if (name.equals("Roberto Yus"))
            name = "roberto";
        else if (name.equals("Primal Pappachan"))
            name = "primal";
        else if (name.equals("Abdul Alsaudi"))
            name = "abdul";
        else if (name.equals("Dhrub"))
            name = "dhrub ghosh";
        name = name.replaceAll(" ", "");
        name = name.replaceAll("\\.", "");
        name = name.toLowerCase();
        return String.format("%s_%sto%s", name, startTime.replaceAll("-", ""), endTime.replaceAll("-", ""));
    }

    public static boolean generateDataFromEmail(String email) {
        try (Connect connectServer = new Connect("server");
             Connect connectLocal = new Connect("local")) {
            Connection serverConn = connectServer.getConnection();
            Connection localConn = connectLocal.getConnection();

            // Create local table, table existence checked in advance.
            Statement st1 = localConn.createStatement();
            String tableName = getTableNameFromEmail(email);
            st1.execute(String.format("create table %s (timestamp datetime, sensor_id varchar(255),mac varchar(255))", tableName));

            // Get Mac-address list
            List<String> macs = new ArrayList<>();
            Statement st2 = serverConn.createStatement();
            ResultSet rs3 = st2.executeQuery(String.format("select SENSOR.id, USER.email\n" +
                    "from USER, SENSOR\n" +
                    "where USER.email = '%s'\n" +
                    "  and USER.SEMANTIC_ENTITY_ID = SENSOR.USER_ID\n" +
                    "  and (SENSOR.sensor_type_id = 3 or SENSOR.sensor_type_id is null);", email));
            while (rs3.next()) {
                macs.add(rs3.getString(1));
            }

            if (macs.size() == 0) {
                PreparedStatement ps = serverConn.prepareStatement("select s.id\n" +
                        "from USER as u,\n" +
                        "     SENSOR as s,\n" +
                        "     PLATFORM as p\n" +
                        "where p.USER_ID = u.SEMANTIC_ENTITY_ID\n" +
                        "  and s.PLATFORM_ID = p.platform_id\n" +
                        "  and email = ?;");
                ps.setString(1,email);
                ResultSet rs4 = ps.executeQuery();
                while (rs4.next()){
                    macs.add(rs4.getString(1));
                }
                if (macs.size()==0){
                    System.out.println(String.format("Didn't find any mac address entry for email %s in SENSOR table.", email));
                    return false;
                }
            }

            // Populate the table
            PreparedStatement ps1 = serverConn.prepareStatement("select timeStamp, sensor_id from OBSERVATION " +
                    "where payload = ? and timeStamp > ? and timeStamp < ? and observation_type_id = 1;");
            ps1.setString(2, startTime);
            ps1.setString(3, endTime);
            long queryStart = System.currentTimeMillis();
            int total_count = 0;
            for (int i = 0; i < macs.size(); ++i) {
                String mac = macs.get(i);
                if (mac.contains("-")) { // This is a beacon id.
                    continue;
                }
                String mac_short = mac.substring(0, 4);
                String clientID = String.format("{\"client_id\":\"%s\"}", mac);
                ps1.setString(1, clientID);
                ResultSet rs1 = ps1.executeQuery();
                int count = 0;
                PreparedStatement ps2 = localConn.prepareStatement(String.format("insert into %s values(?,?,?)", tableName));
                while (rs1.next()) {
                    ++count;
                    ps2.setString(1, rs1.getString(1));
                    ps2.setString(2, rs1.getString(2));
                    ps2.setString(3, mac_short);
                    ps2.executeUpdate();
                }
                long tick = System.currentTimeMillis();
                System.out.println(String.format("Email: %s, Mac-address: %s, count: %d, time: %.2f s", email, mac
                        , count, (double) (tick - queryStart) / 1000));
                total_count += count;
            }
            long queryEnd = System.currentTimeMillis();
            System.out.println(String.format("Email %s has %d different created, queryTime %.2f s", email, total_count,
                    (double) (queryEnd - queryStart) / 1000));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTableNameFromEmail(String email) {
        String name = email.replace('@', '_').replace('.', '_');
        return String.format("%s_%sto%s", name,
                startTime.replaceAll("-", ""), endTime.replaceAll("-", ""));
    }
}
