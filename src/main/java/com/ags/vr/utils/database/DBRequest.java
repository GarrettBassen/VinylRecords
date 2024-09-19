package com.ags.vr.utils.database;

//imports
import com.ags.vr.objects.Request;
import com.ags.vr.utils.Graphical;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static com.ags.vr.utils.Connector.con;

public class DBRequest {

    /**
     * Method used to insert the user input into the database.
     * @param request is a Request object holding all the respected data.
     * @return a boolean value, true meaning it worked while false being that there was an error.
     */
    public static boolean Insert(Request request)
    {
        //try catch to properly insert the values into the request table of the database
        try
        {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO request VALUES (?,?,?,?,?,?)");
            stmt.setNull(1, 0, null);
            stmt.setString(2, request.getCustomerName());
            stmt.setDouble(3, request.getPhoneNumber());
            stmt.setString(4, request.getEmail());
            stmt.setShort(5, request.getDate());
            stmt.setString(6, request.getRequest());
            stmt.execute();
            return true; //return statement (successful)
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Could not add '%s' 's to the database in Insert(Request) | DBRequest.java\n\nCode: %s\n%s",
                    request.getCustomerName(), e.getErrorCode(), e.getMessage()
            ));
            return false; //return statement (has an error)
        }
    }
}