import type { Metadata, Viewport } from "next";
import { Archivo, Space_Mono } from "next/font/google";
import "./globals.css";

// "Archivo Expanded" is the wdth=125 cut of this variable font, selected
// with `font-stretch` by the .font-display utility in globals.css.
const archivo = Archivo({
  subsets: ["latin"],
  axes: ["wdth"],
  variable: "--font-archivo",
});

const spaceMono = Space_Mono({
  subsets: ["latin"],
  weight: ["400", "700"],
  variable: "--font-space-mono",
});

export const metadata: Metadata = {
  title: "Last Wagon — Truck routing, pre-trip checks & CDL prep",
  description:
    "Truck stops, pre-trip inspections, and CDL prep — built for the cab, not the office. An offline-first Android app for commercial drivers.",
};

export const viewport: Viewport = {
  themeColor: "#1A1410",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={`${archivo.variable} ${spaceMono.variable}`}>
      <body className="bg-near-black font-sans text-cream antialiased">
        {children}
      </body>
    </html>
  );
}
