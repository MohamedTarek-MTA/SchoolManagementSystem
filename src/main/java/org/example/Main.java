package org.example;

import org.example.CLI.SchoolCLI;
import org.example.Datebase.DDL;

public class Main {
    public static void main(String[] args) {
        DDL.createDatabase();
        DDL.createTables();
        SchoolCLI cli = new SchoolCLI();
        cli.start();
    }
}