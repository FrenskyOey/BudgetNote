---
name: clarify_requirements
description: Clarifies requirements by asking mandatory questions. Use when users request new features or complex changes.
---

# Requirement Clarification Skill

## Purpose
This skill ensures that the agent gathers ALL necessary information before generating any plan or implementation for a new feature. It prevents assumptions and ensures accuracy.

## When to Use
- When the user asks to implement a new feature.
- When the user requests a complex change that requires planning.
- Triggered automatically by the `implement_feature` workflow.

## Mode Selection

### Quick Mode (Use for Simple Tasks)
**Trigger if request is:**
- Adding a single UI element (button, text field, icon)
- Calling an existing API/UseCase
- Simple configuration change (color, text, timeout)
- Bug fix in existing code
- Adding logging or debug statements

**Quick Mode Process:**
1. Ask ONLY 1-2 clarifying questions (if needed):
   - "Where should this go?" (if ambiguous)
   - "Any specific requirements?" (styling, behavior)
2. Skip detailed API/Storage/UI questions
3. Proceed immediately to implementation

**Examples:**
- "Add logout button to settings" → Quick Mode ✅
- "Add app bar to login screen" → Quick Mode ✅
- "Change icon color to blue" → Quick Mode ✅

---

### Full Mode (Use for Complex Features)
**Trigger if request is:**
- New feature with multiple screens
- API integration (new endpoint)
- Database schema changes
- Complex business logic
- Architecture decisions

**Full Mode Process:**
Follow the detailed questions below

**Examples:**
- "Implement user profile feature" → Full Mode ✅
- "Add payment integration" → Full Mode ✅
- "Create news feed with caching" → Full Mode ✅

---

## Instructions

### 1. Analyze the Request
Read the user's request and identify missing information in these categories:
- **API/Backend**: Endpoint, response structure, pagination, authentication.
- **Storage**: Caching strategy, database schema, retention.
- **UI/UX**: Layout, interactions, loading states, error handling.
- **Business Logic**: Filtering, sorting, data transformations.

### 2. Formulate Questions (MANDATORY)
You MUST ask these questions if the information is missing.

**API Details:**
- What is the complete API URL/endpoint?
- Can you provide a sample JSON response or describe the structure?
- What are the pagination parameters? (page, limit, offset, etc.)

**Storage (if caching is needed):**
- Should we cache this data? Which fields?
- What table name should we use?
- What is the cache expiration strategy?

**Data Layer Architecture (for Repository implementation):**
- **Where is the source of truth for this data?**
  - a) **Database (local storage)**: Data fetched from remote, stored locally, DB emits Flow updates
    - Example: News Detail, User Profile
    - → Will use **CQS Pattern** (separate Query and Command)
  - b) **Remote API (direct call)**: No local storage, direct API response return
    - Example: Login, Submit Form
    - → Will use **Standard Repository Pattern**
  - c) **Mixed**: Some operations need local storage, others don't
    - → Specify which operations need local storage
    - → Will use CQS for local-backed operations only


**UI/UX:**
- What should be displayed in the list items?
- What happens on item click?
- Pull-to-refresh? Infinite scroll?

### 3. Interaction Loop
1.  **Ask**: Present the questions to the user.
2.  **Wait**: Stop and wait for the user's response.
3.  **Document**: Once answered, summarize the requirements.

## Constraints
- ❌ **NEVER** make assumptions about API structure or fields.
- ❌ **NEVER** proceed to planning without these answers.
- ❌ **NEVER** create fake example responses; ask the user for them.
