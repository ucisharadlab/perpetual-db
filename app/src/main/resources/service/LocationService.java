package service;

import dao.Connect;
import dao.LocalDataGeneration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LocationService {

    static public void main(String[] args) {
//        LocationState result = queryTimestamp("Roberto Yus", "2018-06-05 13:00:00", 1200,
//                1, 1, 0.8f, 120, 2);
//        result.print();
//        newQueryTimestamp("Roberto Yus", "2018-01-23 14:25:15");
//        fullQueryTimestamp("Roberto Yus", "2018-01-23 14:25:15",16,79,
//                "20180108","20180402",2);
        newQueryTimestampByEmail("primal@uci.edu","2018-01-23 14:25:15");
    }

    public static LocationState queryTimestamp(String name, String timeStamp, int alpha, int mode, int modeVal,
                                               float gama, int beta, int probMode) {
        /** mode represents different ways to generate the data to learn the distribution.
         * 1. Divide by block
         * 2. Only use the same week day of 'modeVal' weeks before and after
         * 3. Use 'modeVal' days before and after (except sat,sun)
         * 4. Combine both same day of a week with consecutive days
         */

        String tableName = LocalDataGeneration.getTableName(name);
        if (!checkLocalTableExist(tableName)) {
            System.out.println(String.format("Need to generate local data for %s first, which may take a while.", name));
            if (!LocalDataGeneration.generateData(name)) {
                System.out.println(String.format("Query failed (return null), since local data cannot be generated for %s", name));
                return null;
            }
        }

        List<DailyActivity> trainActivities;
        LocalDateTime dateTime = LocalDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        LocalDate thisDay = dateTime.toLocalDate();
        if (mode == 1) {
            if (thisDay.isAfter(LocalDate.of(2018, 4, 1))) {
                trainActivities = LocationPrediction.buildTimeSeries(
                        tableName, "2018-04-02", "2018-06-11");
            } else if (thisDay.isAfter(LocalDate.of(2018, 1, 7))) {
                trainActivities = LocationPrediction.buildTimeSeries(
                        tableName, "2018-01-08", "2018-03-19");
            } else {
                trainActivities = LocationPrediction.buildTimeSeries(tableName, "2017-09-25", "2017-12-11");
            }
        } else if (mode == 2) {
            List<LocalDate> dates = new ArrayList<>();
            dates.add(thisDay);
            for (int i = 1; i < modeVal; i++) {
                dates.add(thisDay.minusDays(modeVal * 7));
                dates.add(thisDay.plusDays(modeVal * 7));
            }
            trainActivities = new ArrayList<>();
            LocationPrediction.buildTimeSeriesAddMany(dates, tableName, trainActivities);
        } else if (mode == 3) {
            LocalDate dateStart, dateEnd;
            int minus = 1;
            for (int i = 1; i < modeVal; i++) {
                while (thisDay.minusDays(minus).getDayOfWeek() == DayOfWeek.SATURDAY ||
                        thisDay.minusDays(minus).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    ++minus;
                }
                ++minus;
            }
            dateStart = thisDay.minusDays(minus - 1);
            int plus = 1;
            for (int i = 1; i < modeVal; i++) {
                while (thisDay.plusDays(plus).getDayOfWeek() == DayOfWeek.SATURDAY ||
                        thisDay.plusDays(plus).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    ++plus;
                }
                ++plus;
            }
            dateEnd = thisDay.plusDays(plus);
            trainActivities = LocationPrediction.buildTimeSeries(tableName, dateStart.toString(), dateEnd.toString());
        } else if (mode == 4) {
            // gama*p(consecutive)+(1-gama)*p(same day of week)
            List<DailyActivity> train1 = new ArrayList<>();
            List<DailyActivity> train2 = new ArrayList<>();
            LocalDate dateStart, dateEnd;
            List<LocalDate> dates = new ArrayList<>();
            dates.add(thisDay);
            int minus = 1;
            for (int i = 1; i < modeVal; i++) {
                while (thisDay.minusDays(minus).getDayOfWeek() == DayOfWeek.SATURDAY ||
                        thisDay.minusDays(minus).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    ++minus;
                }
                if (minus % 7 == 0) {
                    dates.add(thisDay.minusDays(minus));
                }
                ++minus;
            }
            dateStart = thisDay.minusDays(minus - 1);
            int plus = 1;
            for (int i = 1; i < modeVal; i++) {
                while (thisDay.plusDays(plus).getDayOfWeek() == DayOfWeek.SATURDAY ||
                        thisDay.plusDays(plus).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    ++plus;
                }
                if (plus % 7 == 0) {
                    dates.add(thisDay.plusDays(plus));
                }
                ++plus;
            }
            dateEnd = thisDay.plusDays(plus);
            train1 = LocationPrediction.buildTimeSeries(tableName, dateStart.toString(), dateEnd.toString());
            LocationPrediction.buildTimeSeriesAddMany(dates, tableName, train2);
            DailyDistribution d1 = LocationPrediction.calcDistribution(train1, alpha, beta);
            DailyDistribution d2 = LocationPrediction.calcDistribution(train2, alpha, beta);
            DailyDistribution distr = new DailyDistribution(beta);
            int totalUnit = distr.totalUnit;
            for (int i = 0; i < totalUnit; i++) {
                distr.inProb[i] = gama * d1.inProb[i] + (1 - gama) * d2.inProb[i];
                Map<String, Double> map = new HashMap<>();
                Set<String> stringSet = new HashSet<>();
                stringSet.addAll(d1.sensorDistr.get(i).keySet());
                stringSet.addAll(d2.sensorDistr.get(i).keySet());
                for (String s : stringSet) {
                    map.put(s, d1.sensorDistr.get(i).getOrDefault(s, 0d) * gama
                            + (1 - gama) * d2.sensorDistr.get(i).getOrDefault(s, 0d));
                }
                distr.sensorDistr.add(map);
            }
            return LocationPrediction.predictForATimestamp(distr, tableName, timeStamp, alpha, probMode);
        } else {
            trainActivities = new ArrayList<>();
            System.out.println("Training data generation mode can only be 1,2,3 or 4.");
        }
        DailyDistribution distribution = LocationPrediction.calcDistribution(trainActivities, alpha, beta);
        return LocationPrediction.predictForATimestamp(distribution, tableName, timeStamp, alpha, probMode);
    }

    public static LocationState newQueryTimestamp(String name, String timeStamp) {
        // Use this for the default parameters (for upper level)
        String tableName = LocalDataGeneration.getTableName(name);
        if (!checkLocalTableExist(tableName)) {
            System.out.println(String.format("Generating local data for %s first, which may take a while.", name));
            if (!LocalDataGeneration.generateData(name)) {
                System.out.println(String.format("Query failed (return null), since local data cannot be generated for %s", name));
                return null;
            }
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http")
                    .setHost("localhost").setPort(9096).setPath("/query")
                    .setParameter("table_name", tableName)
                    .setParameter("user_name", name)
                    .setParameter("time", timeStamp)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet get = new HttpGet(uri);

        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String answer = EntityUtils.toString(response.getEntity());
            answer = answer.replaceAll("\"","");
            answer = answer.replaceAll("\n","");
            LocationState result = new LocationState();
            if (answer.charAt(0) == 'i') {
                result.inside = true;
                result.accessPoint = answer.split(" ")[1];
                APtoRoom aPtoRoom = new APtoRoom();
                aPtoRoom.load();
                result.possibleRooms = aPtoRoom.find(result.accessPoint);
            } else {
                result.inside = false;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocationState newQueryTimestampByEmail(String email, String timeStamp){
        String tableName = LocalDataGeneration.getTableNameFromEmail(email);
        if (!checkLocalTableExist(tableName)) {
            System.out.println(String.format("Generating local data for %s first, which may take a while.", email));
            if (!LocalDataGeneration.generateDataFromEmail(email)) {
                System.out.println(String.format("Query failed (return null), since local data cannot be generated for %s", email));
                return null;
            }
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http")
                    .setHost("localhost").setPort(9096).setPath("/query")
                    .setParameter("table_name", tableName)
                    .setParameter("user_name", email)
                    .setParameter("time", timeStamp)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet get = new HttpGet(uri);

        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String answer = EntityUtils.toString(response.getEntity());
            answer = answer.replaceAll("\"","");
            answer = answer.replaceAll("\n","");
            LocationState result = new LocationState();
            if (answer.charAt(0) == 'i') {
                result.inside = true;
                result.accessPoint = answer.split(" ")[1];
                APtoRoom aPtoRoom = new APtoRoom();
                aPtoRoom.load();
                result.possibleRooms = aPtoRoom.find(result.accessPoint);
            } else {
                result.inside = false;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocationState fullQueryTimestamp(String name, String timeStamp, int pos, int neg,
                                                   String start_day, String end_day, int step) {
        // Use this for the tuning parameters (run independent experiment for only this part)
        String tableName = LocalDataGeneration.getTableName(name);
        if (!checkLocalTableExist(tableName)) {
            System.out.println(String.format("Need to generate local data for %s first, which may take a while.", name));
            if (!LocalDataGeneration.generateData(name)) {
                System.out.println(String.format("Query failed (return null), since local data cannot be generated for %s", name));
                return null;
            }
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http")
                    .setHost("localhost").setPort(9096).setPath("/fquery")
                    .setParameter("table_name", tableName)
                    .setParameter("user_name", name)
                    .setParameter("time", timeStamp)
                    .setParameter("pos", String.valueOf(pos))
                    .setParameter("neg", String.valueOf(neg))
                    .setParameter("start_day",start_day)
                    .setParameter("end_day",end_day)
                    .setParameter("step",String.valueOf(step))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet get = new HttpGet(uri);
//        RequestConfig config = RequestConfig.custom()
//                .setConnectionRequestTimeout(3000)
//                .setConnectTimeout(3000)
//                .setSocketTimeout(3000)
//                .build();
//        get.setConfig(config);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            String answer = EntityUtils.toString(response.getEntity());
            answer = answer.replaceAll("\"","");
            answer = answer.replaceAll("\n","");
//            System.out.println(answer);
            LocationState result = new LocationState();
            if (answer.charAt(0) == 'i') {
                result.inside = true;
                result.accessPoint = answer.split(" ")[1];
                APtoRoom aPtoRoom = new APtoRoom();
                aPtoRoom.load();
                result.possibleRooms = aPtoRoom.find(result.accessPoint);
            } else {
                result.inside = false;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static LocationState baselineTimestamp(String name, String timeStamp, int alpha) {
        // alpha means the seconds of threshold, should be 3600 if it is an hour.
        LocationState result = new LocationState();
        List<DailyActivity> trainActivities = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        LocalDate thisDay = dateTime.toLocalDate();
        LocalTime thisTime = dateTime.toLocalTime();
        List<LocalDate> dates = new ArrayList<>();
        dates.add(thisDay);
        String tableName = LocalDataGeneration.getTableName(name);
        LocationPrediction.buildTimeSeriesAddMany(dates, tableName, trainActivities);

        DailyActivity activityThisday = null;
        try {
            activityThisday = trainActivities.get(0);
        } catch (IndexOutOfBoundsException e) {
            result.inside = false;
            return result;
        }
        List<LocalTime> allTimes = activityThisday.times;
        int totalOb = allTimes.size();
        if (thisTime.isBefore(allTimes.get(0)) || thisTime.isAfter(allTimes.get(totalOb - 1))) {
            result.inside = false;
            return result;
        }
        int i = 0;
        while (i < totalOb && !allTimes.get(i).isAfter(thisTime)) {
            ++i;
        }
        LocalTime b1 = allTimes.get(i - 1);
        LocalTime b2 = allTimes.get(i);
        if (Duration.between(b1, b2).toMillis() >= alpha * 1000) {
            result.inside = false;
            return result;
        }
        result.inside = true;
        result.accessPoint = activityThisday.sensors.get(i - 1);
        APtoRoom aPtoRoom = new APtoRoom();
        aPtoRoom.load();
        result.possibleRooms = aPtoRoom.find(result.accessPoint);
        return result;
    }

    static boolean checkLocalTableExist(String tableName) {
        try (Connect connect = new Connect("local")) {
            Statement statement = connect.getConnection().createStatement();
            statement.execute(String.format("select 1 from %s limit 1;", tableName));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}