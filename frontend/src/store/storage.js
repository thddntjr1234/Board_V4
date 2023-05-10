import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'

const store = createStore({
    state : {
        token: null
    },
    getters : {
        isValidToken(state) {
            return state.token != null
        },
        getToken(state) {
            return state.token
        }
    },
    mutations : { // commit으로 부를 수 있음
        setToken(state, _token) {
            state.token = _token
        },
        removeToken(state) {
            state.token = null
        }
    },
    actions: { // dispatch로 부를 수 있음
        setToken:({commit}, _token) => {
            commit('setToken', _token)
        },
        removeToken:({commit}) => {
            commit('removeToken')
        }
    },
    plugins: [createPersistedState()],
});

export default store