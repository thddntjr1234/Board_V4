import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router"
import store from "./store/storage"
import "bootstrap/dist/css/bootstrap.min.css" // [bootstrap]
import "bootstrap" // [bootstrap]


createApp(App).use(store).use(router).mount('#app')