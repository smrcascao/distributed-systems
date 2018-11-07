/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_p3_g3_t1;
import Entidades.*;
import Monitores.*;
import genclass.FileOp;
import genclass.GenericIO;
/**
 *
 * @author smrcascao
 */
public class SD_P3_G3_T1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     
    monGeral geral;                                     // repositorio geral
    monLoja loja;                                       // loja
    monOficina oficina;                                 // Oficina
    monArmazemPM armazemPM;                             // armazem de PM
     

    int nCustomer = 3;                                  // Número de clientes que frequentam a Loja
    int nArts = 3;                                      // Número de artesaos que frequentam a Oficina
    int totalPM = 500;                                  // total de PM no sistema no instante inicial
    String fName;                                       // nome do ficheiro de logging
  
    int nMaximoProdutosLoja = 30;                       // Limite maximo de produtos em exposicao na loja
    int precoProduto = 10;                              // preco de cada produto   
    
    
    int alarmeProd=50;                                  // Limite de Alarme da quantidade de produtos
    int limiteProd=200;                                 // Limite maximo de produtos
    int alarmePM=5;                                     // limite minimo de PM
    int prodPM=1;                                       // PM para construir um produto
    int fornPM=100;                                     // PM recebida por fornecimento (por defeito)
    int prodDona=25;                                    // quantidade de produtos que a dona leva para a loja (por defeito)
    
    
    int preco = 5;                                          // preco da unidade de PM
    

    
    boolean success;                                     // validação de dados de entrada
    char opt;                                            // opção

    Cliente [] clientes = new Cliente[nCustomer];        // array de threads cliente
    Artesao [] artesaos = new Artesao[nArts];            // array de threads artesao
    Dona dona;
    
    
    GenericIO.writelnString ("\n" + "      Problema Obrigatorio 1\n");
    GenericIO.writelnString ("Numero de clientes = 3 ");
    GenericIO.writelnString ("Numero de artesaos = 3 ");
    do
    { 
        GenericIO.writeString ("Nome do ficheiro de armazenamento da simulação? ");
        fName = GenericIO.readlnString ();
        if (FileOp.exists (".", fName))
        {
            do
            {
                GenericIO.writeString ("Já existe um directório/ficheiro com esse nome. Quer apagá-lo (s - sim; n - não)? ");
                opt = GenericIO.readlnChar ();
            }while ((opt != 's') && (opt != 'n'));
            if (opt == 's')
                success = true;    
            else success = false;
        }
        else
            success = true;
      }while (!success);
      
    //introducao de dados
    GenericIO.writelnString("Quantidade total de PM no sistema? (se introduzir um valor igual ou inferiror a 0 = 500)");
    totalPM = GenericIO.readlnInt();
    if (totalPM<=0)
        totalPM=500;
    
    GenericIO.writelnString("Limite maximo de produtos na Oficina? (se introduzir um valor igual ou inferiror a 0 = 200)");
    limiteProd = GenericIO.readlnInt();
    if (limiteProd<=0)
        limiteProd=200;
    
    GenericIO.writelnString("Limite de Alarme da quantidade de produtos na Oficina? (se introduzir um valor igual ou inferiror a 0 ou superior ao limite maximo = 50)");
    alarmeProd = GenericIO.readlnInt();
    if (alarmeProd<=0 || alarmeProd>limiteProd)
        alarmeProd=50;
     
    GenericIO.writelnString("Limite minimo de PM na Oficina? (se introduzir um valor igual ou inferiror a 0 = 5)");
    alarmePM = GenericIO.readlnInt();
    if (alarmePM<=0)
        alarmePM=5;
    
    GenericIO.writelnString("PM para construir um produto? (se introduzir um valor igual ou inferiror a 0 = 1)");
    prodPM = GenericIO.readlnInt();
    if (prodPM<=0)
        prodPM=1;
    
/*    GenericIO.writelnString("PM recebida por fornecimento (por defeito)? (se introduzir um valor igual ou inferiror a 0 = 100)");
    fornPM = GenericIO.readlnInt();
    if (fornPM<=0)
        fornPM=100;*/
    
    GenericIO.writelnString("quantidade de produtos que a dona leva para a loja (por defeito)? (se introduzir um valor igual ou inferiror a 0 = 25)");
    prodDona = GenericIO.readlnInt();
    if (prodDona<=0)
        prodDona=25;
    
    //inicializacao dos repositorios
    geral = new monGeral(nArts, nCustomer, totalPM, prodPM, fName);                //monGeral(int numArtesaos, int numClientes, int totalPM, String fileName)
    loja = new monLoja(nMaximoProdutosLoja, precoProduto);                  //monLoja(int nMaximoProdutosLoja, int precoProduto)
    oficina = new monOficina(alarmeProd, limiteProd, alarmePM, prodPM, fornPM, prodDona);                       //monOficina(int alarmeProd, int limiteProd, int alarmePM, int prodPM, int fornPM, prodDona)
    armazemPM = new monArmazemPM(totalPM, preco, fornPM);                             //monArmazemPM(int stockInicialPM, int preco, int PMPorFornecimento)
       
    
    //inicializacao das entidades
    dona = new Dona(geral, loja, oficina, armazemPM);
    
    for (int i = 0; i < nCustomer; i++)
        clientes[i] = new Cliente(i,geral,loja);  
       
    for (int i = 0; i < 3; i++)
        artesaos[i] = new Artesao(i, geral, oficina);  
       
    //inicio 
    dona.start();
    for (int i = 0; i < nArts; i++)
        artesaos[i].start();
    for (int i = 0; i < nCustomer; i++)
        clientes[i].start();
       
       
    //espera pelo fim da vida dos artesaos
       for (int i = 0; i < nArts; i++)
      { try
        { artesaos[i].join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("o artesao " + i + " terminou.");
      }
      GenericIO.writelnString ();
      
    //espera pelo fim da dvida dos clientes  
      for (int i = 0; i < nCustomer; i++)
      { try
        { clientes[i].join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("o cliente " + i + " terminou.");
      }
      GenericIO.writelnString ();
       
    //espera pelo fim da vida da dona  
      try
       { 
           dona.join();
       }
       catch (InterruptedException e) {GenericIO.writelnString ("erro");}
       GenericIO.writelnString ("a thread dona terminou.");
       
            
       
      
    }
      
    
}
