# Last Wagon — Marketing site

Marketing/landing site for the Last Wagon Android app (lastwagon.app). This is
NOT the app itself — no auth, no real routing logic. Its job is to drive APK
downloads and preview the product.

## Run

```sh
npm install
npm run dev    # http://localhost:3000
npm run build  # production build
```

## Stack

- Next.js (App Router) + TypeScript
- Tailwind CSS 3 — the locked palette lives in `tailwind.config.ts`
- Fonts via `next/font/google`

Deploy target is Vercel. The site deliberately avoids API routes, ISR, and
server actions so it can fall back to `output: 'export'` static hosting
(e.g. Bluehost) later. Flag it explicitly if a feature request would break
static export.

## Design system (LOCKED)

`design/direction-board.html` is the canonical visual direction: palette,
type, illustration language, and the working hero animation. Port from it;
don't recreate from memory. Rules:

- No colors outside the locked palette (see `tailwind.config.ts`).
- Display type is Archivo at `font-stretch: 125%` (the `.font-display`
  utility). "Archivo Expanded" is not a separate Google Fonts family — it's
  the wdth=125 cut of the Archivo variable font; loading it as a family
  name silently falls back to a default font.
- Body text never below weight 400; mono (Space Mono) for data/labels.
- Dark-mode-first, single mode for v1 — no light theme toggle.
- Motion is horizontal/horizon-line, steady and mechanical; everything
  respects `prefers-reduced-motion` (see `globals.css`).
- Never literally draw a covered wagon — the ribbed hopper trailer is the
  abstracted motif.

## Placeholders (intentional, pending decisions)

- CTA copy and the APK download link (`#download` anchors) — real signed-APK
  distribution is not approved yet.
- The route map panel is a static illustrated mockup, not live data.
