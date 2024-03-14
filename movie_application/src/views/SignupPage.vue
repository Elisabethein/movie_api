<template>
  <body>
  <section>
    <div class="container">
      <p class="single"><br>Create a new account</p>
      <form @submit.prevent="SignUp" class="login-form">
        <label :class="{ 'shake': shaking }">
          Email
          <input v-model="email" type="text" placeholder="Email" required/>
        </label>
        <label>
          Password
          <input v-model="password" type="password" placeholder="Password" required/>
        </label>
        <button type="submit">Signup</button>
        <!-- Display error message if email is already taken -->
        <div v-if="signupError" class="signup-error">
          {{ signupError }}
        </div>
      </form>
      <p>
        Already have an account?
        <router-link to="/login" class="links" active-class="links-hover">Login</router-link>
      </p>
    </div>
  </section>
  </body>
</template>

<script>
import {mapActions} from 'vuex';

export default {
  name: "SignUp",
  data() {
    return {
      email: '',
      password: '',
      shaking: false,
      signupError: null,
    };
  },
  computed: {},
  methods: {
    ...mapActions(['loginUser']),
    async SignUp() {
      const url = new URL('http://localhost:8080/api/signup');
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
        this.signupError = 'Email is already taken';
        this.shaking = true;
        setTimeout(() => {
          this.shaking = false;
        }, 300);
      }
    },
  },
  watch: {
    password: 'validatePassword',
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
  background-color: #D1DEDE;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #FADDE1;
}

section {
  padding-top: 20px;
  text-align: center;
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

.password-validation p, .password-validation li {
  margin-top: 10px;
  color: rgb(227, 84, 84);
  font-size: 11px;
  text-align: justify;
}

.shake {
  animation: shake 0.3s;
}

@keyframes shake {
  10%, 90% {
    transform: translate3d(-1px, 0, 0);
  }

  20%, 80% {
    transform: translate3d(2px, 0, 0);
  }

  30%, 50%, 70% {
    transform: translate3d(-4px, 0, 0);
  }

  40%, 60% {
    transform: translate3d(4px, 0, 0);
  }
}

.signup-error {
  color: rgb(227, 84, 84);
  font-size: 12px;
  padding-top: 10px;
}

</style>
    