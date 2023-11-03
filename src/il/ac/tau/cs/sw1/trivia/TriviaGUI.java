package il.ac.tau.cs.sw1.trivia;

import java.io.*;
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TriviaGUI {

    private static final int MAX_ERRORS = 3;
    private static int numOfWrongAnswer = 0;
    private static int numOfQuestionsUntilNow = 0;
    private static int passWheel = 0;
    private static int fiftyFiftyWheel = 0;
    private Shell shell;
    private Label scoreLabel;
    private Composite questionPanel;
    private Label startupMessageLabel;
    private Font boldFont;
    private String lastAnswer = "";
    private boolean gameOver;

    // Currently visible UI elements.
    Label instructionLabel;
    Label questionLabel;
    private List<Button> answerButtons = new LinkedList<>();
    private Button passButton;
    private Button fiftyFiftyButton;
    private List<Question> listOfQuestions;

    public void open() {
        createShell();
        runApplication();
    }

    /**
     * Creates the widgets of the application main window
     */
    private void createShell() {
        Display display = Display.getDefault();
        shell = new Shell(display);
        shell.setText("Trivia");

        // window style
        Rectangle monitor_bounds = shell.getMonitor().getBounds();
        shell.setSize(new Point(monitor_bounds.width / 3,
                monitor_bounds.height / 4));
        shell.setLayout(new GridLayout());

        FontData fontData = new FontData();
        fontData.setStyle(SWT.BOLD);
        boldFont = new Font(shell.getDisplay(), fontData);

        // create window panels
        createFileLoadingPanel();
        createScorePanel();
        createQuestionPanel();
    }

    /**
     * Creates the widgets of the form for trivia file selection
     */
    private void createFileLoadingPanel() {
        final Composite fileSelection = new Composite(shell, SWT.NULL);
        fileSelection.setLayoutData(GUIUtils.createFillGridData(1));
        fileSelection.setLayout(new GridLayout(4, false));

        final Label label = new Label(fileSelection, SWT.NONE);
        label.setText("Enter trivia file path: ");

        // text field to enter the file path
        final Text filePathField = new Text(fileSelection, SWT.SINGLE
                | SWT.BORDER);
        filePathField.setLayoutData(GUIUtils.createFillGridData(1));


        // "Browse" button
        final Button browseButton = new Button(fileSelection, SWT.PUSH);
        browseButton.setText("Browse");
        browseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) { //Q1
                if (e.getSource() instanceof Button) {
                    Button b = (Button) e.getSource();
                    String filepath = GUIUtils.getFilePathFromFileDialog(shell);
                    if (filepath != null && filepath.length() > 0) {
                        filePathField.setText(filepath);
                    }
                }
            }
        });


        // "Play!" button
        final Button playButton = new Button(fileSelection, SWT.PUSH);
        playButton.setText("Play!");
        playButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) { //Q2
                gameOver = false;
                if (e.getSource() instanceof Button) {
                    Button b = (Button) e.getSource();
                    String filePath = filePathField.getText();
                    File file = new File(filePath);
                    listOfQuestions = new LinkedList<>();
                    try {
                        Question q;
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        String[] splitLine;
                        while ((line = reader.readLine()) != null) {
                            splitLine = line.split("\t");
                            List<String> answers = new ArrayList<>();
                            answers.add(splitLine[1]);
                            answers.add(splitLine[2]);
                            answers.add(splitLine[3]);
                            answers.add(splitLine[4]);
                            q = new Question(splitLine[0], splitLine[1], answers);
                            listOfQuestions.add(q);
                            scoreLabel.setText("0");
                            lastAnswer = "";
                            numOfQuestionsUntilNow = 0;
                            numOfWrongAnswer = 0;
                            passWheel = 0;
                            fiftyFiftyWheel = 0;
                        }


                    } catch (IOException ex) {
                        System.out.println("An error has occurred");
                    }

                }

                nextQuestion();

                numOfQuestionsUntilNow++;

            }

        });

    }


    /**
     * Creates the panel that displays the current score
     */
    private void createScorePanel() {
        Composite scorePanel = new Composite(shell, SWT.BORDER);
        scorePanel.setLayoutData(GUIUtils.createFillGridData(1));
        scorePanel.setLayout(new GridLayout(2, false));

        final Label label = new Label(scorePanel, SWT.NONE);
        label.setText("Total score: ");

        // The label which displays the score; initially empty
        scoreLabel = new Label(scorePanel, SWT.NONE);
        scoreLabel.setLayoutData(GUIUtils.createFillGridData(1));
    }

    /**
     * Creates the panel that displays the questions, as soon as the game
     * starts. See the updateQuestionPanel for creating the question and answer
     * buttons
     */
    private void createQuestionPanel() {
        questionPanel = new Composite(shell, SWT.BORDER);
        questionPanel.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true));
        questionPanel.setLayout(new GridLayout(2, true));

        // Initially, only displays a message
        startupMessageLabel = new Label(questionPanel, SWT.NONE);
        startupMessageLabel.setText("No question to display, yet.");
        startupMessageLabel.setLayoutData(GUIUtils.createFillGridData(2));
    }

    /**
     * Serves to display the question and answer buttons
     */
    private void updateQuestionPanel(String question, List<String> answers) {
        // Save current list of answers.
        List<String> currentAnswers = answers;

        // clear the question panel
        Control[] children = questionPanel.getChildren();
        for (Control control : children) {
            control.dispose();
        }

        // create the instruction label
        instructionLabel = new Label(questionPanel, SWT.CENTER | SWT.WRAP);
        instructionLabel.setText(lastAnswer + "Answer the following question:");
        instructionLabel.setLayoutData(GUIUtils.createFillGridData(2));

        // create the question label
        questionLabel = new Label(questionPanel, SWT.CENTER | SWT.WRAP);
        questionLabel.setText(question);
        questionLabel.setFont(boldFont);
        questionLabel.setLayoutData(GUIUtils.createFillGridData(2));
        List<String> checkAnswers = new ArrayList<>();
        checkAnswers.addAll(answers);
        // create the answer buttons
        answerButtons.clear();


        for (int i = 0; i < 4; i++) {
            Button answerButton = new Button(questionPanel, SWT.PUSH | SWT.WRAP);
            answerButton.setText(answers.get(i));
            GridData answerLayoutData = GUIUtils.createFillGridData(1);
            answerLayoutData.verticalAlignment = SWT.FILL;
            answerButton.setLayoutData(answerLayoutData);

            answerButtons.add(answerButton);
        }
        if (!gameOver) {
            for (int i = 0; i < answerButtons.size(); i++) {
                answerButtons.get(i).addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) { //Q3
                        if (e.getSource() instanceof Button) {
                            Button b = (Button) e.getSource();
                            if (!gameOver) {

                                Question q = getQuestion(question, answers);

                                String answer = q.getCorrectAnswer(); //????

                                listOfQuestions.remove(q);

                                if (b.getText().equals(answer)) {
                                    scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) + 3));
                                    lastAnswer = "Correct! ";
                                    numOfWrongAnswer = 0;
                                } else //wrong answer
                                {
                                    scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) - 2));
                                    lastAnswer = "Wrong answer...";
                                    numOfWrongAnswer++;
                                }
                                if (numOfWrongAnswer == MAX_ERRORS || listOfQuestions.size() == 0) {
                                    gameOver = true;

                                    GUIUtils.showInfoDialog(shell, "GAME OVER", "Your final score is " + scoreLabel.getText() + " after " + numOfQuestionsUntilNow + " questions.");
                                    numOfWrongAnswer = 0;
                                    numOfQuestionsUntilNow = 0;
                                }
                                if (!gameOver) {
                                    nextQuestion();
                                    numOfQuestionsUntilNow++;
                                }

                            }
                        }
                    }
                });
            }
        }

        // create the "Pass" button to skip a question
        passButton = new Button(questionPanel, SWT.PUSH);
        passButton.setText("Pass");
        GridData data = new GridData(GridData.END, GridData.CENTER, true,
                false);
        data.horizontalSpan = 1;
        passButton.setLayoutData(data);
        if (Integer.parseInt(scoreLabel.getText()) <= 0 && passWheel != 0) //???
            passButton.setEnabled(false);

        passButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) { //Q4
                passWheel++;
                //  numOfQuestionsUntilNow--;
                lastAnswer = "";
                Question q = getQuestion(question, checkAnswers);
                listOfQuestions.remove(q);
                if (Integer.parseInt(scoreLabel.getText()) <= 0 && passWheel > 1) //???
                    passButton.setEnabled(false);
                if (passWheel > 1) {
                    scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) - 1));
                    if (Integer.parseInt(scoreLabel.getText()) <= 0 && passWheel > 1) //???
                        passButton.setEnabled(false);
                }
                if (listOfQuestions.size() > 0)
                    nextQuestion();
                else {
                    GUIUtils.showInfoDialog(shell, "GAME OVER", "Your final score is " + scoreLabel.getText() + " after " + numOfQuestionsUntilNow + " questions.");
                    numOfWrongAnswer = 0;
                    numOfQuestionsUntilNow = 0;
                    gameOver = true;
                }

            }
        });

        // create the "50-50" button to show fewer answer options
        fiftyFiftyButton = new Button(questionPanel, SWT.PUSH);
        fiftyFiftyButton.setText("50-50");
        data = new GridData(GridData.BEGINNING, GridData.CENTER, true,
                false);
        data.horizontalSpan = 1;
        fiftyFiftyButton.setLayoutData(data);
        if (Integer.parseInt(scoreLabel.getText()) <= 0 && fiftyFiftyWheel != 0) // Before the press
            fiftyFiftyButton.setEnabled(false);
        fiftyFiftyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) { //Q5
                fiftyFiftyButton.setEnabled(false);
                Random rand = new Random();
                int nxt = rand.nextInt(4); //3 wrong answer
                Question currQ = getQuestion(question, answers);
                String wrongAns = currQ.getListOfAnswers().get(nxt);
                while (currQ.getListOfAnswers().get(nxt).equals(currQ.getCorrectAnswer())) {
                    nxt = rand.nextInt(4);
                    wrongAns = currQ.getListOfAnswers().get(nxt);
                }
                for (int i = 0; i < 4; i++) {
                    if (!currQ.getListOfAnswers().get(i).equals(currQ.getCorrectAnswer()) && !wrongAns.equals(currQ.getListOfAnswers().get(i)))

                        answerButtons.get(i).setEnabled(false);


                }
                if (fiftyFiftyWheel >= 1)
                    scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) - 1));

                fiftyFiftyWheel++;


            }
        });

        // two operations to make the new widgets display properly
        questionPanel.pack();
        questionPanel.getParent().layout();
    }

    private Question getQuestion(String q, List<String> answers) {
        Question question;
        for (int i = 0; i < this.listOfQuestions.size(); i++) {
            question = this.listOfQuestions.get(i);
            if (question.getQuestion().equals(q)) {
                if (answers.containsAll(this.listOfQuestions.get(i).getListOfAnswers()))
                    return question;
            }
        }
        return null;
    }

    private void nextQuestion() {
        if (listOfQuestions.size() > 0) {
            Random ran = new Random();
            int nxt = ran.nextInt(listOfQuestions.size());
            Collections.shuffle(listOfQuestions.get(nxt).getListOfAnswers());
            updateQuestionPanel(listOfQuestions.get(nxt).getQuestion(), listOfQuestions.get(nxt).getListOfAnswers());

        }

    }

    private List<String> createListOfAns(List<Button> answerButtons) {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < answerButtons.size(); i++) {
            answers.add(answerButtons.get(i).getText());
        }
        return answers;
    }


    /**
     * Opens the main window and executes the event loop of the application
     */
    private void runApplication() {
        shell.open();
        Display display = shell.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        boldFont.dispose();
    }
}
