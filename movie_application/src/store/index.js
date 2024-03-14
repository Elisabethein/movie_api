import {createStore} from 'vuex'

export default createStore({
    state: {
        user: null,
    },
    getters: {
        getUser: (state) => state.user,
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
    },
    actions: {
        loginUser({commit}, user) {
            commit('setUser', user);
        },
        logoutUser({commit}) {
            commit('setUser', null);
        },
    },
    modules: {}
})