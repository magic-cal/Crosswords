package com.example.callum.first;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.callum.first.util.DataBaseHelper;
import com.example.callum.first.util.Globalclass;

public class Play extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Globalclass g1 = new Globalclass();
    DataBaseHelper myDbHelper = new DataBaseHelper(this);
    Button Submit;
    Button nextLevel;
    TextView length;
    TextView clue;
    TextView level;
    int clueNo;
    EditText their_Guess;
    Button[][] buttons = new Button[16][16];
    String[][] b = new String[16][16];
    String[][] a = new String[16][16];
    Button Switch;
    int colomn = 1;
    int row = 1;
    String savedBoard = "";
    int time=0;
    int guesses=0;
    private Handler handler;

    ////    ImageView VarImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String levelNo = myDbHelper.read("Users", "plevel", "loggedIn = 1 AND plevel > 0");
        g1.setAnswers(myDbHelper.read("Level", "answers", "levelNo = '" + levelNo + "'"), myDbHelper.read("Users", "pboard", "loggedIn = '1' AND plevel > 0"), myDbHelper.read("Level", "clues", "levelNo = '" + levelNo + "'"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();
        generateBoard();
        Log.d("After", "Generate board");
        level = (TextView) findViewById(R.id.Level_Text);
        clue = (TextView) findViewById(R.id.Clue_Text);
        Submit = (Button) findViewById(R.id.Submit_button);
        Submit.setOnClickListener(this);
        Submit.setLongClickable(true);
        Submit.setOnLongClickListener(this);
        length = (TextView) findViewById(R.id.Length);
        Switch = (Button) findViewById(R.id.Switch);
        Switch.setOnClickListener(this);
        nextLevel = (Button) findViewById(R.id.nextLevel);
        nextLevel.setOnClickListener(this);
        startTimer();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//         Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }


    public void generateBoard() {
        String[][] tempBoard = g1.getBoard();
        for (int i = 1; i <= 15; i++) {
            for (int c = 1; c <= 15; c++) {
//                running through the temp array to add to the proper board
                Log.d("Generate", "character: " + tempBoard[i][c].charAt(0));
                if (!g1.isNo(tempBoard[i][c].charAt(0))) {
//                    if it is not a number
                    buttons[i][c].setTextColor(getResources().getColor(R.color.Black));
                    switch (tempBoard[i][c]) {
                        case "~":
                            buttons[i][c].setTextColor(getResources().getColor(R.color.White));
                            buttons[i][c].setText("~");
//                            white spaces
                            break;
                        case "#":
//                            black spaces
                            buttons[i][c].setBackgroundColor(getResources().getColor(R.color.Black));
                            buttons[i][c].setText("#");
                            break;
                        default:
//                            for the letters to be set
                            buttons[i][c].setText(tempBoard[i][c]);
                            break;
                    }
                } else if (tempBoard[i][c].contains(".")) {
//                        a number with a letter
//                    splitting it into number and letter
                    boolean period = false;
                    int counter = 0;
                    String hint = "";
                    String text = "";
                    while (tempBoard[i][c].charAt(counter)!='.'){
                        hint = hint+tempBoard[i][c].charAt(counter);
                        counter++;

                    }
                    counter++;
                    while (counter<tempBoard[i][c].length()){
                        text=text+tempBoard[i][c].charAt(counter);
                        counter++;
                    }
                    buttons[i][c].setText(text);
                    buttons[i][c].setHint(hint);

//                    String[] split = tempBoard[i][c].split(".");
//                    buttons[i][c].setHint(split[0]);
//                    Log.d("number temp b",tempBoard[i][c]+split[0]);
//                    buttons[i][c].setText(split[1]+"");

                } else {
//                  just a number
                    buttons[i][c].setHint(tempBoard[i][c]);
                }

            }

        }
    }

    public void saveBoard() {
        boolean containsText = false;
        boolean containsHint = false;
        savedBoard = "";
        for (int i = 1; i <= 15; i++) {
            for (int c = 1; c <= 15; c++) {
                try {
                    buttons[i][c].getHint();
                    containsHint = true;
                } catch (Exception e) {
//                    if there is an error there is no hint
                    containsHint = false;
                }
                try {
                    buttons[i][c].getText();
                    containsText = true;
                } catch (Exception e) {
//                    if it errors there is not any text
                    containsText = false;
                }
                try {
                    if (containsHint && g1.isNo(buttons[i][c].getHint().charAt(0))) {
//                  if it is a number
                        savedBoard = savedBoard + "(" + buttons[i][c].getHint();
                        Log.d("SavedBoardP","."+buttons[i][c].getText()+".");
                        if (containsText&&buttons[i][c].getText()!="") {
                            savedBoard = savedBoard + "." + buttons[i][c].getText();
                        }
                        savedBoard = savedBoard + ")";
                    } else {
                        savedBoard = savedBoard + buttons[i][c].getText();
                    }
                } catch (Exception e) {
                    savedBoard = savedBoard + buttons[i][c].getText();
                    Log.d("Error", "Save Board");
                }
            }
        }
        myDbHelper.saveGame(savedBoard);
        if (Complete()){
            Log.d("Level Up ","leveling up");
            nextLevel.setVisibility(View.VISIBLE);
            refreshPage();
        }
    }

    public boolean Complete() {
        int BlankSpaceCounter = 0;
        Log.d("saved Board out", savedBoard + "" + savedBoard.length());
        for (int i = 0; i < savedBoard.length(); i++) {
            if (savedBoard.charAt(i) == '~') {
                return false;
            }
        }
            return true;
    }

    public void addToGrid(String rotation, String word) {
        if (rotation.charAt(0) == 'h') {
            addToGridHorrisontal(word);
        } else if (rotation.charAt(0) == 'v') {
            addToGridVertical(word);
        } else {
            Log.d("addto", "not h or v");
        }

    }

    public void addToGridHorrisontal(String word) {
        for (int i = 0; i < word.length(); i++) {
            buttons[row][colomn + i].setText("" + word.charAt(i));
            buttons[row][colomn + i].setTextColor(getResources().getColor(R.color.Black));
        }
    }

    public void addToGridVertical(String word) {
        for (int i = 0; i < word.length(); i++) {
            buttons[row + i][colomn].setText("" + word.charAt(i));
            buttons[row + i][colomn].setTextColor(getResources().getColor(R.color.Black));
        }
    }

    private void SubmitClick() {
        Log.d("GUesses",guesses+"");
        their_Guess = (EditText) findViewById(R.id.Guess_Box);
//        if(answer.contains(",")){
//            answer.split(",");
//        }]
        String tempAnswer = g1.getAnswer(clueNo);
        if (tempAnswer.contains(" ")) {
            String[] tempSplit = tempAnswer.split(" ");
            tempAnswer = tempSplit[0] + "" + tempSplit[1];
        }
        if (their_Guess.getText().toString().equalsIgnoreCase(g1.getAnswer(clueNo)) || their_Guess.getText().toString().equalsIgnoreCase(tempAnswer)) {
            Submit.setEnabled(false);
            try {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                Log.d("Keyboard hide", "fail");
            }
//            if()
//            addToGrid(g1.getRotation(clueNo),g1.getAnswer(clueNo));
            addToGrid(g1.getRotation(clueNo), tempAnswer);
            Submit.setTextColor(getResources().getColor(R.color.Black));
            onSwitch();
            saveBoard();
            refreshPage();
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
                    mp.start();
//            startActivity(new Intent("android.intent.action.util.CORRECT"));
        } else {
            guesses++;
            Submit.setTextColor(getResources().getColor(R.color.Red));
        }
        their_Guess.setText("");
    }

    public void whereOnGrid(View v) {
        Log.d("onCLick", "" + v.getId());
        Log.d("onCLickstring", "" + v.toString());
        String whichEditText = v.toString();
        String[] which = new String[2];
        which = whichEditText.split("editText");
        String split2 = "" + which[1];
        Log.d("split2", "" + split2);
        Log.d("length", "" + split2.length());
        switch (split2.length()) {
            case 2:
                colomn = Integer.parseInt("" + split2.charAt(0));
                row = 1;
                break;
            case 3:
                if (split2.charAt(0) == '1') {
                    colomn = Integer.parseInt(split2.charAt(0) + "" + split2.charAt(1));
                    row = 1;
                } else {
                    row = Integer.parseInt(split2.charAt(0) + "");
                    colomn = Integer.parseInt("" + split2.charAt(1));
                }
                break;
            case 4:
                if (Integer.parseInt(split2.charAt(0) + "" + split2.charAt(1)) <= 15) {
                    row = Integer.parseInt(split2.charAt(0) + "" + split2.charAt(1));
                    colomn = Integer.parseInt(split2.charAt(2) + "");
                } else {
                    row = Integer.parseInt(split2.charAt(0) + "");
                    Log.d("column1", colomn + "");
                    colomn = Integer.parseInt(split2.charAt(1) + "" + split2.charAt(2));
                    Log.d("column2", colomn + "");
                }
                break;
            case 5:
                row = Integer.parseInt(split2.charAt(0) + "" + split2.charAt(1));
                colomn = Integer.parseInt(split2.charAt(2) + "" + split2.charAt(3));
                break;
            default:
                Log.d("whereOnGrid", "its dead jim");
                break;

        }
        Log.d("colomn", colomn + "");
        Log.d("row", row + "");
    }

    public void onSwitch() {
        if (g1.getBeforeComma()) {
            g1.setBeforeComma(false);
            Switch.setText("Switch to Accross");
        } else {
            g1.setBeforeComma(true);
            Switch.setText("Switch to Down");
        }
        Log.d("Switch", "" + g1.getBeforeComma());
        Submit.setEnabled(true);
        refreshPage();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.Submit_button) {
            SubmitClick();
        } else if (v.getId() == R.id.Switch) {
            onSwitch();
        }else if(v.getId() == R.id.nextLevel){
            myDbHelper.changeLevel(Integer.parseInt(myDbHelper.getLevel()) + 1);
            nextLevel.setVisibility(View.GONE);
            finish();
        } else {
            try {
                Button b = (Button) v;
                clue.setText("Please Select a number in the grid");
                level.setText("Clue:");
                length.setText("()");
                if (b.getHint().toString().contains("1") || b.getHint().toString().contains("2") || b.getHint().toString().contains("3") || b.getHint().toString().contains("4") || b.getHint().toString().contains("5") || b.getHint().toString().contains("6") || b.getHint().toString().contains("7") || b.getHint().toString().contains("8") || b.getHint().toString().contains("8") || b.getHint().toString().contains("9") || b.getHint().toString().contains("0")) {
                    clueNo = Integer.parseInt(b.getHint().toString());
                    if (g1.twoValues(clueNo)) {
                        Switch.setVisibility(View.VISIBLE);
                    } else {
                        Switch.setVisibility(View.GONE);
                    }
                    Submit.setEnabled(true);
                    refreshPage();
                    whereOnGrid(v);
                }

            } catch (Exception e) {
                Log.d("hint number", "failed");
                Switch.setVisibility(View.GONE);
                Submit.setEnabled(false);
            }
        }
    }

    public void refreshPage() {
        clue.setText("" + g1.getClue(clueNo));
        level.setText("Clue: " + clueNo);
        if (g1.getAnswer(clueNo).contains(" ")) {
            String[] tempSplit = g1.getAnswer(clueNo).split(" ");
            length.setText("(" + tempSplit[0].length() + "," + tempSplit[1].length() + ")");
        } else {
            length.setText("(" + g1.getAnswer(clueNo).length() + ")");
        }

    }

    @Override
    protected void onDestroy() {
//        this runs when you exit out of the section.
        super.onDestroy();
//        stops the timer from running
        handler = null;
        myDbHelper.updateDetails(time, guesses);
        Log.d("destroy", "this" + guesses);

    }
    protected void onPause(){
        super.onPause();
        handler = null;

    }

    public void startTimer() {
//        waits one second before adding a second onto the timer.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (handler == h1) {
                    time++;
                    handler.postDelayed(this, 1000);
                }
            }
            private final Handler h1 = handler;
        };
        r.run();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent("android.intent.action.util.Settings"));
            return true;
        }
        if (id == R.id.action_help) {
            startActivity(new Intent("android.intent.action.util.Help"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {

        buttons[1][1] = (Button) findViewById(R.id.editText1);
        buttons[1][2] = (Button) findViewById(R.id.editText2);
        buttons[1][3] = (Button) findViewById(R.id.editText3);
        buttons[1][4] = (Button) findViewById(R.id.editText4);
        buttons[1][5] = (Button) findViewById(R.id.editText5);
        buttons[1][6] = (Button) findViewById(R.id.editText6);
        buttons[1][7] = (Button) findViewById(R.id.editText7);
        buttons[1][8] = (Button) findViewById(R.id.editText8);
        buttons[1][9] = (Button) findViewById(R.id.editText9);
        buttons[1][10] = (Button) findViewById(R.id.editText10);
        buttons[1][11] = (Button) findViewById(R.id.editText11);
        buttons[1][12] = (Button) findViewById(R.id.editText12);
        buttons[1][13] = (Button) findViewById(R.id.editText13);
        buttons[1][14] = (Button) findViewById(R.id.editText14);
        buttons[1][15] = (Button) findViewById(R.id.editText15);
        buttons[2][1] = (Button) findViewById(R.id.editText21);
        buttons[2][2] = (Button) findViewById(R.id.editText22);
        buttons[2][3] = (Button) findViewById(R.id.editText23);
        buttons[2][4] = (Button) findViewById(R.id.editText24);
        buttons[2][5] = (Button) findViewById(R.id.editText25);
        buttons[2][6] = (Button) findViewById(R.id.editText26);
        buttons[2][7] = (Button) findViewById(R.id.editText27);
        buttons[2][8] = (Button) findViewById(R.id.editText28);
        buttons[2][9] = (Button) findViewById(R.id.editText29);
        buttons[2][10] = (Button) findViewById(R.id.editText210);
        buttons[2][11] = (Button) findViewById(R.id.editText211);
        buttons[2][12] = (Button) findViewById(R.id.editText212);
        buttons[2][13] = (Button) findViewById(R.id.editText213);
        buttons[2][14] = (Button) findViewById(R.id.editText214);
        buttons[2][15] = (Button) findViewById(R.id.editText215);
        buttons[3][1] = (Button) findViewById(R.id.editText31);
        buttons[3][2] = (Button) findViewById(R.id.editText32);
        buttons[3][3] = (Button) findViewById(R.id.editText33);
        buttons[3][4] = (Button) findViewById(R.id.editText34);
        buttons[3][5] = (Button) findViewById(R.id.editText35);
        buttons[3][6] = (Button) findViewById(R.id.editText36);
        buttons[3][7] = (Button) findViewById(R.id.editText37);
        buttons[3][8] = (Button) findViewById(R.id.editText38);
        buttons[3][9] = (Button) findViewById(R.id.editText39);
        buttons[3][10] = (Button) findViewById(R.id.editText310);
        buttons[3][11] = (Button) findViewById(R.id.editText311);
        buttons[3][12] = (Button) findViewById(R.id.editText312);
        buttons[3][13] = (Button) findViewById(R.id.editText313);
        buttons[3][14] = (Button) findViewById(R.id.editText314);
        buttons[3][15] = (Button) findViewById(R.id.editText315);
        buttons[4][1] = (Button) findViewById(R.id.editText41);
        buttons[4][2] = (Button) findViewById(R.id.editText42);
        buttons[4][3] = (Button) findViewById(R.id.editText43);
        buttons[4][4] = (Button) findViewById(R.id.editText44);
        buttons[4][5] = (Button) findViewById(R.id.editText45);
        buttons[4][6] = (Button) findViewById(R.id.editText46);
        buttons[4][7] = (Button) findViewById(R.id.editText47);
        buttons[4][8] = (Button) findViewById(R.id.editText48);
        buttons[4][9] = (Button) findViewById(R.id.editText49);
        buttons[4][10] = (Button) findViewById(R.id.editText410);
        buttons[4][11] = (Button) findViewById(R.id.editText411);
        buttons[4][12] = (Button) findViewById(R.id.editText412);
        buttons[4][13] = (Button) findViewById(R.id.editText413);
        buttons[4][14] = (Button) findViewById(R.id.editText414);
        buttons[4][15] = (Button) findViewById(R.id.editText415);
        buttons[5][1] = (Button) findViewById(R.id.editText51);
        buttons[5][2] = (Button) findViewById(R.id.editText52);
        buttons[5][3] = (Button) findViewById(R.id.editText53);
        buttons[5][4] = (Button) findViewById(R.id.editText54);
        buttons[5][5] = (Button) findViewById(R.id.editText55);
        buttons[5][6] = (Button) findViewById(R.id.editText56);
        buttons[5][7] = (Button) findViewById(R.id.editText57);
        buttons[5][8] = (Button) findViewById(R.id.editText58);
        buttons[5][9] = (Button) findViewById(R.id.editText59);
        buttons[5][10] = (Button) findViewById(R.id.editText510);
        buttons[5][11] = (Button) findViewById(R.id.editText511);
        buttons[5][12] = (Button) findViewById(R.id.editText512);
        buttons[5][13] = (Button) findViewById(R.id.editText513);
        buttons[5][14] = (Button) findViewById(R.id.editText514);
        buttons[5][15] = (Button) findViewById(R.id.editText515);
        buttons[6][1] = (Button) findViewById(R.id.editText61);
        buttons[6][2] = (Button) findViewById(R.id.editText62);
        buttons[6][3] = (Button) findViewById(R.id.editText63);
        buttons[6][4] = (Button) findViewById(R.id.editText64);
        buttons[6][5] = (Button) findViewById(R.id.editText65);
        buttons[6][6] = (Button) findViewById(R.id.editText66);
        buttons[6][7] = (Button) findViewById(R.id.editText67);
        buttons[6][8] = (Button) findViewById(R.id.editText68);
        buttons[6][9] = (Button) findViewById(R.id.editText69);
        buttons[6][10] = (Button) findViewById(R.id.editText610);
        buttons[6][11] = (Button) findViewById(R.id.editText611);
        buttons[6][12] = (Button) findViewById(R.id.editText612);
        buttons[6][13] = (Button) findViewById(R.id.editText613);
        buttons[6][14] = (Button) findViewById(R.id.editText614);
        buttons[6][15] = (Button) findViewById(R.id.editText615);
        buttons[7][1] = (Button) findViewById(R.id.editText71);
        buttons[7][2] = (Button) findViewById(R.id.editText72);
        buttons[7][3] = (Button) findViewById(R.id.editText73);
        buttons[7][4] = (Button) findViewById(R.id.editText74);
        buttons[7][5] = (Button) findViewById(R.id.editText75);
        buttons[7][6] = (Button) findViewById(R.id.editText76);
        buttons[7][7] = (Button) findViewById(R.id.editText77);
        buttons[7][8] = (Button) findViewById(R.id.editText78);
        buttons[7][9] = (Button) findViewById(R.id.editText79);
        buttons[7][10] = (Button) findViewById(R.id.editText710);
        buttons[7][11] = (Button) findViewById(R.id.editText711);
        buttons[7][12] = (Button) findViewById(R.id.editText712);
        buttons[7][13] = (Button) findViewById(R.id.editText713);
        buttons[7][14] = (Button) findViewById(R.id.editText714);
        buttons[7][15] = (Button) findViewById(R.id.editText715);
        buttons[8][1] = (Button) findViewById(R.id.editText81);
        buttons[8][2] = (Button) findViewById(R.id.editText82);
        buttons[8][3] = (Button) findViewById(R.id.editText83);
        buttons[8][4] = (Button) findViewById(R.id.editText84);
        buttons[8][5] = (Button) findViewById(R.id.editText85);
        buttons[8][6] = (Button) findViewById(R.id.editText86);
        buttons[8][7] = (Button) findViewById(R.id.editText87);
        buttons[8][8] = (Button) findViewById(R.id.editText88);
        buttons[8][9] = (Button) findViewById(R.id.editText89);
        buttons[8][10] = (Button) findViewById(R.id.editText810);
        buttons[8][11] = (Button) findViewById(R.id.editText811);
        buttons[8][12] = (Button) findViewById(R.id.editText812);
        buttons[8][13] = (Button) findViewById(R.id.editText813);
        buttons[8][14] = (Button) findViewById(R.id.editText814);
        buttons[8][15] = (Button) findViewById(R.id.editText815);
        buttons[9][1] = (Button) findViewById(R.id.editText91);
        buttons[9][2] = (Button) findViewById(R.id.editText92);
        buttons[9][3] = (Button) findViewById(R.id.editText93);
        buttons[9][4] = (Button) findViewById(R.id.editText94);
        buttons[9][5] = (Button) findViewById(R.id.editText95);
        buttons[9][6] = (Button) findViewById(R.id.editText96);
        buttons[9][7] = (Button) findViewById(R.id.editText97);
        buttons[9][8] = (Button) findViewById(R.id.editText98);
        buttons[9][9] = (Button) findViewById(R.id.editText99);
        buttons[9][10] = (Button) findViewById(R.id.editText910);
        buttons[9][11] = (Button) findViewById(R.id.editText911);
        buttons[9][12] = (Button) findViewById(R.id.editText912);
        buttons[9][13] = (Button) findViewById(R.id.editText913);
        buttons[9][14] = (Button) findViewById(R.id.editText914);
        buttons[9][15] = (Button) findViewById(R.id.editText915);
        buttons[10][1] = (Button) findViewById(R.id.editText101);
        buttons[10][2] = (Button) findViewById(R.id.editText102);
        buttons[10][3] = (Button) findViewById(R.id.editText103);
        buttons[10][4] = (Button) findViewById(R.id.editText104);
        buttons[10][5] = (Button) findViewById(R.id.editText105);
        buttons[10][6] = (Button) findViewById(R.id.editText106);
        buttons[10][7] = (Button) findViewById(R.id.editText107);
        buttons[10][8] = (Button) findViewById(R.id.editText108);
        buttons[10][9] = (Button) findViewById(R.id.editText109);
        buttons[10][10] = (Button) findViewById(R.id.editText1010);
        buttons[10][11] = (Button) findViewById(R.id.editText1011);
        buttons[10][12] = (Button) findViewById(R.id.editText1012);
        buttons[10][13] = (Button) findViewById(R.id.editText1013);
        buttons[10][14] = (Button) findViewById(R.id.editText1014);
        buttons[10][15] = (Button) findViewById(R.id.editText1015);
        buttons[11][1] = (Button) findViewById(R.id.editText111);
        buttons[11][2] = (Button) findViewById(R.id.editText112);
        buttons[11][3] = (Button) findViewById(R.id.editText113);
        buttons[11][4] = (Button) findViewById(R.id.editText114);
        buttons[11][5] = (Button) findViewById(R.id.editText115);
        buttons[11][6] = (Button) findViewById(R.id.editText116);
        buttons[11][7] = (Button) findViewById(R.id.editText117);
        buttons[11][8] = (Button) findViewById(R.id.editText118);
        buttons[11][9] = (Button) findViewById(R.id.editText119);
        buttons[11][10] = (Button) findViewById(R.id.editText1110);
        buttons[11][11] = (Button) findViewById(R.id.editText1111);
        buttons[11][12] = (Button) findViewById(R.id.editText1112);
        buttons[11][13] = (Button) findViewById(R.id.editText1113);
        buttons[11][14] = (Button) findViewById(R.id.editText1114);
        buttons[11][15] = (Button) findViewById(R.id.editText1115);
        buttons[12][1] = (Button) findViewById(R.id.editText121);
        buttons[12][2] = (Button) findViewById(R.id.editText122);
        buttons[12][3] = (Button) findViewById(R.id.editText123);
        buttons[12][4] = (Button) findViewById(R.id.editText124);
        buttons[12][5] = (Button) findViewById(R.id.editText125);
        buttons[12][6] = (Button) findViewById(R.id.editText126);
        buttons[12][7] = (Button) findViewById(R.id.editText127);
        buttons[12][8] = (Button) findViewById(R.id.editText128);
        buttons[12][9] = (Button) findViewById(R.id.editText129);
        buttons[12][10] = (Button) findViewById(R.id.editText1210);
        buttons[12][11] = (Button) findViewById(R.id.editText1211);
        buttons[12][12] = (Button) findViewById(R.id.editText1212);
        buttons[12][13] = (Button) findViewById(R.id.editText1213);
        buttons[12][14] = (Button) findViewById(R.id.editText1214);
        buttons[12][15] = (Button) findViewById(R.id.editText1215);
        buttons[13][1] = (Button) findViewById(R.id.editText131);
        buttons[13][2] = (Button) findViewById(R.id.editText132);
        buttons[13][3] = (Button) findViewById(R.id.editText133);
        buttons[13][4] = (Button) findViewById(R.id.editText134);
        buttons[13][5] = (Button) findViewById(R.id.editText135);
        buttons[13][6] = (Button) findViewById(R.id.editText136);
        buttons[13][7] = (Button) findViewById(R.id.editText137);
        buttons[13][8] = (Button) findViewById(R.id.editText138);
        buttons[13][9] = (Button) findViewById(R.id.editText139);
        buttons[13][10] = (Button) findViewById(R.id.editText1310);
        buttons[13][11] = (Button) findViewById(R.id.editText1311);
        buttons[13][12] = (Button) findViewById(R.id.editText1312);
        buttons[13][13] = (Button) findViewById(R.id.editText1313);
        buttons[13][14] = (Button) findViewById(R.id.editText1314);
        buttons[13][15] = (Button) findViewById(R.id.editText1315);
        buttons[14][1] = (Button) findViewById(R.id.editText141);
        buttons[14][2] = (Button) findViewById(R.id.editText142);
        buttons[14][3] = (Button) findViewById(R.id.editText143);
        buttons[14][4] = (Button) findViewById(R.id.editText144);
        buttons[14][5] = (Button) findViewById(R.id.editText145);
        buttons[14][6] = (Button) findViewById(R.id.editText146);
        buttons[14][7] = (Button) findViewById(R.id.editText147);
        buttons[14][8] = (Button) findViewById(R.id.editText148);
        buttons[14][9] = (Button) findViewById(R.id.editText149);
        buttons[14][10] = (Button) findViewById(R.id.editText1410);
        buttons[14][11] = (Button) findViewById(R.id.editText1411);
        buttons[14][12] = (Button) findViewById(R.id.editText1412);
        buttons[14][13] = (Button) findViewById(R.id.editText1413);
        buttons[14][14] = (Button) findViewById(R.id.editText1414);
        buttons[14][15] = (Button) findViewById(R.id.editText1415);
        buttons[15][1] = (Button) findViewById(R.id.editText151);
        buttons[15][2] = (Button) findViewById(R.id.editText152);
        buttons[15][3] = (Button) findViewById(R.id.editText153);
        buttons[15][4] = (Button) findViewById(R.id.editText154);
        buttons[15][5] = (Button) findViewById(R.id.editText155);
        buttons[15][6] = (Button) findViewById(R.id.editText156);
        buttons[15][7] = (Button) findViewById(R.id.editText157);
        buttons[15][8] = (Button) findViewById(R.id.editText158);
        buttons[15][9] = (Button) findViewById(R.id.editText159);
        buttons[15][10] = (Button) findViewById(R.id.editText1510);
        buttons[15][11] = (Button) findViewById(R.id.editText1511);
        buttons[15][12] = (Button) findViewById(R.id.editText1512);
        buttons[15][13] = (Button) findViewById(R.id.editText1513);
        buttons[15][14] = (Button) findViewById(R.id.editText1514);
        buttons[15][15] = (Button) findViewById(R.id.editText1515);
        buttons[1][1].setOnClickListener(this);
        buttons[1][2].setOnClickListener(this);
        buttons[1][3].setOnClickListener(this);
        buttons[1][4].setOnClickListener(this);
        buttons[1][5].setOnClickListener(this);
        buttons[1][6].setOnClickListener(this);
        buttons[1][7].setOnClickListener(this);
        buttons[1][8].setOnClickListener(this);
        buttons[1][9].setOnClickListener(this);
        buttons[1][10].setOnClickListener(this);
        buttons[1][11].setOnClickListener(this);
        buttons[1][12].setOnClickListener(this);
        buttons[1][13].setOnClickListener(this);
        buttons[1][14].setOnClickListener(this);
        buttons[1][15].setOnClickListener(this);
        buttons[2][1].setOnClickListener(this);
        buttons[2][2].setOnClickListener(this);
        buttons[2][3].setOnClickListener(this);
        buttons[2][4].setOnClickListener(this);
        buttons[2][5].setOnClickListener(this);
        buttons[2][6].setOnClickListener(this);
        buttons[2][7].setOnClickListener(this);
        buttons[2][8].setOnClickListener(this);
        buttons[2][9].setOnClickListener(this);
        buttons[2][10].setOnClickListener(this);
        buttons[2][11].setOnClickListener(this);
        buttons[2][12].setOnClickListener(this);
        buttons[2][13].setOnClickListener(this);
        buttons[2][14].setOnClickListener(this);
        buttons[2][15].setOnClickListener(this);
        buttons[3][1].setOnClickListener(this);
        buttons[3][2].setOnClickListener(this);
        buttons[3][3].setOnClickListener(this);
        buttons[3][4].setOnClickListener(this);
        buttons[3][5].setOnClickListener(this);
        buttons[3][6].setOnClickListener(this);
        buttons[3][7].setOnClickListener(this);
        buttons[3][8].setOnClickListener(this);
        buttons[3][9].setOnClickListener(this);
        buttons[3][10].setOnClickListener(this);
        buttons[3][11].setOnClickListener(this);
        buttons[3][12].setOnClickListener(this);
        buttons[3][13].setOnClickListener(this);
        buttons[3][14].setOnClickListener(this);
        buttons[3][15].setOnClickListener(this);
        buttons[4][1].setOnClickListener(this);
        buttons[4][2].setOnClickListener(this);
        buttons[4][3].setOnClickListener(this);
        buttons[4][4].setOnClickListener(this);
        buttons[4][5].setOnClickListener(this);
        buttons[4][6].setOnClickListener(this);
        buttons[4][7].setOnClickListener(this);
        buttons[4][8].setOnClickListener(this);
        buttons[4][9].setOnClickListener(this);
        buttons[4][10].setOnClickListener(this);
        buttons[4][11].setOnClickListener(this);
        buttons[4][12].setOnClickListener(this);
        buttons[4][13].setOnClickListener(this);
        buttons[4][14].setOnClickListener(this);
        buttons[4][15].setOnClickListener(this);
        buttons[5][1].setOnClickListener(this);
        buttons[5][2].setOnClickListener(this);
        buttons[5][3].setOnClickListener(this);
        buttons[5][4].setOnClickListener(this);
        buttons[5][5].setOnClickListener(this);
        buttons[5][6].setOnClickListener(this);
        buttons[5][7].setOnClickListener(this);
        buttons[5][8].setOnClickListener(this);
        buttons[5][9].setOnClickListener(this);
        buttons[5][10].setOnClickListener(this);
        buttons[5][11].setOnClickListener(this);
        buttons[5][12].setOnClickListener(this);
        buttons[5][13].setOnClickListener(this);
        buttons[5][14].setOnClickListener(this);
        buttons[5][15].setOnClickListener(this);
        buttons[6][1].setOnClickListener(this);
        buttons[6][2].setOnClickListener(this);
        buttons[6][3].setOnClickListener(this);
        buttons[6][4].setOnClickListener(this);
        buttons[6][5].setOnClickListener(this);
        buttons[6][6].setOnClickListener(this);
        buttons[6][7].setOnClickListener(this);
        buttons[6][8].setOnClickListener(this);
        buttons[6][9].setOnClickListener(this);
        buttons[6][10].setOnClickListener(this);
        buttons[6][11].setOnClickListener(this);
        buttons[6][12].setOnClickListener(this);
        buttons[6][13].setOnClickListener(this);
        buttons[6][14].setOnClickListener(this);
        buttons[6][15].setOnClickListener(this);
        buttons[7][1].setOnClickListener(this);
        buttons[7][2].setOnClickListener(this);
        buttons[7][3].setOnClickListener(this);
        buttons[7][4].setOnClickListener(this);
        buttons[7][5].setOnClickListener(this);
        buttons[7][6].setOnClickListener(this);
        buttons[7][7].setOnClickListener(this);
        buttons[7][8].setOnClickListener(this);
        buttons[7][9].setOnClickListener(this);
        buttons[7][10].setOnClickListener(this);
        buttons[7][11].setOnClickListener(this);
        buttons[7][12].setOnClickListener(this);
        buttons[7][13].setOnClickListener(this);
        buttons[7][14].setOnClickListener(this);
        buttons[7][15].setOnClickListener(this);
        buttons[8][1].setOnClickListener(this);
        buttons[8][2].setOnClickListener(this);
        buttons[8][3].setOnClickListener(this);
        buttons[8][4].setOnClickListener(this);
        buttons[8][5].setOnClickListener(this);
        buttons[8][6].setOnClickListener(this);
        buttons[8][7].setOnClickListener(this);
        buttons[8][8].setOnClickListener(this);
        buttons[8][9].setOnClickListener(this);
        buttons[8][10].setOnClickListener(this);
        buttons[8][11].setOnClickListener(this);
        buttons[8][12].setOnClickListener(this);
        buttons[8][13].setOnClickListener(this);
        buttons[8][14].setOnClickListener(this);
        buttons[8][15].setOnClickListener(this);
        buttons[9][1].setOnClickListener(this);
        buttons[9][2].setOnClickListener(this);
        buttons[9][3].setOnClickListener(this);
        buttons[9][4].setOnClickListener(this);
        buttons[9][5].setOnClickListener(this);
        buttons[9][6].setOnClickListener(this);
        buttons[9][7].setOnClickListener(this);
        buttons[9][8].setOnClickListener(this);
        buttons[9][9].setOnClickListener(this);
        buttons[9][10].setOnClickListener(this);
        buttons[9][11].setOnClickListener(this);
        buttons[9][12].setOnClickListener(this);
        buttons[9][13].setOnClickListener(this);
        buttons[9][14].setOnClickListener(this);
        buttons[9][15].setOnClickListener(this);
        buttons[10][1].setOnClickListener(this);
        buttons[10][2].setOnClickListener(this);
        buttons[10][3].setOnClickListener(this);
        buttons[10][4].setOnClickListener(this);
        buttons[10][5].setOnClickListener(this);
        buttons[10][6].setOnClickListener(this);
        buttons[10][7].setOnClickListener(this);
        buttons[10][8].setOnClickListener(this);
        buttons[10][9].setOnClickListener(this);
        buttons[10][10].setOnClickListener(this);
        buttons[10][11].setOnClickListener(this);
        buttons[10][12].setOnClickListener(this);
        buttons[10][13].setOnClickListener(this);
        buttons[10][14].setOnClickListener(this);
        buttons[10][15].setOnClickListener(this);
        buttons[11][1].setOnClickListener(this);
        buttons[11][2].setOnClickListener(this);
        buttons[11][3].setOnClickListener(this);
        buttons[11][4].setOnClickListener(this);
        buttons[11][5].setOnClickListener(this);
        buttons[11][6].setOnClickListener(this);
        buttons[11][7].setOnClickListener(this);
        buttons[11][8].setOnClickListener(this);
        buttons[11][9].setOnClickListener(this);
        buttons[11][10].setOnClickListener(this);
        buttons[11][11].setOnClickListener(this);
        buttons[11][12].setOnClickListener(this);
        buttons[11][13].setOnClickListener(this);
        buttons[11][14].setOnClickListener(this);
        buttons[11][15].setOnClickListener(this);
        buttons[12][1].setOnClickListener(this);
        buttons[12][2].setOnClickListener(this);
        buttons[12][3].setOnClickListener(this);
        buttons[12][4].setOnClickListener(this);
        buttons[12][5].setOnClickListener(this);
        buttons[12][6].setOnClickListener(this);
        buttons[12][7].setOnClickListener(this);
        buttons[12][8].setOnClickListener(this);
        buttons[12][9].setOnClickListener(this);
        buttons[12][10].setOnClickListener(this);
        buttons[12][11].setOnClickListener(this);
        buttons[12][12].setOnClickListener(this);
        buttons[12][13].setOnClickListener(this);
        buttons[12][14].setOnClickListener(this);
        buttons[12][15].setOnClickListener(this);
        buttons[13][1].setOnClickListener(this);
        buttons[13][2].setOnClickListener(this);
        buttons[13][3].setOnClickListener(this);
        buttons[13][4].setOnClickListener(this);
        buttons[13][5].setOnClickListener(this);
        buttons[13][6].setOnClickListener(this);
        buttons[13][7].setOnClickListener(this);
        buttons[13][8].setOnClickListener(this);
        buttons[13][9].setOnClickListener(this);
        buttons[13][10].setOnClickListener(this);
        buttons[13][11].setOnClickListener(this);
        buttons[13][12].setOnClickListener(this);
        buttons[13][13].setOnClickListener(this);
        buttons[13][14].setOnClickListener(this);
        buttons[13][15].setOnClickListener(this);
        buttons[14][1].setOnClickListener(this);
        buttons[14][2].setOnClickListener(this);
        buttons[14][3].setOnClickListener(this);
        buttons[14][4].setOnClickListener(this);
        buttons[14][5].setOnClickListener(this);
        buttons[14][6].setOnClickListener(this);
        buttons[14][7].setOnClickListener(this);
        buttons[14][8].setOnClickListener(this);
        buttons[14][9].setOnClickListener(this);
        buttons[14][10].setOnClickListener(this);
        buttons[14][11].setOnClickListener(this);
        buttons[14][12].setOnClickListener(this);
        buttons[14][13].setOnClickListener(this);
        buttons[14][14].setOnClickListener(this);
        buttons[14][15].setOnClickListener(this);
        buttons[15][1].setOnClickListener(this);
        buttons[15][2].setOnClickListener(this);
        buttons[15][3].setOnClickListener(this);
        buttons[15][4].setOnClickListener(this);
        buttons[15][5].setOnClickListener(this);
        buttons[15][6].setOnClickListener(this);
        buttons[15][7].setOnClickListener(this);
        buttons[15][8].setOnClickListener(this);
        buttons[15][9].setOnClickListener(this);
        buttons[15][10].setOnClickListener(this);
        buttons[15][11].setOnClickListener(this);
        buttons[15][12].setOnClickListener(this);
        buttons[15][13].setOnClickListener(this);
        buttons[15][14].setOnClickListener(this);
        buttons[15][15].setOnClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {

        return false;
    }
}
