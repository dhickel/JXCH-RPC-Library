# Chia RPC baseline and source reconciliation

## Scope
Repository main baseline 6055a11 (November 2023), its documented Chia 2.1.0 support, current upstream Chia stable 2.7.5 at ff71448437a137c7092775923d5ecbbe104433e7, main at da4a9e389915877827fec3de69d6d3f48dcb5f35, official RPC documentation, and dkackman's OpenAPI entry point/DataLayer specification.

## Findings
- GitHub releases/latest identifies 2.7.5 as the latest non-prerelease (published October 14, 2026).
- DataLayer transport configuration exists without an endpoint enum or client. Its default port is incorrectly 8561 rather than 8562 in NodeConfig (including Builder), config.yaml and README.
- Existing conventions are endpoint enums, JsonUtils/RequestUtils builders, response records, and paired byte/ApiResponse methods.
- Static inspection identified wrong service metadata constructors for WalletAPI/HarvesterAPI, stop methods invoking health checks, and harvester refresh/config methods invoking unrelated routes. These require request-capture regression tests.
- Stable service-specific routes: wallet 122, DataLayer 35, full node 29, farmer 14, harvester 8, crawler 2, solver 1, timelord 0. RpcServer adds 11 shared HTTP routes per service.
- Wallet routes are generated from wallet_rpc_metadata.py; scanning only string literals in wallet_rpc_api.py:get_routes incorrectly reports one route. Inventory extraction must use metadata.
- Main adds wallet plotnft_transfer/plotnft_melt relative to stable; other inspected service route registrations are unchanged. Main is not the release compatibility target.
- dkackman DataLayer OpenAPI advertises version 2.4.3 and has contradictory port descriptions; pinned released implementation governs conflicts.
- The daemon is a WebSocket API, not the same HTTP POST service architecture. Existing DaemonClient/enum claims must not be expanded into invented HTTP support.
- The unmerged origin/2.4-update branch is reference material, not accepted current behavior; it must not be blindly merged.

## Risk Assessment
Route-name coverage alone cannot prove payload or response compatibility. Existing schema drift, changed optional/default behavior and heterogeneous transaction shapes require deterministic fixtures. No live-node compatibility proof is available from the repository alone.

## Recommendations
Implement disjoint service units against pinned stable source, add call-capture and serialization tests, preserve source compatibility where possible, clearly deprecate removed behavior, and keep daemon limitations explicit rather than introducing an unauthorized transport replacement.

## Follow-ups
Independent service and integration review, complete tests/package gate, and accurate user documentation after APIs settle.
