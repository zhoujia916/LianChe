package puzzle.lianche.controller.plugin.ueditor;

import java.util.Date;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.lang.Math;

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
        pathFormat = pathFormat.replace("{time}", String.valueOf(calendar.getTimeInMillis()));
        pathFormat = pathFormat.replace("{yyyy}", String.valueOf(calendar.get(Calendar.YEAR)));
        pathFormat = pathFormat.replace("{yy}", String.valueOf((calendar.get(Calendar.YEAR) % 100)));
        pathFormat = pathFormat.replace("{MM}", String.valueOf(calendar.get(Calendar.MONTH)));
        pathFormat = pathFormat.replace("{dd}", String.valueOf(calendar.get(Calendar.DATE)));
        pathFormat = pathFormat.replace("{hh}", String.valueOf(calendar.get(Calendar.HOUR)));
        pathFormat = pathFormat.replace("{mm}", String.valueOf(calendar.get(Calendar.MINUTE)));
        pathFormat = pathFormat.replace("{ss}", String.valueOf(calendar.get(Calendar.SECOND)));

        return pathFormat + extension;
    }
}
