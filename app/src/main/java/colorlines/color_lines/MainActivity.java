package colorlines.color_lines;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GameLogic gameLogic= new GameLogic();
    Dialog scoreDialog;
    Dialog gameoverDialog;
    ListView listView;
    ArrayList<Integer> arrayListScore;

    TextView tvScore, tvMaxScore;
    Button lastCellClicked, currentCellCicked, btSave, btNewGame, btViewAllScores ;
    DBHandler dbhandler;

    ArrayList<GameButton> allGameButton;


    int idcell=0;
    Animation animationBouce;
    ArrayList<Button> allButtons= new ArrayList<>();

    TableLayout tablelayout;
    ImageView imageViewLeftBall, imageViewCenterBall,imageViewRightBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSave= (Button)findViewById(R.id.btSave);
        btNewGame= (Button)findViewById(R.id.btNewGame);
        btViewAllScores= (Button)findViewById(R.id.btViewAllScores);
        tvMaxScore=(TextView)findViewById(R.id.tvMaxScore);
        tvScore=(TextView)findViewById(R.id.tvCurentScore);

        imageViewLeftBall=(ImageView)findViewById(R.id.imageviewleftball);
        imageViewCenterBall=(ImageView)findViewById(R.id.imageviewcenterball);
        imageViewRightBall=(ImageView)findViewById(R.id.imageviewrightball);

        scoreDialog= new Dialog(this);
        gameoverDialog= new Dialog(this);
        randomNextBalls();
        arrayListScore=new ArrayList<>();

        dbhandler= new DBHandler(this);
        getMaxScore();
        allGameButton= new ArrayList<GameButton>();
        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbhandler.addScore(Integer.parseInt(tvScore.getText().toString().substring(7))); //saving score to the db
                Toast.makeText(getApplicationContext(), "score saved", Toast.LENGTH_SHORT).show();
                getMaxScore();
            }
        });


        btNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewGame();

            }
        });

        btViewAllScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoreDialog.setContentView(R.layout.dialogscores);//set the dialog conent
                scoreDialog.setTitle("top scores");
                listView = scoreDialog.findViewById(R.id.dialogshowscorelistview);
                Log.i("listview", "1: "+listView);
                List<Integer> twelveScores=  dbhandler.getTwelveScores();//geting 12 scores from the db
                for(int score: twelveScores )
                {
                    Log.i("twelveScores",""+score );
                    arrayListScore.add(score);
                }
                final ArrayAdapter adapter = new ArrayAdapter(scoreDialog.getContext(),android.R.layout.simple_list_item_2, android.R.id.text1,arrayListScore){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position,convertView,null);
                        TextView primary = (TextView) view.findViewById(android.R.id.text1);
                        primary.setText("score: "+arrayListScore.get(position));
                        return view;
                    }
                };
                Log.i("listview", ""+listView);
                listView.setAdapter(adapter);
                scoreDialog.show();
                Button dialogExitButton= scoreDialog.findViewById(R.id.dialogbtexit);
                dialogExitButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        scoreDialog.dismiss();
                    }
                });
            }
        });


        tablelayout=(TableLayout) findViewById(R.id.tablelayout);
        animationBouce= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ballanim);//loads the animation
        for(int i=0;i<9;i++)
        {
            TableRow row = (TableRow)tablelayout.getChildAt(i);  //gets each row in the tablelayout. there are 9 row overal
            for(int j=0;j<9;j++){
                GameButton button = (GameButton) row.getChildAt(j); // get child index on particular row. gets each view(cell) in a row
                button.setText(idcell+"");
                DataManager.idAllCells.put(button, idcell); //puts in the hashSet (idAllcells) a button(=cell. key). idcell=value.
                button.setBtnId(idcell);
                //////////////
                allGameButton.add(button);
                /////////////////////
                //there are 81 cells in idAllCells. each button has an a id from 0(first button) to 80(last button)
                idcell++;
                gameLogic.getAvailableCells().add(button); //add each cell to the availableCells List.
                setButtonListener(button); //add button listener to each cell
                allButtons.add(button); //allButtons is an arrayList that holds all buttons. used in the animationAlgorithm method to clear all animation
            }
        }
        gameLogic.createThreeballs(tablelayout);//create the starting 3 balls

        Integer [] startOfRow={0,9*1,9*2,9*3,9*4, 9*5, 9*6, 9*7, 9*8};

        Integer [] endOfRow={9*1-1,9*2-1,9*3-1,9*4-1, 9*5-1, 9*6-1, 9*7-1, 9*8-1, 9*9-1};

        for(Integer i=0; i<allGameButton.size(); i++)
        {

            if(!Arrays.asList(endOfRow).contains(i))
            {

                if(i<80) {
                    allGameButton.get(i).setRightNeighbor(allGameButton.get(i + 1));
                }

            }

            if(!Arrays.asList(startOfRow).contains(i))
            {
                if(i>0)
                {
                    allGameButton.get(i).setLeftNeighbor(allGameButton.get(i-1));
                }

            }

            if(i>8)
            {
                allGameButton.get(i).setTopNeighbor(allGameButton.get(i-9));
            }
            if(i<71)
            {
                allGameButton.get(i).setBottomNeighbor(allGameButton.get(i+9));
            }
        }
    }

    private void getMaxScore() {
        try {
            tvMaxScore.setText("Max score: " + dbhandler.getTwelveScores().get(0));
        }catch (Exception e)
        {
            Log.i("maxScore", "error");
            e.printStackTrace();
        }
    }

    private void startNewGame() {
        //this method does:
        // restarting the game, by calling it self as a intent and then finish the old Activity.
        finish();
        DataManager.idAllCells=new HashMap<>();//reseting the HashSet idAllCells
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    private void setButtonListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkCurrentClick(button);//checks if the current cell was clicked or a different cell was clicked.
                //if it was a diffrent button-> move button to a new location.
                // same button-> cancel animation
                animationAlgorithm(button); //responsible of all the animation code
                tvScore.setText("score: "+ gameLogic.getScore());
                if(gameLogic.isGameOver()) {
                    gameOver();
                }
            }
        });
    }

    private void checkCurrentClick(Button button)
    //checks if the current cell was clicked or a diffrend cell was clicked.
    //if it was a diffrent button-> move button to a new location.
    // same button-> cancel animation
    {
        if(lastCellClicked==null && currentCellCicked==null)//means first time clicked
        {
            lastCellClicked=button;
            currentCellCicked=button;
        }
        else//not the first click.
        {
            //updating the info about clicking.
            lastCellClicked=currentCellCicked;
            currentCellCicked=button;
        }

        if(lastCellClicked.getAnimation()!=null &&  gameLogic.getAvailableCells().contains(currentCellCicked) && isMovementPossible(lastCellClicked, currentCellCicked))
        //check if the lastCellClicked has animation and if the current click is an empty cell or not.
        // if true-> do movement and create 3 balls
        {
            doMovement(lastCellClicked, currentCellCicked);//moving the current balls to the wanting location.  currentCellCicked- > wanted location.
            gameLogic.createThreeballs(tablelayout);//creates new balls.
            randomNextBalls();
        }
    }

    private void gameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        gameoverDialog.setContentView(R.layout.dialoggameover);
        gameoverDialog.setTitle("game over");
        Button btNewGame= gameoverDialog.findViewById(R.id.dialoggameover_btnewgame);
        gameoverDialog.show();
        btNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewGame();

            }
        });
    }


    private void animationAlgorithm(Button button) {

        if(button.getAnimation()==null) {//checks if the animation is null
            if (!(gameLogic.getAvailableCells().contains(button))) {//checks if button is empty of holds a ball
                //an animation can be valid to balls only. not empty cells.
                //if the current button/cell is not empty it will start an animation.
                // the animation will stop only if the same button was clicked or a different button was clicked.

                gameLogic.clearAllAnimation();//clearing all animation
                button.startAnimation(animationBouce);
            }
        }
        else {
            gameLogic.clearAllAnimation();

        }

    }


    private void doMovement(Button currentButton, Button wantedLocationButton) {
        if(gameLogic.getAvailableCells().contains(wantedLocationButton))//if the wantedLocation is empty or not
        {
            if(isMovementPossible(currentButton,wantedLocationButton))//checks if the movement is possible. a path may be blocked by not an empty cell
            {
                if(!(currentButton.getAnimation()==null)) { //checks if the current button has animation
                    moveSuccessful(currentButton, wantedLocationButton);//if the movement is possible. move to the wanted location
                }
            }else
                Log.i("Path algo", "movement not possible");
        }


    }

    private boolean isMovementPossible(Button currentButton, Button wantedLocationButton) {
        return new PathAlgorithm(((GameButton)currentButton).getBtnId(), ((GameButton)wantedLocationButton).getBtnId(), gameLogic).checkPath();

    }
    public void randomNextBalls()
    {
        Random random= new Random();
        int [] colors= {R.drawable.blueball, R.drawable.greenball, R.drawable.purpleball, R.drawable.redball, R.drawable.yellowball};

        // second way
        int  [] threeRandomColors= {colors[random.nextInt(colors.length)], colors[random.nextInt(colors.length)],colors[random.nextInt(colors.length)]};
        gameLogic.setColorToThreeBalls(threeRandomColors);
        ImageView [] threeBalls= {imageViewLeftBall, imageViewCenterBall,  imageViewRightBall};
        for(int i=0; i<3; i++)
        {
            threeBalls[i].setImageResource(threeRandomColors[i]);
        }
    }

    private void moveSuccessful(Button currentButton, Button wantedLocationButton) {
        //this method will be called only if the movement of a ball is possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            wantedLocationButton.setBackground(currentButton.getBackground());//sets the button background of the newly occupy cell
        }
        wantedLocationButton.setTag(currentButton.getTag());
        gameLogic.getAvailableCells().remove(wantedLocationButton);//remove the newly occupy cell  from the AvailableCells  HashSet
        gameLogic.removeButton(currentButton);
        gameLogic.checkSuccessionAllDirections(wantedLocationButton);


    }

}

