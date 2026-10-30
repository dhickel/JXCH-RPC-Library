# RPC Compatibility

## Purpose
Define the library's compatibility target and the evidence required to verify it.

## Intended Contract
- Target stable Chia 2.7.5 at commit `ff71448437a137c7092775923d5ecbbe104433e7`.
- Preserve the public enum, `NodeConfig` and `RPCClient` APIs, `ApiResponse`, and paired `AsBytes` APIs.
- Model stable data with record schemas and complex data with `JsonNode`.
- Support DataLayer on port 8562.
- Treat daemon RPC as WebSocket-only; HTTP access must report that it is unsupported.
- Existing removed DID recovery methods may remain deprecated, but must not send unsupported requests.

## Constraints
- Do not add a WebSocket transport.
- Do not add simulator support or simulator tests.
- Do not include the unreleased main-branch endpoints `plotnft_transfer` or `plotnft_melt`.

## Decisions
The pinned released source governs compatibility. Route inventory alone does not establish request or response contracts.

## Validation
Use deterministic contract checks against the pinned release. Live, simulator, and end-to-end evidence are not required.

## Open Questions
No unresolved product questions are known.
