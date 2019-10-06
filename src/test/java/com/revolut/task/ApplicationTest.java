package com.revolut.task;

import spark.servlet.SparkApplication;

public class ApplicationTest implements SparkApplication {

    @Override
    public void init() {
        Application.main(new String[0]);
    }
}