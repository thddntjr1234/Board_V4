import apiService from "@/apis/service"

/**
 * 자신의 유저 정보를 요청하는 API
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
function getMyInfo() {
    return apiService.get('user')
}

/**
 * ID, PW를 전달해 AccessToken을 반환받는 로그인 요청 API
 * @param data
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
function signin(data) {
    return apiService.post('user/signin', data)
}

export {
    getMyInfo,
    signin
}