<template>
  <div class="main-page-section">
    <h1>Hello, {{ user }}</h1>
    <h1>Welcome to Movie Application</h1>
    <h2>Here are movies for the next seven days</h2>
    <!-- Filters component -->
    <filter-popup @filtered-sessions="updateSessions"></filter-popup>

    <router-link v-for="session in sessions" :key="session.id" class="session-container"
                 :to="{name: 'ASessionPage', params: {id: session.id}}">
      <img src="@/assets/roman-skrypnyk-gjA24divsqw-unsplash.jpg" alt="movie poster" class="poster">
      <div class="session-info">
        <b>{{ movieTitles[session.id] }}</b>
        <p>{{ parseString(session.date) }}</p>
        <p>Starts at: {{ session.time }}</p>
      </div>
    </router-link>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import FilterPopup from '@/components/FilterPopup.vue';

export default {
  name: 'SessionsPage',
  components: {
    'filter-popup': FilterPopup,
  },
  data() {
    return {
      sessions: [],
      movies: [],
      movieTitles: {},
      user: 'Guest',
      clientId: this.$store.getters.getUser,
    };
  },
  computed: {
    ...mapGetters(['getUser']),
  },
  methods: {
    // This method is called when the filter-popup emits the filtered-sessions event
    updateSessions(filteredSessions) {
      this.sessions = filteredSessions;
    },
    // Fetch all sessions from the server
    async getAllSessions() {
      try {
        const response = await fetch(`http://localhost:8080/api/allSessions`);
        const data = await response.json();
        this.sessions = data;
        await this.fetchMovieTitles();
        return data;
      } catch (err) {
        console.error(err.message);
        throw err;
      }
    },
    // Fetch movie titles for each session
    async fetchMovieTitles() {
      try {
        for (const session of this.sessions) {
          const response = await fetch(`http://localhost:8080/api/movie/${session.movieId}`);
          const data = await response.json();
          this.movieTitles[session.id] = data.title;
        }
      } catch (err) {
        console.error(err.message);
      }
    },
    parseString(date) {
      const dateObj = new Date(date);
      return dateObj.toDateString();
    },
    // Fetch username for the current user to display on the page
    async getUsername(id) {
      if (isNaN(this.clientId) || this.clientId === null || this.clientId === undefined || this.clientId === '') {
        return 'Guest';
      }
      try {
        const response = await fetch(`http://localhost:8080/api/client/${id}`);
        const data = await response.json();
        console.log(data.username);
        this.user = data.username;
        return data.username;
      } catch (err) {
        console.error(err.message);
        throw err;
      }
    },
  },
  mounted() {
    this.getAllSessions();
    this.getUsername(this.clientId);
    console.log('mounted');
  },
};
</script>


<style scoped>
* {
  color: #7d3541;
  text-decoration: none;
}

h1, b {
  text-align: center;
}

.main-page-section {
  max-width: 1000px;
  margin: auto;
  padding: 20px;
  box-sizing: border-box;
  overflow: hidden; /* Add overflow to contain floated children */
}

/* Add a clearfix to handle floated children */
.main-page-section::after {
  content: "";
  display: table;
  clear: both;
}

.session-container {
  padding: 20px;
  background-color: #FADDE1;
  border-radius: 25px;
  margin: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
}

.poster {
  height: 200px;
  border-radius: 15px;;
}

.session-info {
  padding: 30px;
  font-size: 23px;
  width: 100%;
  background-color: #f3eaeb;
  border-radius: 15px;
}

.session-container:hover {
  transform: scale(1.05);
}

</style>
  