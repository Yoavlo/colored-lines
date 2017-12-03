package colorlines.color_lines;

/**
 * Created by yoavl_000 on 03/12/2017.
 */

import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathAlgorithm {
    boolean [] spacesChecked = new boolean[81];  //this array show which cell has been checked. if ture-> checked. if false ->unecked.
    int originBtnIdx;//the origionButton location
    int destinationBtnIdx; //the destination of the wanted button
    private GameLogic logic;

    public PathAlgorithm(int originIdx, int destIdx, GameLogic logic){
        this.destinationBtnIdx = destIdx;
        this.originBtnIdx = originIdx;
        this.logic = logic;
        Arrays.fill(spacesChecked, false);//filles the boolean arry spacedChecked with all false. beacuse we didnt checked any cell yet
    }

    public boolean checkPath(){
        Button originBtn= logic.getKeyByValue(DataManager.idAllCells, originBtnIdx);  //gets the button of the originBtn by the id location
        List<GameButton> pathList = getEmptyNeighbors((GameButton) originBtn); //pathList holds the emty neighbors (only the close one)/
        while(true){ //this while loop will stop dooing a loop in two conditions:
            //1. if the ball has reached to his destination(aka destinationBtnIdx) -return true
            //2. there is no more uncheck places (and yet didnt get to the destination)-> cant go to the wanted location -> return false

            Log.i("Path algo", "Length of pathList: " + pathList.size());
            for(GameButton gb: pathList){
                if(gb.getBtnId() == destinationBtnIdx)//check if one of the neighbors are the wanted location. if yes-> return true
                    return true;
            }

            // we haven't reached the destination yet
            ArrayList<GameButton> tempList = new ArrayList<>();
            for(GameButton gb: pathList){
                tempList.addAll(getEmptyNeighbors(gb));//gets the empty neighbors of  each GameButton in the pathList array.
                // pathList hold a empty neighbors(parents) and temList holds the empty neighbors(Children) of the pathList
            }
            // check if there are more spaces to check
            if(tempList.size() == 0)
                return false; //ends the while loop and return false
            // reset the list with the new wave of neighbors
            pathList = tempList;
        }
    }

    private List<GameButton> getEmptyNeighbors(GameButton target){
        //this method return all the empty neighbors of a button.

        ArrayList<GameButton> list = new ArrayList<>();
        if(checkNeighborValidity(target.getRightNeighbor()))
            list.add(target.getRightNeighbor());
        if(checkNeighborValidity(target.getLeftNeighbor()))
            list.add(target.getLeftNeighbor());
        if(checkNeighborValidity(target.getTopNeighbor()))
            list.add(target.getTopNeighbor());
        if(checkNeighborValidity(target.getBottomNeighbor()))
            list.add(target.getBottomNeighbor());
        for(GameButton gb: list)
            spacesChecked[gb.getBtnId()] = true; //every button is added to the boolean array spacesChecked as true. because we checked the cells
        return list;
    }
    private boolean checkNeighborValidity(GameButton button){
        return button != null  //checks if the button is null or not
                && !spacesChecked[button.getBtnId()] //checks if we already checked the current cell
                && logic.getAvailableCells().contains(button);//checks if the button is empty cell.
    }
}

