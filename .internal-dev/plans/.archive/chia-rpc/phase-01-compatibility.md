# Chia 2.7.5 RPC compatibility

## Context
The library baseline dates to November 2023. GitHub's latest stable release is Chia 2.7.5, commit ff71448437a137c7092775923d5ecbbe104433e7. Official source is available at /tmp/jxch-chia-2.7.5. Documentation and main will be compared for drift.

## Goal
Add DataLayer and modernize RPC coverage and contracts while preserving the established endpoint enums, JsonUtils payloads, paired byte/parsed methods, builders, records, and ApiResponse transport.

## In Scope
DataLayer service; existing wallet, full node, farmer, harvester, daemon and crawler service endpoint coverage; changed request/response schemas; deterministic contract regression tests; usage and compatibility documentation. The user additionally requested a major library version bump: 0.2.0 to 1.0.0 in Maven and README coordinates.

## Out of Scope
Unreleased main-only contracts, simulator support/testing, live-node testing, unrelated refactors, new transport architecture, CI workflows, dependency pinning, and claims of live-node validation. The user explicitly said not to worry about simulation and permitted skipping tests; prioritize exact RPC contracts while retaining required focused validation and compilation checks.

## Implementation Steps
1. Inventory upstream route registrations and compare existing enums and methods.
2. Parallel disjoint advanced implementation units: DataLayer/config; wallet/builders; full-node schemas; farmer/harvester/common services. Root controls Git and exact write boundaries.
3. Independently validate each unit using the configured phaseValidator and commit focused completed units on the feature branch.
4. Writer updates bounded documentation after the technical API settles.
5. Run final integration review, full Maven tests/package, coverage reconciliation and diff review; verify the package version is 1.0.0 and merge to main only on PASS.

## Validation
Advanced implementer and phase/integration validator: openai-codex/gpt-6.1-terra, medium thinking, resolved from active sprint-planner default configuration. Writer uses the same configured tuple. Focused fixture tests must prove payload keys, correct routes, optional/null behavior, changed response deserialization and failure propagation. Final complete suite and package gate required. Compare upstream route inventories with actual callable methods; record any removed routes explicitly.

## Exit Criteria
All current in-scope service routes are callable through the established public API, changed contracts are reconciled, DataLayer configuration uses port 8562, regression checks pass, independent integration review passes, documentation accurately states evidence and limitations, and validated commits are merged to main.

## Completion Evidence
Implementation and independent phase gates passed for full node (70 focused checks), wallet (57), and shared/DataLayer services (260). Final integration PASS: Java 17 clean verify, 390 checks across 18 suites with zero failures/errors/skips; all 299 stable HTTP service-route pairs exercised. Root independently confirmed packaging and JAR metadata for release 1.0.0. Focused commits: 7fe670d, ad19245, 368a73a. No live-node, simulator or WebSocket implementation is claimed. Final closeout records and merge complete this plan.
