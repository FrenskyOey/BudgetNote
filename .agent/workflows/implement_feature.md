---
description: Implement feature with Clean Architectire
---

# Feature Implementation Protocol

**Trigger**: User requests a new feature or complex change.

## // turbo-all

---

## Session 1: Planning & Requirements

### Phase 0: Requirement Clarification (MANDATORY)

1. **Activate `clarify_requirements` skill**
2. Ask mandatory questions
3. **WAIT** for user responses

### Phase 1: Planning

1. **Create task.md** in artifacts directory
   - Break down feature into checklist items
   - Organize by layer (Domain → Data → UI → Integration)

2. **Create Persistent Plan Files (IN SOURCE TREE)**:
   - Create these files within the project source directories (e.g. `src/commonMain/kotlin/feature/...`)
   - **DO NOT** create them as temporary artifacts.
   - Files:
     - `feature/[name]/domain/domain-plan.md`
     - `feature/[name]/data/data-plan.md`
     - `feature/[name]/ui/ui-plan.md`

3. **Request Review**:
   - Present the plan files to the user.
   - **WAIT** for approval.

### Phase 1.5: Address User Concerns (If Raised)

**Trigger**: User identifies issues with the plan.

1. **Create `improvements_summary.md`** (Artifact)
   - For each concern: Problem → Solution → Code Examples → Impact

2. **Request re-approval** - WAIT for approval

3. **Update plans** if needed

### **STOP POINT**
**Constraint**: Do NOT implement code in this session.
**Action**: State: "Planning Complete. Please start a **new session/chat window** to implement Phase 2 (Domain). Reference `domain-plan.md`."

---

## Session 2: Domain Implementation

**Trigger**: New Session + "Implement Phase 2" (referencing `domain-plan.md`)

### Phase 2: Domain Layer

1. **Review Plan**: Read `feature/[name]/domain/domain-plan.md`.

2. Implement models (`domain/model/`)
3. Implement repository interface (`domain/repository/`)

4. **TDD (MANDATORY)**:
   - Activate `tdd_implementation` skill
   - Write FAILING test for UseCase
   - Show test failure
   - Implement UseCase
   - Show test passing

5. **Verify**: Run all domain tests, update task.md

### **STOP POINT**
**Constraint**: Do NOT implement Data layer in this session.
**Action**: State: "Domain Complete. Please start a **new session/chat window** to implement Phase 3 (Data). Reference `data-plan.md`."

---

## Session 3: Data Implementation

**Trigger**: New Session + "Implement Phase 3" (referencing `data-plan.md`)

### Phase 3: Data Layer

**Pre-requisite**: Activate `data_implementation` skill

1. **Review Plan**: Read `feature/[name]/data/data-plan.md`.

2. Define structure:
   - `data/model/` (entity, request, response, mapper)
   - `data/api/[Feature]ApiService`
   - `data/datasource/[Feature]DataSource`

3. **TDD (MANDATORY)**:
   - Write FAILING test for Repository
   - Show test failure
   - Implement Repository + DataSources
   - Show tests passing

4. **DI Registration**: Update `di/[Feature]Module.kt`

5. **Verify**: Run all data tests, update task.md

### **STOP POINT**
**Constraint**: Do NOT implement UI layer in this session.
**Action**: State: "Data Complete. Please start a **new session/chat window** to implement Phase 4 (UI - Logic). Reference `ui-plan.md`."

---

## Session 4: UI Logic & Specs

**Trigger**: New Session + "Implement Phase 4.1/4.2" (referencing `ui-plan.md`)

### Phase 4.1: UI Design Specification

1. **Review Plan**: Read `feature/[name]/ui/ui-plan.md`.

2. **🆕 Validate Domain Context** (MANDATORY):
   - Read `feature/[name]/domain/model/*.kt` - List all domain models
   - Read `feature/[name]/domain/repository/*.kt` - List all repositories
   - Read `feature/[name]/domain/usecase/*.kt` - List all use cases
   - **Output Summary**:
     ```
     Domain Context for [Feature]:
     - Models: [list with key fields and types]
     - Repositories: [list with method signatures]
     - UseCases: [list with parameters and return types]
     ```
   - **Compare with ui-plan.md**: Flag any discrepancies (e.g., type mismatches, missing fields)
   - **Ask User**: "Domain analysis complete. The UI will use [models/types]. Any concerns?"
   - **WAIT** for confirmation

