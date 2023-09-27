package io.mindspice.jxch.rpc.util.bech32;

import org.bouncycastle.util.encoders.Hex;

import java.util.List;

/*
 * Copyright 2018 Coinomi Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Credit: https://github.com/joelcho/
 * Repo: https://github.com/joelcho/chia-rpc-java
 */


public class HexUtil {
    // https://github.com/web3j/web3j/blob/master/utils/src/main/java/org/web3j/utils/Numeric.java
    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static String toHexString(byte[] bytes, boolean prefixed, boolean zeroPadded, int strSize) {
        String s = Hex.toHexString(bytes);
        if (strSize == 0) {
            strSize = bytes.length * 2;
        }
        if (s.length() < strSize && zeroPadded) {
            s = Strings.zeros(strSize - s.length()) + s;
        }
        if (prefixed) {
            s = "0x" + s;
        }
        return s;
    }

    public static byte[] decode(String x) {
        return Hex.decode(x);
    }

    // https://github.com/web3j/web3j/blob/master/utils/src/main/java/org/web3j/utils/Numeric.java
    public static boolean containsHexPrefix(String input) {
        return !Strings.isEmpty(input)
                && input.length() > 1
                && input.charAt(0) == '0'
                && input.charAt(1) == 'x';
    }

    public static class Strings {

        private Strings() {
        }

        public static String toCsv(List<String> src) {
            // return src == null ? null : String.join(", ", src.toArray(new String[0]));
            return join(src, ", ");
        }

        public static String join(List<String> src, String delimiter) {
            return src == null ? null : String.join(delimiter, src.toArray(new String[0]));
        }

        public static String capitaliseFirstLetter(String string) {
            if (string == null || string.length() == 0) {
                return string;
            } else {
                return string.substring(0, 1).toUpperCase() + string.substring(1);
            }
        }

        public static String lowercaseFirstLetter(String string) {
            if (string == null || string.length() == 0) {
                return string;
            } else {
                return string.substring(0, 1).toLowerCase() + string.substring(1);
            }
        }

        public static String zeros(int n) {
            return repeat('0', n);
        }

        public static String repeat(char value, int n) {
            return new String(new char[n]).replace("\0", String.valueOf(value));
        }

        public static boolean isEmpty(String s) {
            return s == null || s.length() == 0;
        }
    }
}