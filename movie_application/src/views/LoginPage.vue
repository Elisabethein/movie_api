<template>
  <body>
  <section>
    <div class="container">
      <p class="single"><br>Login</p>
      <form class="login-form" @submit.prevent="LogIn">
        <label>
          Email
          <input v-model="email" id="username" type="text" placeholder="Email" required/>
        </label>
        <label>
          Password
          <input v-model="password" id="password" type="password" placeholder="Password" required/>
        </label>
        <div v-if="loginError" class="login-error">
          Wrong password/email
        </div>
        <button id="login-button" @click="LogIn">Login</button>
      </form>
      <p>
        Don't have an account?
        <router-link to="/signup" class="links" active-class="links-hover">Signup</router-link>
      </p>
    </div>
  </section>
  </body>
</template>

<script>
import {mapActions} from 'vuex';

export default {
  name: 'LoginPage',
  data: function () {
    return {
      email: "",
      password: "",
      loginError: false,
    };
  },
  computed: {},
  methods: {
    ...mapActions(['loginUser']),
    async LogIn() {
      const url = new URL('http://localhost:8080/api/login');
      url.searchParams.append('username', this.email);
      url.searchParams.append('password', this.password);
      try {
        const response = await fetch(url);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        this.$store.dispatch('loginUser', data.id);
        this.$router.push('/');
      } catch (error) {
        this.loginError = true;
      }
    },
  },
};
</script>

<style scoped>
* {
  font-family: monaco, Consolas, Lucida Console, monospace;
  font-size: 16px;
  color: #7d3541;
}

p.single {
  line-height: 1.5;
  text-align: center;
  text-justify: inter-word;
}

.container {
  padding: 10px 10px 10px;
  max-width: 600px;
  margin: 40px auto 75px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #FADDE1;
}

section {
  padding-top: 20px;
  text-align: center;
  height: 100vh;
}

.links {
  color: #7d3541;
}

.links:hover {
  color: #ae5d6c;
}

.login-form {
  display: flex;
  flex-direction: column;
  max-width: 300px;
  margin: auto;
}

label {
  text-align: center;
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
}

input {
  padding: 10px;
  margin-bottom: 10px;
  width: 100%;
  box-sizing: border-box;
}

button {
  padding: 10px;
  background-color: #7d3541;
  color: #fff;
  border: none;
  cursor: pointer;
  width: 100%;
  box-sizing: border-box;
}

button:hover {
  background-color: #ae5d6c;
}

.login-error {
  color: rgb(227, 84, 84);
  font-size: 12px;
  padding-bottom: 20px;
}

</style>
  