3. **🆕 Interactive Wireframe Generation** (MANDATORY):
   - **Activate `ui_wireframe_interview` skill**
   - Guide user through component-by-component design interview
   - Accept image uploads for visual references
   - Generate component hierarchy and wireframe structure
   - **Update `ui-plan.md`** with wireframe section
   - **WAIT** for wireframe approval

4. **Create `ui-spec.md`** (Artifact or Source File as requested):
   - Use wireframe as skeleton structure
   - Add detailed component specifications:
     - Component file locations (`components/[Name].kt`)
     - Data bindings (ViewModel state fields)
     - Interactions/callbacks
     - Design system usage (colors, spacing, typography)
   - Include Component Implementation Map table

5. **Request Review** - WAIT for approval

### Phase 4.2: State & ViewModel

**🔒 MANDATORY**: Activate `viewmodel_testing` skill (if exists)

1. **Check Skills**:
   - Does `viewmodel_testing` skill exist?
   - If YES: Load and follow patterns
   - If NO: Proceed with standard testing

2. Implement state classes (`ui/state/`)
   - `[Feature]State.kt` (UI state)
   - `[Feature]Event.kt` (user actions)
   - `[Feature]Effect.kt` (one-time effects)

3. Implement ViewModel (`ui/viewmodel/`)
   - Wire up use cases
   - Handle events
   - Manage effects

4. **🆕 Test ViewModel** (MANDATORY):
   - Create `[Feature]ViewModelTest.kt`
   - Follow `viewmodel_testing` skill patterns (if available)
   - Test state updates, event handling, effect emissions
   - Run tests and show passing

5. **Request Approval & STOP**:
   - WAIT for approval.
   - **Constraint**: Do NOT implement Components/Screens in this session.
   - **Action**: State: "UI Logic Complete. Please start a **new session/chat window** to implement Phase 4.3 (Components & Screens). Reference `ui-spec.md`."

---

## Session 5: UI Implementation

**Trigger**: New Session + "Implement Phase 4.3" (referencing `ui-spec.md`)

### Phase 4.3: Component Implementation (Iterative)

**🔒 MANDATORY**: Activate `ui_implementation` skill  
**🔒 MANDATORY**: Prepare `ui_validation` skill for each component

**For EACH component defined in `ui-spec.md`**:

1. **Load Skills**:
   - Read `ui_implementation` skill (MUST exist)
   - Prepare `ui_validation` skill

2. **Announce**: "Now implementing: `components/[ComponentName].kt`"  
   ☝️ Note: Always create in `components/` folder (one component per file)

3. **Implement**: 
   - Create `ui/components/[ComponentName].kt`
   - Follow design system rules (no hardcoded values)
   - Use wireframe specifications from `ui-spec.md`

4. **Validate** (AUTOMATIC - not optional):
   - Run `ui_validation` skill
   - Scan for hardcoded colors, spacing, typography
   - Auto-fix violations
   - State "UI Validation: [PASS/FAIL with fixes applied]"

5. **Request Approval** - WAIT

6. **Update task.md**

### Phase 4.4: Screen Composition

1. Create screen composables (`ui/screen/`)
2. Wire to ViewModel
3. Handle loading/error/empty states
4. **Request Approval** - WAIT

### Phase 4.5: Navigation & Integration

1. **Setup DI**:
   - Create/update `di/[Feature]Module.kt`
   - Register in ALL platforms:
     - Android: `androidMain/.../MyApp.kt`
     - iOS: `iosMain/.../KoinHelper.kt`
     - Desktop: `desktopMain/.../Main.kt`

2. **Setup Navigation**:
   - Add routes to navigation graph
   - Add navigation arguments
   - Setup deep links

3. **Verify Build**: Compile project

4. **Request Testing** - WAIT for feedback

---

## Completion

- User tests feature
- Fix bugs
- Cleanup unused code
- Mark task complete

---

## Quick Reference

**Key Skills**:
- `clarify_requirements` - Phase 0
- `tdd_implementation` - Phase 2 & 3
- `data_implementation` - Phase 3
- `ui_implementation` - Phase 4.3
- `ui_validation` - Phase 4.3

**Mandatory Checkpoints**:
- ✅ TDD for Domain & Data layers
- ✅ UI validation for all components
- ✅ DI registration on all platforms
- ✅ User approval at each phase
- ✅ **Start New Session** between major phases
