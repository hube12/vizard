package com.seedfinding.neil.util;


import java.util.Objects;

public final class Pair<A, B> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public Pair(Pair<? extends A, ? extends B> other) {
        this(other.a, other.b);
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }

    public int hashCode() {
        return Objects.hash(this.a, this.b);
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other != null && other.getClass() == Pair.class) {
            Pair<?, ?> that = (Pair<?, ?>) other;
            return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b);
        } else {
            return false;
        }
    }

    public String toString() {
        return "(" + this.a + ", " + this.b + ")";
    }
}