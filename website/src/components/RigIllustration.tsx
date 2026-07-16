type RigColors = {
  body?: string;
  glass?: string;
  smoke?: string;
  ribs?: string;
  light?: string;
};

// The signature rig: cab, amber-lit windshield, headlight, dual exhaust
// stacks with drifting smoke, and the ribbed hopper trailer that carries
// the covered-wagon-canopy motif. Color-parameterized so the same
// geometry works as the hero silhouette and as a steel "ghost" rig on
// dark ground. viewBox extends above y=0 so smoke puffs aren't clipped.
export default function RigIllustration({
  className,
  colors = {},
}: {
  className?: string;
  colors?: RigColors;
}) {
  const {
    body = "#1A1410",
    glass = "rgba(244,161,92,0.35)",
    smoke = "#8A8378",
    ribs = "#1A1410",
    light = "#FFB870",
  } = colors;

  return (
    <svg className={className} viewBox="0 -14 210 74" fill="none" aria-hidden="true">
      <g className="rig-smoke">
        <circle cx="27" cy="10" r="4" fill={smoke} opacity="0.35" />
        <circle cx="24" cy="4" r="5.5" fill={smoke} opacity="0.25" />
        <circle cx="30" cy="-2" r="6.5" fill={smoke} opacity="0.15" />
        <circle cx="16" cy="9" r="3.5" fill={smoke} opacity="0.3" />
        <circle cx="13" cy="3" r="5" fill={smoke} opacity="0.2" />
        <circle cx="18" cy="-3" r="6" fill={smoke} opacity="0.12" />
      </g>
      <rect x="14" y="10" width="4" height="18" rx="1.5" fill={body} />
      <rect x="25" y="10" width="4" height="18" rx="1.5" fill={body} />
      <path
        d="M4,46 L4,30 Q4,24 10,24 L34,24 L34,20 Q34,17 37,17 L40,17 Q43,17 43,20 L43,28 L48,28 Q54,28 54,34 L54,46 Z"
        fill={body}
        stroke={body}
        strokeWidth="1"
      />
      <path d="M11,30 L11,25 Q11,23 13,23 L32,23 L32,30 Z" fill={glass} />
      <circle cx="5.5" cy="32.5" r="1.6" fill={light} opacity="0.9" />
      <rect x="54" y="40" width="10" height="6" fill={body} />
      <path
        d="M64,46 L64,26 Q64,20 72,20 L182,20 Q190,20 190,26 L190,46 Z"
        fill={body}
        fillOpacity="0.85"
        stroke={body}
        strokeWidth="1"
      />
      <line x1="80" y1="20" x2="80" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="96" y1="20" x2="96" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="112" y1="20" x2="112" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="128" y1="20" x2="128" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="144" y1="20" x2="144" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="160" y1="20" x2="160" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <line x1="176" y1="20" x2="176" y2="46" stroke={ribs} strokeWidth="1.5" opacity="0.5" />
      <circle cx="18" cy="48" r="6" fill={body} stroke="rgba(199,192,180,0.22)" strokeWidth="1" />
      <circle cx="46" cy="48" r="6" fill={body} stroke="rgba(199,192,180,0.22)" strokeWidth="1" />
      <circle cx="150" cy="48" r="6" fill={body} stroke="rgba(199,192,180,0.22)" strokeWidth="1" />
      <circle cx="164" cy="48" r="6" fill={body} stroke="rgba(199,192,180,0.22)" strokeWidth="1" />
      <circle cx="178" cy="48" r="6" fill={body} stroke="rgba(199,192,180,0.22)" strokeWidth="1" />
    </svg>
  );
}
