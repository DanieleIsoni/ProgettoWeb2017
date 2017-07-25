/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;

/**
 *
 * @author massimo
 */



    public class Pair<T,U>
    {
        public T Left;
        public U Right;

       public Pair(T Left, U Right) {
           this.Left = Left;
           this.Right = Right;
       }

    }