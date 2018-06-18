package com.example.callum.first.util;


import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.callum.first.MainActivity;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by callum on 9/5/15.
 */
public class Globalclass {
    //    private DBHelper dbHelper;
//    MainActivity mainActivity = new MainActivity();
    private String answer;
    public int level = 1;
    private String[] answers = new String[33];
    private String[] clues = new String[33];
    private String[][] currentBoard = new String[16][16];
    public boolean loggedIn = true;
    public int levelll = 1;
    private String rotation[] = new String[33];
    private boolean beforePercentage = true;
    private boolean hideKeyboard=true;
//    private String Board = "(1)(2)~(3)#(4)~(5)~(6)#(7)~(8)~/#~#~#~#~#~#~#~#/(9)~~~~~~~#(10)~~~~~/#~#~#~#~#~#~#~#/(11)~~~##(12)~~~~~~~~/###~#(13)###~#~###/(14)(15)~~~~#(16)~~~~~(17)~/#~###~#~#~###~#/(18)~~(19)~~~~#(20)~(21)~~~/###~#~###~#~###/(22)(23)~~~~~(24)~##(25)~(26)~/#~#~#~#~#(27)#~#~#/(28)~~~~~#(29)~~~~~~~/#~#~#~#~#~#~#~#/(30)~~~#(31)~~~~#(32)~~~//";
//    String answersString = "1Onus,4 Khaki,7Pips,9Alter ego,10Remand,11Gnat,12Eastender,14Cranky,16Pro forma,18Heaviest,20Aerate,22Gyroscope,25Ugly,28Salami,29Long haul,30Knee,31Tenon,32Need.2Nylon,3Sweeten,4Keen,5Aroma,6Irrational,7Pimento,8Pence,13Typescript,15Rye,16Put,17Mat,19Violate,21Roughen,23Yearn,24Pylon,26Louse,27Anon.";
//    public Globalclass(DBHelper dbHelper) {
//        this.dbHelper = dbHelper;
//    }


//public void start(){
//
//}


//    public void init(){
//        level =5;
//    }
public boolean isNo (char Char){

    Log.d("character passed",Char+"");
    if((Char)=='1'||(Char)=='2'||(Char)=='3'||(Char)=='4'||(Char)=='5'||(Char)=='6'||(Char)=='7'||(Char)=='8'||(Char)=='9'||(Char)=='0')
    {
        Log.d("isNo","TRUE");
        return true;
    }else Log.d("isNo","FALSE"); return false;
}

    //get method for logged in value
    public boolean loggedIn() {
        return loggedIn;
    }

    public void addAnswers (String answersString){
        boolean stillWord=false;
        Log.d("add Answers","running");
        Log.d("answers",answersString);
        int counter =0;
        String answerNo="";
        char rotationChar = 'h';
        for (int i = 0;i<2;i++) {
            while (answersString.charAt(counter) != '.') {
                if (answersString.charAt(counter) == ' ') {counter++; Log.d("space","run");}
                if (isNo(answersString.charAt(counter))) {
                    stillWord=true;
                    answerNo = "" + answersString.charAt((counter));
                    counter++;
                    if (isNo(answersString.charAt(counter))) {
                        answerNo = answerNo + "" + answersString.charAt(counter);
                        counter++;
                    }
//            while still a word
                    if (answersString.charAt(counter) == ' '){ counter++;  Log.d("space2","run");}
                    if(answers[Integer.parseInt(answerNo)]!=""){
                        answers[Integer.parseInt(answerNo)]= answers[Integer.parseInt(answerNo)]+"%";
                    }
                    while (stillWord){
                        Log.d("WhileWord","run"+counter);
                        answers[Integer.parseInt(answerNo)] = answers[Integer.parseInt(answerNo)] + "" + answersString.charAt(counter);
                        counter++;
//                        ||!(answersString.charAt(counter) != '.')
    if (answersString.charAt(counter) == ','||answersString.charAt(counter) == '.'){
    Log.d("should stop", "ARGHHH!!!");
    stillWord=false;}
                    }
                    Log.d(answerNo,"word: "+answers[Integer.parseInt(answerNo)]);
                   if(answersString.charAt(counter) == ','){ counter++;}
//                    if it is the first time
                    if (i==0){
//                        clears the answers if the old ones are still there.
                        rotation[Integer.parseInt(answerNo)]="£";
                    }
                    if(rotation[Integer.parseInt(answerNo)]!="£"){
                        rotation[Integer.parseInt(answerNo)]= rotation[Integer.parseInt(answerNo)]+"%"+rotationChar;
                    }else{rotation[Integer.parseInt(answerNo)] = rotationChar+"";
                    }
                }
                Log.d("rotations "+rotation[Integer.parseInt(answerNo)],answerNo+" "+i);
            }
            counter++;
            rotationChar='v';
            Log.d("rotation char",i+" "+rotationChar);
        }
}

