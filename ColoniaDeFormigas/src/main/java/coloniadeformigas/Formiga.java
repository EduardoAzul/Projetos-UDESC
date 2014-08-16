 package coloniadeformigas;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Formiga extends javax.swing.JFrame implements Runnable{

    /*
     * 0 - Norte
     * 1 - Sul
     * 2 - Oeste
     * 3 - Leste
     */
    int linha, coluna, direcao;
    static int raioDeVisao;
    boolean estaCarregandoCorpo, temMortoNoChao, altaComplexidade;
    Random geradorAleatorio = new Random(System.currentTimeMillis());
    
    Formiga(int linha, int coluna, int raio, boolean estadoDaFormiga) {
        this.linha = linha;
        this.coluna = coluna;
        this.estaCarregandoCorpo = estadoDaFormiga;
        this.direcao = geraAleatorio(4);
        this.temMortoNoChao = false;
        raioDeVisao = raio;
    }
    
    public void run(){
        while(VisualizadorGrid.janelaEstaPronta() == false)
        {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(Formiga.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while(true)
        {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(Formiga.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(estaCarregandoCorpo)
            {
                procuraLugarParaLargarCorpo();
            }
            else
            {
                procuraCorpo();
            }
        }
    }
    
    private void procuraCorpo() {
        andaRandomicamente();
        if(/*altaComplexidade == true &&*/ temMortoNoChao == true && devePegarCorpo() == true)
        {
            pegarCorpo();
        }
    }
    
    private void procuraLugarParaLargarCorpo() {
        andaRandomicamente();
        if(/*altaComplexidade == true && */temMortoNoChao == false && deveLargarCorpo() == true)
        {
            largarCorpo();
        }
    }

    private void andaRandomicamente() {
        if(temAltaComplexidade()) 
            mudarDirecao();
        while(anda() == false){};
    }

    private boolean devePegarCorpo() {
        int proporcao, aleatorio, resultadoFormula,k;
        proporcao = proporcaoMortosPorArea()+20;
        aleatorio = geraAleatorio(100) + 1;
        if(proporcao < aleatorio)
            return true;       
        return false;
    }
    
    private synchronized int proporcaoMortosPorArea() {
        int qtCelulasEmVolta = 0;
        int iniLinha, iniColuna, fimLinha, fimColuna, dist;
        double proporcao = 0, qtMortos = 0;
        
        iniLinha = linha - raioDeVisao;
        fimLinha = linha + raioDeVisao;
        iniColuna = coluna - raioDeVisao;
        fimColuna = coluna + raioDeVisao;
        
        if(iniLinha < 0)
            iniLinha = 0;
        if(iniColuna < 0)
            iniColuna = 0;
        if(fimLinha > Ambiente.getDimensao() - 1)
            fimLinha = Ambiente.getDimensao() - 1;
        if(fimColuna > Ambiente.getDimensao() - 1)
            fimColuna = Ambiente.getDimensao() - 1;
        
        qtCelulasEmVolta = (fimLinha-iniLinha+1)*(fimColuna-iniColuna+1);
        
        for(int i = iniLinha; i <= fimLinha; i++)
        {
            for(int j = iniColuna; j <= fimColuna; j++)
            {
                if(Celula.FORMIGA_MORTA.equals(Ambiente.getGrid(i, j)))
                {
                    dist = Math.max(Math.abs(linha - i), Math.abs(coluna-j));
                    qtMortos += (2.5 - (float)dist/(raioDeVisao/2))*(2.5 - (float)dist/(raioDeVisao/2));                    
                }
            }
        }
        proporcao += (100*qtMortos)/qtCelulasEmVolta;
        return (int)proporcao;
    }

    private void pegarCorpo() {
        temMortoNoChao = false;
        estaCarregandoCorpo = true;
        Ambiente.setGrid(linha, coluna, Celula.FORMIGA_CARREGANDO);
    }
    
    private int geraAleatorio(int max){
        return geradorAleatorio.nextInt(max);
    }

    private void mudarDirecao() {
        int numRand = geraAleatorio(4);
        
        if(numRand == direcao)
            direcao = (numRand+1)%4;
        else
            direcao = numRand;
        
    }

    private synchronized boolean anda() {
        int novaLinha, novaColuna;
        novaLinha = linha;
        novaColuna = coluna;
        switch(direcao)
        {
            case 0:
                novaLinha--;
                break;
            case 1:
                novaLinha++;
                break;
            case 2:
                novaColuna--;
                break;
            case 3:
                novaColuna++;
                break;
        }
        if(iraSairDoGrid(novaLinha, novaColuna) || iraPisarEmFormigaViva(novaLinha,novaColuna))
        {
            mudarDirecao();
            return false;
        }
                
        if(temMortoNoChao)
            Ambiente.setGrid(linha, coluna, Celula.FORMIGA_MORTA);
        else
            Ambiente.setGrid(linha, coluna, Celula.VAZIA);
        
        if(Celula.FORMIGA_MORTA.equals(Ambiente.getGrid(novaLinha, novaColuna)))
            temMortoNoChao = true;
        else
            temMortoNoChao = false;
        
        Ambiente.setGrid(novaLinha, novaColuna, estadoFormiga());
        
        linha = novaLinha;
        coluna = novaColuna;
        
        return true;
    }
    
    private boolean iraSairDoGrid(int l, int c){
        if(l < 0 || l > Ambiente.getDimensao()-1 || c < 0 || c > Ambiente.getDimensao()-1)
        {
            return true;
        }
        return false;
    }

    private boolean iraPisarEmFormigaViva(int l,int c) {
        if(Celula.FORMIGA_CARREGANDO.equals(Ambiente.getGrid(l,c)))
            return true;
        if(Celula.FORMIGA_PROCURANDO.equals(Ambiente.getGrid(l,c)))
            return true;
        return false;
    }
        
    private Celula estadoFormiga(){
        if(estaCarregandoCorpo == true)
            return Celula.FORMIGA_CARREGANDO;
        return Celula.FORMIGA_PROCURANDO;
    }

    private boolean deveLargarCorpo() {
        int proporcao, aleatorio, resultadoFormula,k;
        proporcao = proporcaoMortosPorArea()-12;
        aleatorio = geraAleatorio(100) + 1;
        if(proporcao > aleatorio)
            return true;
        return false;
    }

    private void largarCorpo() {
        temMortoNoChao = true;
        estaCarregandoCorpo = false;
        Ambiente.setGrid(linha, coluna, Celula.FORMIGA_PROCURANDO);
    }

    private boolean temAltaComplexidade() {
        /*if(estaNaBorda(linha, coluna))
        {
        if(geraAleatorio(2) == 0)
        return true;
        else
        return false;
        }*/
        int i,c=0, be,bd,bs,bi;
        be=bd=bs=bi=1;
        if(coluna==0) be=0;
        if(coluna==Ambiente.getDimensao() - 1) bd=0;
        if(linha==0) bs=0;
        if(linha==Ambiente.getDimensao() - 1) bi=0;
        
        for(i=linha-bs;i<=linha+bi;i++)
        {
            if(be == 1 && Ambiente.getGrid(i, coluna-1) != Ambiente.getGrid(i, coluna))
                c++; //pega essa Java, ba dum tss
            if(bd == 1 && Ambiente.getGrid(i, coluna) != Ambiente.getGrid(i, coluna+1))
                c++; //:)
        }
        for(i=coluna-be;i<=coluna+bd;i++)
        {
            if(bs == 1 && Ambiente.getGrid(linha-1, i) != Ambiente.getGrid(linha, i))
                c++; //pega essa Java, ba dum tss
            if(bi == 1 && Ambiente.getGrid(linha, i) != Ambiente.getGrid(linha+1, i))
                c++; //:)
        }
        
        if(geraAleatorio(13) > 12 - c)
            altaComplexidade = true;
        else
            altaComplexidade = false;
        return altaComplexidade;
    }

    private boolean estaNaBorda(int l, int c) {
        if(l == 0 || l == Ambiente.getDimensao()-1 || c == 0 || c == Ambiente.getDimensao()-1)
        {
            return true;
        }
        return false;
    }
    
}
