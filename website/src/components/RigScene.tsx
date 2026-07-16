// Signature hero scene, ported from design/direction-board.html: layered
// dusk sky, sun disc, three parallax ridgelines, dashed road, and the
// traveling rig (cab, windshield, dual stacks with smoke, ribbed hopper
// trailer). Purely decorative — hidden from assistive tech.
export default function RigScene() {
  return (
    <div className="pointer-events-none absolute inset-0" aria-hidden="true">
      <div className="hero-sun" />
      <svg
        className="hero-ridge hero-ridge-back"
        viewBox="0 0 1440 200"
        preserveAspectRatio="none"
      >
        <path
          d="M0,140 L120,110 L260,150 L420,90 L600,130 L780,80 L960,140 L1140,100 L1320,150 L1440,120 L1440,200 L0,200 Z"
          fill="#2A2140"
        />
      </svg>
      <svg
        className="hero-ridge hero-ridge-mid"
        viewBox="0 0 1440 160"
        preserveAspectRatio="none"
      >
        <path
          d="M0,100 L180,60 L360,110 L540,50 L720,90 L900,40 L1080,100 L1260,70 L1440,100 L1440,160 L0,160 Z"
          fill="#3D2F5C"
        />
      </svg>
      <svg
        className="hero-ridge hero-ridge-front"
        viewBox="0 0 1440 100"
        preserveAspectRatio="none"
      >
        <path
          d="M0,60 L200,30 L400,55 L640,20 L880,50 L1120,15 L1440,45 L1440,100 L0,100 Z"
          fill="#1A1410"
        />
      </svg>
      <svg className="hero-road" viewBox="0 0 1440 4" preserveAspectRatio="none">
        <line
          x1="0"
          y1="2"
          x2="1440"
          y2="2"
          stroke="#1A1410"
          strokeWidth="4"
          strokeDasharray="24 18"
        />
      </svg>
      <svg className="rig-track" viewBox="0 0 210 60" fill="none">
        <g>
          <circle cx="27" cy="10" r="4" fill="#8A8378" opacity="0.35" />
          <circle cx="24" cy="4" r="5.5" fill="#8A8378" opacity="0.25" />
          <circle cx="30" cy="-2" r="6.5" fill="#8A8378" opacity="0.15" />
          <circle cx="16" cy="9" r="3.5" fill="#8A8378" opacity="0.3" />
          <circle cx="13" cy="3" r="5" fill="#8A8378" opacity="0.2" />
          <circle cx="18" cy="-3" r="6" fill="#8A8378" opacity="0.12" />
        </g>
        <rect x="14" y="10" width="4" height="18" rx="1.5" fill="#1A1410" />
        <rect x="25" y="10" width="4" height="18" rx="1.5" fill="#1A1410" />
        <path
          d="M4,46 L4,30 Q4,24 10,24 L34,24 L34,20 Q34,17 37,17 L40,17 Q43,17 43,20 L43,28 L48,28 Q54,28 54,34 L54,46 Z"
          fill="#1A1410"
          stroke="#1A1410"
          strokeWidth="1"
        />
        <path
          d="M11,30 L11,25 Q11,23 13,23 L32,23 L32,30 Z"
          fill="rgba(244,161,92,0.35)"
        />
        <rect x="54" y="40" width="10" height="6" fill="#1A1410" />
        <path
          d="M64,46 L64,26 Q64,20 72,20 L182,20 Q190,20 190,26 L190,46 Z"
          fill="rgba(26,20,16,0.85)"
          stroke="#1A1410"
          strokeWidth="1"
        />
        <line x1="80" y1="20" x2="80" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="96" y1="20" x2="96" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="112" y1="20" x2="112" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="128" y1="20" x2="128" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="144" y1="20" x2="144" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="160" y1="20" x2="160" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <line x1="176" y1="20" x2="176" y2="46" stroke="#1A1410" strokeWidth="1.5" opacity="0.5" />
        <circle cx="18" cy="48" r="6" fill="#1A1410" />
        <circle cx="46" cy="48" r="6" fill="#1A1410" />
        <circle cx="150" cy="48" r="6" fill="#1A1410" />
        <circle cx="164" cy="48" r="6" fill="#1A1410" />
        <circle cx="178" cy="48" r="6" fill="#1A1410" />
      </svg>
    </div>
  );
}
