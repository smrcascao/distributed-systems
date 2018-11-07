
package VectorTime;

import java.io.Serializable;

/**
 * Relogio vectorial.
 * @author rofler
 */
public class VectorTime implements Serializable{
    //variaveis
    /**
     * @serial timeStamp
     */
    private int[] timeStamp;
    
    /**
     * @serial localIndex
     */
    private int localIndex;
    
    /**
   *  Construtor (Instanciação do relogio).
   * 
    * @param size tamanho do array timeStamp
   *  @param localIndex indice inicial do array
   */
    public VectorTime(int size, int localIndex)
    {
        this.timeStamp=new int[size];
        
        for(int i=0; i<this.timeStamp.length; i++)
            this.timeStamp[i]=0;
        
        this.localIndex=localIndex;
    }
    
    /**
   *  Incrementaçao da posiçao actual do array.
   * 
   */
    public synchronized void increments()
    {
        this.timeStamp[this.localIndex]++;
                
    }
    
    /**
   *  actualizaçao do relogio.
   * 
   * @param newVector relogio actual
   */
    public synchronized void update(VectorTime newVector)
    {
        this.timeStamp=newVector.toIntArray();
        this.localIndex=newVector.getLocalIndex();
    }
    
    /**
   *  funçao usada para receber uma copia integral do relogio vectorial na situaçao actual.
   * 
   * @return devolve uma copia integral do relogio vectorial.
   */
    public synchronized VectorTime getCopy() throws CloneNotSupportedException
    {/*
        VectorTime newVector = new VectorTime(this.timeStamp.length, localIndex);
        
        int[] array = toIntArray();
        
        return newVector;*/
        
        return (VectorTime) super.clone();
    }
    
    /**
   *  funçao usada para receber o array de inteiros timeStamp.
   * 
   * @return devolve o array de inteiros timeStamp.
   */
    public synchronized int[] toIntArray()
    {
        int[] array = new int[7];
        
        System.arraycopy(this.timeStamp, 0, array, 0, 7);
        
        return array;
    }

    /**
   *  funçao usada para receber o indice actual do array de inteiros timeStamp.
   * 
   * @return devolve o indice actual do array de inteiros timeStamp.
   */
    public synchronized int getLocalIndex() {
        return localIndex;
    }
    
    
}
