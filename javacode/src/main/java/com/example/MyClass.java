package com.example;

public class MyClass {

    public static void main(String... args ){
        ServerSocket serverSocket_;
        serverSocket_ = new ServerSocket();
        new Thread(serverSocket_).start();

        String tarIp = "183.60.204.64";
        String localIp = "183.14.29.129";
        String localIp2 = "183.14.29.83";
        String localIp3 = "192.168.1.184";
        String cmd = "人物2014获取〓hy814〓123\t183.14.29.83\t183.14.29.83"
//                + "\t" + tarIp
//                + "\t" + localIp
                ;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverSocket_.send(cmd);
    }
}
