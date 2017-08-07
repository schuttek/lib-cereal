package com.wezr.lib.cereal;

public class Tuple<L, R> {

    @Override
    public String toString() {
        return "Tuple [left=" + left + ", right=" + right + "]";
    }

    private final L left;
    private final R right;

    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple<?, ?> pairo = (Tuple<?, ?>) o;
        return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
    }


}
