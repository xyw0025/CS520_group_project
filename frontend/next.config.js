/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ['storage.googleapis.com'],
  },
  experimental: {
    typedRoutes: true,
  },
};

module.exports = nextConfig;
