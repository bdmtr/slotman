<template>
  <div class="container">
    <div class="form-container">
      <h2>Create new user</h2>
      <form @submit.prevent="registerUser">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" id="username" v-model="userData.username" required>
          <p class="symbols">5 symbols minimum</p>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" v-model="userData.password" required>
          <p class="symbols">5 symbols minimum</p>
        </div>
        <button type="submit">Register</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userData: {
        username: '',
        password: '',
      },
      errorMessage: null,
    };
  },
  methods: {
    registerUser() {
      this.errorMessage = null;
      axios
          .post('http://localhost:8080/api/users/create', {
            username: this.userData.username,
            password: this.userData.password,
          })
          .then(() => {
            this.$router.push('/');
          })
          .catch((error) => {
            if (error.response && error.response.status === 409) {
              this.errorMessage = 'user already exists';
            } else {
              this.errorMessage = 'Error. Please try again';
            }
            console.error(error);
          });
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70vh;
}

.form-container {
  background-color: #fff;
  padding: 110px;
  border-radius: 8px;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.2);
  width: 20%;
}

.form-group {
  margin-bottom: 20px;
}

label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

.error-message {
  color: red;
  font-weight: bold;
}

.symbols {
  margin-top: 5px;
  font-size: 12px;
  color: grey;
}
</style>