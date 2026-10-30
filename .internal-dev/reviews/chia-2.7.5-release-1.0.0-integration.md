# Chia 2.7.5 release 1.0.0 final integration

## Scope
User-directed DataLayer support and current/new/changed/deprecated HTTP RPC contracts, preserving endpoint enums, existing transport, builders, records and paired raw/parsed APIs. Added user-directed major version bump from 0.2.0 to 1.0.0. Stable Chia identity: 2.7.5 at ff71448437a137c7092775923d5ecbbe104433e7. Plan: plans/.archive/chia-rpc/phase-01-compatibility.md.

## Findings
VERDICT: PASS. Independent final integration ran `mvn -q -Dmaven.compiler.release=17 clean verify`: 390 checks across 18 suites, zero failures/errors/skips. The route inventory test exercised all 299 stable HTTP service-route pairs and locale-independent paths. All three units had independent latest PASS before integration. Integration repaired only a reflection fixture: FullNodeContractTest now supplies exact BigInteger values for Number/Object parameters introduced by shared logging operations; production behavior was not weakened.

Root independently ran `mvn -q -Dmaven.compiler.release=17 -DskipTests package`, checked all Surefire XML totals, verified target/jxch-rpc-library-1.0.0.jar and its embedded Maven version, checked README coordinates, reviewed diff scope and whitespace, and verified global-policy synchronization. POM dependency coordinates and versions are unchanged; only the project version changed.

### Execution and routing evidence
Fallback orchestration of user prose; no generated sprint source or frozen execution-record identifier exists. Units: full-node, wallet and common/DataLayer services. Dedicated feature branch: feat/chia-2.7.5-rpc. Focused unit commits: 7fe670d, ad19245, 368a73a. Source comparison and disjoint implementation/repair work were parallelized; overlapping common network-return changes were serialized before final wallet/full-node validation.

Configured advanced implementer, phase/integration validator and writer: openai-codex/gpt-6.1-terra, medium thinking. Senior corrective implementation: openai-codex/gpt-6.1-sol at high then xhigh. Final common-service escalation: openai-codex/gpt-6-astra at xhigh, per the senior skill's third-pass contract. Initial read-only exploration: openai-codex/gpt-5.6-luna at high. Earlier incomplete work and BLOCKED findings were repaired, not treated as PASS.

## Risk Assessment
This is source-contract and deterministic validation, not live-node, simulator or end-to-end evidence; the user explicitly did not require those. Necessary corrected return/accessor types may require caller migration, documented in readme.md. Daemon commands remain WebSocket-only and fail explicitly through unsupported HTTP surfaces. Unreleased main-only PlotNFT operations are outside the stable baseline. No unresolved in-scope defect remains identified.

## Recommendations
Merge validated work to main without publishing or pushing unless separately requested. Use readme.md migration notes for corrected numeric types, response envelopes and removed operations.

## Follow-ups
No external follow-up is required for the requested implementation. Future release compatibility work should repeat the pinned-source inventory and numeric/default/nullable contract checks rather than relying on documentation or route names alone.
