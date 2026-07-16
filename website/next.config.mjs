/** @type {import('next').NextConfig} */
const nextConfig = {
  // Keep this site static-export compatible (no API routes, no ISR, no
  // server actions) so it can move from Vercel to plain static hosting by
  // setting `output: 'export'` if that day comes.
  reactStrictMode: true,
};

export default nextConfig;
