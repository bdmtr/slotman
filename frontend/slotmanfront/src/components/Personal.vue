<template>
  <div class="transactions">
    <h2>Transactions</h2>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Amount</th>
        <th>Type</th>
        <th>Timestamp</th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="(transaction, index) in transactions"
          :key="transaction.id"
          :class="index % 2 === 0 ? 'white-row' : 'gray-row'"
      >
        <td>{{ transaction.id }}</td>
        <td>{{ transaction.amount }}</td>
        <td>{{ transaction.type }}</td>
        <td>{{ formatDate(transaction.timestamp) }}</td>
      </tr>
      </tbody>
    </table>
  </div>

  <div class="pagination">
    <button
        @click="loadPage(currentPage - 1)"
        :disabled="currentPage === 0 || (filtersApplied && currentPage === 1)"
    >
      Previous
    </button>
    <button
        @click="loadPage(currentPage + 1)"
        :disabled="currentPage >= totalPages - 1 || (filtersApplied && currentPage === 0)"
    >
      Next
    </button>
  </div>

  <div class="logout">
    <button @click="logout">Logout</button>
  </div>
</template>

<script>
import {error} from "vue-resource/src/util";

export default {
  name: 'Personal',
  data() {
    return {
      transactions: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      typeFilter: null,
      startDateFilter: null,
      endDateFilter: null,
      filtersApplied: false,
    };
  },
  async mounted() {
    const username = localStorage.getItem('username');
    const token = localStorage.getItem('token');
    await this.loadPage(username, token, this.currentPage);
  },
  methods: {
    async loadPage(username, token, page) {
      this.filtersApplied = false;
      try {
        headers: {
          Authorization: `Bearer ${token}`
        }
        ;

        const response = await fetch(
            `http://localhost:8080/api/personal/all?page=${page}&size=${this.pageSize}&username=${username}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            },
        );
        if (response.status === 200) {
          const data = await response.json();
          this.transactions = data.content;
          this.currentPage = data.number;
          this.totalPages = data.totalPages;
        } else {
          console.error('Error fetching transactions: ', error);
        }
      } catch (error) {
        console.error('Error fetching transactions: ', error);
      }
    },
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      this.$router.push('/login');
    },
    formatDate(timestamp) {
      const date = new Date(timestamp);
      const options = {year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric'};
      return date.toLocaleDateString('en-US', options);
    },
  },
};
</script>

<style scoped>
.search {
  margin-bottom: 10px;
}

.filters {
  margin-top: 10px;
}

table {
  margin-top: 10px;
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #f0f0f0;
}

th {
  background-color: #e1f3f5;
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

tr:hover {
  background-color: #f5f5f5;
}

.white-row {
  background-color: #ffffff;
}

.gray-row {
  background-color: #f0f0f0;
}

.pagination {
  margin-top: 10px;
}

.logout {
  margin-top: 10px;
}
</style>