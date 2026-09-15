# Afsos Nama Frontend Redesign V3

This project uses a fully custom frontend built with:

- Thymeleaf templates
- Base custom CSS (`src/main/resources/static/css/style.css`)
- Final colorful component layer (`src/main/resources/static/css/design-v3.css`)
- Vanilla JavaScript (`src/main/resources/static/js/app.js`)
- Local SVG logo and icon sprite (`src/main/resources/static/images/`)

No Bootstrap, Tailwind CSS, Flowbite, React, external icon library, CDN UI kit, or frontend package manager is used.

The Spring Boot routes, form field names, DTO bindings, services, repositories, and domain logic are preserved. The redesign is focused on the visual and interaction layer.

## V3 design system

- Deep navy sidebar with color-coded navigation icons and active states
- Sidebar navigation scrolls independently; profile/logout is anchored below it without overlaying menu items
- Colorful workspace headers, stat cards, reflection cards, promise boards, report sections, confession cards, profile panels, and admin surfaces
- Fully custom select menus with colored option tiles, selected check state, keyboard navigation, viewport-aware opening direction, and hidden native selects retained for form submission
- Compact custom calendar with gradient header, visible disabled dates, today/selected states, Today/Clear actions, and viewport-aware placement
- Color-accented text inputs, password fields, search fields, textareas, character counters, helper/error states, and buttons
- Active filter chips on My Afsos
- Responsive desktop/tablet/mobile behavior, including drawer navigation and mobile bottom-sheet style popovers
- Local SVG icon system throughout; no emoji-based interface controls