    public void addClues (String clueString){
        boolean stillWord=false;
        Log.d("add clues","running");
        Log.d("clues",clueString);
        int counter =0;
        String clueNo="";
            while (clueString.charAt(counter) != '%') {
//                if (clueString.charAt(counter) == ' ') {counter++; Log.d("space","run");}
                if (isNo(clueString.charAt(counter))) {
                    stillWord=true;
                    clueNo = "" + clueString.charAt((counter));
                    counter++;
                    if (isNo(clueString.charAt(counter))) {
                        clueNo = clueNo + "" + clueString.charAt(counter);
                        counter++;
                    }
//            while still a word
//                    if (clueString.charAt(counter) == ' '){ counter++;  Log.d("space2","run");}
//coud add check for punctuation
                    boolean containsText=false;
                    try{
                        if(clues[Integer.parseInt(clueNo)].contains(".")||clues[Integer.parseInt(clueNo)].contains("?")||clues[Integer.parseInt(clueNo)].contains("!")){
                            containsText=true;
                        }else{
                            containsText=false;
                            clues[Integer.parseInt(clueNo)]="";
                        }
                    }catch (NullPointerException NPE){
                        containsText=false;
                        clues[Integer.parseInt(clueNo)]="";
                    }
                    if(containsText){
                        clues[Integer.parseInt(clueNo)]= clues[Integer.parseInt(clueNo)]+"%";
                    }
                    while (stillWord){
                        Log.d("WhileWord","run"+counter);
                        clues[Integer.parseInt(clueNo)] = clues[Integer.parseInt(clueNo)] + "" + clueString.charAt(counter);
                        counter++;
//                        ||!(clueString.charAt(counter) != '.')
                        if (clueString.charAt(counter) == '.'||clueString.charAt(counter) == '?'||clueString.charAt(counter) == '!'){
                            clues[Integer.parseInt(clueNo)] = clues[Integer.parseInt(clueNo)] + "" + clueString.charAt(counter);
                            Log.d("should stop", "ARGHHH!!!");
                            stillWord=false;}
                    }
                    Log.d(clueNo,"word: "+clues[Integer.parseInt(clueNo)]);
                    if(clueString.charAt(counter) == ','){ counter++;}
//                    if it is the first time
                Log.d(clueNo+" answer","counter"+counter);
            }
            counter++;
        }
        Log.d("output clues",""+clueString);
    }








