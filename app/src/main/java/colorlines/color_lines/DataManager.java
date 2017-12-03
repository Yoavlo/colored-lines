package colorlines.color_lines;

import android.widget.Button;

import java.util.HashMap;

public class DataManager {
    public static  HashMap<Button,Integer> idAllCells=new HashMap<>(); //this HashMap holds a button(key) and a Integer(id)value.
    //the id can be beetween 0-80.
    //to get the button id -> idallCells.get(button)
}
