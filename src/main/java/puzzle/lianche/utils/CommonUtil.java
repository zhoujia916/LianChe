package puzzle.lianche.utils;

import puzzle.lianche.entity.SystemDept;
import puzzle.lianche.entity.SystemMenu;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
    public static List<SystemMenu> showMenuTree(List<SystemMenu> list, int parentId){
        List<SystemMenu> result = new ArrayList<SystemMenu>();
        for(SystemMenu item : list){
            if(item.getParentId() == parentId){
                addChildren(list, item);
//                int level = showMenuLevel(list, item);
//                item.setMenuName("|-" + StringUtil.padLeft(level * 2, "-") + item.getMenuName());
                result.add(item);
            }
        }
        return result;
    }

    public static List<SystemMenu> showMenuSelect(List<SystemMenu> list, int parentId){
        List<SystemMenu> result = new ArrayList<SystemMenu>();
        for(SystemMenu item : list){
            if(item.getParentId() == parentId){
                addChildren(list, item);
                result.add(item);
            }
        }
        return result;
    }

    public static void addChildren(List<SystemMenu> list, SystemMenu current){
        for(SystemMenu item : list){
            if(item.getParentId() == current.getMenuId()){
                if(current.getChildren() == null){
                    current.setChildren(new ArrayList<SystemMenu>());
                }
                addChildren(list, item);
                current.getChildren().add(item);
            }
        }
    }

    public static int showMenuLevel(List<SystemMenu> list, SystemMenu menu){
        if(menu.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (SystemMenu item : list) {
                if (menu.getParentId() == item.getMenuId()) {
                    level++;
                    menu = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }

    public int getLevel(List<SystemDept> list, SystemDept dept){
        if(dept.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (SystemDept item : list) {
                if (dept.getParentId() == item.getDeptId()) {
                    level++;
                    dept = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }


    public static String getClientIp(HttpServletRequest request){
        String ipAddress = null;
        //ipAddress = this.getRequest().getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")){
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }

        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
