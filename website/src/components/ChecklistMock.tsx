import { CanopyGlyph } from "./motifs";

// Pre-trip checklist UI mock, ported from design/direction-board.html §04.
export default function ChecklistMock() {
  return (
    <div className="rounded-lg border border-cream/10 bg-charcoal p-7">
      <div className="mb-5 flex items-center gap-2.5">
        <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-md bg-gradient-to-br from-amber to-rust">
          <CanopyGlyph />
        </div>
        <div>
          <div className="font-display text-[15px] font-bold">Pre-Trip Checklist</div>
          <div className="font-mono text-[11px] uppercase tracking-[0.05em] text-steel">
            132-POINT · CLASS A
          </div>
        </div>
      </div>
      <div className="mb-4">
        <div className="flex items-center justify-between font-mono text-[10px] uppercase tracking-[0.1em] text-steel">
          <span>Progress</span>
          <span className="text-amber">87 of 132</span>
        </div>
        <div
          className="mt-2 h-1 rounded-full bg-cream/10"
          role="img"
          aria-label="Inspection progress: 87 of 132 items complete"
        >
          <div className="h-1 w-2/3 rounded-full bg-gradient-to-r from-rust to-amber" />
        </div>
      </div>
      <ul>
        <li className="flex items-center gap-3 border-t border-cream/10 py-3.5">
          <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-amber/10" aria-hidden="true">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <circle cx="8" cy="8" r="6" stroke="#F4A15C" strokeWidth="2" />
            </svg>
          </span>
          <span className="flex-1">
            <span className="block text-sm font-semibold">Brake lines &amp; air hoses</span>
            <span className="block font-mono text-[11px] uppercase tracking-[0.05em] text-steel">
              SECTION 3 OF 12
            </span>
          </span>
          <span className="rounded-[3px] bg-rust/20 px-2 py-1 font-mono text-[10px] uppercase tracking-[0.05em] text-amber-bright">
            Pending
          </span>
        </li>
        <li className="flex items-center gap-3 border-t border-cream/10 py-3.5">
          <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-amber/10" aria-hidden="true">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M3,8 L6,11 L13,4" stroke="#F4A15C" strokeWidth="2" fill="none" />
            </svg>
          </span>
          <span className="flex-1">
            <span className="block text-sm font-semibold">Tires &amp; wheel fasteners</span>
            <span className="block font-mono text-[11px] uppercase tracking-[0.05em] text-steel">
              SECTION 2 OF 12
            </span>
          </span>
          <span className="rounded-[3px] bg-amber/20 px-2 py-1 font-mono text-[10px] uppercase tracking-[0.05em] text-amber">
            Done
          </span>
        </li>
      </ul>
    </div>
  );
}
