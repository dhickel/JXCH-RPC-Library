package io.mindspice.jxch.rpc.util.bech32;

public class Bytes32 {

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


    public static Bytes32 fromHex(String str) throws RuntimeException {
        str = HexUtil.cleanHexPrefix(str);
        if (str.length() > 64) {
            throw new IllegalArgumentException("invalid hash hex str:" + str);
        }
        byte[] data = HexUtil.decode(str);
        return new Bytes32(data);
    }

    private final byte[] bytes = new byte[32];

    public Bytes32(byte[] b) {
        System.arraycopy(b, 0, this.bytes, 0, Math.min(b.length, this.bytes.length));
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public String toString() {
        return this.toHexString(false);
    }

    public String toHexString(boolean prefixed) {
        return HexUtil.toHexString(this.bytes, prefixed, true, 64);
    }
}

