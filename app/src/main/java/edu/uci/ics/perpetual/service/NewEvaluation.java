package service;

import dao.Connect;
import dao.LocalDataGeneration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewEvaluation {

    static Set<String> skip_list = new HashSet<>();

    public static void main(String args[]) {
        skip_list.add("Chen Li");
        skip_list.add("Nisha Panwar");
        TestWithDifferentAmoutData();
    }

    static void TestWithFixedParameters() {
        String resultDB = "result_0331";
        String path = "./test_file/sensys_groundtruth.txt";
        String logPath = "./logs/temp.txt";
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)))) {
            evaluateFromTXTQuery(path, 20, 100, "20180101", "20180630", 4, 2,
                    logger, true, resultDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void TestWithDifferentParameters() {
        String resultDB = "result_0331";
        String path = "./test_file/sensys_truth_v1.txt";
        String logPath = "./logs/temp.txt";
        int pos_list[] = {15};
        int neg_list[] = {240};
        int step_list[] = {2};
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)))) {
            for (int pos : pos_list)
                for (int neg : neg_list)
                    for (int step : step_list) {
                        evaluateFromTXTQuery(path, pos, neg, "20180101", "20180630",
                                step, 2, logger, true, resultDB);
                    }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void TestWithDifferentAmoutData() {
        String resultDB = "result_0331";
        String path = "./test_file/sensys_truth_v1.txt";
        String logPath = "./logs/temp.txt";
        int pos = 25;
        int neg = 200;
        String[] start_list = { "20180510", "20180501", "20180420",
                "20180410", "20180401", "20180310", "20180225", "20180201"};
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)))) {
            for (String start : start_list) {
                evaluateFromTXTQuery(path, pos, neg, start, "20180630",
                        2, 2, logger, true, resultDB);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void evaluateFromTXTQuery(String path, int pos, int neg, String start_day, String end_day, int step,
                                     int verbose, PrintWriter logger, boolean dbMode, String resultDB) {
        // Use a generated query file to evaluate AP prediction result.
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String name;
            int correctIn = 0, correctOut = 0, correctAP = 0;
            int totalIn = 0, totalOut = 0, total = 0;
            APtoRoom apToRoom = new APtoRoom();
            apToRoom.load();
            Map<String, Integer> m1 = new HashMap<>();
            Map<String, Integer> m2 = new HashMap<>();
            long maxTime = 0;
            long minTime = 100000000;
            double avgTime = 0;
            while ((name = br.readLine()) != null) {
                if (skip_list.contains(name)) {
                    // Temperarily skip some users
                    br.readLine();
                    br.readLine();
                    continue;
                }
                int c1 = m1.getOrDefault(name, 0);
                m1.put(name, c1 + 1);
                String timeStamp = br.readLine();
                String truth = br.readLine();
                String tableName = LocalDataGeneration.getTableName(name);
                if (!LocationService.checkLocalTableExist(tableName)) {
                    System.out.println("Need to generate the local data for " + name + " first.");
                }
                LocationState result;
                long timeStart = System.currentTimeMillis();

                result = LocationService.fullQueryTimestamp(name, timeStamp, pos, neg, start_day, end_day, step);
                if (result == null)
                    continue;

                long timeUsed = System.currentTimeMillis() - timeStart;
                if (timeUsed > maxTime) {
                    maxTime = timeUsed;
                }
                if (timeUsed < minTime) {
                    minTime = timeUsed;
                }
                avgTime += timeUsed;
                total += 1;
                if (truth.equals("out")) {
                    totalOut += 1;
                    if (result.verifyOut(verbose, timeStamp, name)) {
                        correctOut += 1;
                        int c2 = m2.getOrDefault(name, 0);
                        m2.put(name, c2 + 1);
                    }
                } else {
                    totalIn += 1;
                    if (result.verifyIn(truth, verbose, timeStamp, name)) {
                        correctIn += 1;
                        if (result.verifyAP(truth, apToRoom, verbose, timeStamp, name)) {
                            correctAP += 1;
                            int c2 = m2.getOrDefault(name, 0);
                            m2.put(name, c2 + 1);
                        }
                    }
                }
            }
            avgTime = avgTime / (double) total;
            Map<String, Double> m3 = new HashMap<>();
            for (Map.Entry<String, Integer> entry : m2.entrySet()) {
                int i1 = entry.getValue();
                String s = entry.getKey();
                int i2 = m1.get(s);
                int i3 = Math.floorDiv(10000 * i1, i2);
                double a = (double) i3 / 100d;
                m3.put(s, a);
            }
            double acc1 = (double) correctIn / (double) totalIn * 100d;
            double acc2 = (double) correctOut / (double) totalOut * 100d;
            double acc3 = (double) correctAP / (double) correctIn * 100d;
            double acc = (double) (correctOut + correctAP) / (double) total * 100d;
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.printf("%s %nTest File: %s", sdf.format(d), path);
            logger.printf("Positive threshold: %d, Negative threshold: %d, Training data: %s ~ %s, training step: %d. %n",
                    pos, neg, start_day, end_day, step);
            logger.printf("CorrectIn: %d, TotalIn: %d, Prediction Accuracy: %.2f. %n", correctIn, totalIn, acc1);
            logger.printf("CorrectOut: %d, TotalOut: %d, Prediction Accuracy: %.2f. %n", correctOut, totalOut, acc2);
            logger.printf("CorrectAP: %d, CorrectIn: %d, Prediction Accuracy: %.2f. %n", correctAP, correctIn, acc3);
            logger.printf("Correct: %d, Total: %d, Prediction Accuracy: %.2f. %n", (correctOut + correctAP), total, acc);
            logger.printf("Correct of each person: %s %n", m2.toString());
            logger.printf("Total of each person: %s %n", m1.toString());
            logger.printf("Acc of each person: %s %n", m3.toString());
            logger.printf("MaxTime: %d ms, MinTime: %d ms, AvgTime: %.2f ms %n", maxTime, minTime, avgTime);
            logger.printf("%n");
            if (dbMode) {
                try (Connect connect = new Connect("local")) {
                    Connection connection = connect.getConnection();
                    if (!LocationService.checkLocalTableExist(resultDB)) {
                        Statement s = connection.createStatement();
                        s.execute("create table " + resultDB + " (expertime datetime, tesefile varchar(255), " +
                                "pos integer, neg integer, start varchar(30), end varchar(30), step integer, " +
                                "correctIn integer, correctOut integer, correctAP integer, totalIn integer, totalOut integer, total integer, " +
                                "accIn DECIMAL(5,2), accOut DECIMAL(5,2), accAP DECIMAL(5,2), acc DECIMAL(5,2), " +
                                "maxTime integer, minTime integer, avgTime DECIMAL(7,2), epCorrect varchar(255), " +
                                "epTotal varchar(255), epAcc varchar(255));");
                    }
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into " + resultDB + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, sdf.format(d));
                    ps.setString(2, path);
                    ps.setInt(3, pos);
                    ps.setInt(4, neg);
                    ps.setString(5, start_day);
                    ps.setString(6, end_day);
                    ps.setInt(7, step);
                    ps.setInt(8, correctIn);
                    ps.setInt(9, correctOut);
                    ps.setInt(10, correctAP);
                    ps.setInt(11, totalIn);
                    ps.setInt(12, totalOut);
                    ps.setInt(13, total);
                    ps.setDouble(14, acc1);
                    ps.setDouble(15, acc2);
                    ps.setDouble(16, acc3);
                    ps.setDouble(17, acc);
                    ps.setLong(18, maxTime);
                    ps.setLong(19, minTime);
                    ps.setDouble(20, avgTime);
                    ps.setString(21, m2.toString());
                    ps.setString(22, m1.toString());
                    ps.setString(23, m3.toString());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
