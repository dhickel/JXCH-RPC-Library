package io.mindspice.jxch.rpc.util.bech32;

import java.io.ByteArrayOutputStream;


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


public class AddressUtil {
    private static final int M = 0x2bc830a3;

    public static String encode(String prefix, Bytes32 hex) {
        final byte[] bytes = convertBits(hex.getBytes(), 8, 5, true);
        return Bech32.encode(new Bech32.Bech32Data(prefix, bytes), M);
    }

    public static Bytes32 decode(String str) {
        final Bech32.Bech32Data decode = Bech32.decode(str, M);
        if (decode.data.length == 0) {
            throw new IllegalArgumentException("invalid value");
        }
        final byte[] bytes = convertBits(decode.data, 5, 8, false);
        return new Bytes32(bytes);
    }

    public static String encode(String prefix, String hex) {
        Bytes32 h = Bytes32.fromHex(hex);
        final byte[] bytes = convertBits(h.getBytes(), 8, 5, true);
        return Bech32.encode(new Bech32.Bech32Data(prefix, bytes), M);
    }

    /**
     * General power-of-2 base conversion.
     */
    private static byte[] convertBits(final byte[] in, final int fromBits, final int toBits, final boolean pad) {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        final int maxv = (1 << toBits) - 1;
        final int max_acc = (1 << (fromBits + toBits - 1)) - 1;
        for (int i = 0; i < in.length; i++) {
            int value = in[i] & 0xff;
            if ((value >>> fromBits) != 0) {
                throw new IllegalArgumentException("input value " + value + " exceeds " + fromBits + " bit size, at index " + i);
            }
            acc = ((acc << fromBits) | value) & max_acc;
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                out.write((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0) out.write((acc << (toBits - bits)) & maxv);
        } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) != 0) {
            throw new IllegalArgumentException("invalid padding");
        }
        return out.toByteArray();
    }
}