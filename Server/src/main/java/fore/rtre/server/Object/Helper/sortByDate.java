package fore.rtre.server.Object.Helper;

import fore.rtre.server.Object.SubProjectMeta;

import java.util.Comparator;

public class sortByDate implements Comparator<SubProjectMeta> {

    public int compare(SubProjectMeta a, SubProjectMeta b)
    {

        // Returning the value after comparing the objects
        // this will sort the data in Ascending order
        return a.date.compareTo(b.date);
    }
}
