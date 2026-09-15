(() => {
    "use strict";

    const $ = (selector, root = document) => root.querySelector(selector);
    const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];

    const formatDate = (date) => new Intl.DateTimeFormat("en-GB", {
        day: "2-digit",
        month: "long",
        year: "numeric"
    }).format(date);

    const toIsoDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    };

    const parseIsoDate = (value) => {
        if (!/^\d{4}-\d{2}-\d{2}$/.test(value || "")) return null;
        const [year, month, day] = value.split("-").map(Number);
        const date = new Date(year, month - 1, day);
        return Number.isNaN(date.getTime()) ? null : date;
    };

    function initNavigation() {
        const sidebar = $("#appSidebar");
        const button = $("#mobileMenuButton");
        const backdrop = $("#sidebarBackdrop");
        const moreButton = $("[data-mobile-more]");

        if (!sidebar || !backdrop) return;

        const close = () => {
            sidebar.classList.remove("is-open");
            backdrop.classList.remove("is-visible");
            button?.setAttribute("aria-expanded", "false");
            document.body.style.overflow = "";
        };

        const open = () => {
            sidebar.classList.add("is-open");
            backdrop.classList.add("is-visible");
            button?.setAttribute("aria-expanded", "true");
            document.body.style.overflow = "hidden";
        };

        button?.addEventListener("click", () => {
            sidebar.classList.contains("is-open") ? close() : open();
        });
        moreButton?.addEventListener("click", open);
        backdrop.addEventListener("click", close);
        $$(".nav-link", sidebar).forEach((link) => link.addEventListener("click", close));
        window.addEventListener("keydown", (event) => {
            if (event.key === "Escape") close();
        });
    }

    function initConfirmation() {
        $$('[data-confirm]').forEach((form) => {
            form.addEventListener("submit", (event) => {
                const message = form.dataset.confirm || "Are you sure?";
                if (!window.confirm(message)) event.preventDefault();
            });
        });
    }

    function initLogoutModal() {
        const modal = $("#logoutModal");
        const form = $("#logoutForm");
        const openButton = $("[data-open-logout-modal]");
        const cancelButton = $("[data-close-logout-modal]");
        const confirmButton = $("[data-confirm-logout]");

        if (!modal || !form || !openButton || !cancelButton || !confirmButton) return;

        // Keep the modal at page level so sidebar/layout CSS can never size it.
        if (modal.parentElement !== document.body) document.body.appendChild(modal);

        const openModal = () => {
            document.dispatchEvent(new CustomEvent("afsos:close-popovers"));
            modal.hidden = false;
            modal.setAttribute("aria-hidden", "false");
            document.body.classList.add("logout-modal-open");
            requestAnimationFrame(() => {
                modal.classList.add("is-open");
                cancelButton.focus();
            });
        };

        const closeModal = () => {
            modal.classList.remove("is-open");
            modal.setAttribute("aria-hidden", "true");
            document.body.classList.remove("logout-modal-open");
            window.setTimeout(() => {
                modal.hidden = true;
                openButton.focus();
            }, 160);
        };

        openButton.addEventListener("click", openModal);
        cancelButton.addEventListener("click", closeModal);

        confirmButton.addEventListener("click", () => {
            confirmButton.disabled = true;
            form.requestSubmit();
        });

        modal.addEventListener("click", (event) => {
            if (event.target === modal) closeModal();
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && !modal.hidden) closeModal();
        });
    }

    function initPasswordToggles() {
        $$('[data-password-toggle]').forEach((button) => {
            button.addEventListener("click", () => {
                const input = document.getElementById(button.dataset.passwordToggle);
                if (!input) return;
                const reveal = input.type === "password";
                input.type = reveal ? "text" : "password";
                button.setAttribute("aria-label", reveal ? "Hide password" : "Show password");
                button.classList.toggle("is-active", reveal);
            });
        });
    }

    function initAlerts() {
        $$(".alert-close").forEach((button) => {
            button.addEventListener("click", () => {
                const alert = button.closest(".app-alert");
                if (!alert) return;
                alert.style.opacity = "0";
                alert.style.transform = "translateY(-8px)";
                setTimeout(() => alert.remove(), 180);
            });
        });
    }

    function initCharacterCounters() {
        $$('[data-char-count-for]').forEach((counter) => {
            const input = document.getElementById(counter.dataset.charCountFor);
            if (!input) return;
            const max = Number(input.getAttribute("maxlength")) || 0;
            const update = () => {
                counter.textContent = `${input.value.length}${max ? ` / ${max}` : ""}`;
            };
            input.addEventListener("input", update);
            update();
        });
    }

    function initControlTones() {
        const tones = ["blue", "violet", "cyan", "amber", "green", "coral", "pink"];
        const forced = {
            title: "coral",
            description: "blue",
            category: "cyan",
            level: "coral",
            status: "violet",
            relatedCourse: "blue",
            semester: "violet",
            consequence: "amber",
            lessonLearned: "coral",
            imageUrl: "green",
            username: "blue",
            password: "violet",
            regName: "blue",
            regEmail: "cyan",
            regPassword: "violet",
            confirmPassword: "coral"
        };

        $$(".form-field").forEach((field, index) => {
            const control = $("input[id], textarea[id], select[id]", field);
            field.dataset.tone = forced[control?.id] || tones[index % tones.length];
        });

        $$(".filter-form .search-field, .filter-form .select-field").forEach((field, index) => {
            field.style.setProperty("--field-accent", ["#3165f5", "#20aecd", "#f2772f", "#7559eb"][index % 4]);
        });
    }

    function initCustomSelects() {
        const selects = $$("select").filter((select) => !select.multiple && select.dataset.nativeSelect !== "true");
        if (!selects.length) return;

        const colorByValue = {
            ATTENDANCE: "#f2a20b", ASSIGNMENT: "#3165f5", EXAM: "#ef5a54",
            PRESENTATION: "#7559eb", GROUP_PROJECT: "#12aa7d", LATE_START: "#f2772f",
            SLEEP: "#5d6ee8", MONEY: "#e89b0b", COURSE_SELECTION: "#20aecd",
            TEACHER_ISSUE: "#12a89a", CAMPUS_LIFE: "#d8569d", OTHER: "#7e8ca0",
            LOW: "#12aa7d", MEDIUM: "#e89b0b", HIGH: "#ef6157", EXTREME: "#cf3555",
            NEW: "#3165f5", STILL_SUFFERING: "#f2772f", LESSON_LEARNED: "#7559eb",
            FIXED: "#12aa7d", REPEATED_AGAIN: "#df3f53",
            ACTIVE: "#3165f5", COMPLETED: "#12aa7d", BROKEN: "#ef6157"
        };

        const symbolByValue = {
            ATTENDANCE: "AT", ASSIGNMENT: "AS", EXAM: "EX", PRESENTATION: "PR",
            GROUP_PROJECT: "GP", LATE_START: "LS", SLEEP: "SL", MONEY: "$",
            COURSE_SELECTION: "CS", TEACHER_ISSUE: "TI", CAMPUS_LIFE: "CL", OTHER: "OT",
            LOW: "1", MEDIUM: "2", HIGH: "3", EXTREME: "!",
            NEW: "N", STILL_SUFFERING: "S", LESSON_LEARNED: "L", FIXED: "F", REPEATED_AGAIN: "R",
            ACTIVE: "A", COMPLETED: "C", BROKEN: "B"
        };

        const metaFor = (select) => {
            const key = `${select.id || ""} ${select.name || ""}`.toLowerCase();
            if (key.includes("category")) return { title: "Choose category", subtitle: "Pick the closest theme", symbol: "CAT" };
            if (key.includes("level")) return { title: "Choose impact level", subtitle: "How serious was it?", symbol: "LVL" };
            if (key.includes("status")) return { title: "Choose current status", subtitle: "Where does it stand now?", symbol: "STS" };
            if (key.includes("filter")) return { title: "Choose filter", subtitle: "Refine the list", symbol: "FLT" };
            return { title: "Choose an option", subtitle: "Select one value", symbol: "OPT" };
        };

        const closeAll = (except = null) => {
            $$(".custom-select-host.is-open").forEach((host) => {
                if (host === except) return;
                host.classList.remove("is-open", "opens-up");
                const trigger = $(".custom-select-trigger", host);
                const menu = $(".custom-select-menu", host);
                trigger?.setAttribute("aria-expanded", "false");
                if (menu) menu.hidden = true;
            });
        };

        document.addEventListener("afsos:close-popovers", (event) => closeAll(event.detail || null));

        selects.forEach((select, selectIndex) => {
            if (select.dataset.customSelectReady === "true") return;

            const host = select.closest(".select-shell, .select-field") || select.parentElement;
            if (!host) return;

            host.classList.add("custom-select-host");
            select.classList.add("native-select-control");
            select.dataset.customSelectReady = "true";

            const meta = metaFor(select);
            const trigger = document.createElement("button");
            trigger.type = "button";
            trigger.className = "custom-select-trigger";
            trigger.setAttribute("aria-haspopup", "listbox");
            trigger.setAttribute("aria-expanded", "false");
            trigger.setAttribute("aria-controls", `custom-select-menu-${selectIndex}`);

            const value = document.createElement("span");
            value.className = "custom-select-value";
            const chevron = document.createElement("span");
            chevron.className = "custom-select-chevron";
            chevron.setAttribute("aria-hidden", "true");
            trigger.append(value, chevron);

            const menu = document.createElement("div");
            menu.className = "custom-select-menu";
            menu.id = `custom-select-menu-${selectIndex}`;
            menu.setAttribute("role", "listbox");
            menu.hidden = true;

            const head = document.createElement("div");
            head.className = "custom-select-menu-head";
            const headSymbol = document.createElement("span");
            headSymbol.textContent = meta.symbol;
            const headCopy = document.createElement("div");
            const headTitle = document.createElement("strong");
            headTitle.textContent = meta.title;
            const headSubtitle = document.createElement("small");
            headSubtitle.textContent = meta.subtitle;
            headCopy.append(headTitle, headSubtitle);
            head.append(headSymbol, headCopy);
            menu.appendChild(head);

            const optionButtons = [...select.options].map((option, optionIndex) => {
                const button = document.createElement("button");
                button.type = "button";
                button.className = "custom-select-option";
                button.setAttribute("role", "option");
                button.dataset.optionIndex = String(optionIndex);
                button.disabled = option.disabled;

                const optionValue = option.value || "";
                const accent = colorByValue[optionValue] || (optionValue ? "#5d6ee8" : "#8794a6");
                button.style.setProperty("--option-accent", accent);
                if (!optionValue) button.classList.add("is-placeholder-option");

                const avatar = document.createElement("span");
                avatar.className = "custom-select-option-avatar";
                avatar.setAttribute("aria-hidden", "true");
                avatar.textContent = symbolByValue[optionValue] || (optionValue ? option.textContent.trim().slice(0, 2).toUpperCase() : "--");

                const label = document.createElement("span");
                label.className = "custom-select-option-label";
                label.textContent = option.textContent.trim();

                const check = document.createElement("span");
                check.className = "custom-select-option-check";
                check.setAttribute("aria-hidden", "true");
                button.append(avatar, label, check);

                button.addEventListener("click", (event) => {
                    event.preventDefault();
                    event.stopPropagation();
                    if (button.disabled) return;
                    select.selectedIndex = optionIndex;
                    select.dispatchEvent(new Event("input", { bubbles: true }));
                    select.dispatchEvent(new Event("change", { bubbles: true }));
                    sync();
                    closeAll();
                    trigger.focus();
                });

                menu.appendChild(button);
                return button;
            });

            const sync = () => {
                const selectedOption = select.options[select.selectedIndex] || select.options[0];
                const selectedText = selectedOption ? selectedOption.textContent.trim() : "Select an option";
                const accent = colorByValue[selectedOption?.value] || "#7e8ca0";
                host.style.setProperty("--select-accent", accent);
                value.textContent = selectedText;
                value.classList.toggle("is-placeholder", !select.value);
                optionButtons.forEach((button, index) => {
                    const active = index === select.selectedIndex && Boolean(select.value);
                    button.classList.toggle("is-selected", active);
                    button.setAttribute("aria-selected", String(active));
                });
            };

            const open = () => {
                document.dispatchEvent(new CustomEvent("afsos:close-popovers", { detail: host }));
                closeAll(host);
                const rect = trigger.getBoundingClientRect();
                const roomBelow = window.innerHeight - rect.bottom;
                const roomAbove = rect.top;
                host.classList.toggle("opens-up", roomBelow < 300 && roomAbove > roomBelow);
                host.classList.add("is-open");
                menu.hidden = false;
                trigger.setAttribute("aria-expanded", "true");
                requestAnimationFrame(() => {
                    const active = $(".custom-select-option.is-selected:not(:disabled)", menu)
                        || $(".custom-select-option:not(:disabled)", menu);
                    active?.scrollIntoView({ block: "nearest" });
                });
            };

            const close = () => {
                host.classList.remove("is-open", "opens-up");
                menu.hidden = true;
                trigger.setAttribute("aria-expanded", "false");
            };

            trigger.addEventListener("click", (event) => {
                event.preventDefault();
                event.stopPropagation();
                host.classList.contains("is-open") ? close() : open();
            });

            trigger.addEventListener("keydown", (event) => {
                const enabled = optionButtons.filter((button) => !button.disabled);
                if (!enabled.length) return;
                const current = optionButtons[select.selectedIndex];
                let enabledIndex = Math.max(0, enabled.indexOf(current));

                if (event.key === "Escape") {
                    close();
                    return;
                }
                if (event.key === "Enter" || event.key === " ") {
                    event.preventDefault();
                    host.classList.contains("is-open") ? close() : open();
                    return;
                }
                if (event.key === "ArrowDown" || event.key === "ArrowUp") {
                    event.preventDefault();
                    if (!host.classList.contains("is-open")) open();
                    enabledIndex += event.key === "ArrowDown" ? 1 : -1;
                    enabledIndex = Math.max(0, Math.min(enabled.length - 1, enabledIndex));
                    enabled[enabledIndex].focus();
                }
            });

            menu.addEventListener("keydown", (event) => {
                const enabled = optionButtons.filter((button) => !button.disabled);
                const currentIndex = enabled.indexOf(document.activeElement);
                if (event.key === "Escape") {
                    event.preventDefault();
                    close();
                    trigger.focus();
                } else if (event.key === "ArrowDown" || event.key === "ArrowUp") {
                    event.preventDefault();
                    const delta = event.key === "ArrowDown" ? 1 : -1;
                    const nextIndex = Math.max(0, Math.min(enabled.length - 1, currentIndex + delta));
                    enabled[nextIndex]?.focus();
                } else if (event.key === "Home" || event.key === "End") {
                    event.preventDefault();
                    (event.key === "Home" ? enabled[0] : enabled[enabled.length - 1])?.focus();
                }
            });

            select.addEventListener("change", sync);
            host.append(trigger, menu);
            sync();
        });

        document.addEventListener("click", () => closeAll());
        window.addEventListener("resize", () => closeAll());
        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape") closeAll();
        });
    }

    function initDatePickers() {
        const pickers = $$('[data-date-picker]');
        if (!pickers.length) return;

        const setCalendarOpen = (picker, calendar, trigger, open) => {
            calendar.hidden = !open;
            picker.classList.toggle("is-calendar-open", open);
            trigger.setAttribute("aria-expanded", String(open));
        };

        const closeAll = (except = null) => {
            pickers.forEach((picker) => {
                if (picker === except) return;
                const calendar = $("[data-calendar]", picker);
                const trigger = $("[data-date-trigger]", picker);
                picker.classList.remove("calendar-opens-up");
                if (calendar && trigger) setCalendarOpen(picker, calendar, trigger, false);
            });
        };

        document.addEventListener("afsos:close-popovers", (event) => closeAll(event.detail || null));

        pickers.forEach((picker) => {
            const input = $("[data-date-input]", picker);
            const trigger = $("[data-date-trigger]", picker);
            const display = $("[data-date-display]", picker);
            const calendar = $("[data-calendar]", picker);
            const title = $("[data-calendar-title]", picker);
            const days = $("[data-calendar-days]", picker);
            const prev = $("[data-calendar-prev]", picker);
            const next = $("[data-calendar-next]", picker);
            const todayButton = $("[data-calendar-today]", picker);
            const clearButton = $("[data-calendar-clear]", picker);

            if (!input || !trigger || !display || !calendar || !title || !days) return;

            trigger.setAttribute("aria-haspopup", "dialog");
            trigger.setAttribute("aria-expanded", "false");
            calendar.setAttribute("role", "dialog");
            calendar.setAttribute("aria-label", "Choose a date");

            const maxToday = picker.dataset.maxToday === "true";
            const minToday = picker.dataset.minToday === "true";
            const today = new Date();
            today.setHours(0, 0, 0, 0);
            let selected = parseIsoDate(input.value);
            let view = selected ? new Date(selected) : new Date(today);

            const commit = (date) => {
                selected = date ? new Date(date.getFullYear(), date.getMonth(), date.getDate()) : null;
                input.value = selected ? toIsoDate(selected) : "";
                display.textContent = selected ? formatDate(selected) : "Select a date";
                input.dispatchEvent(new Event("change", { bubbles: true }));
                render();
            };

            const render = () => {
                title.textContent = new Intl.DateTimeFormat("en-US", {
                    month: "long",
                    year: "numeric"
                }).format(view);
                days.innerHTML = "";

                const first = new Date(view.getFullYear(), view.getMonth(), 1);
                const start = new Date(view.getFullYear(), view.getMonth(), 1 - first.getDay());

                for (let index = 0; index < 42; index += 1) {
                    const date = new Date(start);
                    date.setDate(start.getDate() + index);
                    date.setHours(0, 0, 0, 0);

                    const button = document.createElement("button");
                    button.type = "button";
                    button.className = "calendar-day";
                    button.textContent = String(date.getDate());
                    button.setAttribute("aria-label", formatDate(date));

                    if (date.getMonth() !== view.getMonth()) button.classList.add("is-outside");
                    if (date.getTime() === today.getTime()) button.classList.add("is-today");
                    if (selected && date.getTime() === selected.getTime()) button.classList.add("is-selected");
                    if (maxToday && date > today) button.disabled = true;
                    if (minToday && date < today) button.disabled = true;

                    button.addEventListener("click", () => {
                        if (button.disabled) return;
                        commit(date);
                        setCalendarOpen(picker, calendar, trigger, false);
                    });
                    days.appendChild(button);
                }
            };

            trigger.addEventListener("click", (event) => {
                event.preventDefault();
                event.stopPropagation();
                const willOpen = calendar.hidden;
                document.dispatchEvent(new CustomEvent("afsos:close-popovers", { detail: picker }));
                closeAll(picker);
                if (willOpen) {
                    const rect = trigger.getBoundingClientRect();
                    const roomBelow = window.innerHeight - rect.bottom;
                    const roomAbove = rect.top;
                    picker.classList.toggle("calendar-opens-up", roomBelow < 390 && roomAbove > roomBelow);
                    view = selected ? new Date(selected) : new Date(today);
                    render();
                } else {
                    picker.classList.remove("calendar-opens-up");
                }
                setCalendarOpen(picker, calendar, trigger, willOpen);
            });

            calendar.addEventListener("click", (event) => event.stopPropagation());
            prev?.addEventListener("click", () => {
                view = new Date(view.getFullYear(), view.getMonth() - 1, 1);
                render();
            });
            next?.addEventListener("click", () => {
                const candidate = new Date(view.getFullYear(), view.getMonth() + 1, 1);
                if (maxToday && candidate > new Date(today.getFullYear(), today.getMonth(), 1)) return;
                view = candidate;
                render();
            });
            todayButton?.addEventListener("click", () => {
                commit(today);
                view = new Date(today);
                setCalendarOpen(picker, calendar, trigger, false);
            });
            clearButton?.addEventListener("click", () => {
                commit(null);
                setCalendarOpen(picker, calendar, trigger, false);
            });

            if (selected) display.textContent = formatDate(selected);
            render();
        });

        document.addEventListener("click", () => closeAll());
        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape") closeAll();
        });
    }

    function initDonutCharts() {
        const palette = ["#1769ff", "#ff6554", "#ffb018", "#12a87d", "#7158e8", "#14b8d4", "#ef476f", "#8d6e63"];

        $$('[data-category-chart]').forEach((chart) => {
            const donut = $("[data-donut-chart]", chart);
            const items = $$('[data-value]', chart);
            if (!donut || !items.length) return;

            const values = items.map((item) => Math.max(0, Number(item.dataset.value) || 0));
            const total = values.reduce((sum, value) => sum + value, 0);
            if (!total) return;

            let cursor = 0;
            const segments = values.map((value, index) => {
                const start = cursor;
                cursor += (value / total) * 100;
                const color = palette[index % palette.length];
                items[index].style.setProperty("--chart-color", color);
                return `${color} ${start.toFixed(2)}% ${cursor.toFixed(2)}%`;
            });
            donut.style.background = `conic-gradient(${segments.join(",")})`;
        });
    }

    function initJourneyTimeline() {
        const board = $("[data-journey-timeline]");
        if (!board) return;

        const canvas = $(".journey-canvas", board);
        const pathSvg = $("[data-journey-path]", board);
        const nodes = $$(".journey-node", board);
        if (!canvas || !pathSvg || !nodes.length) return;

        const draw = () => {
            const spacing = 278;
            const leftPadding = 18;
            const minimumWidth = board.clientWidth - 44;
            const width = Math.max(minimumWidth, leftPadding * 2 + nodes.length * spacing);
            const height = 560;
            canvas.style.width = `${width}px`;

            nodes.forEach((node, index) => {
                node.style.left = `${leftPadding + index * spacing}px`;
            });

            const points = nodes.map((node) => {
                const dot = $(".journey-dot", node);
                return {
                    x: node.offsetLeft + node.offsetWidth / 2,
                    y: node.offsetTop + (dot?.offsetTop || 0) + (dot?.offsetHeight || 38) / 2
                };
            });

            let path = "";
            points.forEach((point, index) => {
                if (index === 0) {
                    path = `M ${point.x} ${point.y}`;
                    return;
                }
                const previous = points[index - 1];
                const middleX = (previous.x + point.x) / 2;
                path += ` C ${middleX} ${previous.y}, ${middleX} ${point.y}, ${point.x} ${point.y}`;
            });

            pathSvg.setAttribute("viewBox", `0 0 ${width} ${height}`);
            pathSvg.innerHTML = `
                <path d="${path}" fill="none" stroke="#c7d4e7" stroke-width="4" stroke-linecap="round" stroke-dasharray="4 11"></path>
                <path d="${path}" fill="none" stroke="url(#journeyGradient)" stroke-width="2" stroke-linecap="round" opacity=".7"></path>
                <defs>
                    <linearGradient id="journeyGradient" x1="0" y1="0" x2="1" y2="0">
                        <stop offset="0" stop-color="#1769ff"></stop>
                        <stop offset=".35" stop-color="#ff6554"></stop>
                        <stop offset=".65" stop-color="#ffb018"></stop>
                        <stop offset="1" stop-color="#12a87d"></stop>
                    </linearGradient>
                </defs>`;
        };

        draw();
        let resizeFrame;
        window.addEventListener("resize", () => {
            cancelAnimationFrame(resizeFrame);
            resizeFrame = requestAnimationFrame(draw);
        });
    }

    function initRoadmap() {
        const links = $$(".roadmap-step[href^='#']");
        const sections = links.map((link) => document.querySelector(link.getAttribute("href"))).filter(Boolean);
        if (!links.length || !sections.length || !("IntersectionObserver" in window)) return;

        const observer = new IntersectionObserver((entries) => {
            const visible = entries
                .filter((entry) => entry.isIntersecting)
                .sort((a, b) => b.intersectionRatio - a.intersectionRatio)[0];
            if (!visible) return;
            links.forEach((link) => link.classList.toggle("is-active", link.getAttribute("href") === `#${visible.target.id}`));
        }, { rootMargin: "-20% 0px -60%", threshold: [0.15, 0.35, 0.6] });

        sections.forEach((section) => observer.observe(section));
    }

    document.addEventListener("DOMContentLoaded", () => {
        initNavigation();
        initConfirmation();
        initLogoutModal();
        initPasswordToggles();
        initAlerts();
        initCharacterCounters();
        initControlTones();
        initCustomSelects();
        initDatePickers();
        initDonutCharts();
        initJourneyTimeline();
        initRoadmap();
    });
})();
