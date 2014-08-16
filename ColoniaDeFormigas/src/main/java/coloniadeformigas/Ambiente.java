
package coloniadeformigas;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Ambiente {

    
    
    static int raio_de_visao, qt_fmg_mortas,qt_fmg_vivas, dimensao_ambiente;
    static Celula grid[][];

    
    
    Ambiente(int dimensao_ambiente, int raio_de_visao, int qt_fmg_mortas, int qt_fmg_vivas) {
        this.dimensao_ambiente = dimensao_ambiente;
        this.raio_de_visao = raio_de_visao;
        this.qt_fmg_mortas = qt_fmg_mortas;
        this.qt_fmg_vivas = qt_fmg_vivas;
        this.grid = new Celula[dimensao_ambiente][dimensao_ambiente];
        limpaGrid();
    }
    
    void iniciaAmbiente(boolean rand_fmg_mortas, boolean rand_fmg_vivas) {
        long seed;
        
        if(rand_fmg_mortas == true)
            seed = System.currentTimeMillis();
        else
            seed = 42;
        
        geraGridAleatorio(seed);
        
        if(rand_fmg_vivas == true)
            seed = System.currentTimeMillis();
        else
            seed = 42;
        
        espalha_formigas(seed);
        
        VisualizadorGrid janela = new VisualizadorGrid(dimensao_ambiente);
        janela.inicializaGrid(grid);
        janela.setSize(new Dimension(800,800));
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
        
        janela.setTitle("Ant Clustering");
    }
    
    private void limpaGrid() {
        for(int i=0;i<dimensao_ambiente;i++)
            Arrays.fill(grid[i],Celula.VAZIA);
    }

    private void geraGridAleatorio(long seed) {
        
        Random generator = new Random(seed);
        int numero_randomico, linha, coluna, numeroDaCelula;
        for(int i = 0; i<qt_fmg_mortas; i++)
        {
            numero_randomico = generator.nextInt(dimensao_ambiente*dimensao_ambiente);
            linha = numero_randomico/dimensao_ambiente;
            coluna = numero_randomico%dimensao_ambiente;
            Celula celulaAtual = grid[linha][coluna];
            
            if(Celula.FORMIGA_MORTA.equals(celulaAtual))
            {
                numeroDaCelula = procuraProximaCelulaLivre(linha,coluna);
                linha = numeroDaCelula/dimensao_ambiente;
                coluna = numeroDaCelula%dimensao_ambiente;
            }
            
            grid[linha][coluna] = Celula.FORMIGA_MORTA;
        }
    }

    private int procuraProximaCelulaLivre(int linha,int coluna) {
        for(int i=linha; i<dimensao_ambiente;i++)
        {
            for(int j=coluna;j<dimensao_ambiente;j++)
            {
                if(Celula.VAZIA.equals(grid[i][j]))
                {
                    return i*dimensao_ambiente + j;
                }
            }
        }
        //Caso nao ache espaço vazio ali, começa do inicio
        for(int i=0; i<dimensao_ambiente;i++)
        {
            for(int j=0;j<dimensao_ambiente;j++)
            {
                if(Celula.VAZIA.equals(grid[i][j]))
                {
                    return i*dimensao_ambiente + j;
                }
            }
        }
        return 0;
    }
    
    private void espalha_formigas(long seed) {
        Random generator = new Random(seed);
        int numero_randomico, linha, coluna;
        boolean estaCarregando;
        for(int i = 0; i<qt_fmg_vivas; i++)
        {
            numero_randomico = generator.nextInt(dimensao_ambiente*dimensao_ambiente);
            linha = numero_randomico/dimensao_ambiente;
            coluna = numero_randomico%dimensao_ambiente;
            Celula celulaAtual = grid[linha][coluna];
            
            if(Celula.FORMIGA_MORTA.equals(celulaAtual))
            {
                grid[linha][coluna] = Celula.FORMIGA_CARREGANDO;
                estaCarregando = true;
            }
            else
            {
                grid[linha][coluna] = Celula.FORMIGA_PROCURANDO;
                estaCarregando = false;
            }
            
            celulaAtual = grid[linha][coluna];
            Formiga formiga = new Formiga(linha,coluna,raio_de_visao,estaCarregando);
            Thread threadFormiga = new Thread(formiga);
            threadFormiga.start();
        }
    
    }
    
    static synchronized void setGrid(int linha, int coluna, Celula estadoFormiga) {
        grid[linha][coluna] = estadoFormiga;
        
        VisualizadorGrid.trocaCorCelula(linha, coluna, estadoFormiga);
    }
    
    static int getDimensao() {
        return dimensao_ambiente;
    }
    
    static Celula getGrid(int l, int c) {
        return grid[l][c];
    }
}
