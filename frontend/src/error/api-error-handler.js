import store from "@/store/storage";
import router from "@/router/router";

export async function apiErrorHandler(error) {

    alert(error.response.data.errorMessage)

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

