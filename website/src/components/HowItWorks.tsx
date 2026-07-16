import RouteMapMock from "./RouteMapMock";
import ChecklistMock from "./ChecklistMock";

const STEPS = [
  {
    n: "01",
    title: "Plan the route",
    body: "Set your rig's profile, review the route yourself, and see the corridor you'll actually drive.",
  },
  {
    n: "02",
    title: "Check the rig",
    body: "Walk the truck with the checklist in hand — visible progress means nothing gets skipped in the dark.",
  },
  {
    n: "03",
    title: "Study the downtime",
    body: "CDL practice and one practical safety question a day, offline in the sleeper.",
  },
] as const;

export default function HowItWorks() {
  return (
    <section id="how-it-works" className="border-b border-cream/10 px-[8vw] py-20">
      <span className="mb-4 block font-mono text-xs uppercase tracking-[0.15em] text-amber">
        02 — How it works
      </span>
      <h2 className="font-display text-3xl font-extrabold tracking-[-0.01em] sm:text-4xl">
        From the yard to the horizon
      </h2>
      <div className="mt-10 grid gap-10 lg:grid-cols-[1fr_1.2fr]">
        <ol className="space-y-8">
          {STEPS.map((step) => (
            <li key={step.n} className="flex gap-5">
              <span className="font-mono text-sm text-rust" aria-hidden="true">
                {step.n}
              </span>
              <div>
                <h3 className="font-display text-lg font-bold">{step.title}</h3>
                <p className="mt-2 max-w-md text-sm leading-relaxed text-steel-light">
                  {step.body}
                </p>
              </div>
            </li>
          ))}
        </ol>
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-1 xl:grid-cols-2">
          <RouteMapMock />
          <ChecklistMock />
        </div>
      </div>
    </section>
  );
}
