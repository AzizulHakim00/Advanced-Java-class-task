# Afsos Nama V3 Redesign Notes

This pass focuses on the full visual component system rather than only page shells.

## Major fixes

- Sidebar navigation now owns the scroll area; the profile/logout card stays in the sidebar flow and cannot cover menu items.
- Native select elements remain in forms for Spring/Thymeleaf submission but are visually replaced by custom dropdowns.
- Dropdown options use colored identity tiles, hover states, a clean selected checkmark, keyboard navigation, and viewport-aware opening direction. No checkbox-looking option boxes are used.
- Date picker is compact, colorful, responsive, and opens above or below the trigger depending on available space. Only one popover can stay open at a time.
- Inputs, password fields, search fields, textareas, counters, helper/error states, filter controls, and buttons use one colorful component system.
- Afsos list now includes active filter chips and richer card/category/action colors.
- Dashboard, promises, confessions, report, profile, admin, login, and registration inherit the V3 color and surface system.
- Mobile popovers use bottom-sheet positioning and the existing navigation becomes a drawer/bottom navigation experience.

## Frontend stack

- Thymeleaf
- Custom CSS only (`style.css` + `design-v3.css`)
- Vanilla JavaScript (`app.js`)
- Local SVG icons

No Bootstrap or external UI framework is used.
