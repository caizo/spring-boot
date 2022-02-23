package org.pmv.springboot.student;

public class Views {

    // show public data
    public static class External {}

    // show public + "private" data
    public static class Internal extends External{}
}
