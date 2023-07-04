import axios from "axios";
import store from "@/store/storage"

const instance = axios.create({
    baseURL: '/api/'
})

instance.interceptors.request.use(
    (config) => {
        if (store.getters.isValidToken) {
            config.headers["Authorization"] = `Bearer ${store.getters.getToken}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

instance.interceptors.response.use(
    function (response) {
        return response;
    },
    function (error) {
        throw error
    }
)

export default instance;


