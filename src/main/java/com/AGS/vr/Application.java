package com.AGS.vr;

import com.AGS.vr.utils.Connector;
import com.AGS.vr.utils.Database;

public class Application
{
    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        Connector.connect();
        // TODO ERROR CHECKING TO ENSURE CONNECTOR IS VALID AND NOT NULL
    }
}