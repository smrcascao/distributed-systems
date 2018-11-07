package VectorTime;

import java.io.Serializable;

/**
 * Relogio vectorial.
 *
 * @author rofler
 */
public class VectorTime implements Serializable {

    //variaveis

    /**
     * Identificador interno para serialização
     */

    private static final long serialVersionUID = 20140404L;

    /**
     * @serial timeStamp
     */
    private int[] timeStamp;

    /**
     * @serial size
     */
    private int size;

    /**
     * @serial localIndex
     */
    private int localIndex;

    /**
     * Construtor (Instanciação do relogio).
     *
     * @param size tamanho do array timeStamp
     * @param localIndex indice inicial do array
     */
    public VectorTime(int size, int localIndex) {
        this.timeStamp = new int[size];

        for (int i = 0; i < this.timeStamp.length; i++) {
            this.timeStamp[i] = 0;
        }

        this.size = size;
        this.localIndex = localIndex;
    }

    /**
     * Incrementaçao da posiçao actual do array.
     *
     */
    public synchronized void increments() {
        this.timeStamp[this.localIndex]++;

    }

    /**
     * actualizaçao do relogio.
     *
     * @param newVector relogio actual
     */
    public synchronized void update(VectorTime newVector) {
        assert (newVector.getSize() == this.size);

        //this.timeStamp[this.localIndex]++;
        
        int[] aux=newVector.toIntArray();
        
        for (int i = 0; i < this.timeStamp.length; i++) {
            timeStamp[i] = Math.max(timeStamp[i], aux[i]);
        }
    }

    /**
     * funçao usada para receber uma copia integral do relogio vectorial na
     * situaçao actual.
     *
     * @return devolve uma copia integral do relogio vectorial.
     * @throws java.lang.CloneNotSupportedException excepcao para remote do rmi
     */
    public synchronized VectorTime getCopy() throws CloneNotSupportedException {
        VectorTime vecClone = null;

        vecClone = new VectorTime(this.size, this.localIndex);
        vecClone.update(this);
        
        return vecClone;
    }

    /**
     * funçao usada para receber o array de inteiros timeStamp.
     *
     * @return devolve o array de inteiros timeStamp.
     */
    public synchronized int[] toIntArray() {
        int[] array = new int[this.size];

        for (int i = 0; i < this.size; i++) {
            array[i] = this.timeStamp[i];
        }

        //System.arraycopy(this.timeStamp, 0, array, 0, this.size);
        return array;
    }

    /**
     * funçao usada para receber o indice actual do array de inteiros timeStamp.
     *
     * @return devolve o indice actual do array de inteiros timeStamp.
     */
    public synchronized int getLocalIndex() {
        return localIndex;
    }

    /**
     * funçao usada para receber o tamanho do array de inteiros timeStamp.
     *
     * @return devolve o tamanho do array de inteiros timeStamp.
     */
    public int getSize() {
        return size;
    }

}
