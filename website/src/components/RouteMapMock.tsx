"use client";

import { useEffect, useRef, useState } from "react";

// Static, styled mockup of the routing feature — NOT a live map. The route
// line draws itself when scrolled into view (horizon-line motion principle);
// prefers-reduced-motion shows it fully drawn via globals.css.
export default function RouteMapMock() {
  const ref = useRef<HTMLDivElement>(null);
  const [inView, setInView] = useState(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setInView(true);
          observer.disconnect();
        }
      },
      { threshold: 0.4 },
    );
    observer.observe(el);
    return () => observer.disconnect();
  }, []);

  return (
    <div
      ref={ref}
      className={`rounded-lg border border-cream/10 bg-charcoal p-7 ${inView ? "is-inview" : ""}`}
    >
      <div className="mb-5 flex items-center justify-between">
        <div>
          <div className="font-display text-[15px] font-bold">Route preview</div>
          <div className="font-mono text-[11px] uppercase tracking-[0.05em] text-steel">
            AMARILLO → FLAGSTAFF
          </div>
        </div>
        <span className="rounded-[3px] bg-rust/20 px-2 py-1 font-mono text-[10px] uppercase tracking-[0.05em] text-amber-bright">
          Mockup
        </span>
      </div>
      <svg viewBox="0 0 340 150" fill="none" className="w-full" role="img" aria-label="Illustration of a planned truck route arcing between two stops over a ridgeline horizon">
        <path
          d="M0,110 L60,90 L110,105 L160,75 L210,95 L260,70 L340,88"
          stroke="#3D2F5C"
          strokeWidth="4"
          opacity="0.8"
        />
        <line x1="10" y1="130" x2="330" y2="130" stroke="#8A8378" strokeWidth="2" strokeDasharray="6 6" />
        <path
          className="route-draw"
          pathLength={1}
          d="M35,130 Q100,30 170,30 Q240,30 305,130"
          stroke="#B8677A"
          strokeWidth="3"
        />
        <circle cx="35" cy="130" r="6" fill="#F4A15C" />
        <circle cx="305" cy="130" r="6" fill="#F4A15C" />
        {/* Waypoint stops along the corridor, plus the pulsing rig position. */}
        <circle cx="123" cy="41.5" r="3" fill="#C7C0B4" opacity="0.7" />
        <circle cx="217" cy="41.5" r="3" fill="#C7C0B4" opacity="0.7" />
        <circle className="you-dot" cx="170" cy="30" r="5" fill="#C4502A" />
      </svg>
      <div className="mt-5 border-t border-cream/10 pt-4 font-mono text-[13px] uppercase tracking-[0.05em] text-steel-light">
        ETA 04:12 · MILE 1,204 · DIESEL $3.89/GAL
      </div>
    </div>
  );
}
