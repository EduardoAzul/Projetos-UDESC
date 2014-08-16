package coloniadeformigas;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

public class Inicializador {

    static int raioDeVisao, qt_fmg_mortas, qt_fmg_vivas, dimensao_ambiente;
    static boolean rand_fmg_mortas = false, rand_fmg_vivas = false;

    public static void inicializa() {

        leituraDeDados();

        Ambiente amb = new Ambiente(dimensao_ambiente, raioDeVisao, qt_fmg_mortas, qt_fmg_vivas);
        amb.iniciaAmbiente(rand_fmg_mortas, rand_fmg_vivas);

    }

    private static void leituraDeDados() throws NumberFormatException, HeadlessException {
        dimensao_ambiente = solicitaInteiro("Insira a dimensão do ambiente: ");
        raioDeVisao = solicitaInteiro("Insira o raio de visão das formigas: ");
        
        do
        {
            qt_fmg_mortas = solicitaInteiro("Insira a quantidade de formigas mortas: ");
            qt_fmg_vivas = solicitaInteiro("Insira a quantidade de formigas vivas: ");
            if(!formigasCabemNoGrid())
                exibeMensagem("A quantidade total de formigas ultrapassa o limite do ambiente");
        }while(!formigasCabemNoGrid());
        
        int resp = solicitaSimNao("Você deseja que as formigas mortas sejam espalhadas aleatoriamente?");
        if (resp == JOptionPane.YES_OPTION)
        {
            rand_fmg_mortas = true;
        }
        else
        {
            resp = solicitaSimNao("Você deseja que as formigas vivas sejam espalhadas aleatoriamente?");
            if (resp == JOptionPane.YES_OPTION)
                rand_fmg_vivas = true;
        }
    }
    
    public static int solicitaInteiro(String msg){
        return Integer.parseInt(JOptionPane.showInputDialog(msg));
    }
    
    public static void exibeMensagem(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public static int solicitaSimNao(String msg){
        return JOptionPane.showConfirmDialog(null, msg, "", JOptionPane.YES_NO_OPTION);
    }
    
    public static boolean formigasCabemNoGrid()
    {
        if(qt_fmg_mortas+qt_fmg_vivas > dimensao_ambiente*dimensao_ambiente)
            return false;
        return true;
    }
}
