// Illustration motifs ported from design/direction-board.html §03.
// All are decorative; parents label the feature in text.

export function HopperCanopyMotif() {
  return (
    <svg viewBox="0 0 200 90" fill="none" className="h-[90px] w-full" aria-hidden="true">
      <path
        d="M20,70 L20,45 Q20,30 40,30 L160,30 Q180,30 180,45 L180,70"
        stroke="#F4A15C"
        strokeWidth="3"
        fill="rgba(244,161,92,0.08)"
      />
      <line x1="45" y1="30" x2="45" y2="70" stroke="#F4A15C" strokeWidth="1.5" opacity="0.5" />
      <line x1="70" y1="30" x2="70" y2="70" stroke="#F4A15C" strokeWidth="1.5" opacity="0.5" />
      <line x1="95" y1="30" x2="95" y2="70" stroke="#F4A15C" strokeWidth="1.5" opacity="0.5" />
      <line x1="120" y1="30" x2="120" y2="70" stroke="#F4A15C" strokeWidth="1.5" opacity="0.5" />
      <line x1="145" y1="30" x2="145" y2="70" stroke="#F4A15C" strokeWidth="1.5" opacity="0.5" />
      <circle cx="45" cy="75" r="7" fill="#C4502A" />
      <circle cx="95" cy="75" r="7" fill="#C4502A" />
      <circle cx="145" cy="75" r="7" fill="#C4502A" />
    </svg>
  );
}

export function RouteArcMotif() {
  return (
    <svg viewBox="0 0 200 90" fill="none" className="h-[90px] w-full" aria-hidden="true">
      <path
        d="M30,80 Q60,20 100,20 Q140,20 170,80"
        stroke="#B8677A"
        strokeWidth="3"
        fill="none"
      />
      <circle cx="100" cy="20" r="6" fill="#F4A15C" />
      <line x1="20" y1="80" x2="180" y2="80" stroke="#8A8378" strokeWidth="2" strokeDasharray="6 6" />
    </svg>
  );
}

export function RidgelineMotif() {
  return (
    <svg viewBox="0 0 200 90" fill="none" className="h-[90px] w-full" aria-hidden="true">
      <path
        d="M0,70 L60,50 L110,65 L160,35 L200,55"
        stroke="#3D2F5C"
        strokeWidth="4"
        fill="none"
        opacity="0.8"
      />
      <path
        d="M0,80 L50,68 L100,75 L150,58 L200,72"
        stroke="#F4A15C"
        strokeWidth="3"
        fill="none"
      />
    </svg>
  );
}

export function CanopyGlyph({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" width="18" height="18" fill="none" className={className} aria-hidden="true">
      <path d="M3,17 L3,11 Q3,8 8,8 L18,8 Q21,8 21,11 L21,17" stroke="#1A1410" strokeWidth="2" />
    </svg>
  );
}
