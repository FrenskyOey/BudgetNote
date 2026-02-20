---
name: ui_wireframe_interview
description: Interactive design interview to create component wireframes. Use during Phase 4.1 before ui-spec creation.
---

# UI Wireframe Interview Skill

## Purpose
Guide user through structured design discussion to create component hierarchy and visual references before implementing UI.

## When to Use
- During Phase 4.1 of `implement_feature` workflow
- After Domain context validation
- Before creating `ui-spec.md`

---

## Execution Steps

### Step 1: Request Design Reference

**Prompt the user**:
> "Please provide a design reference (screenshot, mockup, or description) for the **[FeatureName] screen**."
> 
> You can upload an image or describe the layout.

**Wait**: for image upload or description

**Accept**: Screenshots, mockups, hand-drawn sketches, or text descriptions

---

### Step 2: Analyze & Extract Components

**After receiving the design reference:**

1. **Identify UI Layers**:
   - Navigation (TopAppBar, BottomBar, Tabs)
   - Content sections (Header, Body, Footer)
   - Reusable components (Cards, List items, Buttons)
   - Interactive elements (FABs, Dialogs, Sheets)

2. **Create Component Hierarchy**:

```
Screen: [FeatureName]Screen
├── TopAppBar
│   ├── Back Button
│   ├── Title
│   └── Actions (Share, etc.)
├── Header Section
│   ├── Hero Image
│   └── Category Badge
├── Metadata Section
│   ├── Author Info
│   ├── Published Date
│   └── Read Time
├── Content Section
│   ├── Paragraph
│   ├── Quote (highlighted)
│   └── ...
└── Footer Section
    └── Tags
```

3. **Present to User**:
> "Based on your design, I've identified these components:
> 
> [Show hierarchy]
> 
> Does this structure match your vision? Any adjustments?"

**Wait**: for confirmation or corrections

---

### Step 3: Component-by-Component Interview

**For EACH component in the structure (top to bottom):**

#### 3.1 Announce Component
> "Let's design: **[ComponentName]**"

#### 3.2 Ask Targeted Questions

**For TopAppBar**:
- "What app bar style? (Basic/BackStack/Search/Content/Custom)"
- "What title/actions should it have?"

**For Data Display Components** (Lists, Cards, Detail sections):
- "What data fields should be shown?"
- "What's the layout/arrangement?"
- "Upload a reference image (optional)"

**For Interactive Elements** (Buttons, FABs, Icons):
- "What happens on click/interaction?"
- "Should it trigger navigation, API call, or UI state change?"

**For Complex Components** (Custom views, animations):
- "Describe the visual behavior"
- "Upload a reference image or GIF"

#### 3.3 Image Request (Optional)
> "Upload a design reference for **[ComponentName]** if you have one (helps with accuracy)."

**Wait**: for response or "skip"

#### 3.4 Document Details
Record in working notes:
- Component name
- Data sources (ViewModel state fields)
- Interactions/callbacks
- Visual reference (if provided)

---

### Step 4: Generate Wireframe Documentation

**Create structured output for `ui_plan.md`**:

```markdown
## Component Wireframe

### Screen Structure
\`\`\`
[FeatureName]Screen
├── TopAppBar (CoreBackStackAppBar)
├── ScrollableContent
│   ├── NewsDetailHeader
│   ├── NewsAuthor
│   ├── NewsContent (LazyColumn)
│   └── NewsDetailTags
\`\`\`

### Component Details

#### TopAppBar
- **Type**: CoreBackStackAppBar
- **Title**: Article title from ViewModel
- **Actions**: Share icon (triggers share effect)
- **Navigation**: Back to news feed

#### NewsDetailHeader
- **Data**: image URL, category
- **Layout**: Full-width hero image with overlay badge
- **Dependencies**: Coil for image loading

#### NewsAuthor
- **Data**: author name, avatar, publication, publishedAt
- **Layout**: Horizontal row with avatar + text column
- **Interaction**: Click to view author profile (future)

[... etc for each component ...]
```

**Add to `ui_plan.md`**: Append this wireframe section

---

### Step 5: Pass to ui-spec Phase

**Output Summary**:
> "Wireframe complete! We've defined:
> - [X] components
> - [Y] interactive elements
> - [Z] data bindings
> 
> This structure will guide the `ui-spec.md` creation."

**Next Step**: Proceed to creating detailed `ui-spec.md` using this wireframe as skeleton

---

## Best Practices

### DO
- ✅ Ask ONE component at a time (avoid overwhelming user)
- ✅ Use visual hierarchy (parent → children)
- ✅ Reference Core components when applicable
- ✅ Document data sources for each component
- ✅ Save uploaded images to artifacts for reference

### DON'T
- ❌ Skip the wireframe and go straight to coding
- ❌ Ask all questions at once (use iterative approach)
- ❌ Assume component structure without user input
- ❌ Ignore uploaded design references

---

## Example Interaction

**Agent**:
> "Please provide a design reference for the **News Detail screen**."

**User**: [Uploads screenshot]

**Agent**:
> "Based on your design, I've identified these components:
> 
> ```
> NewsDetailScreen
> ├── TopAppBar (back + share)
> ├── Header (hero image + category)
> ├── Author (avatar + name + date)
> ├── Content (paragraphs + quotes)
> └── Tags (chips)
> ```
> 
> Does this match your vision?"

**User**: "Yes, looks good"

**Agent**:
> "Let's design: **TopAppBar**
> 
> What app bar style? (Basic/BackStack/Search/Content/Custom)"

**User**: "BackStack with share"

**Agent**:
> "Let's design: **Header**
> 
> Should the category badge be fixed position or overlay on image? Upload a reference if helpful."

[... continues for each component ...]

---

## Integration Points

**Called By**: `implement_feature` workflow, Phase 4.1  
**Calls**: None (terminal skill)  
**Outputs**: Wireframe structure for `ui_plan.md` and `ui-spec.md`

---

## Verification Checklist

Before completing wireframe interview:
- [ ] All major components identified
- [ ] Component hierarchy established
- [ ] Data sources mapped for each component
- [ ] Interactive elements documented
- [ ] User confirmed structure
- [ ] Wireframe added to `ui_plan.md`
