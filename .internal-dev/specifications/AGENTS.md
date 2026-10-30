# Specification Workflow

- Read relevant living specifications before changing architecture, APIs, artifact layout, workflow behavior, validation contracts, or user-facing behavior.
- Update an existing specification by default. Create a new class only when ownership does not fit an existing file, then update `index.md` in the same change.
- Record durable architecture, design, product, and workflow tradeoffs in `decisions.md` with justification, alternatives, caveats, affected specs, source, and review timing when known.
- Put unaccepted future direction in `horizon-ideas.md`; put accepted but deferred capability in `deferred-features.md`.
- Report stale or conflicting specifications instead of silently rewriting broad project direction.
