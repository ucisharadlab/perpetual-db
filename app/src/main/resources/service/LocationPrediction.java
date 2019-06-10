package service;

import dao.Connect;

import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LocationPrediction {
    public static void main(String[] args) {
        int alpha = 1200;
        List<DailyActivity> activityList = buildTimeSeries("roberto", "2018-04-02", "2018-06-21");
        DailyDistribution distr = calcDistribution(activityList, alpha, 60);
        distr.print();
        distr.printToViz("roberto_viz.txt");
    }

    static List<DailyActivity> buildTimeSeries(String tableName, String startTime, String endTime) {
        List<DailyActivity> activities = new ArrayList<>();
        try (Connect connectLocal = new Connect("local")) {
            Connection conn = connectLocal.getConnection();
            PreparedStatement ps = conn.prepareStatement(String.format("select * from %s where timestamp>? and timestamp<?",
                    tableName));
            ps.setString(1, startTime);
            ps.setString(2, endTime);
            ResultSet rs = ps.executeQuery();
            DailyActivity activity = new DailyActivity();
            while (rs.next()) {
                String datetimeString = rs.getString(1);
                String sensor = rs.getString(2);
                LocalDateTime dateTime = LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
                LocalDate date = dateTime.toLocalDate();
                LocalTime time = dateTime.toLocalTime();
                if (activity.date == null) {
                    activity.date = date;
                } else if (!activity.date.equals(date)) {
                    if (!SatOrSun(activity)) {
                        activities.add(new DailyActivity(activity));
                    }
                    activity = new DailyActivity();
                    activity.date = date;
                }
                activity.addEntry(time, sensor);
            }
            if (activity.date != null && !SatOrSun(activity)) {
                activities.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    static boolean SatOrSun(DailyActivity activity) {
        return activity.date.getDayOfWeek() == DayOfWeek.SATURDAY || activity.date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    static void buildTimeSeriesAddMany(List<LocalDate> dates, String tableName, List<DailyActivity> activities) {
        try (Connect connectLocal = new Connect("local")) {
            Connection conn = connectLocal.getConnection();
            PreparedStatement ps = conn.prepareStatement(String.format("select * from %s where date(timestamp) = ?",
                    tableName));
            for (LocalDate date : dates) {
                ps.setString(1, date.toString());
                DailyActivity activity = new DailyActivity();
                activity.date = date;
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String datetimeString = rs.getString(1);
                    String sensor = rs.getString(2);
                    LocalDateTime dateTime = LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
                    LocalTime time = dateTime.toLocalTime();
                    activity.addEntry(time, sensor);
                }
                if (activity.dayActCount != 0) {
                    activities.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static DailyDistribution calcDistribution(List<DailyActivity> activityList, int alpha, int beta) {
        DailyDistribution distribution = new DailyDistribution(beta);
        for (DailyActivity activity : activityList) {
            if (activity.date != null && activity.date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    activity.date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                activity.calculateDailyContribution(alpha, beta);
                distribution.addAnActivity(activity);
            }
        }
        distribution.distributionUpdate();
        return distribution;
    }

    static DailyPrediction predictInAndOutForADay(DailyActivity activity, DailyDistribution distribution, int alpha) {
        DailyPrediction prediction = new DailyPrediction();
        prediction.date = activity.date;
        int pointer = 0;
        int totalActivity = activity.times.size();
        if (totalActivity == 1 || totalActivity == 0) {
            System.out.println(String.format("%s only has %d activity, no need to analyze.", prediction.date.toString(), totalActivity));
            return prediction;
        }
        while (true) {
            LocalTime start = activity.times.get(pointer);
            ++pointer;
            if (pointer == totalActivity) {
                break;
            }
            LocalTime end = activity.times.get(pointer);
            LocalTime beforeEnd = start;
            boolean shortInterval = false;
            while (pointer != totalActivity - 1 && end.isBefore(beforeEnd.plusMinutes(alpha))) {
                shortInterval = true;
                LocalTime next = activity.times.get(pointer + 1);
                if (end.plusMinutes(alpha).isAfter(next)) {
                    ++pointer;
                    beforeEnd = end;
                    end = activity.times.get(pointer);
                } else {
                    break;
                }
            }
            prediction.startList.add(LocalTime.of(start.getHour(), start.getMinute(), start.getSecond()));
            prediction.endList.add(LocalTime.of(end.getHour(), end.getMinute(), end.getSecond()));
            if (shortInterval) {
                prediction.insideProbList.add(1.0);
            } else {
                prediction.insideProbList.add(0.0);
            }
        }
        prediction.totalNum = prediction.startList.size();
        for (int i = 0; i < prediction.totalNum; i++) {
            if (prediction.insideProbList.get(i) == 1.0) {
                continue;
            }
            LocalTime startTime = prediction.startList.get(i);
            LocalTime endTime = prediction.endList.get(i);
            int start = startTime.minusHours(7).getHour() * 60 + startTime.getMinute();
            int end = endTime.minusHours(7).getHour() * 60 + endTime.getMinute();
            end = end > 899 ? 899 : end;
            double prob = 0.0;
            for (int j = start; j <= end; j++) {
                prob += distribution.inProb[j];
            }
            prob /= (end - start + 1);
            prediction.insideProbList.set(i, prob);
        }
        return prediction;
    }

    static void InAndOutDecision(DailyPrediction prediction) {
        int total = prediction.totalNum;
        prediction.insideList = new boolean[total];
        List<Double> numbers = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            double d = prediction.insideProbList.get(i);
            if (d != 1.0) {
                numbers.add(d);
            }
        }
        double[] sorted = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            sorted[i] = numbers.get(i);
        }
        Arrays.sort(sorted);
        double minStd = 1000000;
        int minPivot = 1;
        for (int i = 1; i < sorted.length; i++) {
            double stdSum = calcStd(sorted, 0, i) + calcStd(sorted, i, sorted.length);
            if (stdSum < minStd) {
                minStd = stdSum;
                minPivot = i;
            }
        }
        double threshold;
        if (numbers.size() == 0 || numbers.size() == 1) {
            threshold = 0.25; //TODO: need to handle these boundary cases better.
        } else {
            threshold = (sorted[minPivot - 1] + sorted[minPivot]) / 2;
        }
//        System.out.println(String.format("Threshold is %.3f", threshold));
        for (int i = 0; i < total; i++) {
            if (prediction.insideProbList.get(i) < threshold) {
                prediction.insideList[i] = false;
            } else {
                prediction.insideList[i] = true;
            }
        }
    }

    static void InAndOutDecision(DailyPrediction prediction, double threshold) {
        int total = prediction.totalNum;
        prediction.insideList = new boolean[total];
        for (int i = 0; i < total; i++) {
            if (prediction.insideProbList.get(i) < threshold) {
                prediction.insideList[i] = false;
            } else {
                prediction.insideList[i] = true;
            }
        }
    }

    static double calcThresholdFromDistr(DailyDistribution dailyDistribution, double k) {
        double bar = 0;
        int ct = 0;
        for (int i = 0; i < dailyDistribution.totalUnit; i++) {
            double t = dailyDistribution.inProb[i];
            if (t != 0) {
                ct += 1;
                bar += t;
            }
        }
        bar = bar / ct;
        bar = bar * k;
        return bar;
    }

    static LocationState predictForATimestamp(DailyDistribution distribution, String name, String timeStamp, int alpha,
                                              int probMode) {
        LocationState result = new LocationState();
        LocalDateTime localDateTime = LocalDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        LocalDate date = localDateTime.toLocalDate();
        LocalDate dateEnd = date.plusDays(1);
        List<DailyActivity> activities = buildTimeSeries(name, date.toString(), dateEnd.toString());
        if(activities.size() == 0){
            result.inside = false;
            return result;
        }
        DailyActivity activity = activities.get(0);

        int unitInterval = distribution.unitInterval;
        int alphaUnit = Math.floorDiv(alpha, unitInterval);
        LocalTime thisTime = localDateTime.toLocalTime();
        int currentUnit = getUnitFromTime(thisTime, unitInterval);
        int totalObservation = activity.times.size();
        int startUnit = getUnitFromTime(activity.times.get(0), unitInterval);
        int endUnit = getUnitFromTime(activity.times.get(totalObservation - 1), unitInterval);

        if (totalObservation == 0 || totalObservation == 1 || currentUnit < startUnit || currentUnit > endUnit) {
            result.inside = false;
            return result;
        }

        if (thisTime.isBefore(activity.times.get(0)) || !thisTime.isBefore(activity.times.get(totalObservation - 1))) {
            if (currentUnit == startUnit) {
                result.accessPoint = activity.sensors.get(0);
            } else {
                result.accessPoint = activity.sensors.get(totalObservation - 1);
            }
            result.inside = true;
            APtoRoom aPtoRoom = new APtoRoom();
            aPtoRoom.load();
            result.possibleRooms = aPtoRoom.find(result.accessPoint);
            return result;
        }

        int bound = 0;
        while (!thisTime.isBefore(activity.times.get(bound))) {
            ++bound;
        }
        LocalTime t1 = activity.times.get(bound - 1);
        LocalTime t2 = activity.times.get(bound);
        String ap1 = activity.sensors.get(bound - 1);
        String ap2 = activity.sensors.get(bound);

        int unit1 = getUnitFromTime(t1, unitInterval);
        int unit2 = getUnitFromTime(t2, unitInterval);
        if (unit2 - unit1 <= 1) { // If two observations are really close
            result.inside = true;
            Duration dr1 = Duration.between(t1, thisTime);
            Duration dr2 = Duration.between(thisTime, t2);
            if (dr1.toMillis() > dr2.toMillis()) {
                result.accessPoint = ap2;
            } else {
                result.accessPoint = ap1;
            }
            APtoRoom aPtoRoom = new APtoRoom();
            aPtoRoom.load();
            result.possibleRooms = aPtoRoom.find(result.accessPoint);
            return result;
        }

        double inProb = 0d;
        double Pwap1 = 0d, Pwap2 = 0d;
        if (probMode == 1) {
            for (int i = unit1; i <= unit2; i++) {
                inProb += distribution.inProb[i];
                Pwap1 += distribution.sensorDistr.get(i).getOrDefault(ap1, 0.0);
                Pwap2 += distribution.sensorDistr.get(i).getOrDefault(ap2, 0.0);
            }
            inProb /= (unit2 - unit1 + 1);
            Pwap1 /= (unit2 - unit1 + 1);
            Pwap2 /= (unit2 - unit1 + 1);
        } else if (probMode == 2) {
            for (int i = currentUnit - alphaUnit; i <= currentUnit + alphaUnit; ++i) {
                inProb += distribution.inProb[i];
                Pwap1 += distribution.sensorDistr.get(i).getOrDefault(ap1, 0.0);
                Pwap2 += distribution.sensorDistr.get(i).getOrDefault(ap2, 0.0);
            }
            inProb /= (2 * alphaUnit + 1);
            Pwap1 /= (2 * alphaUnit + 1);
            Pwap2 /= (2 * alphaUnit + 1);
        } else if (probMode == 3) {
            for (int i = unit1; i <= unit2; i++) {
                double c;
                if (i == currentUnit) {
                    c = 1.0;
                } else if (i < currentUnit) {
                    c = 1.0 - (double) (currentUnit - i) / (double) (currentUnit - unit1 + 1);
                } else {
                    c = 1.0 - (double) (i - currentUnit) / (double) (unit2 - currentUnit + 1);
                }
                inProb += distribution.inProb[i] * c;
                Pwap1 += distribution.sensorDistr.get(i).getOrDefault(ap1, 0.0) * c;
                Pwap2 += distribution.sensorDistr.get(i).getOrDefault(ap2, 0.0) * c;
            }
            inProb /= ((unit2 - unit1 + 1) / 2);
            Pwap1 /= ((unit2 - unit1 + 1) / 2);
            Pwap2 /= ((unit2 - unit1 + 1) / 2);
        } else {
            System.out.println("probMode can only be 1,2 or 3");
        }

        if (Duration.between(t1, t2).toMillis() < alpha*1000) {
            inProb = 1.0;
        }
        double threshold = calcThresholdFromDistr(distribution, 1.0);
        if (inProb < threshold) {
            result.inside = false;
        } else {
            result.inside = true;
            if (Pwap1 >= Pwap2) {
                result.accessPoint = ap1;
            } else {
                result.accessPoint = ap2;
            }
        }
        APtoRoom aPtoRoom = new APtoRoom();
        aPtoRoom.load();
        result.possibleRooms = aPtoRoom.find(result.accessPoint);
        return result;
    }

    static int getUnitFromTime(LocalTime time, int beta) {
        return Math.floorDiv(time.toSecondOfDay(), beta);
    }

    static double calcStd(double[] array, int start, int end) {
        if (end - start == 1 || end == start)
            return 0;
        double mean = 0;
        for (int i = start; i < end; i++) {
            mean += array[i];
        }
        mean /= (double) (end - start);
        double std = 0;
        for (int i = start; i < end; i++) {
            std += (array[i] - mean) * (array[i] - mean);
        }
        return std;
    }
}

class DailyActivity {
    LocalDate date;
    List<LocalTime> times;
    List<String> sensors;
    float[] daliyInsideProb;
    int dayStart;
    int dayEnd;
    int dayActCount;
    List<Map<String, Double>> dailySensorDistr;
    List<Map<String, Double>> accessPointList;
    List<String> accessPointPrediction;


    DailyActivity() {
        date = null;
        dayActCount = 0;
        times = new ArrayList<>();
        sensors = new ArrayList<>();
    }

    DailyActivity(DailyActivity dA) {
        date = LocalDate.of(dA.date.getYear(), dA.date.getMonth(), dA.date.getDayOfMonth());
        dayActCount = dA.dayActCount;
        times = new ArrayList<>();
        sensors = new ArrayList<>();
        times.addAll(dA.times);
        sensors.addAll(dA.sensors);
    }

    void addEntry(LocalTime localTime, String sensor) {
        times.add(localTime);
        sensors.add(sensor);
        ++dayActCount;
    }

    void calculateDailyContribution(int alpha, int beta) {
        int totalUnit = Math.floorDiv(86400, beta) + 1;
        int alphaUnit = Math.floorDiv(alpha, beta);
        daliyInsideProb = new float[totalUnit];
        dailySensorDistr = new ArrayList<>();
        for (int i = 0; i < totalUnit; i++) {
            daliyInsideProb[i] = 0;
            dailySensorDistr.add(new HashMap<>());
        }
        int size = times.size();
        for (int i = 0; i < size; i++) {
            String sensor = sensors.get(i);
            int unitInDay = Math.floorDiv(times.get(i).toSecondOfDay(), beta);
            int startUnit = unitInDay - alphaUnit + 1;
            int endUnit = unitInDay + alphaUnit - 1;
            startUnit = startUnit >= 0 ? startUnit : 0;
            endUnit = endUnit < totalUnit ? endUnit : totalUnit - 1;
            for (int j = startUnit; j <= endUnit; j++) {
                double value = 1.0 - (double) Math.abs(j - unitInDay) / (double) alphaUnit;
                daliyInsideProb[j] += value;
                Map<String, Double> map = dailySensorDistr.get(j);
                double val = map.getOrDefault(sensor, 0.0) + value;
                map.put(sensor, val);
            }
        }
    }

    void predictAPforADay(DailyDistribution distribution) {
        accessPointList = new ArrayList<>();
        accessPointPrediction = new ArrayList<>();
        for (int i = 0; i < times.size() - 1; i++) {
            Set<String> possiblaAPs = new HashSet<>();
            LocalTime startTime = times.get(i);
            LocalTime endTime = times.get(i + 1);
            possiblaAPs.add(sensors.get(i));
            possiblaAPs.add(sensors.get(i + 1));
            int start = startTime.minusHours(7).getHour() * 60 + startTime.getMinute();
            int end = endTime.minusHours(7).getHour() * 60 + endTime.getMinute();
            end = end > 899 ? 899 : end;
            Map<String, Double> intervalMap = new HashMap<>();
            double max = -0.1;
            String maxSensor = null;
            for (String sensor : possiblaAPs) {
                double prob = 0d;
                for (int j = start; j <= end; j++) {
                    prob += distribution.sensorDistr.get(j).getOrDefault(sensor, 0d);
                }
                prob /= (double) (end - start + 1);
                intervalMap.put(sensor, prob);
                if (prob > max) {
                    max = prob;
                    maxSensor = sensor;
                }
            }
            accessPointList.add(intervalMap);
            accessPointPrediction.add(maxSensor);
        }
    }

    void print() {
        System.out.println(String.format("%s has %d entries", date.toString(), times.size()));
        for (int i = 0; i < times.size(); ++i) {
            System.out.print(String.format("(%s,%s)", times.get(i).toString(), sensors.get(i)));
            if (i % 6 == 5) {
                System.out.println();
            }
        }
        System.out.println();
    }

    void printAccessPoint() {
        System.out.println(String.format("%s has %d entries", date.toString(), times.size()));
        for (int i = 0; i < times.size() - 1; ++i) {
            System.out.print(String.format("(%s,%s): %s", times.get(i).toString(), times.get(i + 1).toString(),
                    accessPointPrediction.get(i)));
            for (Map.Entry<String, Double> entry : accessPointList.get(i).entrySet()) {
                System.out.print(String.format("[%s:%.3f] ", entry.getKey(), entry.getValue()));
            }
            System.out.println();
        }
    }

    void printBrief() {
        System.out.println(String.format("%s has %d entries", date.toString(), times.size()));
    }
}

class DailyDistribution {
    int dayCount;
    float[] inProb;
    int unitInterval;
    int totalUnit;
    List<Map<String, Double>> sensorDistr;

    DailyDistribution(int beta) {
        dayCount = 0;
        unitInterval = beta;
        totalUnit = Math.floorDiv(86400, unitInterval) + 1;
        inProb = new float[totalUnit];
        sensorDistr = new ArrayList<>();
        for (int i = 0; i < totalUnit; i++) {
            sensorDistr.add(new HashMap<>());
        }
    }

    void distributionUpdate() {
        if (dayCount == 0) {
            //System.out.println("DayCount is 0. This function is omitted.");
            return;
        }
        for (int i = 0; i < totalUnit; i++) {
            inProb[i] /= (double) dayCount;
            Map<String, Double> map = sensorDistr.get(i);
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                map.put(entry.getKey(), entry.getValue() / (double) dayCount);
            }
        }
    }

    void addAnActivity(DailyActivity activity) {
        dayCount += 1;
        for (int i = 0; i < totalUnit; i++) {
            double insideValue = activity.daliyInsideProb[i];
            insideValue = insideValue <= 1.0 ? insideValue : 1.0;
            inProb[i] += insideValue;
            Map<String, Double> activityMap = activity.dailySensorDistr.get(i);
            Map<String, Double> sensorMap = sensorDistr.get(i);
            for (Map.Entry<String, Double> activityEntry : activityMap.entrySet()) {
                double value = activityEntry.getValue();
                value = value <= 1.0 ? value : 1.0;
                double newValue = sensorMap.getOrDefault(activityEntry.getKey(), 0.0) + value;
                sensorMap.put(activityEntry.getKey(), newValue);
            }
        }
    }

    void print() {
        for (int i = 0; i < totalUnit; i++) {
            if (inProb[i] != 0.0) {
                System.out.println(String.format("%s ~ %s has %.3f probability inside.",
                        LocalTime.ofSecondOfDay(i * unitInterval).toString(),
                        LocalTime.ofSecondOfDay((i + 1) * unitInterval - 1).toString(), inProb[i]));
            }
        }
    }

    void printToViz(String fileName) {
        try (PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (int i = 0; i < totalUnit; i++) {
                if (inProb[i] != 0.0) {
                    logger.printf("%d %.3f%n", i, inProb[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DailyPrediction {
    int totalNum;
    LocalDate date;
    List<LocalTime> startList;
    List<LocalTime> endList;
    List<Double> insideProbList;
    boolean[] insideList;

    DailyPrediction() {
        totalNum = 0;
        startList = new ArrayList<>();
        endList = new ArrayList<>();
        insideProbList = new ArrayList<>();
    }

    void print() {
        boolean verbose = true;
        System.out.println(date.toString());
        for (int i = 0; i < totalNum; i++) {
            if (insideList == null) {
                System.out.println(String.format("Interval: (%s,%s) has %.3f probability inside.",
                        startList.get(i), endList.get(i), insideProbList.get(i)));
            } else {
                if (insideProbList.get(i) != 1.0) {
                    String in = insideList[i] ? "inside" : "outside";
                    System.out.println(String.format("Interval: (%s,%s) has %.3f probability inside, classified as %s.",
                            startList.get(i), endList.get(i), insideProbList.get(i), in));
                }
            }
        }
    }

    boolean[] getSecondSchedule() {
        boolean[] schedule = new boolean[86400];
        for (int i = 0; i < totalNum; i++) {
            int start = startList.get(i).toSecondOfDay();
            int end = endList.get(i).toSecondOfDay();
            for (int j = start; j < end; j++) {
                schedule[j] = insideList[i];
            }
        }
        return schedule;
    }
}
