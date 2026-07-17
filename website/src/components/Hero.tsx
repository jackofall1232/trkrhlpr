import RigScene from "./RigScene";
import { APK_DOWNLOAD_URL } from "@/lib/release";

export default function Hero() {
  return (
    <section className="hero-sky grain relative flex min-h-screen flex-col justify-end overflow-hidden p-[8vw]">
      <RigScene />
      <div className="relative z-10 mb-16 max-w-2xl lg:mb-8">
        <span className="inline-block rounded-sm bg-amber-bright px-2.5 py-1 font-mono text-xs uppercase tracking-[0.15em] text-near-black">
          Route planning for the long haul
        </span>
        <h1 className="font-display mb-6 mt-5 text-[clamp(48px,8vw,96px)] font-extrabold leading-[0.95] tracking-[-0.01em] text-near-black [text-shadow:0_2px_0_rgba(255,255,255,0.08)]">
          Last Wagon
        </h1>
        <p className="max-w-[460px] text-lg font-medium text-near-black/90 sm:text-xl">
          Truck stops, pre-trip checks, and CDL prep — built for the cab, not
          the office.
        </p>
        <div className="mt-8 flex flex-wrap gap-4">
          {/* Sideload caveats + the no-platform-gating decision: see lib/release.ts */}
          <a
            href={APK_DOWNLOAD_URL}
            download
            className="rounded bg-near-black px-6 py-3.5 font-display text-sm font-bold uppercase tracking-wide text-cream transition-colors hover:text-amber-bright"
          >
            Download the APK
          </a>
          <a
            href="#how-it-works"
            className="rounded border-2 border-near-black px-6 py-3 font-display text-sm font-bold uppercase tracking-wide text-near-black transition-colors hover:bg-near-black hover:text-cream"
          >
            See how it works
          </a>
        </div>
      </div>
    </section>
  );
}
