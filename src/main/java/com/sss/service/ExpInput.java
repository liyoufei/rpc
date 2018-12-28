package com.sss.service;

/**
 * ExpInput class
 *  指数输入有底数和指数
 * @author Sss
 * @date 2018/12/26
 */
public class ExpInput {
    private int base;
    private int exp;

    public ExpInput() {
    }

    public ExpInput(int base, int exp) {
        this.base = base;
        this.exp = exp;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
