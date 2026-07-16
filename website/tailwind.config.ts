import type { Config } from "tailwindcss";

// Locked Last Wagon palette — see design/direction-board.html.
// Do not add colors outside this set without explicit approval.
const config: Config = {
  content: ["./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        "near-black": "#1A1410",
        charcoal: "#241C16",
        amber: "#F4A15C",
        "amber-bright": "#FFB870",
        rust: "#C4502A",
        plum: "#3D2F5C",
        "plum-deep": "#2A2140",
        "dusty-rose": "#B8677A",
        steel: "#8A8378",
        "steel-light": "#C7C0B4",
        cream: "#F0E9DC",
      },
      fontFamily: {
        sans: ["var(--font-archivo)", "sans-serif"],
        mono: ["var(--font-space-mono)", "monospace"],
      },
    },
  },
  plugins: [],
};

export default config;
