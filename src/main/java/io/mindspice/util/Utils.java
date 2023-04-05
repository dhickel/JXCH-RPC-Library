package io.mindspice.util;

import io.mindspice.schemas.TypeRefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;


public class Utils {

    public static class Pair<U, V> {
        public final U first;
        public final V second;

        public Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }

        public static <U, V> Pair<U, V> of(U obj1, V obj2) {
            return new Pair<>(obj1, obj2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return first.equals(pair.first) && second.equals(pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "Pair{" + "\n" +
                    "first=" + first + ",\n"
                    + "second=" + second + "\n" +
                    "}\n";
        }
    }


    public static class Triple<U, V, W> {
        public final U first;
        public final V second;
        public final W third;

        public Triple(U first, V second, W third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public static <U, V, W> Triple<U, V, W> of(U obj1, V obj2, W obj3) {
            return new Triple<>(obj1, obj2, obj3);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
            return first.equals(triple.first) && second.equals(triple.second) && third.equals(triple.third);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second, third);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Triple.class.getSimpleName() + "[", "]")
                    .add("first=" + first)
                    .add("second=" + second)
                    .add("third=" + third)
                    .toString();
        }
    }


}
