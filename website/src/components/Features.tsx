import { HopperCanopyMotif, RidgelineMotif, RouteArcMotif } from "./motifs";

const FEATURES = [
  {
    title: "Truck routing & stops",
    body: "Truck-aware routes with driver review built in, and the stops that matter along the corridor — not car directions in disguise.",
    motif: RouteArcMotif,
  },
  {
    title: "Pre-trip inspection helper",
    body: "A 131-point checklist with study mode and real inspection mode, ordered the way you actually walk the truck — with progress you can see at a glance.",
    motif: HopperCanopyMotif,
  },
  {
    title: "CDL test prep",
    body: "Authoritative study material and practice tests for your class and endorsements, plus one practical safety question every day.",
    motif: RidgelineMotif,
  },
] as const;

export default function Features() {
  return (
    <section className="border-b border-cream/10 px-[8vw] py-20">
      <span className="mb-4 block font-mono text-xs uppercase tracking-[0.15em] text-amber">
        01 — What it does
      </span>
      <h2 className="font-display text-3xl font-extrabold tracking-[-0.01em] sm:text-4xl">
        Three jobs, one app
      </h2>
      <div className="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {FEATURES.map(({ title, body, motif: Motif }) => (
          <article
            key={title}
            className="rounded-md border border-cream/10 bg-gradient-to-br from-plum-deep to-charcoal p-8"
          >
            <Motif />
            <h3 className="font-display mt-6 text-lg font-bold">{title}</h3>
            <p className="mt-3 text-sm leading-relaxed text-steel-light">{body}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
