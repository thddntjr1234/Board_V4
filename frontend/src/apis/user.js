import {authApiService, nonAuthApiService} from "@/apis/service"

function getMyInfo() {
    return authApiService.get('user')
}

function signin(data) {
    return nonAuthApiService.post('user/signin', data)
}

export {
    getMyInfo,
    signin
}