package colorlines.color_lines;

import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class GameLogic {
    int score;


    TableLayout tableLayout;
    boolean gameOver=false;
    int colorIndex;
    private  HashSet<Button> availableCells= new HashSet<>(); //this HashSet contains all the available Cells
    //gameLogic.getAvailableCells().contains(button)-> if true -> this cell/button is empty.
    //gameLogic.getAvailableCells().contains(button)-> if fale-> this cell/button is NOT empty.
    int numberOfBallsSuccession=1;
    int ballsInOneDirection=1;
    HashSet <Button> buttonToExplosion=new HashSet();
    boolean alredycheckReverse=false;


    //TODO error in direction 5,4,6,7
    //TODO works: 0, 1,2,3


    public boolean checkSuccessionAllDirections(Button button)
    {
        boolean doExplosion = false;
        for(int direction=0; direction<8; direction++) {
            if(checkSuccessionByDirection(button, direction));// direction ->0=left, 1=left-up, 2=up, 3=up-right, 4=right 5=right-down 6=down 7=down-left ;
            {
                doExplosion = true;
            }
        }
        return doExplosion;
    }

    public boolean checkSuccessionByDirection(Button button, int direction) {
        boolean doExplosion = false;
        int currentLocation = DataManager.idAllCells.get(button);
        Button nearButton= getKeyByValue(DataManager.idAllCells, currentLocation-1);

        Button [] buttonsDirection= {getKeyByValue(DataManager.idAllCells, currentLocation-1), getKeyByValue(DataManager.idAllCells, currentLocation-10),
                getKeyByValue(DataManager.idAllCells, currentLocation-9), getKeyByValue(DataManager.idAllCells, currentLocation-8),
                getKeyByValue(DataManager.idAllCells, currentLocation +1),getKeyByValue(DataManager.idAllCells, currentLocation +10),
                getKeyByValue(DataManager.idAllCells,currentLocation +9), getKeyByValue(DataManager.idAllCells, currentLocation +8)};
        //second way
        for(int i=0; i<buttonsDirection.length; i++)
        {
            if(direction==i)
            {
                nearButton=buttonsDirection[i];
            }
        }
        buttonToExplosion.add(button);

        checkIfNearButtonIsSameColor(button,nearButton,direction);

        if(numberOfBallsSuccession>1 && alredycheckReverse==false) {
            if (!alredycheckReverse) {
                numberOfBallsSuccession = 1;
            }
            //second way
//            for(int i=0; i<buttonsDirection.length; i++)
//            {
//                alredycheckReverse = true;
//                if(i==direction) {
//                    if (direction < 4) {
//                        checkIfNearButtonIsSameColor(button, buttonsDirection[i * -1], i + 4);
//
//                    } else if (direction >= 4) {
//                        checkIfNearButtonIsSameColor(button, buttonsDirection[i * -1], i - 4);
//
//                    }
//                }
//
//            }



            //first way
            if (direction == 0) {//checks also direction 4 {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 1), 4);

            } else if (direction == 1) //checks also direction 5
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 10), 5);
            } else if (direction == 2) //up. checks also down(direction 6)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 9), 6);
            }
            else if(direction==3 ) //3- up-right. checks also down-right(7)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation + 8), 7);
            }
            else if(direction==4)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 1), 0);
            }
            else if(direction==5)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 10), 1);
            }
            else if(direction==6)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 9), 2);
            }
            else if(direction==7)
            {
                alredycheckReverse = true;
                checkIfNearButtonIsSameColor(button, getKeyByValue(DataManager.idAllCells, currentLocation - 8), 3);
            }
        }
        if(numberOfBallsSuccession>4) //if there is more then 5 balls in the same color in a row
        {
            doExplosion=true;
            numberOfBallsSuccession=1;
            doExplosion(buttonToExplosion);
            buttonToExplosion.clear();
            alredycheckReverse=false;
            return doExplosion;
        }
        else
        {
            numberOfBallsSuccession=1;
            doExplosion=false;
            buttonToExplosion.clear();
            alredycheckReverse=false;
            return doExplosion;
        }
    }

    private void checkIfNearButtonIsSameColor(Button button,Button nearButton, int direction) {

        if(nearButton!=null && button!=null) {  //checks if the nearButton is null/not in the range of 0-80
            if (!getAvailableCells().contains(nearButton)) //checks if there is a ball near the right cell of the button
            {
                try {
                    if (button.getTag().equals(nearButton.getTag())) { //checks if the nearButton and button are the same color
                        numberOfBallsSuccession++; //add plus one to the succession int
                        checkSuccessionByDirection(nearButton, direction); //doing a recursion, in order to check if there one more ball in the same color and direction
                        buttonToExplosion.add(nearButton); //add the nearbutton to the explosion array
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doExplosion(HashSet<Button> buttonToExplosion)
    {
        int numberOfBalls=0;
        for(Button button: buttonToExplosion) //geting each button from the Hashset and then remove it
        {
            numberOfBalls++;
            removeButton(button);
        }
        score=score+numberOfBalls*(numberOfBalls-2); //adding a score
        clearAllAnimation();//reset all the animation
    }

    public int getScore() {
        return score;
    }


    public void clearAllAnimation() {
        for (int i = 0; i < 81; i++) {
            //this for loop select each cell/button from the AllButtons list which keeps all the 81 cells.
            getKeyByValue(DataManager.idAllCells,i).clearAnimation();
            //this line clear every button from animation.
        }
    }



    public boolean isGameOver() {
        return gameOver;
    }
    public HashSet<Button> getAvailableCells() {
        return availableCells;
    }
    public void createThreeballs(TableLayout tablelayout) {
        this.tableLayout=tablelayout;
        //creates 5 balls
        for(int i=0; i<3; i++)
        {
            colorIndex=i;
            createBall();
        }
    }

    public void createBall() {
                /*
        this method does:
            1.create a ball
            2. the balls has random location (0-80)
            3.the balls has random colors(blue, yellow, red, purple, green)
         */

        if (!(availableCells.size() == 0))
        //checks if the availableCells is empty. if yes- > no more room for balls, thus game over.
        //if no -> there is room for more balls, and continue to add them
        {

            Random random = new Random();
            int randomLocation = random.nextInt(DataManager.idAllCells.size()); //DataManager.idAllCells.size()=81. randomLocationcan be (0-80) idnumber

            if (getAvailableCells().contains(getKeyByValue(DataManager.idAllCells, randomLocation)))
            //checks if getAvailableCells contains the random button selected. if yes- > the random location is an empty cell
            // if no-> the random location is NOT a empty cell,
            // thus we need a new random location. we do that by Recursion
            {
                Log.i("getAvailableCells", "inside getAvailableCells");
                //the program will go into side block if, the random location is empty.

                Button button = getKeyByValue(DataManager.idAllCells, randomLocation);//gets the button key from the HashSet idAllCells by the value (randomLocation)

                int[] colors = {R.drawable.blueball, R.drawable.greenball, R.drawable.purpleball, R.drawable.redball, R.drawable.yellowball};



                //  second way:
                for(int i=0; i<3; i++)
                {
                    if(i==colorIndex){
                        button.setBackgroundResource(this.threeRandomColors[i]);
                        button.setTag(this.threeRandomColors[i]);
                    }
                }


                getAvailableCells().remove(button); //removes the button from the AvailableCells
                checkSuccessionAllDirections(button);

                if(availableCells.size()==0)
                {
                    gameOver = true;
                }
            } else
            {
                createBall();//need a different location
            }
        } else {
            gameOver = true;
        }
    }
    //second way:
    int [] threeRandomColors;
    public void setColorToThreeBalls(int [] threeRandomColors)
    {
        this.threeRandomColors=threeRandomColors;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }

        }
        return null;
    }
    public void removeButton(Button Button) {
        Button.setTag(null);
        Button.setBackgroundResource(R.drawable.cell);//set the old cell to be a cell drawable. will look like empty to the plyers
        getAvailableCells().add(Button);
    }

}

