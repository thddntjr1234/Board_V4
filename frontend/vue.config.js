// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
module.exports = {
  outputDir: "../src/main/resources/static",
  devServer: {
    port: 8080,
    proxy: {
      '/boards/free/*': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
}