    //initilises all of the variable values when called
    public void setAnswers(String answersString,String Board,String cluesString) {

        for (int i =1; i<33;i++){
            answers[i]="";
            rotation[i]="";
        }

//        answers[1] = "Onus";
//        answers[2] = "Nylon";
//        answers[3] = "Sweeten";
//        answers[4] = "Khaki%Keen";
//        answers[5] = "Aroma";
//        answers[6] = "Irrational";
//        answers[7] = "Pips%Pimento";
//        answers[8] = "Pence";
//        answers[9] = "Alter Ego";
//        answers[10] = "Remand";
//        answers[11] = "Gnat";
//        answers[12] = "Eastender";
//        answers[13] = "Typescript";
//        answers[14] = "Cranky";
//        answers[15] = "Rye";
//        answers[16] = "Pro forma%Put";
//        answers[17] = "Mat";
//        answers[18] = "Heaviest";
//        answers[19] = "Violate";
//        answers[20] = "Aerate";
//        answers[21] = "Roughen";
//        answers[22] = "Gyroscope";
//        answers[23] = "Yearn";
//        answers[24] = "Pylon";
//        answers[25] = "Ugly";
//        answers[26] = "Louse";
//        answers[27] = "Anon";
//        answers[28] = "Salami";
//        answers[29] = "Long haul";
//        answers[30] = "Knee";
//        answers[31] = "Tenon";
//        answers[32] = "Need";
        addAnswers(answersString);
        Log.d("After", "add Answers");
        stringToArray(Board);
        Log.d("After", "String to array");
        addClues(cluesString);
//        clues[1] = "Heron usually has responsibility.";
//        clues[2] = "Synthetic fabric many Londoners conceal.";
//        clues[3] = "See Newt in terms of making it taste nicer.";
//        clues[4] = "Color of milkshake shaken without the elms.%Make entrance to have a sharp centrepiece.";
//        clues[5] = "Scent of a romantic part.";
//        clues[6] = "Tailor rain to meet the needs of not being logical.";
//        clues[7] = "Rank of oranges or lemons?%Point me towards the pepper.";
//        clues[8] = "Open centre for holding small change.";
//        clues[9] = "My other self or eaglet flying about.";
//        clues[10] = "Red man to change, or put in prison.";
//        clues[11] = "Going naturally to enclose the insect.";
//        clues[12] = "Resented a change as a Cockney.";
//        clues[13] = "MS that is a kind of writing?";
//        clues[14] = "Irritable in a very odd sort of way.";
//        clues[15] = "Sort of whiskey that would be dry if said with a smile.";
//        clues[16] = "Set pattern sheet could go for a romp.%Place in the computer.";
//        clues[17] = "Coaster in formatting environment.";
//        clues[18] = "Have ties to undo which weigh the most.";
//        clues[19] = "Desecrate a violet genetically modified.";
//        clues[20] = "Attempt to alliterate till lost and expose to fresh air.";
//        clues[21] = "Make coarse in through entrance.";
//        clues[22] = "A spinner - does it have the range of the cheque?";
//        clues[23] = "Long for end of May to arrive before starting in earnest.";
//        clues[24] = "Power tower said to load up.";
//        clues[25] = "Sort of fruit you don't want to look like?";
//        clues[26] = "ehold and exploit the sucker.";
//        clues[27] = "How soon I'll see you without a name briefly.";
//        clues[28] = "Alas I'm in a mess with the sausage.";
//        clues[29] = "Lengthy journey - would it be an extended tug?";
//        clues[30] = "What to jerk in a quick reaction.";
//        clues[31] = "Saw that has a decade more.";
//        clues[32] = "Require to make the bread, they say.";

//        rotation[1] = "h";
//        rotation[2] = "v";
//        rotation[3] = "v";
//        rotation[4] = "h%v";
//        rotation[5] = "v";
//        rotation[6] = "v";
//        rotation[7] = "h%v";
//        rotation[8] = "v";
//        rotation[9] = "h";
//        rotation[10] = "h";
//        rotation[11] = "h";
//        rotation[12] = "h";
//        rotation[13] = "v";
//        rotation[14] = "h";
//        rotation[15] = "v";
//        rotation[16]= "h%v";
//        rotation[17] = "v";
//        rotation[18] = "h";
//        rotation[19] = "v";
//        rotation[20] = "h";
//        rotation[21] = "v";
//        rotation[22] = "h";
//        rotation[23] = "v";
//        rotation[24] = "v";
//        rotation[25] = "h";
//        rotation[26] = "v";
//        rotation[27] = "v";
//        rotation[28] = "h";
//        rotation[29] = "h";
//        rotation[30] = "h";
//        rotation[31] = "h";
//        rotation[32] = "h";


//        Users[1][0]="cal";
//        Users[2][0]="pi";

    }

    //     get method for answer
    public String getAnswer() {
        if (answers[level].contains("%")) {
            String[] split = answers[level].split("%");
            if (beforePercentage) {
                return split[0];
            } else {
                return split[1];
            }
        } else {
            return answers[level];

        }
    }

    public String[][] getBoard() {
        return currentBoard;
    }
//    public String getB(){
////        myDbHelper.read("Level","board");
//        return myDbHelper.read("Level","board","levelNo = '1'");
//    }

