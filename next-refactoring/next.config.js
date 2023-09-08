/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false, // default : true, 그러나 컴포넌트에서 useEffect()가 두번 실행되는 것을 방지하기 위해 false로 설정
  async rewrites() {
    return [
      {
        // API 호출시 CORS 해결을 위한 리버스 프록시 등록
        source: "/api/:path*",
        destination: "http://localhost:8081/api/:path*"
      }
    ]
  }
}

module.exports = nextConfig
