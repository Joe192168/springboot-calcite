package com.joe.test;

public class TestStr {

    public static void main(String[] args) {
        String str = "Error while creating SQL connection: Jdbc=jdbc:oracle:thin:@192.168.0.223:1521:edw; JdbcUser=inmon; JdbcPassword=jhsz0603";
        System.out.println(str.substring(str.indexOf("JdbcPassword"), str.length()));
    }

}
