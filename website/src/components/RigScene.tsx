import RigIllustration from "./RigIllustration";

const STARS: Array<[number, number, number, number]> = [
  [60, 40, 1.1, 0.35],
  [140, 120, 1.4, 0.2],
  [230, 60, 1.0, 0.5],
  [320, 150, 0.9, 0.25],
  [410, 35, 1.2, 0.4],
  [500, 110, 0.8, 0.2],
  [590, 70, 1.5, 0.3],
  [680, 25, 1.0, 0.45],
  [760, 130, 0.9, 0.2],
  [850, 55, 1.3, 0.35],
  [940, 95, 0.8, 0.25],
  [1030, 30, 1.2, 0.5],
  [1120, 140, 1.0, 0.2],
  [1210, 65, 1.4, 0.3],
  [1300, 105, 0.9, 0.4],
  [1380, 45, 1.1, 0.25],
  [90, 200, 0.8, 0.15],
  [400, 220, 0.9, 0.12],
  [760, 210, 0.8, 0.15],
  [1100, 230, 0.9, 0.12],
  [1350, 190, 0.8, 0.15],
  [250, 250, 0.7, 0.1],
];

// Indices of stars that twinkle (motion-safe only), with staggered delays.
const TWINKLERS = new Map<number, string>([
  [2, "0s"],
  [7, "1.4s"],
  [11, "2.6s"],
  [14, "3.5s"],
  [4, "4.3s"],
]);

// Signature hero scene: starfield, haze-banded sun, three parallax
// ridgelines, dashed road, and the traveling rig. Purely decorative —
// hidden from assistive tech.
export default function RigScene() {
  return (
    <div className="pointer-events-none absolute inset-0" aria-hidden="true">
      <svg
        className="hero-stars"
        viewBox="0 0 1440 420"
        preserveAspectRatio="xMidYMin slice"
      >
        {STARS.map(([cx, cy, r, o], i) => (
          <circle
            key={i}
            cx={cx}
            cy={cy}
            r={r}
            fill="#F0E9DC"
            opacity={o}
            className={TWINKLERS.has(i) ? "star-tw" : undefined}
            style={TWINKLERS.has(i) ? { animationDelay: TWINKLERS.get(i) } : undefined}
          />
        ))}
      </svg>
      <svg className="hero-sun" viewBox="0 0 220 220">
        <defs>
          <radialGradient id="lw-sun-grad">
            <stop offset="0%" stopColor="#FFDFA8" />
            <stop offset="40%" stopColor="#FFB870" />
            <stop offset="70%" stopColor="#F4A15C" />
            <stop offset="100%" stopColor="#F4A15C" stopOpacity="0" />
          </radialGradient>
          {/* Horizon-haze slats: thin near the middle, heavier toward the
              base, like atmosphere stacking up at the horizon. */}
          <mask id="lw-sun-mask">
            <rect width="220" height="220" fill="white" />
            <rect x="0" y="126" width="220" height="3" fill="black" opacity="0.5" />
            <rect x="0" y="140" width="220" height="5" fill="black" opacity="0.65" />
            <rect x="0" y="158" width="220" height="7" fill="black" opacity="0.8" />
            <rect x="0" y="180" width="220" height="10" fill="black" opacity="0.95" />
          </mask>
        </defs>
        <circle cx="110" cy="110" r="110" fill="url(#lw-sun-grad)" mask="url(#lw-sun-mask)" />
      </svg>
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
      <RigIllustration className="rig-track" />
    </div>
  );
}
