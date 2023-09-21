<template>
  <div class="transactions">
    <h2>Transactions</h2>
    <div class="search">
      <label for="transactionId">Transaction ID:</label>
      <input type="number" id="transactionId" v-model="searchId">
      <button @click="findTransaction">Find</button>
      <button @click="fetchTransactions" :disabled="searchId !== ''">Find All</button>
    </div>
    <div class="filters">
      <label for="userIdFilter">User ID:</label>
      <input type="number" id="userIdFilter" v-model="userIdFilter">

      <label for="typeFilter">Transaction Type:</label>
      <select id="typeFilter" v-model="typeFilter">
        <option value="INCOME">INCOME</option>
        <option value="OUTCOME">OUTCOME</option>
      </select>

      <label for="startDateFilter">Start Date:</label>
      <input type="datetime-local" id="startDateFilter" v-model="startDateFilter">

      <label for="endDateFilter">End Date:</label>
      <input type="datetime-local" id="endDateFilter" v-model="endDateFilter">

      <button @click="filterTransactions">Apply Filters</button>
      <button @click="resetFilters">Reset Filters</button>
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>User ID</th>
        <th>Amount</th>
        <th>Type</th>
        <th>Timestamp</th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="(transaction, index) in transactions"
          :key="transaction.id"
          :class="index % 2 === 0 ? 'white-row' : 'gray-row'">
        <td>{{ transaction.id }}</td>
        <td>{{ transaction.userId }}</td>
        <td>{{ transaction.amount }}</td>
        <td>{{ transaction.type }}</td>
        <td>{{ formatDate(transaction.timestamp) }}</td>
      </tr>
      </tbody>
    </table>
  </div>

  <div class="pagination">
    <button @click="loadPage(currentPage - 1)" :disabled="currentPage === 0 || (filtersApplied && currentPage === 1)">Previous</button>
    <button @click="loadPage(currentPage + 1)" :disabled="currentPage >= totalPages - 1 || (filtersApplied && currentPage === 0)">Next</button>
  </div>



</template>


<script>
export default {
  name: 'Transactions',
  data() {
    return {
      transactions: [],
      searchId: '',
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      userIdFilter: null,
      typeFilter: null,
      startDateFilter: null,
      endDateFilter: null,
      filtersApplied: false,
    };
  },
  computed: {
    isFilteringById() {
      return !!this.searchId;
    },
  },
  async mounted() {
    await this.loadPage(this.currentPage);
  },

  methods: {
    async loadPage(page) {
      this.filtersApplied = false;
      try {
        const response = await fetch(
            `http://localhost:8080/api/v1/transactions/all?page=${page}&size=${this.pageSize}`
        );
        if (response.status === 200) {
          const data = await response.json();
          this.transactions = data.content;
          this.currentPage = data.number;
          this.totalPages = data.totalPages;
        } else {
          console.error('Error fetching transactions:', error);
        }
      } catch (error) {
        console.error('Error fetching transactions:', error);
      }
    },

    async fetchTransactions() {
      try {
        const response = await fetch(
            `http://localhost:8080/api/v1/transactions/all?page=${this.currentPage}&size=${this.pageSize}`
        );

        if (response.status === 200) {
          const data = await response.json();
          this.transactions = data.content;
          this.currentPage = data.number;
          this.totalPages = data.totalPages;
        } else {
          console.error('Error fetching transactions:', error);
        }
      } catch (error) {
        console.error('Error fetching transactions:', error);
      }
    },

    async findTransaction() {
      if (!this.searchId) {
        return;
      }
      try {
        const response = await fetch(`http://localhost:8080/api/v1/transactions/${this.searchId}`);
        if (response.status === 200) {
          const transaction = await response.json();
          this.transactions = [transaction];
        } else {
          this.transactions = [];
        }
      } catch (error) {
        console.error('Error fetching transaction by ID:', error);
      }
    },
    formatDate(timestamp) {
      const date = new Date(timestamp);
      const options = {year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric'};
      return date.toLocaleDateString('en-US', options);
    },

    resetFilters() {
      this.userIdFilter = null;
      this.typeFilter = null;
      this.startDateFilter = null;
      this.endDateFilter = null;
      this.loadPage(0);
    },

    async filterTransactions() {
      let apiUrl = 'http://localhost:8080/api/v1/transactions/user?';

      if (this.userIdFilter) {
        apiUrl += `userId=${this.userIdFilter}&`;
      }

      if (this.typeFilter) {
        apiUrl += `type=${this.typeFilter}&`;
      }

      if (this.startDateFilter) {
        apiUrl += `start=${encodeURIComponent(this.startDateFilter)}&`;
      }

      if (this.endDateFilter) {
        apiUrl += `end=${encodeURIComponent(this.endDateFilter)}&`;
      }

      try {
        const response = await fetch(apiUrl);

        if (response.status === 200) {
          const transactions = await response.json();

          if (Array.isArray(transactions) && transactions.length > 0) {
            this.transactions = transactions;
          } else {
            this.transactions = [];
          }

        } else {
          this.transactions = [];
        }
      } catch (error) {
        console.error('Error fetching filtered transactions:', error);
        this.transactions = [];
      }
      this.filtersApplied = this.userIdFilter || this.typeFilter || this.startDateFilter || this.endDateFilter;
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
</style>
