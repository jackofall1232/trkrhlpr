import { CanopyGlyph } from "./motifs";

// Slim overlay header: wordmark left, always-reachable CTA right. Sits on
// the near-black top of the hero sky, so light text is safe.
export default function SiteHeader() {
  return (
    <header className="absolute inset-x-0 top-0 z-20 flex items-center justify-between px-[8vw] py-6">
      <a href="#" className="flex items-center gap-2.5" aria-label="Last Wagon — home">
        <CanopyGlyph stroke="#F4A15C" />
        <span className="font-mono text-xs uppercase tracking-[0.2em] text-cream">
          Last Wagon
        </span>
      </a>
      <a
        href="#download"
        className="rounded border border-amber/40 px-4 py-2 font-mono text-[11px] uppercase tracking-[0.1em] text-amber transition-colors hover:bg-amber hover:text-near-black"
      >
        Get the APK
      </a>
    </header>
  );
}
