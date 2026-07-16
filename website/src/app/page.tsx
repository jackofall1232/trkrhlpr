import SiteHeader from "@/components/SiteHeader";
import Hero from "@/components/Hero";
import Features from "@/components/Features";
import HowItWorks from "@/components/HowItWorks";
import DownloadCta from "@/components/DownloadCta";
import Footer from "@/components/Footer";

export default function Home() {
  return (
    <>
      <SiteHeader />
      <main>
        <Hero />
        <Features />
        <HowItWorks />
        <DownloadCta />
      </main>
      <Footer />
    </>
  );
}
