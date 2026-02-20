# Design System Rules

## Mandatory Skill Activation
**ALWAYS** activate and read `.agent/skills/ui_implementation/SKILL.md` before writing any Compose UI code (`@Composable`).

## Strict Token Usage
- **NEVER** use hardcoded `dp` values (e.g., `16.dp`). Use `Spacing.*` or `Dimens.*`.
- **NEVER** use `Color(...)` or `MaterialTheme.colorScheme.*` directly. Use `get*Color()` helpers.
- **NEVER** use `MaterialTheme.typography.*`. Use `getText*()` helpers.
- **ALWAYS** use `core.theme.*` helpers.

## Core Component Usage
- **ALWAYS** prefer `Core*` components (e.g., `CoreButton`) over standard Compose Material components.
- **CHECK** `core/components/` before creating any new UI element.
