# Chia wallet and full-node numeric contract validation

## Scope
Independent wallet and full-node phase reviews by openai-codex/gpt-6.1-terra at medium thinking against Chia 2.7.5. These are distinct source reports, retained below without replacing the earlier common-service review.

## Findings
### Wallet validator
VERDICT: BLOCKED. 46 focused wallet checks passed in /tmp/jxch-wallet-independent.xt2oKn. Repaired primitive NFT-count overload to decode uint64 as BigInteger and added maximum-value checks. Identified legacy request wallet IDs represented as int despite upstream uint32 (for example didGetDIDAsBytes), requiring a systematic compatible wide-value request path audit. Complete direct wallet source-contract review remained unfinished.

### Full-node validator
VERDICT: BLOCKED. 65 focused checks passed in /tmp/jxch-fullnode-review-KnMMYR. Repaired zero-amount coin identity canonical encoding and verified six vectors against chia_rs 0.48.0; updated the Network fixture for genesis challenge. Identified int height bounds and List<Integer> fee target times as narrower than upstream request contracts. Required compatible wide-value request paths and representative boundaries.

## Risk Assessment
Response precision and route completeness do not establish request-domain completeness. Generic-list type erasure prevents naive overloads differing only in generic parameters. Existing source calls and nullable overload resolution must be considered deliberately.

## Recommendations
Escalate each independent numeric request audit to the configured senior agent at xhigh, then rerun each configured phase validator. Preserve the canonical coin-ID and NFT-count fixes. Do not open the final integration gate until latest phase verdicts are PASS.

## Follow-ups
Wallet and full-node senior request audits are in progress on disjoint write sets. No live-node or simulator testing is required or claimed.
