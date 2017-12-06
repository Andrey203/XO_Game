/**
 * Created by Администратор on 04.11.2017.
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
public class XO_Game extends Applet implements ActionListener {
    Button cells[];
    Button newGameButton;
    Label score, youWon, youLost;
    int emptyCells = 9;
    String quantityWon = "0";
    String quantityLost = "0";
    /**
     * init – конструктор апплета
     */
    public void init() {
        this.setLayout( new BorderLayout() );
        this.setBackground( Color.YELLOW );
// Изменяем шрифт и размер апплета
        Font appletFont = new Font("Monospased", Font.BOLD, 20);
        this.setFont( appletFont );
        newGameButton = new Button("New Game");
        newGameButton.addActionListener(this);
        Panel topPanel = new Panel();
        topPanel.setLayout(new BorderLayout() );
        topPanel.add( newGameButton, "North" );
        this.add(topPanel, "North");
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new GridLayout(3, 3));
        this.add(centerPanel, "Center");
        score = new Label("Your turn!");
        this.add(score, "South");
        youWon = new Label(quantityWon);
        topPanel.add( youWon, "West");
        youLost = new Label(quantityLost);
        topPanel.add( youLost, "East");

        cells = new Button[9];
        for(int i=0; i<9; i++) {
            cells[i]=new Button();
            cells[i].addActionListener(this);
            cells[i].setBackground(Color.ORANGE);
            centerPanel.add(cells[i]);
        }
    }
    /**
     * Обрабатываем события
     */
    public void actionPerformed(ActionEvent e) {
        Button theButton = (Button) e.getSource();
        if (theButton == newGameButton){
            for(int i=0; i<9; i++){
                cells[i].setEnabled(true);
                cells[i].setLabel("");
                cells[i].setBackground(Color.ORANGE);
            }
            emptyCells = 9;
            score.setText("Your turn!");
            newGameButton.setEnabled(false);
            return;
        }
        String winner = "";

        for ( int i=0; i<9; i++ ) {
            if ( theButton == cells[i] ) {
                cells[i].setLabel("X");
                cells[i].setEnabled(false);
                winner = lookWinner();
                if(!"".equals(winner)){
                    theEndGame();
                } else {
                    compMove();
                    winner = lookWinner();
                    if ( !"".equals(winner)){
                        theEndGame();
                    }
                }
                break;
            }
        }
        if ( winner.equals("X") ) {
            score.setText("You won!");
            quantityWon += quantityWon;
            youWon.setText(quantityWon);
        } else if (winner.equals("O")){
            score.setText("You lost!");
            quantityLost += quantityLost;
            youLost.setText(quantityLost);
        } else if (winner.equals("T")){
            score.setText("It's a tie!");
        }
    }
    /**
     * Вызываем метод после каждого хода, чтобы узнать победителя.
     * Проверяем все ряды, диагонали и колонки, чтобы найти одинаковые символы в трех клетках
     * Возвращаем “X", "O" или "T", если ничья; "" - еще нет победителя
     */
    String lookWinner() {
        String theWinner = "";
        emptyCells--;
        if (emptyCells == 0) {
            return "T";
        }

        if (!cells[0].getLabel().equals("") &&
                cells[0].getLabel().equals(cells[1].getLabel()) &&
                cells[0].getLabel().equals(cells[2].getLabel())) {
            theWinner = cells[0].getLabel();
            lineWinner(0, 1, 2);
        } else if (!cells[3].getLabel().equals("") &&
                cells[3].getLabel().equals(cells[4].getLabel()) &&
                cells[3].getLabel().equals(cells[5].getLabel())) {
            theWinner = cells[3].getLabel();
            lineWinner(3, 4, 5);
        } else if ( ! cells[6].getLabel().equals("") &&
                cells[6].getLabel().equals(cells[7].getLabel()) &&
                cells[6].getLabel().equals(cells[8].getLabel())) {
            theWinner = cells[6].getLabel();
            lineWinner(6, 7, 8);
        } else if ( ! cells[0].getLabel().equals("") &&
                cells[0].getLabel().equals(cells[3].getLabel()) &&
                cells[0].getLabel().equals(cells[6].getLabel())) {
            theWinner = cells[0].getLabel();
            lineWinner(0, 3, 6);
        } else if ( ! cells[1].getLabel().equals("") &&
                cells[1].getLabel().equals(cells[4].getLabel()) &&
                cells[1].getLabel().equals(cells[7].getLabel())) {
            theWinner = cells[1].getLabel();
            lineWinner(1, 4, 7);
        } else if ( ! cells[2].getLabel().equals("") &&
                cells[2].getLabel().equals(cells[5].getLabel()) &&
                cells[2].getLabel().equals(cells[8].getLabel())) {
            theWinner = cells[2].getLabel();
            lineWinner(2, 5, 8);
        } else if ( ! cells[0].getLabel().equals("") &&
                cells[0].getLabel().equals(cells[4].getLabel()) &&
                cells[0].getLabel().equals(cells[8].getLabel())) {
            theWinner = cells[0].getLabel();
            lineWinner(0, 4, 8);
        } else if ( ! cells[2].getLabel().equals("") &&
                cells[2].getLabel().equals(cells[4].getLabel()) &&
                cells[2].getLabel().equals(cells[6].getLabel())) {
            theWinner = cells[2].getLabel();
            lineWinner(2, 4, 6);
        }
        return theWinner;
    }
    /**
     * Этот метод выбирает наилучший ход компа. Если хороший ход
     * не найден, выбирается случайная клетка.
     */
    void compMove() {
        int selectedCell;
// Сначала комп пытается найти клетку рядом со своими двумя
        selectedCell = findEmptyCell("O");
// Если он не находит, ставит нолик рядом с двумя крестиками
        if ( selectedCell == -1 ) {
            selectedCell = findEmptyCell("X");
        }
// если selectedCell все еще равен -1, то занимает клетку по центру
        if ( (selectedCell == -1) && (cells[4].getLabel().equals("")) ) {
            selectedCell = 4;
        }
// если центральная клетка занята, ставит “O”  в случайную клетку
        if ( selectedCell == -1 ) {
            selectedCell = getRandomCell();
        }
        cells[selectedCell].setLabel("O");
    }
    /**
     * Проверяем на наличие одинаковых надписей в двух клетках и одной пустой
     * по всем столбцам, строкам и диагоналям.
     * X – пользователь, O – комп.
     * Возвращаем -1, если двух клеток с одинаковыми символами не найдено,
     * либо количество свободных клеток
     */
    int findEmptyCell(String player) {
        int square[] = new int[9];
        for ( int i = 0; i < 9; i++ ) {
            if ( cells[i].getLabel().equals("O") )
                square[i] = -1;
            else if ( cells[i].getLabel().equals("X") )
                square[i] = 1;
            else
                square[i] = 0;
        }
        int identicalSquares = player.equals("O") ? -2 : 2;

        if ( square[0] + square[1] + square[2] == identicalSquares ) {
            if ( square[0] == 0 )
                return 0;
            else if ( square[1] == 0 )
                return 1;
            else
                return 2;
        }


        if (square[3] + square[4] + square[5] == identicalSquares) {
            if ( square[3] == 0 )
                return 3;
            else if ( square[4] == 0 )
                return 4;
            else
                return 5;
        }

        if (square[6] + square[7] + square[8] == identicalSquares ) {
            if ( square[6] == 0 )
                return 6;
            else if ( square[7] == 0 )
                return 7;
            else
                return 8;
        }

        if (square[0] + square[3] + square[6] == identicalSquares) {
            if ( square[0] == 0 )
                return 0;
            else if ( square[3] == 0 )
                return 3;
            else
                return 6;
        }

        if (square[1] + square[4] + square[7] == identicalSquares ) {
            if ( square[1] == 0 )
                return 1;
            else if ( square[4] == 0 )
                return 4;
            else
                return 7;
        }

        if (square[2] + square[5] + square[8] == identicalSquares ){
            if ( square[2] == 0 )
                return 2;
            else if ( square[5] == 0 )
                return 5;
            else
                return 8;
        }

        if (square[0] + square[4] + square[8] == identicalSquares ){
            if ( square[0] == 0 )
                return 0;
            else if ( square[4] == 0 )
                return 4;
            else
                return 8;
        }

        if (square[2] + square[4] + square[6] == identicalSquares ){
            if ( square[2] == 0 )
                return 2;
            else if ( square[4] == 0 )
                return 4;
            else
                return 6;
        }

        return -1;
    }
    /**
     * Этот метод выбирает любую пустую клетку
     * и возвращает случайно выбранный номер клетки
     */
    int getRandomCell() {
        boolean gotEmptyCell = false;
        int selectedCell = -1;
        do {
            selectedCell = (int) (Math.random() * 9 );
            if (cells[selectedCell].getLabel().equals("")){
                gotEmptyCell = true;
            }
        } while (!gotEmptyCell );
        return selectedCell;
    }
    /**
     * Этот метод выделяет выигравшую линию (три клетки).
     */
    void lineWinner(int winCell1, int winCell2, int winCell3) {
        cells[winCell1].setBackground(Color. YELLOW);
        cells[winCell2].setBackground(Color. YELLOW);
        cells[winCell3].setBackground(Color. YELLOW);
    }

    void theEndGame(){
        newGameButton.setEnabled(true);
        for(int i=0; i<9; i++){
            cells[i].setEnabled(false);
        }
    }
}
