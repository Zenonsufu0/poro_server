# Greatsword variant rules

- This folder contains multiple greatsword variants.
- Each new greatsword attempt must live in its own subfolder.
- A variant folder should contain:
  - `design.md`
  - `task.md`
  - `refs/`
  - `source/<variant>.bbmodel`
  - optional `exports/`
- Do not mix multiple unrelated greatsword attempts directly in this folder.
- When working on one variant, stay inside that variant folder unless explicitly told otherwise.
- Do not use references from sibling variant folders unless explicitly asked.
- For design tasks, use the `/bb-design` skill.
- For modeling tasks, use the `/bb-build-phase` skill.
- For live Blockbench verification, use the `/bb-mcp-verify` skill.
