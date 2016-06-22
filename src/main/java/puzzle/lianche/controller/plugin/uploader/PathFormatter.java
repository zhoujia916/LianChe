package puzzle.lianche.controller.plugin.uploader;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathFormatter {
    public static String format(String originFileName, String pathFormat){
        if (pathFormat == null || pathFormat.trim().length() == 0){
            pathFormat = "{filename}{rand:6}";
        }
        String invalidPattern = "[\\\\/:\\*\\?\042<>|]";
        originFileName = originFileName.replaceAll(invalidPattern, "");

        String extension = originFileName.substring(originFileName.lastIndexOf('.'));
        String filename = originFileName.substring(0, originFileName.lastIndexOf('.'));

        pathFormat = pathFormat.replace("{filename}", filename);
        String randomPattern = "\\{rand(:?)(\\d+)\\}";
        Pattern pattern = Pattern.compile(randomPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pathFormat);
        if (matcher.find()){
            int number = Integer.valueOf(matcher.group(2));
            int min = (int)Math.pow(10, number - 1);
            int max = (int)Math.pow(10, number);
            Random random = new Random(new Date().getTime());
            int result = random.nextInt(max - min + 1) + min;
            pathFormat = pathFormat.replace(matcher.group(0), String.valueOf(result));
        }

        Calendar calendar = Calendar.getInstance();

        pathFormat = pathFormat.replace("{yyyy}", String.valueOf(calendar.get(Calendar.YEAR)));
        pathFormat = pathFormat.replace("{yy}", String.valueOf((calendar.get(Calendar.YEAR) % 100)));
        int month = calendar.get(Calendar.MONTH);
        pathFormat = pathFormat.replace("{MM}", (month < 9 ? "0" : "") + String.valueOf(month + 1));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        pathFormat = pathFormat.replace("{dd}", (day < 10 ? "0" : "") + String.valueOf(day));
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        pathFormat = pathFormat.replace("{HH}", (hourOfDay < 10 ? "0" : "") + String.valueOf(hourOfDay));
        int hour = calendar.get(Calendar.HOUR);
        pathFormat = pathFormat.replace("{hh}", (hour < 10 ? "0" : "") + String.valueOf(hour));
        int minute = calendar.get(Calendar.MINUTE);
        pathFormat = pathFormat.replace("{mm}", (minute < 10 ? "0" : "") + String.valueOf(minute));
        int second = calendar.get(Calendar.SECOND);
        pathFormat = pathFormat.replace("{ss}", (second < 10 ? "0" : "") + String.valueOf(second));
        int millisecond = calendar.get(Calendar.MILLISECOND);
        pathFormat = pathFormat.replace("{fff}", (millisecond < 10 ? "00" : millisecond < 100 ? "0" : "") + String.valueOf(millisecond));
        pathFormat = pathFormat.replace("{time}", String.valueOf(calendar.getTimeInMillis()));
        return pathFormat + extension;
    }
}
