#!/usr/bin/env bash
set -euo pipefail
shopt -s nullglob

BASE="$(pwd)"
TS="$(date +%Y%m%d_%H%M%S)"

echo "[1/6] BASE = $BASE"

# 백업
echo "[2/6] Creating backup..."
tar -czf "_backup_before_unify_${TS}.tar.gz" eternal_guardian 5th_bond_fate 2>/dev/null || true

move_one() {
  local src="$1"
  local dest="$2"

  [[ -e "$src" ]] || return 0
  mkdir -p "$dest"

  local name target base ext
  name="$(basename "$src")"
  target="$dest/$name"

  if [[ -e "$target" ]]; then
    if [[ "$name" == *.* ]]; then
      base="${name%.*}"
      ext="${name##*.}"
      target="$dest/${base}__dup_${TS}.${ext}"
    else
      target="$dest/${name}__dup_${TS}"
    fi
  fi

  mv "$src" "$target"
}

move_dir_contents() {
  local from="$1"
  local dest="$2"

  [[ -d "$from" ]] || return 0
  mkdir -p "$dest"

  local p
  for p in "$from"/* "$from"/.[!.]* "$from"/..?*; do
    [[ -e "$p" ]] || continue
    move_one "$p" "$dest"
  done

  rmdir "$from" 2>/dev/null || true
}

init_set() {
  local set="$1"
  shift

  mkdir -p "$BASE/$set/00_meta"
  mkdir -p "$BASE/$set/01_overview/raw_grids"
  mkdir -p "$BASE/$set/01_overview/selected_refs"
  mkdir -p "$BASE/$set/01_overview/support_refs"
  mkdir -p "$BASE/$set/01_overview/set_full"
  mkdir -p "$BASE/$set/03_selected_by_weapon_type"
  mkdir -p "$BASE/$set/99_archive/unclassified_root_png"
  mkdir -p "$BASE/$set/99_archive/old_empty_dirs"

  for weapon in \
    01_greatsword \
    02_katana \
    03_spear \
    04_shortsword \
    05_doublesword \
    06_gun \
    07_staff \
    08_orbs \
    09_arks \
    support_future
  do
    mkdir -p "$BASE/$set/03_selected_by_weapon_type/$weapon/selected"
    mkdir -p "$BASE/$set/03_selected_by_weapon_type/$weapon/support"
  done

  for arch in "$@"; do
    mkdir -p "$BASE/$set/02_archetypes/$arch/weapons/raw"
    mkdir -p "$BASE/$set/02_archetypes/$arch/weapons/selected"
    mkdir -p "$BASE/$set/02_archetypes/$arch/weapons/support"
    mkdir -p "$BASE/$set/02_archetypes/$arch/costumes/raw"
    mkdir -p "$BASE/$set/02_archetypes/$arch/costumes/selected"
    mkdir -p "$BASE/$set/02_archetypes/$arch/costumes/support"

    [[ -f "$BASE/$set/02_archetypes/$arch/notes.md" ]] || {
      cat > "$BASE/$set/02_archetypes/$arch/notes.md" <<EOF
# ${set} / ${arch}

## Use
-

## Selected
-

## Support
-

## Avoid
- exact recreation
- exact emblems/logos
EOF
    }
  done

  [[ -f "$BASE/$set/00_meta/set_notes.md" ]] || {
    cat > "$BASE/$set/00_meta/set_notes.md" <<EOF
# ${set}

## Reference Level
private_homage

## Summary
-

## Core Theme
-

## Best Candidates
-

## Avoid
- exact weapon silhouette copy
- exact costume recreation
- exact symbols/logos/emblems
EOF
  }
}

absorb_selected_refs() {
  local set="$1"
  local dir="$BASE/$set/selected_refs"
  [[ -d "$dir" ]] || return 0

  local p name
  for p in "$dir"/*; do
    [[ -e "$p" ]] || continue
    name="$(basename "$p")"

    if [[ -d "$p" && "$name" =~ ^[0-9][0-9]_ ]]; then
      mkdir -p "$BASE/$set/03_selected_by_weapon_type/$name/selected"
      move_dir_contents "$p" "$BASE/$set/03_selected_by_weapon_type/$name/selected"
    else
      move_one "$p" "$BASE/$set/01_overview/selected_refs"
    fi
  done

  rmdir "$dir" 2>/dev/null || true
}

absorb_support_refs() {
  local set="$1"
  local dir="$BASE/$set/support_refs"
  [[ -d "$dir" ]] || return 0

  local p name
  for p in "$dir"/*; do
    [[ -e "$p" ]] || continue
    name="$(basename "$p")"

    if [[ -d "$p" && "$name" =~ ^[0-9][0-9]_ ]]; then
      mkdir -p "$BASE/$set/03_selected_by_weapon_type/$name/support"
      move_dir_contents "$p" "$BASE/$set/03_selected_by_weapon_type/$name/support"
    else
      move_one "$p" "$BASE/$set/01_overview/support_refs"
    fi
  done

  rmdir "$dir" 2>/dev/null || true
}

echo "[3/6] Initializing standard folders..."

init_set eternal_guardian \
  warrior_male \
  martial_artist \
  hunter \
  mage_specialist \
  assassin

init_set 5th_bond_fate \
  warrior_male \
  warrior_female \
  martial_artist \
  hunter \
  mage \
  assassin \
  specialist

echo "[4/6] Reorganizing eternal_guardian..."

SET="$BASE/eternal_guardian"

# 기존 루트 하위 폴더 흡수
move_dir_contents "$SET/raw_grids" "$SET/01_overview/raw_grids"
absorb_selected_refs "eternal_guardian"
absorb_support_refs "eternal_guardian"

# notes
if [[ -f "$SET/reference_notes.md" ]]; then
  move_one "$SET/reference_notes.md" "$SET/00_meta"
  if [[ ! -f "$SET/00_meta/set_notes.md" && -f "$SET/00_meta/reference_notes.md" ]]; then
    mv "$SET/00_meta/reference_notes.md" "$SET/00_meta/set_notes.md"
  fi
fi

# overview 파일
for f in "$SET"/*fullset*.png "$SET"/*full_set*.png; do
  [[ -e "$f" ]] && move_one "$f" "$SET/01_overview/set_full"
done

for f in "$SET"/*grid*.png; do
  [[ -e "$f" ]] && move_one "$f" "$SET/01_overview/raw_grids"
done

# warrior_male weapon/costume root png 정리
for f in "$SET"/*.png; do
  name="$(basename "$f")"

  case "$name" in
    *armor*.png|*costume*.png)
      move_one "$f" "$SET/02_archetypes/warrior_male/costumes/raw"
      ;;
    *greatsword*.png|*hammer*.png|*holyknight*.png|*warlord*.png|*lance_shield*.png|*sword*.png)
      move_one "$f" "$SET/02_archetypes/warrior_male/weapons/raw"
      ;;
    *)
      move_one "$f" "$SET/99_archive/unclassified_root_png"
      ;;
  esac
done

echo "[5/6] Reorganizing 5th_bond_fate..."

SET="$BASE/5th_bond_fate"

# warrior_male
if [[ -d "$SET/warrior_male" ]]; then
  for f in "$SET/warrior_male"/*.md; do
    [[ -e "$f" ]] && move_one "$f" "$SET/00_meta"
  done

  for f in "$SET/warrior_male"/*.png; do
    name="$(basename "$f")"
    case "$name" in
      *armor*.png|*costume*.png|*full*.png)
        move_one "$f" "$SET/02_archetypes/warrior_male/costumes/raw"
        ;;
      *)
        move_one "$f" "$SET/02_archetypes/warrior_male/weapons/raw"
        ;;
    esac
  done

  rmdir "$SET/warrior_male" 2>/dev/null || true
fi

# hunter
if [[ -d "$SET/hunter" ]]; then
  move_dir_contents "$SET/hunter" "$SET/02_archetypes/hunter/weapons/raw"
fi

# martial_artist
if [[ -d "$SET/martial_artist" ]]; then
  move_dir_contents "$SET/martial_artist" "$SET/02_archetypes/martial_artist/weapons/raw"
fi

# 혹시 기존 selected/support가 있으면 흡수
absorb_selected_refs "5th_bond_fate"
absorb_support_refs "5th_bond_fate"

# root png 남은 것 처리
for f in "$SET"/*.png; do
  [[ -e "$f" ]] && move_one "$f" "$SET/99_archive/unclassified_root_png"
done

# index 생성
echo "[6/6] Writing indexes..."

for set in eternal_guardian 5th_bond_fate; do
  if [[ -d "$BASE/$set" ]]; then
    {
      echo "# File Index - $set"
      echo
      echo "Generated: $TS"
      echo
      find "$BASE/$set" -type f | sed "s#^$BASE/$set/##" | sort
    } > "$BASE/$set/00_meta/file_index.md"
  fi
done

echo
echo "DONE."
echo "Backup: $BASE/_backup_before_unify_${TS}.tar.gz"
echo
echo "Check:"
echo "  find eternal_guardian 5th_bond_fate -maxdepth 4 -type d | sort"
