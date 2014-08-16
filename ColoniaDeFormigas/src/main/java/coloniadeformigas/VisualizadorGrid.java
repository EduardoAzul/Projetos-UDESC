package coloniadeformigas;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;


public class VisualizadorGrid extends JFrame{

    static boolean janelaEstaPronta() {
        return estaPronto;
    }
    
    int dimensaoGrid;
    static JLabel gridLabel[][];
    static Color marrom;
    Border borda;
    static boolean estaPronto=false;
    
    public VisualizadorGrid(int dim){
       
        this.dimensaoGrid = dim;
        this.marrom = new Color(131, 60, 0);
        this.borda = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.gridLabel = new JLabel[dimensaoGrid][dimensaoGrid];
        
        setLayout(new GridLayout(dimensaoGrid, dimensaoGrid));
                
    }

    private static  Color escolheCor(Celula celula) {
        switch(celula)
        {
            case FORMIGA_CARREGANDO:
                return Color.RED;
            case FORMIGA_MORTA:
                return Color.BLACK;
            case FORMIGA_PROCURANDO:
                return Color.WHITE;
            default:
                return marrom;
        }
    }

    public void inicializaGrid(Celula grid[][]) {
        for(int i=0;i<dimensaoGrid;i++)
        {
            for(int j=0;j<dimensaoGrid;j++)
            {
                gridLabel[i][j] = new JLabel("");
                gridLabel[i][j].setOpaque(true);
                gridLabel[i][j].setBackground(escolheCor(grid[i][j]));
                gridLabel[i][j].setBorder(borda);
                add(gridLabel[i][j]);
            }
        }
        estaPronto = true;
    }
    
    public static void trocaCorCelula(int linha, int coluna, Celula estadoDestino){
        try
        {
            gridLabel[linha][coluna].setBackground(escolheCor(estadoDestino));
        }
        catch(Exception e)
        {
            System.out.println("TRETA: "+Thread.currentThread().getName()+" "+linha+" "+coluna+ e.toString());
            e.toString();
        }
    }
    
}
