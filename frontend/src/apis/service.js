import axios from "axios";
import store from "@/store/storage"

// 토큰을 설정하는 부분에 axios 인터셉터를 적용할 수도 있으나 별도로 인터셉터에서 수행할 것이 없기 때문에 사용하지 않는다.

function createAxiosServiceWithAuth() {
    console.log("인증서비스 생성, store 접근" + store.state.token)
    return axios.create({
        baseURL: '/api/',
        headers: {
            Authorization: `Bearer ${store.state.token}`,
        }
    })
}

function createAxiosServiceWithoutAuth () {
    return axios.create({
        baseURL: '/api/',
    })
}

export const nonAuthApiService = createAxiosServiceWithoutAuth()
export const authApiService = createAxiosServiceWithAuth()

