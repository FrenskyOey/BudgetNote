---
name: documentation_maintenance
description: Maintains documentation accuracy. Use when codebase architecture, patterns, or dependencies change.
---

# Documentation Maintenance Skill

## Objective
Prevent "Project Context Rot" by ensuring that the documentation in `.agent/rules/` and `.agent/workflows/` always reflects the actual state of the codebase.

## When to Use
Activate this skill when:
1.  **Architectural Changes**: You create new modules, change layer boundaries, or alter dependency rules.
2.  **Pattern Changes**: You introduce a new pattern (e.g., a new way to handle errors or navigation) that contradicts existing rules.
3.  **Refactoring**: You move files to locations that violate the current `project_architecture.md`.
4.  **Workflow Adjustments**: You find yourself skipping steps in `implement_feature.md` because they are obsolete.

## Protocol

### 1. Analyze the Drift
Compare the **Current Codebase State** vs. **Documentation Rules**.
*   **Check**: Does the directory structure match `project_architecture.md`?
*   **Check**: Do the coding patterns match `coding_standards.md`?
*   **Check**: Are there new libraries or tools used that aren't in `tech_stack.md`?

### 2. Update the Rules
If you find a discrepancy, **DO NOT** just ignore the rule. You must either:
*   **Fix the Code**: If the code violates a valid rule.
*   **Fix the Rule**: If the rule is outdated and the code is the new source of truth.

### 3. Verification
After updating a rule file (e.g., `project_architecture.md`), verify that the new rule is consistent with other rules.

## Maintenance Checklist
When closing a major feature or refactor task, run this check:

- [ ] **Architecture**: Did I create new folders? -> Update `project_architecture.md`.
- [ ] **Patterns**: Did I change how we handle DI, Navigation, or Errors? -> Update `coding_standards.md`.
- [ ] **Libraries**: Did I add a new KMP dependency? -> Update `tech_stack.md`.
- [ ] **Workflows**: Did I discover a better way to work? -> Update `workflows/*.md`.

### 4. Post-Update Verification

After updating documentation:
- [ ] Run a test implementation using the updated rule to verify accuracy
- [ ] Confirm no conflicts with other rules in `.agent/rules/` or `.agent/skills/`
- [ ] Update the `LAST_UPDATED` comment in the rule file (if applicable)

