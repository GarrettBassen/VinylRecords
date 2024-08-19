package com.AGS.vr;

import com.AGS.vr.utils.Connector;
import com.AGS.vr.utils.Database;

import java.sql.SQLException;

import java.util.Scanner;

public class Application
{
    public static void main(String[] args) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();
        Connector.setUsr(username);
        Connector.setPass(password);
        Connector.connect();

        String command = scan.nextLine();
        while(!command.equals("exit"))
        {
            if(command.equals("ag")){
                System.out.print(Database.insertGenre(scan.nextLine()));
            }
            else if(command.equals("cg"))
            {
                System.out.print(Database.containsGenre(scan.nextLine()));
            }
            else if (command.equals("dg")){
                System.out.print(Database.deleteGenre(scan.nextLine()));
            }
            System.out.println();
            command = scan.nextLine();
        }
        // TODO ERROR CHECKING TO ENSURE CONNECTOR IS VALID AND NOT NULL
    }
}