    public void stringToArray(String Board) {
        Log.d("board",Board);
        Log.d("Length", Board.length() + "");
        int counter = -1;
        for (int i = 1; i <= 15; i++) {
            for (int c = 1; c <= 15; c++) {
                counter++;
                Log.d("toArray", "" + (counter));
                if (Board.charAt(counter) == '/') {
                    counter++;
                }
                switch (Board.charAt(counter)) {
                    case '(':
                        counter++;
//                        if there is a two character numebr then add it to the board.
                        while (Board.charAt(counter)!=')'){
                           if(Board.charAt(counter)=='.'){
//                               the next character is a letter and only one letter
                                counter++;
                               currentBoard[i][c]=currentBoard[i][c]+"."+Board.charAt(counter);
                           }else if(isNo(Board.charAt(counter))){
                                currentBoard[i][c]=""+Board.charAt(counter);
                               if(isNo(Board.charAt(counter+1))){
                                   counter++;
                                   currentBoard[i][c]=currentBoard[i][c]+""+Board.charAt(counter);
                               }
                            }
                            counter++;
                        }Log.d("finished sample",currentBoard[i][c]);

//                        if (Board.charAt(3 + counter) == ')') {
//                            counter++;
//                            currentBoard[i][c] = Board.charAt(counter) + "" + Board.charAt(counter + 1);
//                            counter = counter + 2;
//                        } else if (Board.charAt(2 + counter) == ')') {
//                            counter++;
//                            currentBoard[i][c] = Board.charAt(counter) + "";
//                            counter = counter + 1;
//                        } else {
//                            Log.d("StringArray", "BadFormatting");
//                        }
                        break;
                    case '/':
                        Log.d("toArray", "/ in Wrong place");
                        break;
                    default:
                        currentBoard[i][c] = Board.charAt(counter) + "";
                        break;

                }


            }

        }
    }

    public void setBeforeComma(boolean beforeComma1) {
        beforePercentage = beforeComma1;
    }

    public boolean getBeforeComma() {
        return beforePercentage;
    }

    public boolean twoValues(int clueNo) {
        if (rotation[clueNo].contains("h")&&rotation[clueNo].contains("v")) {
            return true;
        } else return false;
    }

    public String getAnswer(int clueNo) {
        if (answers[clueNo].contains("%")) {
            String[] split = answers[clueNo].split("%");
            if (beforePercentage) {
                return split[0];
            } else {
                return split[1];
            }
        } else {
            return answers[clueNo];

        }
    }

    //    get method for clue
    public String getRotation(int clueNo) {
        if (rotation[clueNo].contains("h")&&rotation[clueNo].contains("v")){
            String[] split = rotation[clueNo].split("%");
            if (beforePercentage) {
                return split[0];
            } else {
                return split[1];
            }
        } else if(rotation[clueNo].contains("h")){
            return "h";
        }else{
            return "v";
        }
    }


    public String getClue() {
        if (clues[level].contains("%")) {
            String[] split = clues[level].split("%");
            if (beforePercentage) {
                return split[0];
            } else {
                return split[1];
            }
        } else {
            return clues[level];

        }
    }

    public String getClue(int clueNo) {
        if (clues[clueNo].contains("%")) {
            String[] split = clues[clueNo].split("%");
            if (beforePercentage) {
                return split[0];

            } else {
                return split[1];
            }
        } else {
            return clues[clueNo];

        }
    }

    //    public void setAnswer(String aAnswer)
//    {
//        answer = aAnswer;
//    }
//  get methof for level
    public int getLevel() {
        return level;
    }

    //set method for level
    public void setLevel(int aLevel) {
        level = aLevel;
    }

    //    Adds one level when they have leveled up
    public void levelUp() {
        level++;
//        System.out.println(level);
    }

    //    sign in function
    public boolean signIn() {
        loggedIn = true;
        return true;
    }

    //    sign up function
    public boolean signUp() {

        loggedIn = true;
        return true;
    }
    public boolean getHideKeyboard(){
        return hideKeyboard;
    }

    public void setHideKeyboard(boolean passedHideKeyboard){
        hideKeyboard=passedHideKeyboard;
        Log.d("HideKeyboard",""+hideKeyboard);
    }

}

