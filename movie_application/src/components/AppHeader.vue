<template>
  <header>
    <div class="nav">
      <router-link to="/" @click="handleNavClick('/')"
                   :class="{ 'nav-button': true, 'pressed': isNavButtonPressed('/') }">Home
      </router-link>
      <router-link v-if="isLoggedIn" to="/login" @click="logout"
                   :class="{ 'nav-button': true, 'pressed': isNavButtonPressed('/login') }">Logout
      </router-link>
      <router-link v-if="!isLoggedIn" to="/login" @click="handleNavClick('/login')"
                   :class="{ 'nav-button': true, 'pressed': isNavButtonPressed('/login') }">Login
      </router-link>
      <router-link v-if="!isLoggedIn" to="/signup" @click="handleNavClick('/signup')"
                   :class="{ 'nav-button': true, 'pressed': isNavButtonPressed('/signup') }">Signup
      </router-link>
    </div>
  </header>
</template>

<script>

export default {
  data() {
    return {
      pressedNavButtons: [],
      isLogoutPressed: false,
    };
  },
  computed: {
    // change the header buttons based on the user's login status
    isLoggedIn() {
      return this.$store.getters.getUser !== null && this.$store.getters.getUser !== undefined && this.$store.getters.getUser !== '' && !isNaN(this.$store.getters.getUser);
    }
  },
  methods: {
    handleNavClick(route) {
      const index = this.pressedNavButtons.indexOf(route);
      if (index !== -1) {
        this.pressedNavButtons.splice(index, 1);
      } else {
        this.pressedNavButtons.push(route);
      }
      setTimeout(() => {
        this.resetNavButtonState();
      }, 100);
    },
    isNavButtonPressed(route) {
      return this.pressedNavButtons.includes(route);
    },
    resetNavButtonState() {
      this.pressedNavButtons = [];
    },
    // remove the user's id from the store
    logout() {
      this.isLogoutPressed = true;
      setTimeout(() => {
        this.isLogoutPressed = false;
      }, 100);
      this.$store.dispatch('logoutUser');
    },
  },
};
</script>

<style scoped>
header {
  position: relative;
  padding: 0.5em;
  background-color: #FADDE1;
  border-radius: 5px;
  opacity: 0.95;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50px;
  width: min-content;
  margin: 0 auto;
}

.nav {
  align-items: center;
  display: flex;

}

.nav-button {
  padding: 10px 15px;
  text-align: center;
  display: block;
  color: #ae5d6c;
  font-size: 0.99em;
  text-decoration: none;
  border: none;
  background: none;
  cursor: pointer;
}

.nav-button:hover {
  background-color: #ae5d6c;
  color: white;
  border-radius: 25px;
}

.nav-button.pressed {
  transform: scale(0.9);
}
</style>