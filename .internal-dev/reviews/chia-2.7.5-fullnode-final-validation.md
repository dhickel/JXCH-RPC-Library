# Chia full-node final phase validation

## Scope
Full-node compatibility unit, stable Chia 2.7.5 at ff71448437a137c7092775923d5ecbbe104433e7. Independent validator openai-codex/gpt-6.1-terra, medium thinking, following senior corrective request precision work. Unit commit 7fe670d.

## Findings
VERDICT: PASS. 70 focused checks passed with zero failures/errors/skips in an isolated Maven copy. All 40 route pairs are callable; previous request-domain gaps are resolved. Legacy int signatures remain, with long overloads for uint32 heights and bounds. Fee targets accept exact integral Number implementations without truncation and reject unsupported numeric forms. Coin IDs match six native chia_rs vectors including zero and uint64 maximum.

## Risk Assessment
Necessary response accessor changes are documented. No remaining concrete in-scope defect was identified by the final validator. Integration remains gated on other units and the final complete build/check run; no live-node or simulator evidence is claimed.

## Recommendations
Preserve the full-node request/source contract and latest PASS while integrating disjoint wallet and shared-service work.

## Follow-ups
Final integration review and package gate pending. Earlier blocked attempts remain recorded in chia-2.7.5-numeric-contract-validation-attempt-1.md.
