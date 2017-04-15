package com.august;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PROJECT_NAME: multiProperties
 * PACKAGE_NAME: com.august
 * Author: August
 * Update: August(2017/4/15)
 * Description:
 */
public class IPUtils {
    public static final Map<String, Object> LOCAL_HOST_INFO = new HashMap<>();
    public static final Map<String, Object> LOOPBACK_HOST_INFO = new HashMap<>();
    public static final Map<String, Object> IP_LIST = new HashMap<>();

    /**
     * 获取本地主机信息
     *
     * @return 返回本地主机信息集合
     */
    public static Map<String, Object> getLocalHost() {
        try {
            if (LOCAL_HOST_INFO == null || LOCAL_HOST_INFO.size() < 1) {
                InetAddress inetAddress = InetAddress.getLocalHost();//声明并实例化InetAddress对象，返回本地主机
                byte[] ipAddress = inetAddress.getAddress();//获取原始IP地址
                //获取本地主机名
                LOCAL_HOST_INFO.put("HostName(本地主机别名名称)", inetAddress.getHostName());
                //输出此IP地址的完全限定域名
                LOCAL_HOST_INFO.put("CanonicalHostName(本地主机完全限定域名)", inetAddress.getCanonicalHostName());
                //输出本地主机的原始IP地址
                LOCAL_HOST_INFO.put("OriginalIPAddress(本地主机原始IP地址)", (ipAddress[0] < 0 ? ipAddress[0] + 256 : ipAddress[0]) + "." + (ipAddress[1] < 0 ? ipAddress[1] + 256 : ipAddress[1]) + "." + (ipAddress[2] < 0 ? ipAddress[2] + 256 : ipAddress[2]) + "." + (ipAddress[3] < 0 ? ipAddress[3] + 256 : ipAddress[3]));
                //输出本地主机的IP地址
                LOCAL_HOST_INFO.put("HostAddress(本地主机IP地址)", inetAddress.getHostAddress());
                //是否能到达此本地IP地址
                LOCAL_HOST_INFO.put("Reachable(是否能到达此本地IP地址)", inetAddress.isReachable(2000));
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LOCAL_HOST_INFO;
    }

    /**
     * 获取回环主机信息
     *
     * @return 返回回环主机信息集合
     */
    public static Map<String, Object> getLoopbackHost() {
        try {
            if (LOOPBACK_HOST_INFO == null || LOOPBACK_HOST_INFO.size() < 1) {
                InetAddress inetAddress = InetAddress.getLoopbackAddress();//声明并实例化InetAddress对象，返回回环主机
                byte[] ipAddress = inetAddress.getAddress();//获取原始IP地址
                //获取回环主机名
                LOOPBACK_HOST_INFO.put("HostName(回环主机别名名称)", inetAddress.getHostName());
                //输出此IP地址的完全限定域名
                LOOPBACK_HOST_INFO.put("CanonicalHostName(回环主机完全限定域名)", inetAddress.getCanonicalHostName());
                //输出回环主机的原始IP地址
                LOOPBACK_HOST_INFO.put("OriginalIPAddress(回环主机原始IP地址)", (ipAddress[0] < 0 ? ipAddress[0] + 256 : ipAddress[0]) + "." + (ipAddress[1] < 0 ? ipAddress[1] + 256 : ipAddress[1]) + "." + (ipAddress[2] < 0 ? ipAddress[2] + 256 : ipAddress[2]) + "." + (ipAddress[3] < 0 ? ipAddress[3] + 256 : ipAddress[3]));
                //输出回环主机的IP地址
                LOOPBACK_HOST_INFO.put("HostAddress(回环主机IP地址)", inetAddress.getHostAddress());
                //是否能到达此回环IP地址
                LOOPBACK_HOST_INFO.put("Reachable(是否能到达此回环IP地址)", inetAddress.isReachable(2000));
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LOOPBACK_HOST_INFO;
    }

    /**
     * 多IP处理，可以得到最终ip
     * 该IP以外网IP为主，如果没有外网IP则返回本地IP
     *
     * @return
     */
    public static Map<String, Object> getIpList() {
        if (IP_LIST == null || IP_LIST.size() < 1) {
            List localIP = new ArrayList();// 本地IP列表
            List netIP = new ArrayList();// 外网IP列表
            try {
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                InetAddress ip = null;
                boolean canFind = false;// 是否找到外网IP
                while (netInterfaces.hasMoreElements() && !canFind) {
                    Enumeration<InetAddress> address = netInterfaces.nextElement().getInetAddresses();
                    while (address.hasMoreElements()) {
                        ip = address.nextElement();
                        /*if (ip != null
                                && !ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                            netIP.add(ip.getHostAddress());
                            canFind = true;
                            break;
                        } else*/
                        if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                            localIP.add(ip.getHostAddress());
                        }
                    }
                }
                netIP = getNetIP();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            IP_LIST.put("netIP(公网IP列表)", netIP);
            IP_LIST.put("localIP(内网IP列表)", localIP);
        }

        return IP_LIST;
    }

    /**
     * 获取公网IP信息
     *
     * @return 返回公网ip
     * @throws IOException
     */
    private static List getNetIP() throws IOException {
        List<String> netIPList = new ArrayList<>();
        String ip = "";
//        String urlStr="http://ip.chinaz.com";
//        String charset="UTF-8";
//        String regex = "\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>";
        String urlStr = "http://www.net.cn/static/customercare/yourip.asp";
        String charset = "GBK";
        String regex = "您的本地上网IP是：\\<h2>(.*?)\\<\\/h2>";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
        }
        if (ip != null && ip.indexOf(",") > -1) {
            netIPList = Arrays.asList(ip.replaceAll(" ", "").split(","));
        } else {
            netIPList.add(ip);
        }
        return netIPList;
    }

}
