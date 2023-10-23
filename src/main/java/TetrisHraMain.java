import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TetrisHra extends Hra {

    public TetrisHra() {
        setTitle("Hra TETRIS");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        hraciaPlocha = new int[30][10];
        skore = 0;
        pozastavenie = false;

// inicializácia tetromina
        tetrominoX = 4;
        tetrominoY = 0;

// inicializácia aktualneho tetromina
        aktualneTetromino = generateRandomTetromino();
        aktFarba = Color.BLUE;

// časovač pre pohyb tetromina smerom dole
        casovac = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!pozastavenie) {
                    posunTetrominaDole();
                }
            }
        });
        casovac.start();
            }

    public void inicializujDocasnePlatno() {
        docasnePlatno = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY); // tu sa vytvára nova instancia s nazvom BufferedImage → určuje formát obrazku
        grafickyKontextDocasPlatna = docasnePlatno.createGraphics();
    }

    public void paint(Graphics g) {
        if (docasnePlatno == null) { // vola sa metoda inicializujDocasnePlatno
            inicializujDocasnePlatno();
        }

        grafickyKontextDocasPlatna.clearRect(0, 0, getWidth(), getHeight()); // vyčistí sa herné plátno

//Vykreslenie hernej plochy
        for (int riadok = 0; riadok < 20; riadok++) {
            for (int stlpec = 0; stlpec < 10; stlpec++) {
                int farba = hraciaPlocha[riadok][stlpec];
                if (farba == 0) {
                    grafickyKontextDocasPlatna.setColor(Color.ORANGE);
                } else {
                    grafickyKontextDocasPlatna.setColor(aktFarba);
                }
                grafickyKontextDocasPlatna.fillRect(stlpec * 30, riadok * 30, 30, 30);
                grafickyKontextDocasPlatna.setColor(Color.BLACK);
                grafickyKontextDocasPlatna.drawRect(stlpec * 30, riadok * 30, 30, 30);
            }
        }
// zakladne Tetromino
        grafickyKontextDocasPlatna.setColor(aktFarba); // farba aktualneho tetromina
        for (int riadok = 0; riadok < aktualneTetromino.length; riadok++) {
            for (int stlpec = 0; stlpec < aktualneTetromino[0].length; stlpec++) {
                if (aktualneTetromino[riadok][stlpec] == 1) {
                    grafickyKontextDocasPlatna.fillRect((tetrominoX + stlpec) * 30, (tetrominoY + riadok) * 30, 30, 30);
                }
            }
        }

// skore
        grafickyKontextDocasPlatna.setColor(Color.BLACK);
        grafickyKontextDocasPlatna.setFont(new Font("ARIAL", Font.PLAIN, 18));
        grafickyKontextDocasPlatna.drawString("Aktuálne skóre: " + skore, 30, 50);

        g.drawImage(docasnePlatno, 0, 0, this);
    }

    public int[][] generateRandomTetromino() {
        int[][] tetromino;
        Random random = new Random();
        int randomTetromino = random.nextInt(7);

        switch (randomTetromino) {
            case 0: // I-Tetromino
                tetromino = new int[][]{{1, 1, 1, 1}};
                aktFarba = Color.BLACK;
                break;
            case 1: // J-Tetromino
                tetromino = new int[][]{{1, 0, 0}, {1, 1, 1}};
                aktFarba = Color.BLACK;
                break;
            case 2: // L-Tetromino
                tetromino = new int[][]{{0, 0, 1}, {1, 1, 1}};
                aktFarba = Color.BLACK;
                break;
            case 3: // O-Tetromino
                tetromino = new int[][]{{1, 1}, {1, 1}};
                aktFarba = Color.BLACK;
                break;
            case 4: // S-Tetromino
                tetromino = new int[][]{{0, 1, 1}, {1, 1, 0}};
                aktFarba = Color.BLACK;
                break;
            case 5: // T-Tetromino
                tetromino = new int[][]{{0, 1, 0}, {1, 1, 1}};
                aktFarba = Color.BLACK;
                break;
            case 6: // Z-Tetromino
                tetromino = new int[][]{{1, 1, 0}, {0, 1, 1}};
                aktFarba = Color.BLACK;
                break;
            default:
                tetromino = new int[][]{{1, 1, 1, 1}};
                aktFarba = Color.BLACK;
                break;
        }
        return tetromino;
    }
