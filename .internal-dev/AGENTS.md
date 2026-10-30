# Internal Development Document Store Guide (`.internal-dev`)

This guide defines how agents use persistent engineering records in `.internal-dev/`.

## Initialization

`.internal-dev/` is initialized only via explicit user action: the `/internal-dev init` command, the `internal_dev` tool action `initialize`, or the `internal_dev` create action (which offers interactive initialization when the store is missing or partial). Initialization is never triggered automatically at session start. Initialization creates missing workflow directories and starter contracts without overwriting existing files. It does not edit `.gitignore`; follow the repository's tracking policy.

## Purpose

`.internal-dev/` is the development document store for specifications, planning, bug tracking, reviews, changelogs, reusable knowledge, validation evidence, handoffs, brainstorming, and sprint artifacts.

## Source-of-Truth Policy

- Code is the logical source of truth.
- Specifications are intended truth.
- Documentation and changelogs are historical or explanatory truth.
- If code, specifications, and docs diverge, record the mismatch in task output and create or update a tracking artifact in `.internal-dev/`.
- Treat archived files as historical evidence unless a current task explicitly names them as a restoration source.

## Access Discipline

- Do not read `.internal-dev` directories or files randomly.
- Use controlled access: read only what the active task needs.
- Prefer targeted lookups over broad scans.
- Preserve unrelated local edits and untracked files unless the user explicitly asks to change them.

## Directory Contract

- `specifications/`: living intended contracts, durable decisions, deferred capabilities, and horizon ideas.
- `bugs/`: bug reports discovered during implementation or review.
- `plans/`: active implementation plans in nested plan directories.
- `reviews/`: completed review write-ups, including persisted repository-history, architecture or codebase assessments, audits, and analytical assessments.
- `knowledge/`: reusable domain research, implementation gotchas, validation patterns, and learner-facing summaries.
- `changelogs/`: dated change records that summarize completed work.
- `debug_reports/`: timeout-bound runtime/debug records used for diagnosis.
- `skills/`: repo-local skills used for project-specific workflows.
- `handoffs/`: high-level plan directives and other self-contained transfers of settled context into later planning or execution.
- `brainstorm/`: one folder per explicit brainstorming or ideation effort with unaccepted alternatives, containing participating agents' or sources' findings and a clearly separate synthesis when one is produced.
- `sprints/`: self-contained staged sprint records. Runtime state and child-session checkpoints are temporary; manifests record whether implementation and final validation were performed, delegated, or not run. Durable execution-only sprint evidence (source identity, phased ledgers, validator verdicts, and changed-file evidence) belongs to the sprint-planner extension; internal-dev stores only the planning artifacts and manifest.
- `.archive/`: finalized or superseded artifacts in the same parent scope as the active content.

Retired stores such as repo-local `focus/`, catch-all `notes/`, broad `research/`, and inbox ledgers are not active workflow destinations. Classify material into an active store instead.

Artifact naming is advisory, not a rigid schema. Use descriptive names and preserve established local conventions, but do not reject or rename a safe artifact solely because its name lacks a date, slug, or expected extension. New artifacts must never overwrite existing files.

## Beginning Workflow

Before non-trivial work:

- Read `.internal-dev/specifications/AGENTS.md`.
- Read relevant files in `.internal-dev/specifications/` before changing architecture, APIs, examples, tools, persistence-like artifact layout, workflow behavior, validation contracts, or user-facing behavior.
- List or search `.internal-dev/knowledge/` filenames and read only files whose filename or domain matches the task.
- If no knowledge filename looks relevant, proceed without broad reads.

When lost, confused, blocked by project context, or correcting a false assumption:

- Search `.internal-dev/knowledge/` filenames again.
- Run a deeper grep across `.internal-dev/knowledge/`.
- Use web or official documentation when the missing information is external framework, library, tool, protocol, or platform behavior and local knowledge is absent or stale.
- After resolving the learning, update a domain-named knowledge file when another agent is likely to need the same context.

## Mid-Workflow Routing

- Use specifications for intended contracts.
- Use `specifications/decisions.md` for durable architecture, design, product, and workflow tradeoffs.
- Use knowledge for reusable learning, framework techniques, implementation gotchas, validation patterns, corrections, and recurring failure modes.
- Use changelogs for prior edit context.
- Use bugs for defects.
- Use plans for scoped implementation suites.
- Use reviews for completed review write-ups and validation campaigns. When persistence is useful, completed repository-history, architecture or codebase assessments, audits, and analytical assessments belong in reviews.
- Ordinary informational answers need no persistent artifact unless requested or required by another workflow contract.
- Use handoffs for high-level directives that communicate context, objective, settled decisions, constraints, scope, and validation direction. A handoff is not a detailed phase plan and may be produced by ironing out or any other workflow that needs to transfer work.
- Use brainstorm folders only for explicit brainstorming or ideation with unaccepted alternatives, never merely because subagents participated. For a real brainstorm, retain every participating agent's or source's findings separately, keep any synthesis in its own file, and do not treat an idea as an accepted decision, specification, or plan until the relevant workflow accepts it.
- Use a sprint folder only for a self-contained staged sprint effort. Runtime state and private child-session checkpoints may exist while a run is incomplete, but successful runs remove them. A sprint's `planning/` directory contains only `concepts.md`, `orchestration.md`, and flat `phase-NN-*.md` files. Its manifest must state whether implementation and final validation were performed, delegated, or not run.
- Do not route active workflow material to retired catch-all stores.

