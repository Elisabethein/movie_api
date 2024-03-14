<template>
  <div class="container">
    <div class="session-container">
      <div v-if="session && movie" class="session">
        <h1>{{ movie.title }}</h1>
        <p>Date: {{ session.date }}</p>
        <p>Starting time: {{ session.time }}</p>
        <p>Genre: {{ movie.genre }}</p>
        <p>IMDB rating: {{ movie.rating }}</p>
        <p>Language: {{ movie.language }}</p>
        <p v-if="movie.ageRestriction !== ''">Rated: {{ movie.ageRestriction }}</p>
      </div>
    </div>
    <b>Select how many seats would you like to purchase:</b><br>
    <input type="number" v-model="selectedSeats" placeholder="Enter number of seats">
    <button @click="applySelection(selectedSeats)">Apply</button>
    <p v-if="suggestedSeats.length > 0" class="suggested-seats">We suggest seats:</p>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <div id="seating-container" class="seating-container">
    </div>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    <button @click="purchaseSeats(chosenSeats)">Purchase seats</button>
  </div>
</template>

<script>

export default {
  name: "ASessionPage",
  data() {
    return {
      session: null,
      sessionId: null,
      movie: null,
      seatingPlan: null,
      selectedSeats: 0,
      successMessage: null,
      chosenSeats: [],
      suggestedSeats: [],
      errorMessage: '',
    };
  },
  methods: {
    // Fetch session info from the server
    async fetchInfo() {
      try {
        const response = await fetch(`http://localhost:8080/api/session/${this.sessionId}`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        this.session = data;
        // Fetch movie info and seating plan after session info is fetched
        await this.fetchMovieInfo();
        await this.fetchSeatingPlan();
      } catch (error) {
        console.error('Error fetching session info:', error);
      }
    },
    async fetchMovieInfo() {
      try {
        const response = await fetch(`http://localhost:8080/api/movie/${this.session.movieId}`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        this.movie = data;
      } catch (error) {
        console.error('Error fetching movie info:', error);
      }
    },
    async fetchSeatingPlan() {
      try {
        const response = await fetch(`http://localhost:8080/api/seatPlan/${this.sessionId}`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        // Create seating plan for visual representation
        await this.createSeatingPlan(data);
        this.seatingPlan = data;
      } catch (error) {
        console.error('Error fetching seating plan:', error);
      }
    },
    async createSeatingPlan(seatingPlan) {
      // Get the container element and create an SVG element to hold the seating plan
      const container = document.getElementById('seating-container');
      const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
      svg.setAttribute('width', '600');
      svg.setAttribute('height', '600');

      // Create a rectangle for each seat and add it to the SVG
      for (let row = 0; row <= seatingPlan.length; row++) {
        for (let col = 0; col < (row === seatingPlan.length ? seatingPlan[0].length : seatingPlan[row].length); col++) {
          // For the last row add column numbers
          if (row === seatingPlan.length) {
            const columnText = document.createElementNS('http://www.w3.org/2000/svg', 'text');
            columnText.setAttribute('x', col * 60 + 30);
            columnText.setAttribute('y', row * 60 + 30);
            columnText.setAttribute('text-anchor', 'middle');
            columnText.setAttribute('alignment-baseline', 'middle');
            columnText.setAttribute('fill', 'black');
            columnText.textContent = `${col + 1}`;
            svg.appendChild(columnText);
            continue;
          }

          // Create a group for each seat to hold the rectangle and the seat image
          const seat = seatingPlan[row][col];
          const group = document.createElementNS('http://www.w3.org/2000/svg', 'g');

          const rect = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
          rect.setAttribute('x', col * 60);
          rect.setAttribute('y', row * 60);
          rect.setAttribute('width', '60');
          rect.setAttribute('height', '60');

          // Choose the color based on the seat status (could be available, taken or suggested)
          const currentSeat = await this.getSeat(row + 1, col + 1, this.sessionId);
          const isSuggested = this.suggestedSeats.some(seat => {
            return seat.id === currentSeat.id;
          });
          rect.setAttribute('fill', seat === 'X' ? '#990033' : (isSuggested ? '#99FF66' : '#FFFFFF'));

          rect.setAttribute('stroke', 'gray');
          rect.setAttribute('stroke-width', '3');
          rect.setAttribute('rx', '14');
          rect.setAttribute('ry', '14');

          group.appendChild(rect);

          // Add seat image
          const image = document.createElementNS('http://www.w3.org/2000/svg', 'image');
          image.setAttribute('href', require('@/assets/seat-icon.png'));
          image.setAttribute('x', col * 60 + 5);
          image.setAttribute('y', row * 60 + 5);
          image.setAttribute('width', '50');
          image.setAttribute('height', '50');
          group.appendChild(image);

          // Add event listener for seat click
          if (seat === 'O') {
            image.addEventListener('click', () => {
              this.handleSeatClick(row, col, rect);
            });
            rect.addEventListener('click', () => {
              this.handleSeatClick(row, col, rect);
            });
          }

          svg.appendChild(group);
        }
        // For the last row, don't add row number text
        if (row === seatingPlan.length) {
          continue;
        }
        // Add row number text
        const rowText = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        rowText.setAttribute('x', seatingPlan[0].length * 60 + 10);
        rowText.setAttribute('y', row * 60 + 30);
        rowText.setAttribute('text-anchor', 'start');
        rowText.setAttribute('alignment-baseline', 'middle');
        rowText.setAttribute('fill', 'black');
        rowText.textContent = `${row + 1}`;
        svg.appendChild(rowText);
      }
      // Append the SVG to the HTML container
      container.appendChild(svg);
    },
    // Change the color of the seat when clicked
    async handleSeatClick(row, col, rect) {
      const currentColor = rect.getAttribute('fill');
      const seat = await this.getSeat(row + 1, col + 1, this.sessionId);
      // Select the seat
      if (currentColor === '#FFFFFF' || currentColor === '#99FF66') {
        rect.setAttribute('fill', 'pink');
        this.chosenSeats.push(seat);
      } else {
        // Unselect the seat
        rect.setAttribute('fill', '#FFFFFF');
        const index = this.chosenSeats.indexOf(seat);
        if (index > -1) {
          this.chosenSeats.splice(index, 1);
        }
      }
    },
    // Fetch suggested seats from the server
    async applySelection(seats) {
      if (seats > 9) {
        this.errorMessage = 'We can only suggest up to 9 seats. Please select fewer seats.'
        return;
      }
      try {
        const response = await fetch(`http://localhost:8080/api/suggestedSeat/${this.sessionId}/${seats}`);
        const data = await response.json();

        this.suggestedSeats = [];
        if (data.length === 0) {
          this.errorMessage = 'No suggested seats available.';
          return;
        }
        // Add suggested seats to the array and re-render the seating plan
        data.forEach(seat => {
          this.suggestedSeats.push(seat);
        });
        document.getElementById('seating-container').innerHTML = '';
        await this.fetchInfo();

      } catch (error) {
        console.error('Error fetching suggested seats:', error);
      }
    },
    // Fetch seat object from the server
    async getSeat(row, col, sessionId) {
      try {
        const response = await fetch(`http://localhost:8080/api/seat/${sessionId}/${row}/${col}`);
        return await response.json();
      } catch (error) {
        console.error('Error fetching seat id:', error);
      }
    },
    // Purchase selected seats and add the session to the user's history
    async purchaseSeats(chosenSeats) {
      if (chosenSeats.length === 0) {
        this.successMessage = 'Please select seats to purchase.';
        return;
      }
      // Only a logged-in user can purchase seats
      const clientId = this.$store.getters.getUser;
      if (isNaN(clientId) || clientId === null || clientId === undefined || clientId === '') {
        this.successMessage = 'Please log in to purchase seats.';
        return;
      }
      for (const seat of chosenSeats) {
        try {
          const response = await fetch(`http://localhost:8080/api/bookSeat/${this.sessionId}/${seat.row}/${seat.seat}`);
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          console.log(`Seat ${seat.row}-${seat.seat} purchased`);
        } catch (error) {
          console.error('Error purchasing seat:', error);
        }
      }
      // Re-render the seating plan with correct colors
      document.getElementById('seating-container').innerHTML = '';
      this.successMessage = 'Seats purchased successfully!';
      await this.fetchInfo();
      try {
        const response = await fetch(`http://localhost:8080/api/addHistory/${this.sessionId}/${clientId}`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        console.log(`Session ${this.sessionId} added to history for client ${clientId}`);
      } catch (error) {
        console.error('Error adding session to history:', error);
      }
    }
  },
  computed: {},
  mounted() {
    this.sessionId = this.$route.params.id;
    this.fetchInfo()
        .then(() => {
          console.log("mounted");
        })
        .catch(error => {
          console.error("Error fetching session info:", error);
        });
  },
};
</script>

<style scoped>
* {
  color: #7d3541;
  height: 100%;
  text-decoration: none;
}

h1 {
  font-size: 40px;
}

p {
  font-size: 25px;
}

button {
  margin: 10px;
  padding: 6px 10px;
  color: white;
  border: none;
  cursor: pointer;
  background-color: #ae5d6c;
  transition: background-color 0.3s ease;
  border-radius: 10px;
}

button:hover {
  background-color: #7d3541;
}

.session-container {
  padding: 20px;
  border-radius: 25px;
  margin: 10px;
  display: flex;
  justify-content: center;
}

.session {
  margin: 10px;
  padding: 30px 100px 30px 100px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #FADDE1;
  border-radius: 10px;
  color: #7d3541;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-color: #7d3541;
}

.seating-container {
  margin-top: 20px;
  border-radius: 10px;
}

.success-message {
  font-size: large;
  font-weight: bolder;
}
</style>
  