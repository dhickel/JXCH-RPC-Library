package io.mindspice.schemas.wallet.nft;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaData(
	@JsonProperty("uris") List<String> uris,
	@JsonProperty("meta_uris") List<String> metaUris,
	@JsonProperty("license_uris") List<String> licenseUris,
	@JsonProperty("hash") String hash,
	@JsonProperty("meta_hash") String metaHash,
	@JsonProperty("license_hash") String licenseHash,
	@JsonProperty("edition_number") int editionNumber,
	@JsonProperty("edition_total") int editionTotal
) {
	public MetaData {
		if (metaHash == null) metaHash = "";
		if (licenseHash == null) licenseHash = "";
	}
}