## Specification Workflow

- Update existing living specification files by default.
- Create a new specification file only for a genuinely new specification class and update `specifications/index.md` in the same change with its ownership boundary.
- Future project direction goes to `specifications/horizon-ideas.md`.
- Accepted deferred capability goes to `specifications/deferred-features.md`.
- Durable decisions go to `specifications/decisions.md` with justification, alternatives or tradeoffs when known, caveats, affected specs, source, and review timing.
- If an implementation has no impact on specifications, the changelog must say `Specification Impact: none` with one sentence explaining why.

## Knowledge Workflow

- When a false assumption, repeated mistake, major correction, important user correction, or repeated reverification reveals reusable context, update a domain-named knowledge file.
- Link the affected specification or changelog when useful.
- Name knowledge files after the domain they cover, not after a random incident title.
- If reusable context is an intended contract, update the relevant specification instead or in addition.
- If reusable context is a durable decision, record it in `specifications/decisions.md`.

## Workflow Rules

- Out-of-scope bugs discovered in passing must be logged immediately.
- If the project has a GitHub repository, every bug report created under `.internal-dev/bugs/` must be mirrored directly to that repository as a GitHub Issue when it is created or compiled.
- When adding or updating a local bug report in a project with a GitHub repository, check for related closed GitHub Issues before finishing; if the corresponding issue is already closed, move the local bug report to `.internal-dev/bugs/.archive/` instead of leaving it active.
- User hints like "future", "eventually", "later", or "this will become" go to `specifications/horizon-ideas.md` unless accepted as deferred capability.
- Any completed review is written to `reviews/`.
- Plans in progress should live in their own plan directories and include phase implementation files.
- When a bug or plan is finalized, move it to a sibling `.archive/` directory in the same parent path.
- Existing `plans/.completed/` content is legacy/read-only; use `.archive/` going forward.
- Finalized code or documentation changes must have a changelog entry in `changelogs/`.
- Every changelog in a Git repository must include the full current `HEAD` hash under `Git Commit`. Record it when writing the changelog; on a dirty worktree it identifies the baseline commit, not necessarily a commit containing the described changes.
- If a Git repository has no commit yet, do not invent a hash or create a noncompliant changelog. Initialization may proceed without its setup changelog, but make the repository's first commit before finalizing any changelog.
- Inbound remote-work coordination should use the project's designated global coordination workflow; do not create a repo-local inbox ledger unless explicitly required.
- Use exclusive creation for new artifacts. If a target exists, choose another safe name or deliberately update it as an existing artifact; never truncate it as part of creation.
- The only destructive workflow exception is an explicitly confirmed `/sprint reset`: it may permanently delete the selected sprint run directory, including malformed runtime state, but never reverts repository edits. No other artifact workflow may use reset deletion.

## Closeout Workflow

- Update affected specifications, knowledge, bugs, changelogs, plans, reviews, handoffs, brainstorm records, and sprint records.
- Record specification impact in the changelog, or state `Specification Impact: none` with one sentence explaining why.
- Record reusable lessons from false assumptions, repeated mistakes, large corrections, important user corrections, repeated reverification, and missing context in domain-named knowledge files.
- Report stale or conflicting specifications in the final response instead of silently rewriting broad project direction.
- Do not use retired catch-all workflow stores for closeout material.

## Minimum Templates

### Bug (`bugs/<bug-id>/report.md`)

Required headings: `Summary`, `Scope`, `Reproduction`, `Expected`, `Actual`, `Evidence`, `Impact`, `Status`, `Next Action`.

### Plan phase (`plans/<plan-id>/phase-XX-<name>.md`)

Required headings: `Context`, `Goal`, `In Scope`, `Out of Scope`, `Implementation Steps`, `Validation`, `Exit Criteria`.

### Review (`reviews/<date>-<topic>-review.md`)

Required headings: `Scope`, `Findings`, `Risk Assessment`, `Recommendations`, `Follow-ups`.

### Changelog (`changelogs/<date>-<topic>.md`)

Required headings: `Date`, `Git Commit`, `Change Summary`, `Files`, `Behavioral Impact`, `Specification Impact`, `Risks`, `Follow-up Items`.

### Knowledge (`knowledge/<topic>.md`)

Required headings: `Topic`, `Source References`, `Key Takeaways`, `Project Relevance`, `Open Questions`.

### Handoff (`handoffs/<topic>.md`)

Required headings: `Context`, `Objective`, `Settled Decisions`, `Constraints`, `Scope`, `Recommended Direction`, `Validation`, `Open Questions`.

### Brainstorm finding or synthesis (`brainstorm/<topic>/<source-or-synthesis>.md`)

Required headings: `Prompt`, `Source`, `Findings`, `Options`, `Trade-offs`, `Open Questions`, `Recommended Next Step`.

### Sprint manifest (`sprints/<run-id>/manifest.md`)

Required headings: `Directive`, `Stages`, `Artifacts`, `Implementation Evidence`, `Final Validation`, `Outcome`.

## Related Guides

When present, also consult the repository's top-level `AGENTS.md`, `.internal-dev/specifications/index.md`, project API/internal documentation indexes, and relevant `.internal-dev/skills/*/SKILL.md` files.
