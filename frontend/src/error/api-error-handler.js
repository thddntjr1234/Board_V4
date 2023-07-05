import store from "@/store/storage";
import router from "@/router/router";

/**
 * api 요청 중 발생한 에러를 axios interceptor에서 반환하면 try-catch 문에 의해 이 함수에서 에러가 처리됨
 * @param error
 * @returns {Promise<void>}
 */
export async function apiErrorHandler(error) {

    alert(error.response.data.errorMessage)

    /**
     * HTTP status code값과 error code 값별로 별도의 행동을 수행
     * @type {{400: httpStatusErrorHandler.400, 401: httpStatusErrorHandler.401, 403: httpStatusErrorHandler.403, 404: httpStatusErrorHandler.404}}
     */
    const httpStatusErrorHandler = {
        400: () => {

        },
        401: () => {
            if (store.getters.isValidToken) {
                store.commit("removeToken")
            }
        },
        403: (errorCode) => {
            switch (errorCode) {
                case 40301:
                    router.back()
                    break

                default:
                    break
            }
        },
        404: (errorCode) => {
            switch (errorCode) {
                case 40401:
                    break
                default:
                    router.push({name: 'not-found'})
            }
        }
    }

    if (error.response && error.response.status in httpStatusErrorHandler) {
        let errorCode = null
        if (error.response.data.errorCode) {
            errorCode = error.response.data.errorCode
        }
        httpStatusErrorHandler[error.response.status](errorCode);
    }
}

