package io.mindspice.jxch.rpc.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class XchUtils {
    private static final BigDecimal MOJO_PER_XCH = new BigDecimal("1000000000000");

    private XchUtils() { }

    public static BigInteger xchToMojos(BigDecimal xch) {
        return xch.multiply(MOJO_PER_XCH).toBigInteger();
    }

    public static BigInteger xchToMojos(Double xch) {
        BigDecimal xc = new BigDecimal(xch);
        return xc.multiply(MOJO_PER_XCH).toBigInteger();
    }

    public static BigDecimal mojosToXch(BigDecimal mojos) {
        return mojos.divide(MOJO_PER_XCH, 12, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal mojosToXch(Long mojos) {
        BigDecimal mj = new BigDecimal(mojos);
        return mj.divide(MOJO_PER_XCH, 12, RoundingMode.HALF_UP).stripTrailingZeros();
    }
}
