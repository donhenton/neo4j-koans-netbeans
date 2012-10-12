package com.dhenton9000.neo4j.koans;

import org.neo4j.tutorial.DatabaseHelper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       DatabaseHelper.createDatabase("target/drwho");
    }
}