// overenie, ci je mozne presunut tetromino na novu pozíciu v hernej ploche
    public boolean moznostiPresunu(int noveX, int noveY, int[][] tetromino) {
        for (int riadokVtetromine = 0; riadokVtetromine < tetromino.length; riadokVtetromine++) {
            for (int stlpecVtetromine = 0; stlpecVtetromine < tetromino[0].length; stlpecVtetromine++) {
                if (tetromino[riadokVtetromine][stlpecVtetromine] != 0) {
                    int novaHrotizontalnaPozicia = noveX + stlpecVtetromine;
                    int novaVertikalnaPozicia = noveY + riadokVtetromine;
                    if (novaHrotizontalnaPozicia < 0 || novaHrotizontalnaPozicia >= 10 || novaVertikalnaPozicia >= 20 || hraciaPlocha[novaVertikalnaPozicia][novaHrotizontalnaPozicia] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void posunTetrominaDoLava() {
        if (moznostiPresunu(tetrominoX - 1, tetrominoY, aktualneTetromino)) {
            tetrominoX--;
            repaint();
        }
    }

    public void posunTetrominaDoPrava() {
        if (moznostiPresunu(tetrominoX + 1, tetrominoY, aktualneTetromino)) {
            tetrominoX++;
            repaint();
        }
    }

    public void posunTetrominaDole() {
        if (moznostiPresunu(tetrominoX, tetrominoY + 1, aktualneTetromino)) {
            tetrominoY++;
            repaint(); // prekreslenie hernej plochy
        } else {
            umiestnenieTetromina();
        }
    }
// volanie metody, ked je tetromino dole alebo sa dotkne iných tetromin
    public void umiestnenieTetromina() {
        for (int riadok = 0; riadok < aktualneTetromino.length; riadok++) {
            for (int stlpec = 0; stlpec < aktualneTetromino[0].length; stlpec++) {
                if (aktualneTetromino[riadok][stlpec] != 0) {
                    int plochaX = tetrominoX + stlpec;
                    int plochaY = tetrominoY + riadok;
                    hraciaPlocha[plochaY][plochaX] = 1;
                }
            }
        }
        vyprazdniRiadky();
        tetrominoX = 4;
        tetrominoY = 0;
        aktualneTetromino = generateRandomTetromino();
        repaint();
    }

    private void vyprazdniRiadky() {
        for (int riadok = 19; riadok >= 0; riadok--) {
            boolean jePlny = true;
            for (int stlpec = 0; stlpec < 10; stlpec++) {
                if (hraciaPlocha[riadok][stlpec] == 0) {
                    jePlny = false;
                    break;
                }
            }
            if (jePlny) {
                for (int r = riadok; r > 0; r--) {
                    for (int s = 0; s < 10; s++) {
                        hraciaPlocha[r][s] = hraciaPlocha[r - 1][s];
                    }
                }
                skore += 100;
                riadok++;
            }
        }
    }

    public void otocenieTetnorina() {
        int[][] rotatedTetromino = new int[aktualneTetromino[0].length][aktualneTetromino.length];
        for (int row = 0; row < aktualneTetromino.length; row++) {
            for (int col = 0; col < aktualneTetromino[0].length; col++) {
                rotatedTetromino[col][aktualneTetromino.length - 1 - row] = aktualneTetromino[row][col];
            }
        }
        if (moznostiPresunu(tetrominoX, tetrominoY, rotatedTetromino)) {
            aktualneTetromino = rotatedTetromino;
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // lambda vyraz
            TetrisHra tetris = new TetrisHra();
            tetris.setVisible(true);


// TODO nastavit tlacitka tak, aby boli stále zobrazené + upratať
// Tlačítka pre pohyb doľava, doprava, otocenie
            JButton tlacitkoDolava = new JButton("←");
            tlacitkoDolava.addActionListener(e -> tetris.posunTetrominaDoLava());

            JButton tlacitkoDoprava = new JButton("→");
            tlacitkoDoprava.addActionListener(e -> tetris.posunTetrominaDoPrava());

            JButton tlacitkoDole = new JButton("↓");
            tlacitkoDole.addActionListener(e -> tetris.posunTetrominaDole());

            JButton tlacitkoOtocenie = new JButton("OTOCENIE");
            tlacitkoOtocenie.addActionListener(e -> tetris.otocenieTetnorina());

            JButton tlacitkoStartStop = new JButton("Start/Stop");
            tlacitkoStartStop.addActionListener(e -> tetris.spustenieStopnutieHry());



            // Panel pre tlačítka
            JPanel panelPreTlacitka = new JPanel();
            panelPreTlacitka.add(tlacitkoDolava);
            panelPreTlacitka.add(tlacitkoOtocenie);
            panelPreTlacitka.add(tlacitkoDole);
            panelPreTlacitka.add(tlacitkoDoprava);
            panelPreTlacitka.add(tlacitkoStartStop);




            JPanel mojPanel = new JPanel(new BorderLayout());
            mojPanel.add(panelPreTlacitka, BorderLayout.SOUTH);


            tetris.add(mojPanel);
            tetris.repaint();
        });
    }
    private void spustenieStopnutieHry(){
        hraSpustena = !hraSpustena;
        if (hraSpustena) {
            casovac.start();
        } else {
            casovac.stop();
        }
    }
}
