<template>
  <div class="container">
    <div class="form-container">
      <h2>Вход</h2>
      <form @submit.prevent="loginUser">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" id="username" v-model="loginData.username" required>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" v-model="loginData.password" required>
        </div>
        <button type="submit">Login</button>
        <div v-if="loginError" class="error-message">{{ loginError }}</div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      loginData: {
        username: '',
        password: '',
      },
      loginError: null,
    };
  },
  methods: {
    loginUser() {
      this.loginError = null;
      axios
          .post('http://localhost:8080/api/auth/login', {
            username: this.loginData.username,
            password: this.loginData.password,
          })
          .then((response) => {
            const token = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('username', this.loginData.username);
            this.$router.push('/personal');
          })
          .catch((error) => {
            this.loginError = 'Error, please try again';
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
</style>