package puzzle.lianche.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String Empty = "";

	public static boolean isNullOrEmpty(String input){
		return input == null || input.trim().length() == 0;
	}

    public static boolean isNotNullOrEmpty(String input){ return !isNullOrEmpty(input); }

    public static boolean hasNullOrEmpty(String[] inputs){
        for(String input : inputs){
            if(input == null || input.trim().length() == 0){
                return true;
            }
        }
        return false;
    }

    public static boolean hasNullOrEmpty(Collection<String> list){
        boolean has = false;
        if(list != null && list.size() > 0){
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()){
                if(isNullOrEmpty(iterator.next())){
                    has = true;
                    break;
                }
            }
        }
        return has;
    }

    public static String format(String input, Object...params){
        if(isNullOrEmpty(input))
            return Empty;
        if(params.length == 0)
            return input;

        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\{\\d+\\}");
        Matcher m = pattern.matcher(input);
        int index = 0;
        while(m.find()){
            sb.append(input.substring(index, m.start()));
            int number =  ConvertUtil.toInt(m.group().substring(1, m.group().length() - 1));
            if(params.length > number){
                sb.append(params[number]);
            }
            index = m.end();
        }
        sb.append(input.substring(index));
        return sb.toString();
    }

    public static String trim(String input, String...trims){
        if(isNullOrEmpty(input))
            return Empty;
        if(trims.length == 0)
            return input.trim();

        for(String trim : trims){
            if(input.startsWith(trim)){
                input = input.substring(trim.length());
            }
            else if(input.endsWith(trim)){
                input = input.substring(0, input.length() - trim.length());
            }
        }

        return input;
    }

    public static String padLeft(int length, char c){
        return padLeft(length, String.valueOf(c));
    }

    public static String padLeft(int length, String prefix){
        return padLeft(null, length, prefix);
    }

    public static String padLeft(String input, int length, String prefix){
        if(input == null)
            input = Empty;

        if(length < 1)
            return Empty;
        if(input.length() >= length)
            return input;

        StringBuffer buffer = new StringBuffer(input);
        while(buffer.length() < length){
            buffer.insert(0, prefix);
        }
        return buffer.toString();
    }

    public static String padRight(int length, String suffix){
        return padRight(null, length, suffix);
    }

    public static String padRight(String input, int length, String suffix){
        if(input == null)
            input = Empty;
        if(length < 1)
            return Empty;
        if(input.length() > length)
            return input;

        StringBuffer buffer = new StringBuffer(input);
        while(buffer.length() < length){
            buffer.append(suffix);
        }
        return buffer.toString();
    }

    public static String concat(Collection<?> list, String spliter){
        StringBuffer buffer = new StringBuffer();
        if(list != null && list.size() > 0 && isNotNullOrEmpty(spliter)){
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                buffer.append(iterator.next());
                if(iterator.hasNext()) {
                    buffer.append(spliter);
                }
            }
        }

        return buffer.toString();
    }

    //region �������
    public static boolean isMatch(String input, String pattern){
        return isNullOrEmpty(input) ? false : input.trim().matches(pattern);
    }

    public static boolean isNumber(String input){
        String pattern = "^\\d+(\\.\\d+)?$";
        return isMatch(input, pattern);
    }

    public static boolean isMinLength(String input, int minLength){
        return input.length() >= minLength;
    }

    public static boolean isMaxLength(String input, int maxLength){
        return input.length() <= maxLength;
    }

    public static boolean isRangeLength(String input, int minLength, int maxLength){
        return input.length() >= minLength && input.length() <= maxLength;
    }

    public static boolean isEmail(String input){
        String pattern = "^\\w+[\\w_-]+@\\w+(\\.[\\w-_]+)+$";
        return isMatch(input, pattern);
    }

    public static boolean isPhone(String input){
        String pattern = "^\\d{11,11}$";
        return isMatch(input, pattern);
    }
    public static boolean isIdNumber(String input) {
        String pattern = "(^\\d{18}$)|(^\\d{15}$)";
        return isMatch(input, pattern);
    }
    public static boolean isDate(String input){
        String pattern = "^\\d{4}-\\d{2}-\\d{2}$";
        return isMatch(input, pattern);
    }
    //endregion
}
