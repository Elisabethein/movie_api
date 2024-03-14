<template>
  <div class="filter-popup">
    <select v-model="selectedGenre" class="options">
      <option value="">All Genres</option>
      <option v-for="genre in genres" :key="genre" :value="genre">{{ genre }}</option>
    </select>
    <select v-model="selectedLanguage" class="options">
      <option value="">All Languages</option>
      <option v-for="language in languages" :key="language" :value="language">{{ language }}</option>
    </select>
    <select v-model="selectedAgeRestriction" class="options">
      <option value="">All Age Restrictions</option>
      <option v-for="restriction in restrictions" :key="restriction" :value="restriction">{{ restriction }}</option>
    </select>
    <select v-model="selectedBeginningTime" class="options">
      <option value="">All Beginning Times</option>
      <option v-for="time in startTimes" :key="time" :value="time">{{ time }}</option>
    </select>

    <!-- Apply buttons to trigger filtering -->
    <button @click="applyFilters" class="apply-button">Apply Filters</button>
    <b>OR</b>
    <button @click="chooseByHitsory()" class="apply-button">Suggest by history</button>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedGenre: '',
      selectedLanguage: '',
      selectedAgeRestriction: '',
      selectedBeginningTime: '',
      genres: [],
      languages: [],
      restrictions: [],
      startTimes: [],
      clientId: null,
      errorMessage: '',
    };
  },
  methods: {
    // Fetch the filtered sessions based on the selected filters
    applyFilters() {
      const url = new URL('http://localhost:8080/api/filteredSessions');
      url.searchParams.append('genre', this.selectedGenre);
      url.searchParams.append('language', this.selectedLanguage);
      url.searchParams.append('ageRestriction', this.selectedAgeRestriction);
      url.searchParams.append('time', this.selectedBeginningTime);
      fetch(url)
          .then(response => response.json())
          .then(data => {
            this.$emit('filtered-sessions', data);
          })
          .catch(error => {
            console.error('Error fetching filtered sessions:', error);
          });
    },
    // Fetch the filtered sessions based on the user's history
    chooseByHitsory() {
      this.clientId = this.$store.getters.getUser;
      console.log(this.clientId);
      // If the user is not logged in, suggest the user to log in
      if (isNaN(this.clientId) || this.clientId === null || this.clientId === undefined || this.clientId === '') {
        this.errorMessage = 'Please log in to use this feature.';
        this.clientId = null;
        return;
      }
      fetch(`http://localhost:8080/api/sessionByHistory/${this.clientId}`)
          .then(response => response.json())
          .then(data => {
            this.$emit('filtered-sessions', data);
          })
          .catch(error => {
            console.error('Error fetching filtered sessions:', error);
          });
    },
  },
  mounted() {
    // Fetch the filter options to display in the filter popup
    fetch('http://localhost:8080/api/allGenres')
        .then(response => response.json())
        .then(data => {
          this.genres = data;
        }).catch(error => {
      console.error('Error fetching genres:', error);
    });
    fetch('http://localhost:8080/api/allLanguages')
        .then(response => response.json())
        .then(data => {
          this.languages = data;
        })
        .catch(error => {
          console.error('Error fetching languages:', error);
        });
    fetch('http://localhost:8080/api/allAgeRestrictions')
        .then(response => response.json())
        .then(data => {
          this.restrictions = data;
        })
        .catch(error => {
          console.error('Error fetching age restrictions:', error);
        });
    fetch('http://localhost:8080/api/allStartTimes')
        .then(response => response.json())
        .then(data => {
          this.startTimes = data;
        })
        .catch(error => {
          console.error('Error fetching start times:', error);
        });
  },
};
</script>

<style scoped>
/* Style your filter popup as needed */
.filter-popup {
  color: #7d3541;
}

.apply-button {
  background-color: #7d3541;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
}

.apply-button:hover {
  background-color: #6f212e;
}

.options {
  margin: 5px;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #7d3541;
  color: #3a181e;
}
</style>