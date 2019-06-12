package service;

import dao.Connect;
import dao.LocalDataGeneration;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultEvaluation {

    public static void main(String args[]) {
        baselineEvaluation();
    }

    static void smallTest() {
        String resultDB = "result_919s";
        for (int i = 1; i < 5; i++) {
            if (i == 1) {
                int[] alphas = {600};
                int[] betas = {60};
                for (int alpha : alphas) {
                    for (int beta : betas) {
                        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919s.txt", true)))) {
                            evaluateFromTXTQuery(0, 1, "./finaltest_new.txt", alpha, 0, logger, i, 1, beta, 2, true, resultDB);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (i == 2 || i == 3) {
                int[] trainVals;
                int[] alphas, betas;
                if (i == 2) {
                    int[] vs = {3, 7};
                    int[] as = {1800};
                    int[] bs = {600};
                    trainVals = vs;
                    alphas = as;
                    betas = bs;
                } else {
                    int[] vs = {20, 30};
                    int[] as = {1800};
                    int[] bs = {600};
                    trainVals = vs;
                    alphas = as;
                    betas = bs;
                }
                for (int trainVal : trainVals)
                    for (int alpha : alphas) {
                        for (int beta : betas) {
                            try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919s.txt", true)))) {
                                evaluateFromTXTQuery(0, 1, "./finaltest_new.txt", alpha, 0,
                                        logger, i, trainVal, beta, 2, true, resultDB);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            } else {
                int trvs[] = {20, 30};
                float gamas[] = {0.6f, 0.9f};
                int alpha = 1800;
                int beta = 600;
                for (int trainVal : trvs) {
                    for (float gama : gamas) {
                        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919s.txt", true)))) {
                            evaluateFromTXTQuery(gama, 1, "./finaltest_new.txt", alpha, 0,
                                    logger, i, trainVal, beta, 2, true, resultDB);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static void extraExp4() {
        String resultDB = "result_919";
        String path = "./finaltest_new.txt";
        String printFile = "result_919.txt";
        int alpha = 1800;
        int beta = 600;
        int[] trainVals = {15, 25};
        float[] gamas = {0.5f, 0.8f};
        for (float gama : gamas) {
            for (int trainVal : trainVals) {
                try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(printFile, true)))) {
                    evaluateFromTXTQuery(gama, 1, path, alpha, 0, logger, 4, trainVal,
                            beta, 2, true, resultDB);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    static void formalTest() {
        String resultDB = "result_919";
        String path = "./finaltest_new.txt";
        for (int i = 1; i < 5; i++) {
            if (i == 1) {
                int[] alphas = {600, 750, 900, 1050, 1200, 1350, 1500, 1650, 1800, 1950, 2100, 2250, 2400};
                int[] betas = {30, 60, 90, 120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720, 780, 840, 900};
                for (int alpha : alphas) {
                    for (int beta : betas) {
                        if (alpha > beta) {
                            try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
                                evaluateFromTXTQuery(0, 1, path, alpha, 0, logger, i,
                                        1, beta, 2, true, resultDB);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (i == 2 || i == 3) {
                int[] trainVals;
                int[] alphas, betas;
                if (i == 2) {
                    int[] vs = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
                    int[] as = {1800};
                    int[] bs = {600};
                    trainVals = vs;
                    alphas = as;
                    betas = bs;
                } else {
                    int[] vs = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 20, 22, 25, 27, 30};
                    int[] as = {1800};
                    int[] bs = {600};
                    trainVals = vs;
                    alphas = as;
                    betas = bs;
                }
                for (int trainVal : trainVals)
                    for (int alpha : alphas) {
                        for (int beta : betas) {
                            try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
//                                if (i == 3) {
////                                    evaluateFromTXTQuery("./finaltest.txt", alpha, 0, logger, i, trainVal, beta, 1, true,resultDB);
//                                    evaluateFromTXTQuery(1,path, alpha, 0, logger, i, trainVal, beta, 2, true, resultDB);
//                                } else {
                                evaluateFromTXTQuery(0, 1, path, alpha, 0, logger, i, trainVal, beta, 2, true, resultDB);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            } else {
                int trvs[] = {10, 15, 20, 25, 30, 35, 40, 45, 50};
                float gamas[] = {0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
                int alpha = 1800;
                int beta = 600;
                for (int trainVal : trvs) {
                    for (float gama : gamas) {
                        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
                            evaluateFromTXTQuery(gama, 1, path, alpha, 0,
                                    logger, i, trainVal, beta, 2, true, resultDB);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static void extraExp1() { //For trainMode 1
        String resultDB = "result_919";
        int[] alphas = {2550, 2700};
        int[] betas = {30, 60, 90, 120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720, 780, 840, 900};
        for (int alpha : alphas) {
            for (int beta : betas) {
                try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
                    evaluateFromTXTQuery(0, 1, "./finaltest_new.txt", alpha, 0, logger,
                            1, 1, beta, 2, true, resultDB);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void extraExp2() { //For trainMode 2
        String resultDB = "result_919";
        int alpha = 1800;
        int beta = 600;
        int tvs[] = {14, 15, 16};
        for (int v : tvs) {
            try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
                evaluateFromTXTQuery(0, 1, "./finaltest_new.txt", alpha, 0, logger,
                        2, v, beta, 2, true, resultDB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void extraExp3() { // For trainMode 3
        String resultDB = "result_919";
        int[] trainVals = {32,35,37,40};
        int alpha = 1800;
        int beta = 600;
        for (int trainVal : trainVals) {
            try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_919.txt", true)))) {
                evaluateFromTXTQuery(0, 1, "./finaltest_new.txt", alpha, 0, logger,
                        3, trainVal, beta, 2, true, resultDB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void normalMode(int alpha) { // old one
        List<DailyActivity> trainList = LocationPrediction.buildTimeSeries("roberto", "2018-04-02", "2018-05-21");
        List<DailyActivity> testActivities = LocationPrediction.buildTimeSeries("roberto", "2018-05-21", "2018-05-26");
        DailyDistribution distrInit = LocationPrediction.calcDistribution(trainList, alpha, 60);
        distrInit.printToViz(String.format("distr_a%d_f.txt", alpha));
        double acc_avg = 0d;
        for (int i = 0; i < testActivities.size(); i++) {
            DailyActivity activity = testActivities.get(i);
            DailyPrediction prediction = LocationPrediction.predictInAndOutForADay(activity, distrInit, alpha);
            LocationPrediction.InAndOutDecision(prediction);
            activity.predictAPforADay(distrInit);
            prediction.print();
            activity.printAccessPoint();
            double acc = prepareGroundTruthRoberto1(alpha).get(i).evaluate(prediction);
            double acc_base = prepareGroundTruthRoberto1(alpha).get(i).evaluateBaseline();
            acc_avg += acc;
            System.out.println(String.format("Accuracy today is : %.2f", acc * 100d));
            System.out.println(String.format("Accuracy baseline today is : %.2f", acc_base * 100d));
        }
        System.out.println(String.format("Average accuracy on five days is : %.2f", acc_avg * 20d));
    }

    static void baselineEvaluation() {
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("result_new.txt", true)))) {
            evaluateFromTXTQuery(0, 2, "./finaltest_new.txt", 0, 0, logger, 0, 0,
                    0, 2, false, "result_919");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void evaluateFromTXTQuery(float gama, int evalMode, String path, int alpha, int verbose, PrintWriter logger,
                                     int trainMode, int trainModeVal, int beta, int probMode, boolean dbMode,
                                     String resultDB) {
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
                if (evalMode == 1) {
                    result = LocationService.queryTimestamp(name, timeStamp, alpha, trainMode, trainModeVal, gama,
                            beta, probMode);
                } else if (evalMode == 2) {
                    result = ResultEvaluation.baseline1(name, timeStamp, 3600);
                } else {
                    result = new LocationState();
                    System.out.println("Evaluation Mode can only be 1,2.");
                }
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
            avgTime = (double) avgTime / (double) total;
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
            logger.printf("Alpha: %d, TrainMode: %d, TrainModeVal: %d, Beta: %d, Gama: %.2f, ProbM: %d %n",
                    alpha, trainMode, trainModeVal, beta, gama, probMode);
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
                        s.execute("create table " + resultDB + " (expertime datetime, tesefile varchar(255), alpha integer, beta integer, trainMode integer, trainVal integer, probMode integer, " +
                                "correctIn integer, correctOut integer, correctAP integer, totalIn integer, totalOut integer, total integer, " +
                                "accIn DECIMAL(4,2), accOut DECIMAL(4,2), accAP DECIMAL(4,2), acc DECIMAL(4,2), " +
                                "maxTime integer, minTime integer, avgTime DECIMAL(6,2), epCorrect varchar(255), " +
                                "epTotal varchar(255), epAcc varchar(255), gama DECIMAL(4,2));");
                    }
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into " + resultDB + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, sdf.format(d));
                    ps.setString(2, path);
                    ps.setInt(3, alpha);
                    ps.setInt(4, beta);
                    ps.setInt(5, trainMode);
                    ps.setInt(6, trainModeVal);
                    ps.setInt(7, probMode);
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
                    ps.setFloat(24, gama);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static LocationState baseline1(String name, String ts, int alpha) {
        return LocationService.baselineTimestamp(name, ts, alpha);
    }

    static void printQuery2File(int interval, int alpha, String fileName) {
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (int i = 0; i < 5; i++) {
                prepareGroundTruthRoberto1(alpha).get(i).generateTXTQuery(interval, logger);
            }
            for (int i = 0; i < 18; i++) {
                prepareGroundTruthRoberto2(alpha).get(i).generateTXTQuery(interval, logger);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<GroundTruth> prepareGroundTruthRoberto1(int alpha) {
        // From 05-21 to 05-25, alpha 5~30, aligned with nearby oberservations.
        List<GroundTruth> truthList = new ArrayList<>();
        GroundTruth groundTruth1 = new GroundTruth("Roberto Yus", "2018-05-21", "10:20:44", "20:39:51");
        groundTruth1.addLeaving("13:14:10", "14:29:15");
        groundTruth1.addLeaving("17:19:49", "18:05:03");
        groundTruth1.addLocation("10:20:00", "11:00:00", "2076");
        groundTruth1.addLocation("11:00:00", "13:00:00", "2065");
        groundTruth1.addLocation("14:20:00", "14:30:00", "2069");
        groundTruth1.addLocation("14:30:00", "17:10:00", "2082");
        groundTruth1.addLocation("17:10:00", "17:20:00", "2076");
        groundTruth1.addLocation("17:50:00", "20:35:00", "2076");
        truthList.add(groundTruth1);

        GroundTruth groundTruth2 = new GroundTruth("Roberto Yus", "2018-05-22", "10:33:44", "19:06:21");
        groundTruth2.addLeaving("12:25:15", "13:43:41");
        groundTruth2.addLeaving("17:31:38", "17:45:59");
        if (alpha <= 9) {
            groundTruth2.addLeaving("17:22:11", "17:31:38");
        }
        groundTruth2.addLocation("10:30:00", "12:20:00", "2076");
        groundTruth2.addLocation("13:30:00", "13:40:00", "2069");
        groundTruth2.addLocation("13:40:00", "16:50:00", "2076");
        groundTruth2.addLocation("16:50:00", "17:20:00", "2059");
        groundTruth2.addLocation("17:45:00", "19:00:00", "2076");
        truthList.add(groundTruth2);

        GroundTruth groundTruth3 = new GroundTruth("Roberto Yus", "2018-05-23", "10:41:17", "19:47:49");
        groundTruth3.addLeaving("12:39:27", "14:20:01");
        groundTruth3.addLeaving("16:32:55", "16:58:09");
        groundTruth3.addLocation("10:40:00", "12:30:00", "2076");
        groundTruth3.addLocation("14:00:00", "16:30:00", "2076");
        groundTruth3.addLocation("17:00:00", "19:40:00", "2076");
        truthList.add(groundTruth3);

        GroundTruth groundTruth4 = new GroundTruth("Roberto Yus", "2018-05-24", "09:43:00", "18:21:59");
        groundTruth4.addLeaving("12:19:45", "13:26:34");
        groundTruth4.addLeaving("15:48:43", "15:56:47");
        groundTruth4.addLocation("09:40:00", "11:05:00", "2076");
        groundTruth4.addLocation("11:05:00", "11:15:00", "third floor");
        groundTruth4.addLocation("11:15:00", "12:15:00", "2076");
        groundTruth4.addLocation("13:45:00", "15:40:00", "2076");
        groundTruth4.addLocation("15:50:00", "18:10:00", "2076");
        truthList.add(groundTruth4);

        GroundTruth groundTruth5 = new GroundTruth("Roberto Yus", "2018-05-25", "10:33:08", "18:30:22");
        groundTruth5.addLeaving("11:16:25", "11:43:15");
        groundTruth5.addLeaving("12:33:53", "13:22:30");
        groundTruth5.addLeaving("14:50:49", "16:38:36");
        groundTruth5.addLocation("10:20:00", "11:00:00", "2076");
        groundTruth5.addLocation("11:40:00", "12:30:00", "2076");
        groundTruth5.addLocation("13:20:00", "14:50:00", "2076");
        groundTruth5.addLocation("16:10:00", "18:30:00", "2076");
        truthList.add(groundTruth5);

        return truthList;
    }

    public static List<GroundTruth> prepareGroundTruthRoberto2(int alpha) {
        List<GroundTruth> truthList = new ArrayList<>();
        GroundTruth g1 = new GroundTruth("Roberto Yus", "2018-01-22", "09:30:00", "19:45:00");
        g1.addLocation("09:30:00", "10:00:00", "2076");
        g1.addLocation("10:00:00", "10:00:05", "Mail Room (third floor)");
        g1.addLocation("10:00:05", "11:00:00", "2076");
        g1.addLocation("11:00:00", "13:05:00", "2065");
        g1.addLeaving("13:05:00", "14:20:00");
        g1.addLocation("14:20:00", "15:15:00", "2076");
        g1.addLocation("15:15:00", "15:30:00", "2065");
        g1.addLocation("15:30:00", "16:20:00", "2076");
        g1.addLeaving("16:20:00", "17:10:00");
        g1.addLocation("17:10:00", "18:10:00", "2076");
        g1.addLocation("18:10:00", "18:30:00", "2065");
        g1.addLocation("18:30:00", "19:00:00", "2076");
        g1.addLocation("19:00:00", "19:20:00", "2069");
        g1.addLocation("19:20:00", "19:45:00", "2076");
        truthList.add(g1);

        GroundTruth g2 = new GroundTruth("Roberto Yus", "2018-01-23", "09:40:00", "19:50:00");
        g2.addLocation("09:40:00", "12:20:00", "2076");
        g2.addLocation("12:20:00", "12:55:00", "2082");
        g2.addLeaving("12:55:00", "13:40:00");
        g2.addLocation("13:40:00", "14:30:00", "2076");
        g2.addLocation("14:30:00", "16:30:00", "2065");
        g2.addLeaving("16:30:00", "17:45:00");
        g2.addLocation("17:45:00", "18:10:00", "2069");
        g2.addLocation("18:10:00", "18:15:00", "2065");
        g2.addLocation("18:15:00", "19:45:00", "2076");
        g2.addLocation("19:45:00", "19:50:00", "2099");
        truthList.add(g2);

        GroundTruth g3 = new GroundTruth("Roberto Yus", "2018-01-24", "09:25:00", "19:00:00");
        g3.addLocation("09:25:00", "09:50:00", "2076");
        g3.addLocation("09:50:00", "10:25:00", "2065");
        g3.addLocation("10:30:00", "11:10:00", "2082");
        g3.addLocation("11:10:00", "11:15:00", "2065");
        g3.addLocation("11:15:00", "12:05:00", "2076");
        g3.addLeaving("12:05:00", "13:20:00");
        g3.addLocation("13:20:00", "15:50:00", "2076");
        g3.addLocation("15:50:00", "15:55:00", "2069");
        g3.addLocation("15:55:00", "16:05:00", "2076");
        g3.addLeaving("16:05:00", "16:50:00");
        g3.addLocation("16:50:00", "17:35:00", "2076");
        g3.addLocation("17:35:00", "17:40:00", "2099");
        g3.addLocation("17:40:00", "18:45:00", "2065");
        g3.addLocation("18:45:00", "19:00:00", "2076");
        truthList.add(g3);

        GroundTruth g4 = new GroundTruth("Roberto Yus", "2018-01-25", "10:00:00", "18:30:00");
        g4.addLocation("10:00:00", "11:40:00", "2076");
        g4.addLeaving("11:40:00", "13:00:00");
        g4.addLocation("13:00:00", "16:00:00", "2076");
        g4.addLeaving("16:00:00", "16:40:00");
        g4.addLocation("16:40:00", "18:30:00", "2076");
        truthList.add(g4);

        GroundTruth g5 = new GroundTruth("Roberto Yus", "2018-01-26", "09:50:00", "19:05:00");
        g5.addLocation("09:50:00", "10:15:00", "2076");
        g5.addLocation("10:15:00", "11:45:00", "2082");
        g5.addLocation("11:45:00", "12:30:00", "2076");
        g5.addLeaving("12:30:00", "14:00:00");
        g5.addLocation("14:00:00", "15:30:00", "2076");
        g5.addLocation("15:30:00", "16:00:00", "2082");
        g5.addLocation("16:00:00", "17:00:00", "2065");
        g5.addLocation("17:00:00", "17:40:00", "2099");
        g5.addLocation("17:40:00", "18:10:00", "2076");
        g5.addLocation("18:10:00", "18:45:00", "2082");
        g5.addLocation("18:45:00", "19:05:00", "2076");
        truthList.add(g5);

        GroundTruth g6 = new GroundTruth("Roberto Yus", "2018-01-29", "10:00:00", "22:00:00");
        g6.addLocation("10:00:00", "11:00:00", "2076");
        g6.addLocation("11:00:00", "12:50:00", "2065");
        g6.addLeaving("12:50:00", "13:50:00");
        g6.addLocation("13:50:00", "14:50:00", "2076");
        g6.addLocation("14:50:00", "15:05:00", "2082");
        g6.addLocation("15:05:00", "16:00:00", "2076");
        g6.addLeaving("16:00:00", "16:30:00");
        g6.addLocation("16:30:00", "22:00:00", "2076");
        truthList.add(g6);

        GroundTruth g7 = new GroundTruth("Roberto Yus", "2018-01-30", "09:30:00", "21:30:00");
        g7.addLocation("09:30:00", "12:30:00", "2076");
        g7.addLeaving("12:30:00", "13:15:00");
        g7.addLocation("13:15:00", "13:30:00", "2076");
        g7.addLeaving("13:30:00", "14:30:00");
        g7.addLocation("14:30:00", "16:40:00", "2065");
        g7.addLeaving("16:40:00", "18:00:00");
        g7.addLocation("18:00:00", "21:30:00", "2065");
        truthList.add(g7);

        GroundTruth g8 = new GroundTruth("Roberto Yus", "2018-01-31", "09:40:00", "21:30:00");
        g8.addLocation("09:40:00", "10:30:00", "2076");
        g8.addLocation("10:30:00", "11:30:00", "2082");
        g8.addLocation("11:30:00", "12:50:00", "2076");
        g8.addLeaving("12:50:00", "14:00:00");
        g8.addLocation("14:00:00", "14:30:00", "2076");
        g8.addLocation("14:30:00", "15:30:00", "2082");
        g8.addLocation("15:30:00", "16:15:00", "2076");
        g8.addLeaving("16:15:00", "17:00:00");
        g8.addLocation("17:00:00", "21:30:00", "2076");
        truthList.add(g8);

        GroundTruth g9 = new GroundTruth("Roberto Yus", "2018-02-01", "09:30:00", "23:30:00");
        g9.addLocation("09:30:00", "11:00:00", "2076");
        g9.addLocation("11:00:00", "12:10:00", "2082");
        g9.addLeaving("12:10:00", "13:30:00");
        g9.addLocation("13:30:00", "16:00:00", "2076");
        g9.addLeaving("16:00:00", "16:45:00");
        g9.addLocation("16:45:00", "23:30:00", "2076");
        truthList.add(g9);

        GroundTruth g10 = new GroundTruth("Roberto Yus", "2018-05-15", "11:00:00", "18:55:00");
        g10.addLocation("11:00:00", "12:30:00", "2076");
        g10.addLeaving("12:30:00", "13:30:00");
        g10.addLocation("13:30:00", "17:45:00", "2076");
        g10.addLocation("17:45:00", "18:00:00", "2082");
        g10.addLocation("18:00:00", "18:55:00", "2076");
        truthList.add(g10);

        GroundTruth g11 = new GroundTruth("Roberto Yus", "2018-05-16", "10:35:00", "19:00:00");
        g11.addLocation("10:35:00", "11:00:00", "2076");
        g11.addLocation("11:00:00", "12:00:00", "2082");
        g11.addLocation("12:00:00", "12:30:00", "2076");
        g11.addLeaving("12:30:00", "13:35:00");
        g11.addLocation("13:35:00", "15:30:00", "2076");
        g11.addLocation("15:30:00", "15:55:00", "2059");
        g11.addLocation("15:55:00", "17:20:00", "2082");
        g11.addLocation("17:20:00", "17:30:00", "2099");
        g11.addLocation("17:30:00", "19:00:00", "2076");
        truthList.add(g11);

        GroundTruth g12 = new GroundTruth("Roberto Yus", "2018-05-17", "09:50:00", "18:30:00");
        g12.addLocation("09:50:00", "11:15:00", "2082");
        g12.addLocation("11:15:00", "11:30:00", "2076");
        g12.addLocation("11:30:00", "12:40:00", "2082");
        g12.addLocation("12:40:00", "12:50:00", "2099");
        g12.addLeaving("12:50:00", "14:20:00");
        g12.addLocation("14:20:00", "16:20:00", "2076");
        g12.addLocation("16:20:00", "17:30:00", "2082");
        g12.addLocation("17:30:00", "18:30:00", "2076");
        truthList.add(g12);

        GroundTruth g13 = new GroundTruth("Roberto Yus", "2018-05-29", "10:45:00", "18:45:00");
        g13.addLocation("10:45:00", "12:25:00", "2076");
        g13.addLeaving("12:25:00", "13:45:00");
        g13.addLocation("13:45:00", "15:10:00", "2076");
        g13.addLocation("15:10:00", "15:20:00", "third floor");
        g13.addLocation("15:20:00", "18:45:00", "2076");
        truthList.add(g13);

        GroundTruth g14 = new GroundTruth("Roberto Yus", "2018-05-31", "09:50:00", "19:35:00");
        g14.addLocation("09:50:00", "12:30:00", "2076");
        g14.addLeaving("12:30:00", "13:30:00");
        g14.addLocation("13:30:00", "19:35:00", "2076");
        truthList.add(g14);

        GroundTruth g15 = new GroundTruth("Roberto Yus", "2018-06-04", "10:20:00", "19:30:00");
        g15.addLocation("10:20:00", "11:00:00", "2076");
        g15.addLocation("11:00:00", "13:10:00", "2065");
        g15.addLeaving("13:10:00", "14:00:00");
        g15.addLocation("14:00:00", "16:15:00", "2076");
        g15.addLeaving("16:15:00", "16:50:00");
        g15.addLocation("16:50:00", "17:20:00", "2082");
        g15.addLocation("17:20:00", "19:30:00", "2076");
        truthList.add(g15);

        GroundTruth g16 = new GroundTruth("Roberto Yus", "2018-06-05", "10:15:00", "19:30:00");
        g16.addLocation("10:15:00", "12:00:00", "2076");
        g16.addLeaving("12:00:00", "13:10:00");
        g16.addLocation("13:10:00", "14:00:00", "2076");
        g16.addLocation("14:00:00", "14:40:00", "2065");
        g16.addLocation("14:40:00", "16:00:00", "2076");
        g16.addLeaving("16:00:00", "16:40:00");
        g16.addLocation("16:40:00", "17:45:00", "2076");
        g16.addLocation("17:45:00", "18:05:00", "2082");
        g16.addLocation("18:05:00", "19:30:00", "2076");
        truthList.add(g16);

        GroundTruth g17 = new GroundTruth("Roberto Yus", "2018-06-06", "10:40:00", "14:00:00");
        g17.addLocation("10:40:00", "12:10:00", "2076");
        g17.addLeaving("12:10:00", "14:00:00");
        truthList.add(g17);

        GroundTruth g18 = new GroundTruth("Roberto Yus", "2018-06-08", "10:15:00", "17:00:00");
        g18.addLocation("10:15:00", "10:55:00", "2076");
        g18.addLocation("10:55:00", "11:55:00", "4011");
        g18.addLocation("11:55:00", "12:10:00", "2076");
        g18.addLeaving("12:10:00", "13:30:00");
        g18.addLocation("13:30:00", "14:00:00", "2076");
        g18.addLocation("14:00:00", "15:00:00", "6011");
        g18.addLocation("15:00:00", "16:05:00", "2076");
        g18.addLeaving("16:05:00", "16:25:00");
        g18.addLocation("16:25:00", "17:00:00", "2076");
        truthList.add(g18);

        return truthList;
    }
}

class GroundTruth {
    String name;
    LocalDate date;
    LocalTime start;
    LocalTime end;
    int total;
    boolean[] schedule;
    String[] location;

    GroundTruth(String person, String dateString, String startTime, String endTime) {
        name = person;
        date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        total = end.toSecondOfDay() - start.toSecondOfDay();
        schedule = new boolean[86400];
        location = new String[86400];
        for (int i = start.toSecondOfDay(); i < end.toSecondOfDay(); i++) {
            schedule[i] = true;
            location[i] = "out";
        }
    }

    void addLeaving(String startTime, String endTime) {
        LocalTime leaveStart = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime leaveEnd = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        for (int i = leaveStart.toSecondOfDay(); i < leaveEnd.toSecondOfDay(); i++) {
            schedule[i] = false;
        }
    }

    void addLocation(String startTime, String endTime, String loc) {
        LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        for (int i = start.toSecondOfDay(); i < end.toSecondOfDay(); i++) {
            location[i] = loc;
        }
    }

    double evaluate(DailyPrediction prediction) {
        int count = 0;
        boolean[] predictedSchedule = prediction.getSecondSchedule();
        for (int i = start.toSecondOfDay(); i < end.toSecondOfDay(); i++) {
            if (schedule[i] == predictedSchedule[i]) {
                count += 1;
            }
        }
        double accuracy = (double) count / (double) total;
        return accuracy;
    }

    double evaluateBaseline() { // The accuracy if all intervals are predicted inside.
        int count = 0;
        for (int i = start.toSecondOfDay(); i < end.toSecondOfDay(); i++) {
            if (schedule[i] == true) {
                count += 1;
            }
        }
        double accuracy = (double) count / (double) total;
        return accuracy;
    }

    void generateTXTQuery(int intervalMinutes, PrintWriter logger) {
        int rand = start.toSecondOfDay() % (60 * intervalMinutes);
        LocalTime timePointer = start.minusSeconds(rand);
        while (!timePointer.isAfter(end)) {
            logger.printf("%s%n", name);
            logger.printf("%s %s:00%n", date.toString(), timePointer.toString());
            String s = location[timePointer.toSecondOfDay()];
            s = s == null ? "out" : s;
            logger.println(s);
            timePointer = timePointer.plusMinutes(intervalMinutes);
        }
    }
}

class APtoRoom implements Serializable {
    List<String> aps;
    Map<String, List<String>> apMapRoom;
    static final String path = "ap2room.dat";

    void init() {
        aps = new ArrayList<>();
        apMapRoom = new HashMap<>();
        try (Connect connect = new Connect("server")) {
            Connection connection = connect.getConnection();
            Statement st1 = connection.createStatement();
            ResultSet rs1 = st1.executeQuery("select id from SENSOR where sensor_type_id = 1;");
            while (rs1.next()) {
                aps.add(rs1.getString(1));
            }
            PreparedStatement ps1 = connection.prepareStatement("select INFRASTRUCTURE.name from SENSOR, COVERAGE_INFRASTRUCTURE, INFRASTRUCTURE\n" +
                    "where SENSOR.id = ? and SENSOR.COVERAGE_ID = COVERAGE_INFRASTRUCTURE.id and COVERAGE_INFRASTRUCTURE.SEMANTIC_ENTITY_ID = INFRASTRUCTURE.SEMANTIC_ENTITY_ID;");
            for (String ap : aps) {
                ps1.setString(1, ap);
                ResultSet rs2 = ps1.executeQuery();
                List<String> rooms = new ArrayList<>();
                while (rs2.next()) {
                    rooms.add(rs2.getString(1));
                }
                apMapRoom.put(ap, rooms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(apMapRoom);
            System.out.println("Successfully write to disk.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void load() {
        try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(path))) {
            apMapRoom = (Map<String, List<String>>) ooi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void print() {
        System.out.println("AP number: " + apMapRoom.size());
    }

    List<String> find(String ap) {
        return apMapRoom.get(ap);
    }
}

class RoomToAP {
    Map<String, List<String>> room2ap;

    RoomToAP(APtoRoom aptoRoom) {
        room2ap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : aptoRoom.apMapRoom.entrySet()) {
            for (String room : entry.getValue()) {
                List<String> roomList = room2ap.getOrDefault(room, new ArrayList<>());
                roomList.add(entry.getKey());
                room2ap.put(room, roomList);
            }
        }
    }

    void print() {
        System.out.println("Total Room: " + room2ap.size());
    }

    List<String> find(String room) {
        return room2ap.get(room);
    }
}

class NameToMac {
    Map<String, String> name2mac;

    NameToMac() {
        name2mac = new HashMap<>();
        name2mac.put("Roberto", "258e22fb74c43539a1eb05ad681e991cbdd6711a");
        // another mac doesn't have any connection at least from 2018-01-01
    }

    String find(String name) {
        return name2mac.get(name);
    }

}