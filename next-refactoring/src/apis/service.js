import axios from "axios";

import {checkValidToken} from "@/utils/token";

const instance = axios.create({
    baseURL: '/api/'
})

instance.interceptors.request.use(
    (config) => {
        if (checkValidToken()) {
            config.headers["Authorization"] = `Bearer ${localStorage.getItem("token")}`
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


