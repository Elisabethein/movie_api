import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

const storedUserId = localStorage.getItem('userId');

// Set user ID in Vuex store
if (storedUserId) {
    store.dispatch('loginUser', storedUserId);
  }


createApp(App).use(store).use(router).mount('#app')
