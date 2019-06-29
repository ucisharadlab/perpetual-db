package edu.uci.ics.perpetual.persistence.mysql;

import edu.uci.ics.perpetual.storage.MysqlStorage;
import edu.uci.ics.perpetual.types.MetadataType;
import edu.uci.ics.perpetual.util.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class MySQLService {

    private MysqlStorage storage;
    private final String INSERT = "INSERT INTO CachingRules VALUES(?, ?);";
    private final String SELECT = "SELECT * FROM CachingRules WHERE version = ?;";
    private final String NEW_VER = "INSERT INTO CachingVersions VALUES(?, ?);";
    private final String SELECT_MAX_VER = "SELECT max(version) FROM CachingVersions;";

    public MySQLService(MysqlStorage storage) {
        this.storage = storage;
    }

    public void insertRule(String rule, int version) {
        try {
            PreparedStatement ps = storage.getConn().prepareStatement(INSERT);
            ps.setString(1, rule);
            ps.setInt(2, version);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCachingRules(int version) {
        return null;
    }

    public int getMaxVersion() {
        try {
            ResultSet rs = storage.getConn().prepareStatement(SELECT_MAX_VER).executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return -1;
    }

    public int bumpVersion() {
        int version = getMaxVersion();
        try {
            PreparedStatement ps = storage.getConn().prepareStatement(NEW_VER);
            ps.setInt(1,version+1);
            ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return version+1;
    }

    public Map<Integer, List<String>> getAllRules() {

        return null;
    }

}
