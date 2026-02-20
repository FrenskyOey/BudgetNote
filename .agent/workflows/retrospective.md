---
description: Generate a concise retrospective report analyzing today's work, identifying gaps, and recommending improvements
---

# Retrospective Protocol

**Trigger**: User runs `@[/retrospective]` at end of session or after completing major work.

**Objective**: Provide a short, actionable report on what worked, what didn't, and what needs improvement.

---

## Execution Steps

### 1. Analyze Today's Work

**Review:**
- Artifacts created in this conversation (task.md, implementation_plan.md, walkthrough.md)
- Code files modified (check git status or recent edits)
- Tests created/modified
- Workflows and skills used

**Identify:**
- Features implemented
- Bugs fixed
- Architecture decisions made
- Patterns introduced

---

### 2. Evaluate Process Compliance

**Check:**
- ✅ Was `/implement_feature` workflow followed?
- ✅ Was `task.md` created and maintained?
- ✅ Were tests written first (TDD)?
- ✅ Did `ui_validation` auto-trigger for UI components?
- ✅ Were skills activated appropriately?

**Note violations:**
- Which steps were skipped?
- Why were they skipped?
- Was it intentional or oversight?

---

### 3. Identify Gaps

**Ask:**
- Were there patterns invented that should become Knowledge Items?
- Did we encounter problems that required multiple iterations?
- Are there missing skills that would have helped?
- Are there rules that need updating?
- Did workflows match actual process?

---

### 4. Generate Report

**Create a SHORT report (max 2 pages) with:**

#### Section 1: Summary (3-5 bullets)
- What was accomplished today
- Key metrics (files changed, tests added, features completed)
+ **Token usage**: ~X tokens this session
+ **Efficiency**: Y tokens per feature/fix
+ **Repeated patterns**: Z instructions given 3+ times

#### Section 2: What Worked Well (3-5 bullets)
- Successful patterns
- Effective workflows/skills
- Good decisions
+ **Skills that saved time**: [list with estimated time saved]
+ **Rules that prevented errors**: [list with what they caught]

#### Section 3: Issues Encountered (3-5 bullets)
- Bugs found
- Process violations
- Workflow gaps
- Skill failures
+ **Token waste analysis**:
  - Instructions repeated 3+ times: [list]
  - Estimated tokens wasted: ~X
  - Could be solved by: [skill/rule name]

#### Section 4: Recommendations (prioritized)
**HIGH PRIORITY** (do next session):
- Critical gaps to fix
- Rules to add/update
- Skills to create

**MEDIUM PRIORITY** (do this week):
- Workflow improvements
- Knowledge Items to create

**LOW PRIORITY** (nice to have):
- Optimizations
- Documentation updates

#### Section 5: Action Items (checklist)
- [ ] Specific, actionable tasks
- [ ] Assigned priority
- [ ] Estimated effort

---

## Output Format

Create `retrospective_[date].md` in artifacts directory with the structure above.

**Keep it SHORT:**
- Use bullet points
- No long explanations
- Focus on actionable items
- Prioritize ruthlessly

---

## Example Output

```markdown
# Retrospective: 2026-02-09

## Summary
- ✅ Implemented logout feature
- ✅ Fixed AuthRepository tests (password hashing bug)
- ✅ Created LoginViewModel tests (8 passing)
- 📊 3 files modified, 22 tests total

## What Worked Well
- SessionRepository pattern reused successfully
- TDD caught password hashing bug early
- UI updates were straightforward

## Issues Encountered
- UI validation didn't auto-trigger (LoginHeader, app bar)
- ViewModel tests required 4 iterations (no documented patterns)
- Tests written AFTER implementation (TDD not enforced)

## Recommendations

### HIGH PRIORITY
1. Create `viewmodel_testing` skill (save 3+ iterations)
2. Fix `ui_validation` auto-trigger mechanism
3. Enforce TDD for ViewModels/UseCases

### MEDIUM PRIORITY
4. Create Knowledge Item: SessionRepository pattern
5. Add "Quick Mode" to clarify_requirements

### LOW PRIORITY
6. Document ViewModel testing patterns in wiki

## Action Items
- [ ] HIGH: Create viewmodel_testing skill (30 min)
- [ ] HIGH: Update ui_validation trigger (15 min)
- [ ] MEDIUM: Create SessionRepository KI (20 min)
```

---

## Tips for Effective Retrospectives

1. **Be Honest**: Note violations even if they worked out
2. **Be Specific**: "UI validation didn't trigger" not "validation issues"
3. **Be Actionable**: Every recommendation should be doable
4. **Be Concise**: User should read in < 2 minutes
5. **Prioritize**: Not everything is urgent

---

## When to Run

- **End of day**: Review full day's work
- **After major feature**: Capture learnings while fresh
- **Before planning session**: Review what needs improvement
- **Weekly**: Aggregate patterns and trends