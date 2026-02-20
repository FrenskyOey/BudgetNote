---
description: Lightweight workflow for simple tasks (UI additions, config changes, bug fixes) without full planning overhead
---

# Quick Fix Workflow

**Trigger**: User requests a simple change that doesn't require architecture decisions.

## When to Use

✅ **Use Quick Fix for:**
- Adding a single UI element (button, icon, text field)
- Updating configuration (colors, timeouts, URLs)
- Bug fixes in existing code
- Adding logging/debug statements
- Simple text changes
- Renaming or moving code

❌ **Don't Use Quick Fix for:**
- New features with multiple screens
- API integration (new endpoints)
- Database schema changes
- Architecture decisions
- Complex business logic

---

## Process (4 Steps)

### 1. Quick Clarification (30 seconds)

**Activate `clarify_requirements` skill in Quick Mode**

Ask ONLY if ambiguous:
- "Where should this go?"
- "Any specific requirements?"

**Max 1-2 questions, then proceed.**

---

### 2. Implement (5-15 minutes)

**For UI Changes:**
- Read `ui_implementation` skill
- Make changes
- Run `ui_validation` skill
- Fix violations

**For Code Changes:**
- Make the change
- Follow existing patterns
- Keep it simple

**For Bug Fixes:**
- Identify root cause
- Fix the issue
- Remove any commented-out or leftover code from the fix
- Add test if missing

---

### 3. Verify (2-5 minutes)

**Always:**
- Compile the project
- Run relevant tests

**If UI change:**
- Check design system compliance

**If bug fix:**
- Verify the bug is fixed
- Check for regressions

---

### 4. Done

No walkthrough needed for quick fixes.

**Optional:** Add one-line summary to conversation:
> "✅ Added logout button to settings screen"

---

## Examples

### Example 1: Add Button
```
User: "Add a refresh button to the news screen"

AI: [Quick Mode] "Where on the screen? Any specific icon?"
User: "Top right, use refresh icon from Res"
AI: [Implements button using CoreIconButton]
AI: [Runs ui_validation - Pass]
AI: [Compiles - Success]
AI: "✅ Added refresh button to news screen"
```

### Example 2: Fix Bug
```
User: "Login button stays disabled after error"

AI: [Analyzes code]
AI: [Finds issue: loading state not cleared on error]
AI: [Fixes: Add loading = false in error handler]
AI: [Runs tests - Pass]
AI: "✅ Fixed login button state on error"
```

### Example 3: Config Change
```
User: "Change API timeout to 30 seconds"

AI: [Updates NetworkConfig.kt]
AI: [Compiles - Success]
AI: "✅ Updated API timeout to 30s"
```

---

## Time Estimates

| Task Type | Typical Duration |
|-----------|------------------|
| Add UI element | 5-10 min |
| Config change | 2-5 min |
| Bug fix | 10-20 min |
| Add logging | 2-5 min |
| Text/color change | 2-5 min |

**If it takes > 30 minutes, use `/implement_feature` instead.**

---

## Checklist

Quick fixes should:
- [ ] Take < 30 minutes
- [ ] Modify 1-3 files max
- [ ] Not change architecture
- [ ] Not require new dependencies
- [ ] Compile successfully
- [ ] Pass existing tests

If any checkbox fails → Use `/implement_feature`

---

## Quick Reference

**Workflow:** Clarify (1-2 Q) → Implement → Verify → Done

**Skills to use:**
- `clarify_requirements` (Quick Mode)
- `ui_implementation` (if UI)
- `ui_validation` (if UI)

**No need for:**
- task.md
- implementation_plan.md
- walkthrough.md
- Detailed planning

**Keep it simple, keep it fast!**
