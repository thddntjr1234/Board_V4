import apiService from "@/apis/service"

function getMyInfo() {
    return apiService.get('user')
}

function signin(data) {
    return apiService.post('user/signin', data)
}

export {
    getMyInfo,
    signin
}