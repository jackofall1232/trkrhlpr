import RigIllustration from "./RigIllustration";

export default function DownloadCta() {
  return (
    <section
      id="download"
      className="relative overflow-hidden border-b border-cream/10 px-[8vw] py-24 text-center"
    >
      <div className="glow-amber pointer-events-none absolute inset-0" aria-hidden="true" />
      <span className="mb-4 block font-mono text-xs uppercase tracking-[0.15em] text-amber">
        03 — Get the app
      </span>
      <h2 className="font-display mx-auto max-w-2xl text-4xl font-extrabold uppercase tracking-[-0.01em] text-amber-bright sm:text-5xl">
        Ready when you roll out
      </h2>
      <p className="mx-auto mt-5 max-w-md text-base text-steel-light">
        Last Wagon is an Android app, distributed as a direct APK while we work
        toward the Play Store. Offline-first — it keeps working where coverage
        doesn&apos;t.
      </p>
      {/* TODO: point at the real signed APK once release distribution is approved */}
      <a
        href="#download"
        className="mt-9 inline-block rounded bg-amber px-8 py-4 font-display text-sm font-bold uppercase tracking-wide text-near-black transition-colors hover:bg-amber-bright"
      >
        Download the APK
      </a>
      <div className="mt-6 font-mono text-[11px] uppercase tracking-[0.1em] text-steel">
        DIRECT APK · ANDROID · GOOGLE PLAY COMING SOON
      </div>
      {/* Ghost rig rolling along the section's bottom border. */}
      <div aria-hidden="true">
        <RigIllustration
          className="mini-rig-track"
          colors={{
            body: "#8A8378",
            glass: "rgba(26,20,16,0.45)",
            smoke: "#8A8378",
            ribs: "rgba(26,20,16,0.6)",
            light: "#FFB870",
          }}
        />
      </div>
    </section>
  );
}
