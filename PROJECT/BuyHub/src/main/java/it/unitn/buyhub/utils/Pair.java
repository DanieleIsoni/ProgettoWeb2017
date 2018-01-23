package it.unitn.buyhub.utils;

/**
 * A simple pair class, used to manage double columns keys
 *
 * @author Massimo Girondi
 */
public class Pair<T, U> {

    public T Left;
    public U Right;

    public Pair(T Left, U Right) {
        this.Left = Left;
        this.Right = Right;
    }

}
