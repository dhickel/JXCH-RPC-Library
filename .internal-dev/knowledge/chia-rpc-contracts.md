# Chia RPC contracts

## Topic
Chia RPC source inventory and compatibility evidence.

## Source References
- Chia 2.7.5, commit `ff71448437a137c7092775923d5ecbbe104433e7`
- `wallet_rpc_metadata.py`
- `rpc_server`
- dkackman DataLayer OpenAPI specification, version 2.4.3

## Key Takeaways
- Wallet `get_routes` is generated from `wallet_rpc_metadata.py`. Scanning API literals produces a false `/` inventory; the stable source has 122 wallet routes.
- APIs moved from `chia/rpc` into per-service folders. `rpc_server` owns 11 shared routes.
- The dkackman DataLayer OpenAPI 2.4.3 specification contains conflicting port descriptions and request mismatches. The pinned released code governs.
- Main differs from stable for wallet plotnft endpoints.
- Full route coverage does not prove numeric domain coverage. A uint32 request needs a wide Java path; uint64 and uint128 need exact `BigInteger` handling. Numeric fixtures must respect each upstream sized range; synchronization uint32 fields are not unbounded integers.
- Jackson's default integer parsing can coerce floats and strings. Use a scoped `ExactIntegerDeserializer` to reject coercion without changing `JsonUtils` globally.
- `JsonUtils.ObjectBuilder.put` creates POJONodes, so `isIntegralNumber` before serialization is unreliable. Validate the original `Number` or a canonical JSON tree.
- String and Number overloads can make null calls ambiguous. Preserve legacy null resolution using an Object supertype or a named numeric API, as appropriate.
- Coin ID hashing encodes a zero amount as empty CLVM bytes, not `{0}`. Six canonical vectors were verified with native `chia_rs` 0.48.
- Shared endpoint metadata must bind to the actual service. Preserve the existing `Network` return type when adding common methods.
- Data Layer 2.7.5 defaults an omitted or null fee to zero, not the configured fee; current official documentation is stale on this point.
- A source route inventory alone does not prove payload or response contracts.
- Initial technical workers stopped with partial results because child escalation tools were unavailable. The root must act on a precise escalation request rather than accept partial work. The root has escalated and will verify the result.

## Project Relevance
Use the pinned release and deterministic contract checks to establish compatibility; do not infer schemas or supported endpoints from route names alone.

## Open Questions
None known.
