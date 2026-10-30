package io.mindspice.jxch.rpc.schemas.wallet.nft;

import java.util.List;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;


public record MetaData(
        @JsonProperty("uris") List<String> uris,
        @JsonProperty("meta_uris") List<String> metaUris,
        @JsonProperty("license_uris") List<String> licenseUris,
        @JsonProperty("hash") String hash,
        @JsonProperty("meta_hash") String metaHash,
        @JsonProperty("license_hash") String licenseHash,
        @JsonProperty("edition_number") BigInteger editionNumber,
        @JsonProperty("edition_total") BigInteger editionTotal
) {
    public MetaData(List<String> uris, List<String> metaUris, List<String> licenseUris, String hash,
            String metaHash, String licenseHash, long editionNumber, long editionTotal) {
        this(uris, metaUris, licenseUris, hash, metaHash, licenseHash,
                BigInteger.valueOf(editionNumber), BigInteger.valueOf(editionTotal));
    }

    public MetaData {
        editionNumber = editionNumber != null ? editionNumber : BigInteger.ONE;
        editionTotal = editionTotal != null ? editionTotal : BigInteger.ONE;
        uris = uris != null ? List.copyOf(uris) : List.of();
        metaUris = metaUris != null ? List.copyOf(metaUris) : List.of();
        licenseUris = licenseUris != null ? List.copyOf(licenseUris) : List.of();
        if ("".equals(metaHash)) { metaHash = null; }
        if ("".equals(licenseHash)) { licenseHash = null; }
    }

    public MetaData cloneSetEdt(int edt) {
        return cloneSetEdt((long) edt);
    }

    public MetaData cloneSetEdt(long edt) {
        return cloneSetEdt(BigInteger.valueOf(edt));
    }

    public MetaData cloneSetEdt(BigInteger edt) {
        return new MetaData(
                uris,
                metaUris,
                licenseUris,
                hash,
                metaHash,
                licenseHash,
                edt,
                editionTotal
        );
    }
}